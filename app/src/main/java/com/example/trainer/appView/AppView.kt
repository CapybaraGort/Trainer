package com.example.trainer.appView

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trainer.destionations.Home
import com.example.trainer.destionations.ListOfWords
import com.example.trainer.destionations.SaveNote
import com.example.trainer.destionations.Training
import com.example.trainer.viewModels.QuestionViewModel

@Composable
fun App(questionViewModel: QuestionViewModel) {
    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Home) {
        composable<Home> {
            StartScreen(
                navHostController = navHostController,
                isQuestionsEmpty = questionViewModel.allQuestions.value?.isEmpty() == true
            )
        }
        composable<ListOfWords> {
            ListOfNotes(
                viewModel = questionViewModel,
                navHostController = navHostController
            )
        }
        composable<SaveNote> { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("questionId")
            SaveNoteView(
                goBack = { navHostController.navigateUp() },
                questionViewModel = questionViewModel,
                questionId = id ?: 0
            )
        }
        composable<Training> {
            TrainingScreen(
                questionViewModel = questionViewModel,
                onBack = { navHostController.navigateUp() }
            )
        }
    }
}