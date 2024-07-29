package com.example.trainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.trainer.appView.App
import com.example.trainer.ui.theme.AppTheme
import com.example.trainer.viewModels.QuestionViewModel
import com.example.trainer.viewModels.factories.QuestionViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var questionViewModelFactory: QuestionViewModelFactory
    private val questionViewModel: QuestionViewModel by viewModels {
        questionViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme { App(questionViewModel = questionViewModel) }
        }
    }
}
