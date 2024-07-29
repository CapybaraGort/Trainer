package com.example.trainer.destionations

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class SaveNote(val questionId: Int)

@Serializable
object Training