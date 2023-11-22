package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.BestTrainersAdapter
import com.app.ebfitapp.adapter.MuscleGroupsAdapter
import com.app.ebfitapp.adapter.PopularWorkoutsAdapter
import com.app.ebfitapp.databinding.FragmentWorkoutBinding
import com.app.ebfitapp.model.BestTrainersModel
import com.app.ebfitapp.model.MuscleGroupModel
import com.app.ebfitapp.viewmodel.WorkoutViewModel

class WorkoutFragment : Fragment() {

    private lateinit var muscleGroups: ArrayList<MuscleGroupModel>
    private lateinit var bestTrainers: ArrayList<BestTrainersModel>

    private lateinit var bestTrainersAdapter: BestTrainersAdapter
    private lateinit var muscleGroupsAdapter: MuscleGroupsAdapter
    private lateinit var popularWorkoutsAdapter: PopularWorkoutsAdapter

    private val workoutViewModel: WorkoutViewModel by viewModels()
    private lateinit var fragmentWorkoutBinding: FragmentWorkoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentWorkoutBinding = FragmentWorkoutBinding.inflate(layoutInflater)

        muscleGroups = ArrayList()
        bestTrainers = ArrayList()

        bestTrainersAdapter = BestTrainersAdapter(bestTrainers)
        muscleGroupsAdapter = MuscleGroupsAdapter(muscleGroups)
        popularWorkoutsAdapter = PopularWorkoutsAdapter()

        fragmentWorkoutBinding.bestTrainersRecycler.adapter = bestTrainersAdapter
        fragmentWorkoutBinding.muscleGroupsRecycler.adapter = muscleGroupsAdapter
        fragmentWorkoutBinding.popularWorkoutsRecycler.adapter = popularWorkoutsAdapter

        workoutViewModel.getMuscleGroups()
        workoutViewModel.getBestTrainers()
        observeMuscleGroups()
        observeBestTrainers()


        return fragmentWorkoutBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeMuscleGroups() = with(fragmentWorkoutBinding) {
        workoutViewModel.muscleGroupLiveData.observe(viewLifecycleOwner, Observer { liveMuscleGroups ->
            if (liveMuscleGroups != null) {
                muscleGroups.clear()
                muscleGroups.addAll(liveMuscleGroups)
                muscleGroupsRecycler.adapter?.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeBestTrainers() = with(fragmentWorkoutBinding) {
        workoutViewModel.bestTrainersLiveData.observe(viewLifecycleOwner, Observer { liveBestTrainers ->
            if (liveBestTrainers != null) {
                bestTrainers.clear()
                bestTrainers.addAll(liveBestTrainers)
                bestTrainersRecycler.adapter?.notifyDataSetChanged()
            }
        })
    }


}