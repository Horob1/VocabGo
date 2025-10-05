package com.acteam.vocago.presentation.screen.user.ranking.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.acteam.vocago.data.model.RankingEntry

@Composable
fun RankingItem(
    rank: Int,
    userRanking: RankingEntry,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    val isTop3 = rank in 1..3

    val borderStroke = if (isTop3) {
        BorderStroke(
            width = 2.dp,
            brush = when (rank) {
                1 -> Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA500)))
                2 -> Brush.linearGradient(listOf(Color(0xFFC0C0C0), Color(0xFF808080)))
                else -> Brush.linearGradient(listOf(Color(0xFFCD7F32), Color(0xFF8B4513)))
            }
        )
    } else null

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isTop3) Color(0xFFFEF9C3) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isTop3) 10.dp else 2.dp
        ),
        shape = RoundedCornerShape(16.dp),
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar / Rank
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = when (rank) {
                                1 -> listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                                2 -> listOf(Color(0xFFC0C0C0), Color(0xFF808080))
                                3 -> listOf(Color(0xFFCD7F32), Color(0xFF8B4513))
                                else -> listOf(Color(0xFFF3F4F6), Color(0xFFE5E7EB))
                            }
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isTop3) {
                    Icon(
                        imageVector = when (rank) {
                            1 -> Icons.Default.EmojiEvents
                            2 -> Icons.Default.WorkspacePremium
                            else -> Icons.Default.MilitaryTech
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Text(
                        text = rank.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = userRanking.userId.username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // streak
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Color(0xFFEF4444).copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${userRanking.currentStreak}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151)
                        )
                    }

                    // TOEIC point
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Color(0xFF10B981).copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${userRanking.maxToeicPoint}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Total points
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            Color(0xFFF59E0B).copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userRanking.totalPoints.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }
        }
    }
}