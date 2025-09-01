package com.example.langpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langpal.data.repository.VocabularyRepository
import com.example.langpal.domain.model.Language
import com.example.langpal.domain.model.QuizQuestion
import com.example.langpal.domain.model.QuizResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: VocabularyRepository = VocabularyRepository()
) : ViewModel() {

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer: StateFlow<String?> = _selectedAnswer.asStateFlow()

    private val _showResult = MutableStateFlow(false)
    val showResult: StateFlow<Boolean> = _showResult.asStateFlow()

    private val _results = MutableStateFlow<List<QuizResult>>(emptyList())
    val results: StateFlow<List<QuizResult>> = _results.asStateFlow()

    private val _quizCompleted = MutableStateFlow(false)
    val quizCompleted: StateFlow<Boolean> = _quizCompleted.asStateFlow()

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadQuiz(languageCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _language.value = repository.getLanguageByCode(languageCode)

            repository.generateQuizQuestions(languageCode).collect { questionList ->
                _questions.value = questionList
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }

    fun submitAnswer() {
        val currentQuestion = getCurrentQuestion()
        val answer = _selectedAnswer.value

        if (currentQuestion != null && answer != null) {
            val isCorrect = answer == currentQuestion.correctOption
            val result = QuizResult(
                questionId = currentQuestion.id,
                selectedAnswer = answer,
                isCorrect = isCorrect
            )

            _results.value += result
            _showResult.value = true
        }
    }

    fun nextQuestion() {
        val questionsList = _questions.value
        if (_currentQuestionIndex.value < questionsList.size - 1) {
            _currentQuestionIndex.value += 1
            _selectedAnswer.value = null
            _showResult.value = false
        } else {
            _quizCompleted.value = true
        }
    }

    fun restartQuiz() {
        _currentQuestionIndex.value = 0
        _selectedAnswer.value = null
        _showResult.value = false
        _results.value = emptyList()
        _quizCompleted.value = false
    }

    fun getCurrentQuestion(): QuizQuestion? {
        val questionsList = _questions.value
        return if (_currentQuestionIndex.value < questionsList.size) {
            questionsList[_currentQuestionIndex.value]
        } else null
    }

    fun getProgress(): Float {
        val questionsList = _questions.value
        return if (questionsList.isNotEmpty()) {
            (_currentQuestionIndex.value + 1).toFloat() / questionsList.size.toFloat()
        } else 0f
    }

    fun calculateScore(): Int {
        val correctAnswers = _results.value.count { it.isCorrect }
        return if (_results.value.isNotEmpty()) {
            (correctAnswers * 100 / _results.value.size)
        } else 0
    }
}