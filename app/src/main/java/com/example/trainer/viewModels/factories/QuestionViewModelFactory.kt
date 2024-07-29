package com.example.trainer.viewModels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trainer.viewModels.QuestionViewModel
import javax.inject.Inject

class QuestionViewModelFactory @Inject constructor(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(application) as T
        }
        else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}