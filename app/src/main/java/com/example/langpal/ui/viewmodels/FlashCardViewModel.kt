package com.example.langpal.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langpal.data.repository.VocabularyRepository
import com.example.langpal.domain.model.Language
import com.example.langpal.domain.model.VocabularyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FlashCardViewModel @Inject constructor(private val repository: VocabularyRepository): ViewModel() {

    private val _vocabulary = MutableStateFlow<List<VocabularyItem>>(emptyList())
    val vocabulary: StateFlow<List<VocabularyItem>> = _vocabulary.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped.asStateFlow()

    private val _language = MutableStateFlow<Language?>(null)
    val language: StateFlow<Language?> = _language.asStateFlow()




    fun loadVocabulary(landuageCode: String){
        viewModelScope.launch {
            _isLoading.value = true
            _language.value = repository.getLanguageByCode(landuageCode)
            repository.getVocabularyData(landuageCode).collect { vocab ->
                _vocabulary.value = vocab
                _isLoading.value = false
            }
        }
    }

    fun nextCard() {
        val currentVocab = _vocabulary.value
        if (_currentIndex.value < currentVocab.size - 1) {
            _currentIndex.value += 1
            _isFlipped.value = false
        }
    }

    fun previousCard() {
        if (_currentIndex.value > 0) {
            _currentIndex.value -= 1
            _isFlipped.value = false
        }
    }

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun resetCards() {
        _currentIndex.value = 0
        _isFlipped.value = false
    }

    fun getCurrentCard(): VocabularyItem? {
        val vocab = _vocabulary.value
        return if (_currentIndex.value < vocab.size) vocab[_currentIndex.value] else null
    }

    fun getProgress(): Float {
        val vocab = _vocabulary.value
        Log.d("vocab size", vocab.size.toString())
        return if (vocab.isNotEmpty()) {
            val value = (_currentIndex.value + 1).toFloat() / vocab.size.toFloat()
            Log.d("vocab size", value.toString())
            value

        } else 0f
    }
}