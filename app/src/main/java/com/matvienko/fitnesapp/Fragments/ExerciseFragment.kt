package com.matvienko.fitnesapp.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.matvienko.fitnesapp.Adapters.ExerciseModel
import com.matvienko.fitnesapp.R
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.Utils.MainViewModel
import com.matvienko.fitnesapp.Utils.TimeUtils
import com.matvienko.fitnesapp.databinding.ExerciseBinding
import pl.droidsonroids.gif.GifDrawable


class ExerciseFragment : Fragment() {

    private var timer: CountDownTimer? = null
    private lateinit var binding: ExerciseBinding
    private var exerciseCounter = 0
    private var exList: ArrayList<ExerciseModel>? = null
    private var ab: ActionBar? = null
    private var currentDay = 0
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.currentDayNumber
        exerciseCounter = model.getExerciseCount()
//        Log.d("MyLog", "Counter: ${model.getPref(model.currentDayNumber.toString())}")
        ab = (activity as AppCompatActivity).supportActionBar
        model.mutableListExercise.observe(viewLifecycleOwner) {
            exList = it
            nextExercise()
        }
        binding.bNext.setOnClickListener {
            nextExercise()
        }
    }

    private fun nextExercise() {
        if (exerciseCounter < exList?.size!!) {
            val ex = exList?.get(exerciseCounter++) ?: return
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            exerciseCounter++
            FragmentManager.setFragment(
                DayFinishFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }

    private fun showExercise(exercise: ExerciseModel) = with(binding) {
        imMine.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        tvNameEx.text = exercise.name
        val tittle = "$exerciseCounter / ${exList?.size}"
        ab?.title = tittle
    }

    private fun setExerciseType(exercise: ExerciseModel) {
        if (exercise.time.startsWith("x",)) {
            timer?.cancel()
            binding.progressBar.progress = 0
            binding.tvTimeEx.text = exercise.time
        }

        else {
            startTimer(exercise)
        }
    }

    private fun showNextExercise() = with(binding) {
        if (exerciseCounter < exList?.size!!) {
            val ex = exList?.get(exerciseCounter) ?: return
            imNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            setTimeType(ex)
        } else {
            imNext.setImageDrawable(GifDrawable(root.context.assets, "awesome_ok.gif"))
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun setTimeType(ex: ExerciseModel) {
        if (ex.time.startsWith("x")) {
            binding.tvNextName.text = ex.time
        } else {
            val name = ex.name + " : ${TimeUtils.getTime(ex.time.toLong() * 1000)} "
            binding.tvNextName.text = name

        }
    }

    private fun startTimer(exercise: ExerciseModel) = with(binding) {
        progressBar.max = exercise.time.toInt() * 1000
        timer?.cancel()
        timer = object : CountDownTimer(exercise.time.toLong() * 1000, 10) {
            override fun onTick(restTime: Long) {
                tvTimeEx.text = TimeUtils.getTime(restTime)
                progressBar.progress = restTime.toInt()
            }

            override fun onFinish() {
                nextExercise()
            }

        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1)
        timer?.cancel()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ExerciseFragment()
    }
}
