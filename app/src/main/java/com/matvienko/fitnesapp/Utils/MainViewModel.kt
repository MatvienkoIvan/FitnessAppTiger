package com.matvienko.fitnesapp.Utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.matvienko.fitnesapp.Adapters.ExerciseModel

class MainViewModel : ViewModel() {
    val mutableListExercise = MutableLiveData <ArrayList<ExerciseModel>>()
    var pref: SharedPreferences? = null
    var currentDayNumber = 0

    fun savePref(key: String, value: Int) {
        pref?.edit()?.putInt(key,value)?.apply ()
    }

    fun getExerciseCount() : Int {
        return pref?.getInt(currentDayNumber.toString(), 0) ?: 0
    }

}