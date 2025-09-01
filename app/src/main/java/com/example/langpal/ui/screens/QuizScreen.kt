package com.example.langpal.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.langpal.domain.model.Language
import com.example.langpal.domain.model.QuizQuestion
import com.example.langpal.domain.model.QuizResult
import com.example.langpal.ui.viewmodels.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    languageCode: String,
    onNavigateToFlashcards: () -> Unit,
    onBackToLanguages: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuizViewModel = viewModel()
) {
    val questions by viewModel.questions.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val showResult by viewModel.showResult.collectAsState()
    val results by viewModel.results.collectAsState()
    val quizCompleted by viewModel.quizCompleted.collectAsState()
    val language by viewModel.language.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(languageCode) {
        viewModel.loadQuiz(languageCode)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (questions.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No vocabulary available for ${language?.name} quiz yet.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onBackToLanguages,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Languages")
            }
        }
        return
    }

    if (quizCompleted) {
        QuizCompletionScreen(
            score = viewModel.calculateScore(),
            results = results,
            questions = questions,
            onRestartQuiz = { viewModel.restartQuiz() },
            onNavigateToFlashcards = onNavigateToFlashcards,
            onBackToLanguages = onBackToLanguages,
            paddingValues = paddingValues
        )
    } else {
        QuizQuestionScreen(
            viewModel = viewModel,
            questions = questions,
            currentQuestionIndex = currentQuestionIndex,
            selectedAnswer = selectedAnswer,
            showResult = showResult,
            results = results,
            language = language,
            onBackToLanguages = onBackToLanguages,
            onNavigateToFlashcards = onNavigateToFlashcards,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun QuizQuestionScreen(
    viewModel: QuizViewModel,
    questions: List<QuizQuestion>,
    currentQuestionIndex: Int,
    selectedAnswer: String?,
    showResult: Boolean,
    results: List<QuizResult>,
    language: Language?,
    onBackToLanguages: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    paddingValues: PaddingValues
) {
    val currentQuestion = viewModel.getCurrentQuestion()
    val progress = viewModel.getProgress()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBackToLanguages
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Languages")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language?.flag ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${language?.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            OutlinedButton(
                onClick = onNavigateToFlashcards
            ) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Flashcards")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AssistChip(
                onClick = { },
                label = {
                    Text("Score: ${results.count { it.isCorrect }}/${results.size}")
                }
            )
        }

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question Card
        currentQuestion?.let { question ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    question.options.forEachIndexed { index, option ->
                        val isSelected = option == selectedAnswer
                        val isCorrect = option == question.correctOption
                        val isWrong = showResult && isSelected && !isCorrect

                        val buttonColors = when {
                            showResult && isCorrect -> ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White
                            )
                            isWrong -> ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                            isSelected -> ButtonDefaults.filledTonalButtonColors()
                            else -> ButtonDefaults.outlinedButtonColors()
                        }

                        Button(
                            onClick = {
                                if (!showResult) {
                                    viewModel.selectAnswer(option)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(vertical = 4.dp),
                            colors = buttonColors,
                            enabled = !showResult
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. ",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (!showResult) {
                Button(
                    onClick = { viewModel.submitAnswer() },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.widthIn(min = 200.dp)
                ) {
                    Text("Submit Answer")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Result Feedback
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        val isCorrect = selectedAnswer == currentQuestion?.correctOption
                        if (isCorrect) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Correct!",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF4CAF50)
                            )
                        } else {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Incorrect. The correct answer is \"${currentQuestion?.correctOption}\"",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.nextQuestion() },
                        modifier = Modifier.widthIn(min = 200.dp)
                    ) {
                        Text(
                            if (currentQuestionIndex < questions.size - 1) "Next Question" else "Finish Quiz"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizCompletionScreen(
    score: Int,
    results: List<QuizResult>,
    questions: List<QuizQuestion>,
    onRestartQuiz: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onBackToLanguages: () -> Unit,
    paddingValues: PaddingValues
) {
    val correctCount = results.count { it.isCorrect }
    val emoji = when {
        score >= 80 -> "🎉"
        score >= 60 -> "👏"
        else -> "💪"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Celebration Section
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "You scored $correctCount out of ${results.size} questions correctly",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
            // Score Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Score: $score%",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LinearProgressIndicator(
                        progress = score / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }

        items(questions.zip(results)) { (question, result) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = question.word,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (result.isCorrect) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Correct",
                            tint = Color(0xFF4CAF50)
                        )
                    } else {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Incorrect",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        item {
            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp,horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRestartQuiz,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Retry Quiz")
                }

                Button(
                    onClick = onNavigateToFlashcards,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Practice More")
                }
            }

            OutlinedButton(
                onClick = onBackToLanguages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp,horizontal = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back to Languages")
            }
        }
    }
}