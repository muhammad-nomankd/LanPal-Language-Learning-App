package com.example.langpal.domain.model

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen: NavKey{

    @Serializable
    data object LanguageSelectionScreen: Screen
    @Serializable
    data class QuizScreen(val language: String): Screen
    @Serializable
    data class FlashCardScreen(val language: String): Screen

}