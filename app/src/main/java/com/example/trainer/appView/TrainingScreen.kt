package com.example.trainer.appView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.trainer.R
import com.example.trainer.appView.uiElements.BackButton
import com.example.trainer.appView.uiElements.NavigationButton
import com.example.trainer.databases.Question
import com.example.trainer.ui.theme.MyStyles
import com.example.trainer.viewModels.QuestionViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val DEBUG_TAG = "TrainingLog"

val questionSaver = Saver<Question, String>(
    save = { question ->
        Json.encodeToString(question)
    },
    restore = { questionString ->
        Json.decodeFromString<Question>(questionString)
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(questionViewModel: QuestionViewModel, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val allQuestions by questionViewModel.allQuestions.observeAsState(emptyList())

    val usedQuestions: MutableList<Question> by rememberSaveable {
        mutableStateOf(mutableListOf())
    }

    var passedQuestionsCount by rememberSaveable {
        mutableIntStateOf(0)
    }

    var currentQuestion: Question by rememberSaveable(stateSaver = questionSaver) {
        mutableStateOf(
            allQuestions.random()
        )
    }

    var isAnswerShown by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset((-8).dp)
                            .padding(end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BackButton {
                            scope.launch {
                                onBack()
                            }
                        }
                        Text(
                            text = (passedQuestionsCount + 1).toString() + "/" + allQuestions.size,
                            style = MyStyles.textH1
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavigationButton(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = { isAnswerShown = true  },
                        painter = painterResource(id = R.drawable.eye_24),
                        buttonColors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )

                    NavigationButton(
                        iconSize = 32,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                usedQuestions.add(currentQuestion)
                                passedQuestionsCount++
                                if (allQuestions.size == usedQuestions.size) {
                                    usedQuestions.clear()
                                    passedQuestionsCount = 0
                                }
                                currentQuestion =
                                    (allQuestions.minus(usedQuestions.toSet())).random()
                                isAnswerShown = false
                            }
                        },
                        painter = painterResource(id = R.drawable.arrow_small_right_24),
                        buttonColors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = currentQuestion.question, style = MyStyles.textH1)

            if (isAnswerShown)
                Text(text = currentQuestion.answer)
        }
    }
}
