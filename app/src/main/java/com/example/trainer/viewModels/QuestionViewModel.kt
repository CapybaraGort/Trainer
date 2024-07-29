package com.example.trainer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.trainer.databases.Question
import com.example.trainer.databases.QuestionDatabase
import com.example.trainer.databases.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val application: Application) :
    AndroidViewModel(application) {

    private val repository: QuestionRepository
    private val _allQuestions = MutableLiveData<List<Question>>()
    val allQuestions: LiveData<List<Question>> get() = _allQuestions

    init {
        val questionDao = QuestionDatabase.getDatabase(application).questionDao()
        repository = QuestionRepository(questionDao)
        fetchAllQuestions()
    }

    fun insert(question: Question) = viewModelScope.launch {
        repository.insert(question)
        fetchAllQuestions()
    }

    fun update(question: Question) = viewModelScope.launch {
        repository.update(question)
        fetchAllQuestions()
    }

    fun getQuestionById(questionId: Int): LiveData<Question> {
        return liveData {
            emit(repository.getQuestionById(questionId))
        }
    }

    fun delete(question: Question) = viewModelScope.launch {
        repository.delete(question)
        fetchAllQuestions()
    }

    fun deleteQuestions(ids: List<Int>) = viewModelScope.launch {
        repository.deleteQuestions(ids)
        fetchAllQuestions()
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        fetchAllQuestions()
    }

    private fun fetchAllQuestions() = viewModelScope.launch {
        _allQuestions.value = repository.getAllQuestions()
    }

}