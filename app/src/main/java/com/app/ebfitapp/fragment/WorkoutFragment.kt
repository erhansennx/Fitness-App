package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.BestTrainersAdapter
import com.app.ebfitapp.adapter.MuscleGroupsAdapter
import com.app.ebfitapp.adapter.PopularWorkoutsAdapter
import com.app.ebfitapp.databinding.FragmentWorkoutBinding

class WorkoutFragment : Fragment() {

    private lateinit var bestTrainersAdapter: BestTrainersAdapter
    private lateinit var muscleGroupsAdapter: MuscleGroupsAdapter
    private lateinit var popularWorkoutsAdapter: PopularWorkoutsAdapter
    private lateinit var fragmentWorkoutBinding: FragmentWorkoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentWorkoutBinding = FragmentWorkoutBinding.inflate(layoutInflater)

        bestTrainersAdapter = BestTrainersAdapter()
        muscleGroupsAdapter = MuscleGroupsAdapter()
        popularWorkoutsAdapter = PopularWorkoutsAdapter()

        fragmentWorkoutBinding.bestTrainersRecycler.adapter = bestTrainersAdapter
        fragmentWorkoutBinding.muscleGroupsRecycler.adapter = muscleGroupsAdapter
        fragmentWorkoutBinding.popularWorkoutsRecycler.adapter = popularWorkoutsAdapter


        return fragmentWorkoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}