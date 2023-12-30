package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentProfileEditBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.service.FirebaseFirestoreService
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.PermissionManager
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileEditFragment() : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var newUsername: String? = null
    private var newAge: Int? = null
    private var newImageURI: Uri? = null
    private var newWeight: Double? = null
    private var newTargetWeight : Double? = null
    private lateinit var customProgress: CustomProgress
    private lateinit var firebaseFirestoreService: FirebaseFirestoreService
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var editProfileBinding : FragmentProfileEditBinding
    private lateinit var permissionManager: PermissionManager
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        editProfileBinding = FragmentProfileEditBinding.inflate(layoutInflater)
        firebaseFirestoreService = FirebaseFirestoreService(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        galleryResult()
        setButtonBackground(false)
        permissionManager = PermissionManager(requireContext())
        return editProfileBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)

        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this@ProfileEditFragment)[MainViewModel::class.java]
        mainViewModel.getProfileDetail()
        customProgress = CustomProgress(requireActivity())
        customProgress.show()
        observeProfileDetail()
        with(editProfileBinding){

            profileImage.setOnClickListener {
                if (permissionManager.checkStoragePermission()) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    resultLauncher.launch(intent)
                } else {
                    permissionManager.requestStoragePermission(requireActivity())
                }
            }
            goBackImage.setOnClickListener {
                val goBackAction = ProfileEditFragmentDirections.actionProfileEditFragmentToProfileFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
           //Textwatchers for all
            setupEditTextValidation(ageText,falseAgeText,0.0,100.0,initialColor,underlineColor)
            setupEditTextValidation(weightText,falseWeightText,30.0,250.0,initialColor,underlineColor)
            setupEditTextValidation(targetWeightText,falseTargetWeightText,30.0,250.0,initialColor,underlineColor)
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    usernameText.backgroundTintList = ColorStateList.valueOf(initialColor)
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
                override fun afterTextChanged(s: Editable?) {
                    if(mainViewModel.profileDetails.value?.username != usernameText.text.toString() && getLength())
                    {
                        //Farklı bir değer girilmiş ve 2 ile 20 arasında
                        if(usernameText!=null)
                        {
                            newUsername = usernameText.text.toString()
                            setButtonBackground(true)
                        }else{
                            newUsername = mainViewModel.profileDetails.value?.username
                        }
                        falseUsernameText.visibility = View.INVISIBLE
                    }else if (mainViewModel.profileDetails.value?.username != usernameText.text.toString() &&!getLength()){
                        //Farklı bir değer ama 2 ile 20 arasında değil
                        falseUsernameText.visibility = View.VISIBLE
                        falseUsernameText.text = "Invalid name : must be between 2 and 20 characters"
                        usernameText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                        setButtonBackground(false)
                    }else{
                        //2 ile 20 arasında ama farklı değer değil
                        falseUsernameText.visibility = View.VISIBLE
                        falseUsernameText.text = "Invalid username :  must be different than before"
                        usernameText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                        setButtonBackground(false)
                    }
                }
            }
            usernameText.addTextChangedListener(textWatcher)

            doneButton.setOnClickListener {
                doneButtonClicked()
            }
        }
    }
    private fun observeProfileDetail() {
        mainViewModel.profileDetails.observe(requireActivity(), Observer { userProfileDetails ->
            customProgress.dismiss()
            if (userProfileDetails != null) {
                editProfileBinding.profileImage.downloadImageFromURL(userProfileDetails.profileImageURL.toString())
                editProfileBinding.usernameText.hint = userProfileDetails.username
                editProfileBinding.ageText.hint = userProfileDetails.age
                editProfileBinding.weightText.hint = userProfileDetails.weight.toString()
                editProfileBinding.targetWeightText.hint = userProfileDetails.targetWeight.toString()
            }
        })
    }

    private fun getLength() : Boolean = editProfileBinding.usernameText.length() in 2..20

    private fun doneButtonClicked() {
        customProgress.show()

        mainViewModel.profileDetails.value?.age = newAge.toString()
        mainViewModel.profileDetails.value?.weight = newWeight
        mainViewModel.profileDetails.value?.targetWeight = newTargetWeight
        mainViewModel.profileDetails.value?.profileImageURL = newImageURI.toString()
//Bu değeler ne orospu çocuğu çalışsana
        println("Age: ${newAge?.toString()}")
        println("Weight: $newWeight")
        println("Target Weight: $newTargetWeight")
        println("Profile Image URL: ${newImageURI?.toString()}")

        val updateData = hashMapOf<String, Any?>()

        mainViewModel.profileDetails.value?.let { userProfileDetails ->
            //Changes
            newUsername?.let {
                    updateData["username"] = it
            }
            newAge?.let {
                updateData["age"] = it.toString()
            }
            newWeight?.let {
                updateData["weight"] = it
            }
            newTargetWeight?.let {
                    updateData["targetWeight"] = it
                }
            newImageURI?.let {
                if (it.toString().isNotBlank()) {
                    updateData["profileImageURL"] = it.toString()
                }
            }
            //constants
            updateData["height"] = mainViewModel.profileDetails.value!!.height
            updateData["email"]= mainViewModel.profileDetails.value!!.email
            updateData["gender"]= mainViewModel.profileDetails.value!!.gender
            updateData["goal"]= mainViewModel.profileDetails.value!!.goal
        }
        updateProfile(updateData)
    }
            //Targetweight ve weight neden 180 amına koyim?
    private fun setupEditTextValidation(editText: EditText, falseTextView: TextView, minValue: Double, maxValue: Double, initialColor: Int, underlineColor: Int
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val inputText = editable.toString()

                if (inputText.isNullOrEmpty()) {
                    falseTextView.visibility = View.INVISIBLE
                    editText.backgroundTintList = ColorStateList.valueOf(initialColor)
                    return
                }
                try {
                    val formattedInput = inputText.replace(',', '.')
                    val enteredValue = formattedInput.toDouble()

                    if (enteredValue in minValue..maxValue) {
                        if (editText == editProfileBinding.ageText) {
                            if (mainViewModel.profileDetails.value?.age != enteredValue.toString()) {
                                newAge = enteredValue.toInt()
                                setButtonBackground(true)
                            }
                        } else if (editText == editProfileBinding.weightText) {
                            if (mainViewModel.profileDetails.value?.weight != enteredValue.toString().toDouble()) {
                                newWeight = enteredValue
                                setButtonBackground(true)
                            }
                        } else if (editText == editProfileBinding.targetWeightText) {
                            if (mainViewModel.profileDetails.value?.targetWeight != enteredValue.toString().toDouble()) {
                                newTargetWeight = enteredValue
                                setButtonBackground(true)
                            }
                        }
                        falseTextView.visibility = View.INVISIBLE
                        editText.backgroundTintList = ColorStateList.valueOf(initialColor)
                    } else {
                        falseTextView.visibility = View.VISIBLE
                        editText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                        setButtonBackground(false)
                    }
                } catch (e: NumberFormatException) {
                    falseTextView.visibility = View.VISIBLE
                    editText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                    setButtonBackground(false)
                }
            }
        })
    }
    @SuppressLint("ResourceAsColor")
    private fun setButtonBackground(isConditionTrue: Boolean) {
        if (isConditionTrue) {
          editProfileBinding.doneButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
            editProfileBinding.doneButton.isClickable = true
        } else {
            editProfileBinding.doneButton.backgroundTintList = ColorStateList.valueOf(R.color.initButtonColor)
            editProfileBinding.doneButton.isClickable = false
        }
    }

    private fun galleryResult() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                newImageURI = it.data?.data
                editProfileBinding.profileImage.setImageURI(newImageURI)
                mainViewModel.profileDetails.value?.profileImageURL = newImageURI.toString()
                setButtonBackground(true)
            }
        }
    }
   private fun updateProfile(updateData: HashMap<String, Any?>) {
        firebaseFirestoreService.firestore.collection("profileDetail")
            .document(firebaseAuthService.getCurrentUserEmail()).update(updateData)
            .addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    customProgress.dismiss()
                    Toast.makeText(requireContext(),"Profile information has been successfully updated.",Toast.LENGTH_SHORT).show()
                    setButtonBackground(false)
                }
            }.addOnFailureListener {
                customProgress.dismiss()
                Toast.makeText(requireContext(),"Profile information could not be updated successfully.",Toast.LENGTH_SHORT).show()
            }
    }
}

