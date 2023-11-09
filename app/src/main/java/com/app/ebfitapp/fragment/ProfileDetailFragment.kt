package com.app.ebfitapp.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentProfileDetailBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.service.FirebaseFirestoreService
import com.app.ebfitapp.service.FirebaseStorageService
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.PermissionManager
import com.app.ebfitapp.view.MainActivity

class ProfileDetailFragment : Fragment() {

    private var age: Int? = null
    private var height: Int? = null
    private var weight: Double? = null
    private var targetWeight: Double? = null
    private var goal: String? = null
    private var imageURI: Uri? = null

    private lateinit var customProgress: CustomProgress
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>
    private lateinit var permissionManager: PermissionManager
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var firebaseStorageService: FirebaseStorageService
    private lateinit var firebaseFirestoreService: FirebaseFirestoreService
    private lateinit var fragmentProfileDetailBinding: FragmentProfileDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentProfileDetailBinding = FragmentProfileDetailBinding.inflate(layoutInflater)

        customProgress = CustomProgress(requireContext())
        permissionManager = PermissionManager(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        firebaseStorageService = FirebaseStorageService(requireContext())
        firebaseFirestoreService = FirebaseFirestoreService(requireContext())

        goalsDropDownSettings()
        galleryResult()

        return fragmentProfileDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentProfileDetailBinding) {

            profileImage.setOnClickListener {
                if (permissionManager.checkStoragePermission()) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    resultLauncher.launch(intent)
                } else {
                    permissionManager.requestStoragePermission(requireActivity())
                }
            }

            goalDropDown.setOnItemClickListener { adapterView, view, i, l ->
                goal = adapterView.getItemAtPosition(i).toString()
            }

            startButton.setOnClickListener {
                if (ageText.text.isNullOrEmpty() || heightText.text.isNullOrEmpty() || weightText.text.isNullOrEmpty() ||
                    targetWeightText.text.isNullOrEmpty() || goal.isNullOrEmpty() || imageURI == null) {
                    Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_LONG).show()
                } else {
                    customProgress.show()
                    firebaseStorageService.uploadImage(imageURI!!) { downloadUrl ->
                        if (!downloadUrl.isNullOrEmpty()) {
                            age = ageText.text.toString().toInt()
                            height = heightText.text.toString().toInt()
                            weight = weightText.text.toString().toDouble()
                            targetWeight = targetWeightText.text.toString().toDouble()
                            val updateMap = hashMapOf<String,Any>(
                                "age" to age!!,
                                "height" to height!!,
                                "weight" to weight!!,
                                "targetWeight" to targetWeight!!,
                                "goal" to goal!!,
                                "profileImageURL" to downloadUrl
                            )
                            firebaseFirestoreService.updateProfileDetail(firebaseAuthService.getCurrentUserEmail(), updateMap) { result ->
                                if (result) {
                                    customProgress.dismiss()
                                    requireActivity().finish()
                                    val intent = Intent(requireActivity(), MainActivity::class.java)
                                    startActivity(intent)
                                } else customProgress.dismiss()
                            }
                        } else {
                            customProgress.dismiss()
                        }
                    }
                }
            }

        }

    }

    private fun goalsDropDownSettings() {
        val goalItems = listOf(getString(R.string.weightLoss), getString(R.string.weightGain), getString(R.string.increaseEndurance), getString(R.string.buildMuscleMass), getString(R.string.improveFlexibility))
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, goalItems)
        fragmentProfileDetailBinding.goalDropDown.setAdapter(adapter)
    }

    private fun galleryResult() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                imageURI = it.data?.data
                fragmentProfileDetailBinding.profileImage.setImageURI(imageURI)
            }
        }
    }



}