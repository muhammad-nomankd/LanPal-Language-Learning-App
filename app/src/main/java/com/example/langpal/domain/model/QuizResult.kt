package com.example.langpal.domain.model

data class QuizResult(
    val questionId: String,
    val selectedAnswer: String,
    val isCorrect: Boolean
)
