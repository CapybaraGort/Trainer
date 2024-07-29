package com.example.trainer.appView

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.trainer.R
import com.example.trainer.destionations.Training
import com.example.trainer.ui.theme.MyStyles
import com.example.trainer.viewModels.QuestionViewModel
import kotlinx.coroutines.launch

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    questionViewModel: QuestionViewModel,
    isQuestionsEmpty: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val toast = Toast(LocalContext.current)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomBar()
        }
    ) { innerPadding ->
        when (currentNavigation) {
            Navigation.Start -> {
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(visible) {
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(initialOffsetX = { -it })
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    if (isQuestionsEmpty) {
                                        toast.setText(R.string.no_notes)
                                        toast.show()
                                    } else navHostController.navigate(Training)
                                }
                            },
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(220.dp)
                                .border(
                                    width = 4.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = CircleShape
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.start_train_btn),
                                style = MyStyles.textP
                            )
                        }
                    }
                }
            }

            Navigation.Notes -> {
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(visible) {
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(initialOffsetX = { it })
                ) {
                    ListOfNotes(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = questionViewModel,
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(
        containerColor = Color.Transparent,
        modifier = modifier,
        windowInsets = WindowInsets.systemBars,
        contentPadding = PaddingValues(0.dp)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ) {
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                selected = currentNavigation == Navigation.Start,
                onClick = { currentNavigation = Navigation.Start },
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.home_24),
                            contentDescription = "home"
                        )
                    }
                },
                label = {
                    Spacer(
                        modifier = Modifier
                            .width(64.dp)
                            .height(2.dp)
                            .background(color = MaterialTheme.colorScheme.outline)
                    )
                },
                alwaysShowLabel = false
            )

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                selected = currentNavigation == Navigation.Notes,
                onClick = { currentNavigation = Navigation.Notes },
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu_burger_24),
                            contentDescription = "menu"
                        )
                    }
                },
                label = {
                    Spacer(
                        modifier = Modifier
                            .width(64.dp)
                            .height(2.dp)
                            .background(color = MaterialTheme.colorScheme.outline)
                    )
                },
                alwaysShowLabel = false
            )
        }
    }

}

private enum class Navigation {
    Start, Notes
}

private var currentNavigation by mutableStateOf(Navigation.Start)