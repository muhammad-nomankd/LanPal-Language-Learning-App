package com.example.langpal.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.langpal.domain.model.VocabularyItem
import com.example.langpal.ui.viewmodels.FlashCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    languageCode: String,
    onNavigateToQuiz: () -> Unit,
    onBackToLanguages: () -> Unit,
    viewModel: FlashCardViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val vocabulary by viewModel.vocabulary.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val language by viewModel.language.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(languageCode) {
        viewModel.loadVocabulary(languageCode)
    }

    val currentCard = viewModel.getCurrentCard()
    val progress = viewModel.getProgress()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (vocabulary.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No vocabulary available for ${language?.name} yet.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onBackToLanguages, modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Languages")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                    text = language?.flag ?: "", style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${language?.name}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            OutlinedButton(
                onClick = onNavigateToQuiz
            ) {
                Icon(Icons.Default.Create, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Quiz")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Section
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Word ${currentIndex + 1} of ${vocabulary.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(
                onClick = { viewModel.resetCards() }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset")
            }
        }

        LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Flashcard
        currentCard?.let { card ->
            FlipCard(
                card = card,
                isFlipped = isFlipped,
                onFlip = { viewModel.flipCard() },
                languageName = language?.name ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation Controls
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { viewModel.previousCard() }, enabled = currentIndex > 0
            ) {
                Icon(Icons.Default.Create, contentDescription = null)
                Text("Previous")
            }

            // Dots Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(vocabulary.size) { index ->
                    Box(
                        modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).let {
                                if (index == currentIndex) {
                                    it
                                        .clip(RoundedCornerShape(4.dp))
                                        .then(Modifier.fillMaxSize())
                                } else {
                                    it
                                }
                            }) {
                        Surface(
                            color = if (index == currentIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }, modifier = Modifier.fillMaxSize()
                        ) {}
                    }
                }
            }

            OutlinedButton(
                onClick = { viewModel.nextCard() }, enabled = currentIndex < vocabulary.size - 1
            ) {
                Text("Next")
                Icon(Icons.Default.Create, contentDescription = null)
            }
        }

        // Completion Message
        if (currentIndex == vocabulary.size - 1) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 You've completed all flashcards!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Ready to test your knowledge?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = onNavigateToQuiz
                    ) {
                        Text("Take Quiz")
                    }
                }
            }
        }
    }
}

@Composable
private fun FlipCard(
    card: VocabularyItem,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    languageName: String,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f, animationSpec = tween(600), label = "flip"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { onFlip() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !isFlipped,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = card.word,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    card.pronunciation?.let { pronunciation ->
                        Text(
                            text = "/$pronunciation/",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Text(
                        text = "Tap to see translation",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }


            androidx.compose.animation.AnimatedVisibility(
                visible = isFlipped,
                enter = fadeIn(animationSpec = tween(300, delayMillis = 300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(24.dp)
                        .graphicsLayer { rotationY = 180f }) {
                    Text(
                        text = card.translation,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Tap to see $languageName word",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}