package com.example.trainer.appView.uiElements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.trainer.databases.Question
import com.example.trainer.ui.theme.MyStyles

@Composable
fun QuestionCard(
    modifier: Modifier = Modifier,
    question: Question,
    onClick: () -> Unit,
    isSelected: Boolean,
    onPress: () -> Unit
) {
    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.outlineVariant else Color.Transparent

    Card(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    onPress()
                },
                onTap = { onClick() }
            )
        },
        shape = ShapeDefaults.Large,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 10.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = question.question,
                style = MyStyles.textH1
            )

            Text(
                text = question.answer,
                style = MyStyles.textP
            )
        }
    }
}