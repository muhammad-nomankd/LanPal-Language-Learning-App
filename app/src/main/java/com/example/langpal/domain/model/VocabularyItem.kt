package com.example.langpal.domain.model

data class VocabularyItem(
    val id:String,
    val word:String,
    val translation: String,
    val pronunciation: String? = null
)
