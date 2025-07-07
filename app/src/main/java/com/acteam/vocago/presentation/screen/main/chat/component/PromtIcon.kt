package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face3
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.PsychologyAlt
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.ui.graphics.vector.ImageVector


val personaIcons: Map<Int, List<ImageVector>> = mapOf(
    // Cristiano Ronaldo
    1 to listOf(
        Icons.Default.Face,                // Casual
        Icons.Default.School,              // Trainer
        Icons.Default.TipsAndUpdates,      // Motivator
        Icons.Default.Star,               // Legend Talk
        Icons.Default.Face3,                // Woman
    ),

    // Lionel Messi
    2 to listOf(
        Icons.Default.Face,                // Humble
        Icons.Default.Favorite,            // Family man
        Icons.Default.TagFaces,            // Playful
        Icons.Default.ThumbUp,             // Motivator
        Icons.Default.HistoryEdu           // Retrospective
    ),

    // Donald Trump
    3 to listOf(
        Icons.Default.Gavel,               // Political
        Icons.Default.AttachMoney,         // Businessman
        Icons.Default.LiveTv,              // TV Host
        Icons.Default.SentimentVerySatisfied, // Humorous
        Icons.Default.Campaign             // Campaign Mode
    ),

    // Dev Chatbot
    4 to listOf(
        Icons.Default.Code,                // Tech
        Icons.Default.BugReport,           // Debug Mode
        Icons.Default.School,              // Teacher
        Icons.Default.DataObject,          // Architecture
        Icons.Default.Timelapse             // Startup Dev
    ),

    // Daily Chatbot
    5 to listOf(
        Icons.AutoMirrored.Filled.Chat,                // Friendly
        Icons.Default.Mood,                // Joker
        Icons.Default.PsychologyAlt,       // Philosopher
        Icons.Default.VolunteerActivism,   // Supportive
        Icons.Default.TaskAlt              // Productive
    ),

    // GrammarBot
    6 to listOf(
        Icons.Default.Spellcheck,          // Grammar Expert
        Icons.Default.QuestionAnswer,      // Quiz Mode
        Icons.Default.Build,               // Fixer
        Icons.Default.School,              // IELTS Coach
        Icons.Default.SportsEsports        // Interactive Game
    ),

    // Mark Zuckerberg
    7 to listOf(
        Icons.Default.Memory,              // Technical
        Icons.AutoMirrored.Filled.TrendingUp,          // Investor
        Icons.Default.Face,                // Casual
        Icons.Default.EmojiObjects,        // Visionary
        Icons.Default.QuestionAnswer       // Q&A Mode
    ),

    // Jack (Singer)
    8 to listOf(
        Icons.Default.Favorite,            // Emotional
        Icons.Default.SentimentVerySatisfied, // Playful
        Icons.Default.Mic,                 // Performer
        Icons.Default.Style,               // Poetic
        Icons.Default.ChildCare               // Down to Earth
    ),

    // Faker (Gamer)
    9 to listOf(
        Icons.Default.Analytics,           // Analytical
        Icons.Default.School,              // Coach
        Icons.Default.SportsEsports,       // Casual gaming
        Icons.Default.Groups,              // Team Leader
        Icons.Default.VideogameAsset       // Streaming Mode
    )
)
