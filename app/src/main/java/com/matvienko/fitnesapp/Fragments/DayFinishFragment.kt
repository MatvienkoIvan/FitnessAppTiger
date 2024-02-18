package com.matvienko.fitnesapp.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.matvienko.fitnesapp.R
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.databinding.DayFinishBinding
import pl.droidsonroids.gif.GifDrawable


class DayFinishFragment : Fragment() {


    private lateinit var binding: DayFinishBinding
    private var ab: ActionBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ab = (activity as AppCompatActivity).supportActionBar
        ab?.title = getString(R.string.done_day)
        binding.imMain.setImageDrawable(
            GifDrawable(
                (activity as AppCompatActivity)
                    .assets, "awesome_ok.gif"
            )
        )
        binding.bDoneDay.setOnClickListener {
            FragmentManager.setFragment(
                DaysFragment.newInstance(),
                activity as AppCompatActivity
            )
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = DayFinishFragment()
    }
}
