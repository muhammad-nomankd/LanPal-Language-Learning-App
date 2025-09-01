package com.example.langpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langpal.data.repository.VocabularyRepository
import com.example.langpal.domain.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(private val repository: VocabularyRepository): ViewModel() {

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    init {
        getAvailableLanguages()
    }

    fun getAvailableLanguages(){
        viewModelScope.launch {
            repository.getAvailableLanguages().collect { languagesList->
                _languages.value = languagesList
            }
        }
    }
}