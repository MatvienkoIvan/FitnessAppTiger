package com.matvienko.fitnesapp.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.matvienko.fitnesapp.Adapters.ExerciseAdapter
import com.matvienko.fitnesapp.R
import com.matvienko.fitnesapp.Utils.COUNT_DOWN_TIME
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.Utils.MainViewModel
import com.matvienko.fitnesapp.Utils.TimeUtils
import com.matvienko.fitnesapp.databinding.ExerciseListFragmentBinding
import com.matvienko.fitnesapp.databinding.WaitingFragmentBinding


class WaitingFragment : Fragment() {


    private lateinit var binding: WaitingFragmentBinding
    private lateinit var timer: CountDownTimer
    private var ab: ActionBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.get_ready)
        binding.pBar.max = COUNT_DOWN_TIME.toInt()
        startTimer()
    }

    private fun startTimer() = with(binding) {
        timer = object : CountDownTimer(COUNT_DOWN_TIME, 10) {
            override fun onTick(restTime: Long) {
                tvTimer.text = TimeUtils.getTime(restTime)
                pBar.progress = restTime.toInt()
            }

            override fun onFinish() {
                FragmentManager.setFragment(ExerciseFragment.newInstance(),
                    activity as AppCompatActivity)
            }

        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        timer.cancel()
    }


    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}
