package com.example.langpal.data.repository

import com.example.langpal.domain.model.Language
import com.example.langpal.domain.model.QuizQuestion
import com.example.langpal.domain.model.VocabularyItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class VocabularyRepository {


    val languages = listOf(
        Language("English", "🇬🇧", "en"),
        Language("Spanish", "🇪🇸", "es"),
        Language("German", "🇩🇪", "de"),
        Language("Italian", "🇮🇹", "it"),
        Language("French", "🇫🇷", "fr"),
        Language("Portuguese", "🇵🇹", "pt"),
        Language("Russian", "🇷🇺", "ru"),
        Language("Chinese", "🇨🇳", ""),
        Language("Japanese", "🇯🇵", "ja"),
        Language("Korean", "🇰🇷", "ko"),
        Language("Arabic", "🇸🇦", "ar"),
        Language("Hindi", "🇮🇳", "hi"),
        Language("Bengali", "🇧🇩", "bn"),
        Language("Punjabi", "🇵🇰", "pa"),
        Language("Pashto", "🇵🇰", "ps"),
        Language("Urdu", "🇵🇰", "ur"),
        Language("Tamil", "🇮🇳", "ta"),
        Language("Telugu", "🇮🇳", "te"),
        Language("Marathi", "🇮🇳", "mr"),
        Language("Gujarati", "🇮🇳", "gu"),
        Language("Polish", "🇵🇱", "pl"),
        Language("Dutch", "🇳🇱", "nl"),
        Language("Swedish", "🇸🇪", "sv"),
    )


    val vocabulary = mapOf(
        "en" to listOf(
            VocabularyItem("en1", "Hello", "Hello", "həˈləʊ"),
            VocabularyItem("en2", "Thank you", "Thank you", "θæŋk juː"),
            VocabularyItem("en3", "Goodbye", "Goodbye", "ɡʊdˈbaɪ"),
            VocabularyItem("en4", "Please", "Please", "pliːz"),
            VocabularyItem("en5", "Yes", "Yes", "jɛs"),
            VocabularyItem("en6", "No", "No", "nəʊ"),
            VocabularyItem("en7", "Water", "Water", "ˈwɔːtə"),
            VocabularyItem("en8", "Food", "Food", "fuːd"),
            VocabularyItem("en9", "Friend", "Friend", "frɛnd"),
            VocabularyItem("en10", "Love", "Love", "lʌv"),
        ),
        "es" to listOf(
            VocabularyItem("es1", "Hola", "Hello", "ˈola"),
            VocabularyItem("es2", "Gracias", "Thank you", "ˈɡɾasjas"),
            VocabularyItem("es3", "Adiós", "Goodbye", "aðiˈos"),
            VocabularyItem("es4", "Por favor", "Please", "poɾ faˈβoɾ"),
            VocabularyItem("es5", "Sí", "Yes", "si"),
            VocabularyItem("es6", "No", "No", "no"),
            VocabularyItem("es7", "Agua", "Water", "ˈaɣwa"),
            VocabularyItem("es8", "Comida", "Food", "koˈmiða"),
            VocabularyItem("es9", "Amigo", "Friend", "aˈmiɣo"),
            VocabularyItem("es10", "Amor", "Love", "aˈmoɾ"),
        ),
        "de" to listOf(
            VocabularyItem("de1", "Hallo", "Hello", "ˈhalo"),
            VocabularyItem("de2", "Danke", "Thank you", "ˈdaŋkə"),
            VocabularyItem("de3", "Tschüss", "Goodbye", "tʃʏs"),
            VocabularyItem("de4", "Bitte", "Please", "ˈbɪtə"),
            VocabularyItem("de5", "Ja", "Yes", "ja"),
            VocabularyItem("de6", "Nein", "No", "naɪn"),
            VocabularyItem("de7", "Wasser", "Water", "ˈvasɐ"),
            VocabularyItem("de8", "Essen", "Food", "ˈɛsn̩"),
            VocabularyItem("de9", "Freund", "Friend", "fʁɔʏnt"),
            VocabularyItem("de10", "Liebe", "Love", "ˈliːbə"),

        ),
        "fr" to listOf(
            VocabularyItem("fr1", "Bonjour", "Hello", "bɔ̃ʒuʁ"),
            VocabularyItem("fr2", "Merci", "Thank you", "mɛʁsi"),
            VocabularyItem("fr3", "Au revoir", "Goodbye", "o ʁəvwaʁ"),
            VocabularyItem("fr4", "S’il vous plaît", "Please", "sil vu plɛ"),
            VocabularyItem("fr5", "Oui", "Yes", "wi"),
            VocabularyItem("fr6", "Non", "No", "nɔ̃"),
            VocabularyItem("fr7", "Eau", "Water", "o"),
            VocabularyItem("fr8", "Nourriture", "Food", "nuʁityʁ"),
            VocabularyItem("fr9", "Ami", "Friend", "ami"),
            VocabularyItem("fr10", "Amour", "Love", "amuʁ"),

        ),
        "ur" to listOf(
            VocabularyItem("ur1", "سلام", "Hello", "Salaam"),
            VocabularyItem("ur2", "شکریہ", "Thank you", "Shukriya"),
            VocabularyItem("ur3", "خدا حافظ", "Goodbye", "Khuda Hafiz"),
            VocabularyItem("ur4", "براہ کرم", "Please", "Barah-e-Karam"),
            VocabularyItem("ur5", "جی ہاں", "Yes", "Ji Haan"),
            VocabularyItem("ur6", "نہیں", "No", "Nahi"),
            VocabularyItem("ur7", "پانی", "Water", "Pani"),
            VocabularyItem("ur8", "کھانا", "Food", "Khana"),
            VocabularyItem("ur9", "دوست", "Friend", "Dost"),
            VocabularyItem("ur10", "محبت", "Love", "Mohabbat"),

        )
    )

    fun getAvailableLanguages(): Flow<List<Language>>{
         return flowOf(languages)
    }

    fun getLanguageByCode(code: String): Language{
        return languages.find { it.code == code }?: throw IllegalArgumentException("Language not found")
    }

    fun getVocabularyData(languageCod: String): Flow<List<VocabularyItem>>{
        return flowOf(vocabulary[languageCod]?:emptyList())
    }
    fun generateQuizQuestions(languageCode: String): Flow<List<QuizQuestion>> {
        val vocabulary = vocabulary[languageCode] ?: emptyList()
        if (vocabulary.isEmpty()) return flowOf(emptyList())

        val questionsToGenerate = minOf(5, vocabulary.size)
        val selectedWords = vocabulary.take(questionsToGenerate)

        val questions = selectedWords.map { item ->
            // Generate wrong answers from other vocabulary items
            val wrongAnswers = vocabulary
                .filter { it.id != item.id }
                .map { it.translation }
                .take(3)

            // Shuffle options
            val options = (listOf(item.translation) + wrongAnswers).shuffled()

            QuizQuestion(
                id = item.id,
                question = "What does \"${item.word}\" mean?",
                options = options,
                word = item.word,
                correctOption = item.translation,
            )
        }

        return flowOf(questions)
    }


}