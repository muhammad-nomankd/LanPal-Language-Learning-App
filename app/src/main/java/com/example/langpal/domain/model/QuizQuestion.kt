package com.example.langpal.domain.model
data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctOption: String,
    val word: String,
)
