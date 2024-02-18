package com.matvienko.fitnesapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.matvienko.fitnesapp.Adapters.DayModel
import com.matvienko.fitnesapp.Adapters.DaysAdapter
import com.matvienko.fitnesapp.Adapters.ExerciseModel
import com.matvienko.fitnesapp.R
import com.matvienko.fitnesapp.Utils.DialogManager
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.Utils.MainViewModel
import com.matvienko.fitnesapp.databinding.FragmentDaysBinding


class DaysFragment : Fragment(), DaysAdapter.Listener {

    private lateinit var adapter: DaysAdapter
    private lateinit var binding: FragmentDaysBinding
    private val model: MainViewModel by activityViewModels()
    private var ab: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset) {
            DialogManager.showDialog(activity as AppCompatActivity,
                getString(R.string.reset_all_days),
                object : DialogManager.Listener {
                    override fun onClick() {
                        model.pref?.edit()?.clear()?.apply()
                        adapter.submitList(fillDaysArray())
                    }

                }
            )

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentDayNumber = 0
        initRView()
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRView() = with(binding) {
        adapter = DaysAdapter(this@DaysFragment)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.days)
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter
        adapter.submitList(fillDaysArray())
    }

    private fun fillDaysArray(): ArrayList<DayModel> {
        val tArray = ArrayList<DayModel>()
        var daysDoneCounter = 0
        resources.getStringArray(R.array.day_exercise).forEach {
            model.currentDayNumber++
            val exCounter = it.split(",").size
            tArray.add(DayModel(it, model.getExerciseCount() == exCounter, 0))
        }
        binding.pB.max = tArray.size
        tArray.forEach {
            if (it.isDone) daysDoneCounter++
        }
        updateRestDaysUI(tArray.size - daysDoneCounter, tArray.size)
        return tArray
    }

    private fun updateRestDaysUI(restDays: Int, days: Int) = with(binding) {
        val rDays = getString(R.string.rest) + " $restDays " + " " + getString(R.string.days_rest)
        tvRestDays.text = rDays
        pB.progress = days - restDays
    }

    private fun fillExerciseList(day: DayModel) {
        val tempList = ArrayList<ExerciseModel>()
        day.exercises.split(",").forEach {
            val exerciseList = resources.getStringArray(R.array.exercise)
            val exercise = exerciseList[it.toInt()]
            val exerciseArray = exercise.split("|")
            tempList.add(ExerciseModel(exerciseArray[0], exerciseArray[1], exerciseArray[2], false))
        }
        model.mutableListExercise.value = tempList
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }


    override fun onClick(day: DayModel) {
        if (!day.isDone) {
            fillExerciseList(day)
            model.currentDayNumber = day.dayNumber
            FragmentManager.setFragment(
                ExerciseListFragment.newInstance(),
                activity as AppCompatActivity
            )
        } else {
            DialogManager.showDialog(activity as AppCompatActivity,
                getString(R.string.reset_day),
                object : DialogManager.Listener {
                    override fun onClick() {
                        model.savePref(day.dayNumber.toString(), 0)
                        fillExerciseList(day)
                        model.currentDayNumber = day.dayNumber
                        FragmentManager.setFragment(
                            ExerciseListFragment.newInstance(),
                            activity as AppCompatActivity)
                    }

                }
            )
        }

    }
}
