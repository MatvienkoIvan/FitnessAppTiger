package com.matvienko.fitnesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.matvienko.fitnesapp.Fragments.DaysFragment
import com.matvienko.fitnesapp.Utils.FragmentManager
import com.matvienko.fitnesapp.Utils.MainViewModel

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model.pref = getSharedPreferences("main", MODE_PRIVATE)
        FragmentManager.setFragment(DaysFragment.newInstance(), this)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (FragmentManager.currentFragment is DaysFragment) super.onBackPressed()
        else FragmentManager.setFragment(DaysFragment.newInstance(), this)
    }
}