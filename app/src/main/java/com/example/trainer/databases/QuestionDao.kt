package com.example.trainer.databases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionDao {
    @Insert
    suspend fun insert(question: Question)

    @Update
    suspend fun update(question: Question)

    @Delete
    suspend fun delete(question: Question)

    @Query("DELETE FROM question_table WHERE id IN (:ids)")
    suspend fun deleteQuestions(ids: List<Int>)

    @Query("SELECT * FROM question_table WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): Question

    @Query("DELETE FROM question_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM question_table")
    suspend fun getAllQuestions() : List<Question>
}