package com.example.trainer.appView

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.trainer.R
import com.example.trainer.appView.uiElements.BackButton
import com.example.trainer.appView.uiElements.EmptyTextField
import com.example.trainer.databases.Question
import com.example.trainer.ui.theme.MyStyles
import com.example.trainer.viewModels.QuestionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveNoteView(
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
    questionViewModel: QuestionViewModel,
    questionId: Int,
) {
    val configuration = LocalConfiguration.current
    val halfScreenHeightDp = configuration.screenHeightDp.dp / 2
    val toast = Toast(LocalContext.current)

    val note by questionViewModel.getQuestionById(questionId).observeAsState()

    var question by rememberSaveable {
        mutableStateOf("")
    }

    var answer by rememberSaveable {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(note) {
        if(questionId == 0) {
            SaveBehaviour.current = SaveType.Create
        } else {
            note?.let {
                if(question.isEmpty() && answer.isEmpty()) {
                    question = it.question
                    answer = it.answer
                }
                SaveBehaviour.current = SaveType.Update
            }
        }
    }

    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(modifier = Modifier.offset((-8).dp), title = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    BackButton {
                        if(answer.isNotEmpty() && question.isNotEmpty()) {
                            when (SaveBehaviour.current) {
                                SaveType.Create -> {
                                    scope.launch {
                                        val result = questionViewModel.insert(Question(question = question, answer = answer))
                                        result.join()
                                        withContext(Dispatchers.Main) {
                                            if(result.isCompleted)
                                                goBack()
                                        }
                                    }
                                }

                                SaveType.Update -> {
                                    scope.launch {
                                        note?.question = question
                                        note?.answer = answer
                                        val result = note?.let { questionViewModel.update(it) }
                                        result?.join()
                                        withContext(Dispatchers.Main) {
                                            if(result?.isCompleted == true) {
                                                goBack()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            when (SaveBehaviour.current) {
                                SaveType.Create -> goBack()
                                SaveType.Update -> {
                                    if(question.isEmpty() || answer.isEmpty()) {
                                        toast.setText(R.string.fields_alert)
                                        toast.show()
                                    } else goBack()
                                }
                            }
                        }
                    }
                    note?.let {
                        IconButton(onClick = {
                            questionViewModel.delete(it)
                            goBack()
                        }) {
                            Icon(painter = painterResource(id = R.drawable.trash_24), contentDescription = "Delete")
                        }
                    }
                }
            })
        },
        bottomBar = {
            Spacer(modifier = Modifier.size(64.dp))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EmptyTextField(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .heightIn(max = halfScreenHeightDp),
                value = question,
                onValueChange = { question = it },
                hintText = stringResource(id = R.string.enter_question),
                hintTextStyle = MyStyles.textH1,
                textStyle = MyStyles.textH1
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(2.dp)
                    .background(color = MaterialTheme.colorScheme.outlineVariant)
            )

            EmptyTextField(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxHeight(),
                value = answer,
                onValueChange = { answer = it },
                hintText = stringResource(id = R.string.enter_answer),
                hintTextStyle = MyStyles.textP,
                textStyle = MyStyles.textP
            )
        }
    }
}

private object SaveBehaviour {
    var current by mutableStateOf(SaveType.Create)
}

private enum class SaveType {
    Create, Update
}
