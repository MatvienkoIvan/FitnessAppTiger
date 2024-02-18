package com.matvienko.fitnesapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.matvienko.fitnesapp.Adapters.DayModel
import com.matvienko.fitnesapp.Adapters.DaysAdapter
import com.matvienko.fitnesapp.Adapters.ExerciseAdapter
import com.matvienko.fitnesapp.R
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.Utils.MainViewModel
import com.matvienko.fitnesapp.databinding.ExerciseListFragmentBinding
import com.matvienko.fitnesapp.databinding.FragmentDaysBinding


class ExerciseListFragment : Fragment() {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var binding: ExerciseListFragmentBinding
    private var ab: ActionBar? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.mutableListExercise.observe(viewLifecycleOwner) {
            for (i in 0 until model.getExerciseCount()) {
                it[i] = it[i].copy(isDone = true)
            }
            adapter.submitList(it)
        }
    }

    private fun init() = with(binding) {
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.exercises)
        adapter = ExerciseAdapter()
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        bStart.setOnClickListener {
            FragmentManager.setFragment(
                WaitingFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}
