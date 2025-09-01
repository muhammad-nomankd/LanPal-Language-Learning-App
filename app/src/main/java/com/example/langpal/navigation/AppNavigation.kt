package com.example.langpal.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.langpal.domain.model.Screen
import com.example.langpal.ui.screens.FlashcardsScreen
import com.example.langpal.ui.screens.LanguageSelectionScreen
import com.example.langpal.ui.screens.QuizScreen

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val backStack = rememberNavBackStack(Screen.LanguageSelectionScreen)

    NavDisplay(
        backStack = backStack, onBack = { count ->
            repeat(count) {
                if (backStack.isNotEmpty()) {
                    backStack.removeLastOrNull()
                }
            }
        }, entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        entryProvider = entryProvider {
            entry<Screen.LanguageSelectionScreen> {
                LanguageSelectionScreen(onLanguageSelected = { text ->
                    backStack.add(Screen.FlashCardScreen(text))
                }, innerPadding)
            }
            entry<Screen.QuizScreen> { entry ->
                val languageCode = entry.language
                QuizScreen(
                    languageCode = languageCode,
                    onNavigateToFlashcards = { backStack.add(Screen.FlashCardScreen(languageCode)) },
                    onBackToLanguages = { backStack.add(Screen.LanguageSelectionScreen) },
                    paddingValues = innerPadding

                    )
            }
            entry<Screen.FlashCardScreen> { entry ->
                val languageCode = entry.language
                FlashcardsScreen(
                    languageCode = languageCode,
                    onNavigateToQuiz = { backStack.add(Screen.QuizScreen(languageCode)) },
                    onBackToLanguages = { backStack.removeLastOrNull() },
                    paddingValues = innerPadding
                )
            }
        })
}