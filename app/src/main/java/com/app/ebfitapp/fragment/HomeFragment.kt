package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.ArticleAdapter
import com.app.ebfitapp.databinding.FragmentHomeBinding
import com.app.ebfitapp.model.ArticleModel
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.StreakManager
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

class HomeFragment : Fragment() {

    private var currentStreak: Int = 0

    private lateinit var article: ArrayList<ArticleModel>
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var customProgress: CustomProgress
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

        customProgress = CustomProgress(requireActivity())
        customProgress.show()

        article = ArrayList()
        articleAdapter = ArticleAdapter(article)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getProfileDetail()
        mainViewModel.getArticles()

        observeProfileDetail()
        observeArticle()

        disableSeekBar()
        chips()
        getStreak()

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHomeBinding.articleRecycler.adapter = articleAdapter

    }

    private fun getStreak() = with(fragmentHomeBinding) {
        StreakManager.getCurrentStreak(requireContext()) { streakCounter ->
            if (streakCounter != null) {
                currentStreak = streakCounter
                streakCounterText.text = currentStreak.toString()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableSeekBar() {
        fragmentHomeBinding.circularSeekBar.setOnTouchListener { view, motionEvent -> true }
    }

    private fun chips() {
        val chipTextArray = arrayOf("Yoga", "Cardio", "Strectch", "Food", "Calori")

        for (chipText in chipTextArray) {
            val chip = Chip(requireContext())
            chip.text = chipText
            chip.isCheckable = true
            chip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.chip_background)
            chip.setChipStrokeColorResource(R.color.light_gray)
            chip.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            fragmentHomeBinding.chipGroup.addView(chip)
        }
        
        fragmentHomeBinding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = group.findViewById<Chip>(group.checkedChipId)
            if (selectedChipId != null) Toast.makeText(requireContext(), "${selectedChipId.text}", Toast.LENGTH_LONG).show()
            else fragmentHomeBinding.chipAll.isChecked = true
        }
        
    }

    private fun observeProfileDetail() {
        mainViewModel.profileDetails.observe(requireActivity(), Observer { userProfileDetails ->
            customProgress.dismiss()
            if (userProfileDetails != null) {
                fragmentHomeBinding.homeRootLinear.visibility = View.VISIBLE
                fragmentHomeBinding.profileImage.downloadImageFromURL(userProfileDetails.profileImageURL.toString())
                fragmentHomeBinding.nicknameText.text = "${userProfileDetails.username} ${getString(R.string.wave_hand)}"
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeArticle() {
        mainViewModel.articleLiveData.observe(requireActivity(), Observer { resultArticle ->
            if (resultArticle != null) {
                article.clear()
                article.addAll(resultArticle)
                articleAdapter.notifyDataSetChanged()
            }
        })
    }

}