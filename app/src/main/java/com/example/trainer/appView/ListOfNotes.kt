package com.example.trainer.appView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trainer.R
import com.example.trainer.appView.uiElements.BackButton
import com.example.trainer.appView.uiElements.QuestionCard
import com.example.trainer.databases.Question
import com.example.trainer.destionations.SaveNote
import com.example.trainer.viewModels.QuestionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfNotes(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel,
    navHostController: NavHostController
) {
    val questions by viewModel.allQuestions.observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    var selectedNotes by remember { mutableStateOf(setOf<Question>()) }
    var isSelectionMode by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            if (isSelectionMode) {
                TopAppBar(modifier = Modifier.offset((-8).dp),
                    title = { Spacer(modifier = Modifier) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                selectedNotes = emptySet()
                                isSelectionMode = false
                            }
                            viewModel.deleteQuestions(selectedNotes.map { it.id })
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.trash_24),
                                contentDescription = "Delete"
                            )
                        }
                    }
                )
            }
        },

        floatingActionButton = {
            Button(
                modifier = Modifier.size(56.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = { navHostController.navigate(SaveNote(0)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = ShapeDefaults.Large
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },

        content = { paddingValues ->
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(questions.reversed()) { item ->
                    QuestionCard(
                        question = item,
                        isSelected = item in selectedNotes,
                        onClick = {
                            scope.launch {
                                if (isSelectionMode) {
                                    selectedNotes =
                                        if (item in selectedNotes) selectedNotes - item
                                        else selectedNotes + item

                                    if (selectedNotes.isEmpty()) {
                                        isSelectionMode = false
                                    }
                                } else {
                                    navHostController.navigate(SaveNote(item.id))
                                }
                            }
                        },
                        onPress = {
                            if (!isSelectionMode) {
                                isSelectionMode = true
                                selectedNotes = setOf(item)
                            }
                        }
                    )
                }
            }
        })
}