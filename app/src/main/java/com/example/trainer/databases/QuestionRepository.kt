package com.example.trainer.databases

class QuestionRepository(private val questionDao: QuestionDao) {

    suspend fun insert(question: Question) {
        questionDao.insert(question)
    }

    suspend fun update(question: Question) {
        questionDao.update(question)
    }

    suspend fun delete(question: Question) {
        questionDao.delete(question)
    }
    suspend fun deleteQuestions(ids: List<Int>) {
        questionDao.deleteQuestions(ids)
    }

    suspend fun getQuestionById(questionId: Int): Question {
        return questionDao.getQuestionById(questionId)
    }

    suspend fun deleteAll() {
        questionDao.deleteAll()
    }

    suspend fun getAllQuestions() : List<Question> {
        return questionDao.getAllQuestions()
    }
}