package com.example.trainer.appView

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trainer.R
import com.example.trainer.destionations.ListOfWords
import com.example.trainer.destionations.Training
import com.example.trainer.ui.theme.MyStyles
import com.example.trainer.ui.theme.displayFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(modifier: Modifier = Modifier, navHostController: NavHostController, isQuestionsEmpty: Boolean = true) {
    val scope = rememberCoroutineScope()
    val toast = Toast(LocalContext.current)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(modifier = Modifier.offset((-16).dp),
                title = {
                    Text(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp,
                                    topEnd = 16.dp
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = stringResource(id = R.string.app_name),
                        fontFamily = displayFontFamily,
                        fontSize = 24.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center
                    )
                },
                windowInsets = WindowInsets.statusBars,
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.size(64.dp))
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    scope.launch {
                        if(isQuestionsEmpty) {
                            toast.setText(R.string.no_notes)
                            toast.show()
                        } else navHostController.navigate(Training)
                    }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(220.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = stringResource(id = R.string.start_train_btn),
                    style = MyStyles.textP
                )
            }

            OutlinedButton(
                modifier = Modifier.width(220.dp),
                onClick = { navHostController.navigate(ListOfWords) },
            ) {
                Text(
                    text = stringResource(id = R.string.list_of_notes),
                    style = MyStyles.textP
                )
            }
        }
    }
}