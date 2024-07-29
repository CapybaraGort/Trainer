package com.example.trainer.databases

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "question_table")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var question: String,
    var answer: String
)