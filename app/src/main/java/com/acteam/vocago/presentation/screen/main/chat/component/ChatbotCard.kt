package com.acteam.vocago.presentation.screen.main.chat.component

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType

@Composable
fun UserProfileCard(
    imageResId: Int,
    status: String,
    name: String,
    jobTitle: String,
    modifier: Modifier = Modifier,
    onclick: () -> Unit,
    onToggleLocationClick: () -> Unit,
    onVideoCallClick: () -> Unit
) {
    val deviceType = getDeviceType()

    val isCompact = deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletLandscape
    val isTabletLandscape = deviceType == DeviceType.TabletLandscape

    val nameFontSize = if (isCompact) 20.sp else 24.sp
    val statusFontSize = if (isCompact) 12.sp else 14.sp
    val jobFontSize = if (isCompact) 14.sp else 16.sp
    val iconSize = if (isCompact) 20.dp else 24.dp
    val buttonSize = if (isCompact) 40.dp else 48.dp
    val buttonHeight = if (isCompact) 40.dp else 48.dp
    val contentPadding = if (isCompact) 16.dp else 20.dp
    val sectionSpacing = if (isCompact) 6.dp else 8.dp

    val imageHeight = when (deviceType) {
        DeviceType.TabletPortrait -> 480.dp
        DeviceType.TabletLandscape -> 240.dp
        else -> 240.dp
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Image section
            Box(
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                startY = 0.6f
                            )
                        )
                )
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            ) {
                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = sectionSpacing)
                ) {
                    val statusColor = if (status.equals("online", ignoreCase = true)) {
                        Color(0xFF4CAF50)
                    } else {
                        Color(0xFFF44336)
                    }

                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(statusColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status.replaceFirstChar { it.uppercase() },
                        color = Color.Gray,
                        fontSize = statusFontSize,
                        fontWeight = FontWeight.Medium
                    )
                }

                // User info section
                Column(
                    modifier = Modifier.padding(bottom = sectionSpacing + 4.dp)
                ) {
                    // Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Name",
                            tint = Color.Black,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = name,
                            fontSize = nameFontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    // Job title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "Job",
                            tint = Color.Gray,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = jobTitle,
                            color = Color.Gray,
                            fontSize = jobFontSize,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Action buttons
                if (isTabletLandscape) {
                    // Add spacing for tablet landscape to prevent overlap
                    Spacer(modifier = Modifier.height(8.dp))

                    // Tablet landscape layout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onToggleLocationClick,
                            modifier = Modifier
                                .size(buttonHeight)
                                .background(Color(0xFFF5F5F5), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        IconButton(
                            onClick = onVideoCallClick,
                            modifier = Modifier
                                .size(buttonHeight)
                                .background(Color(0xFFF5F5F5), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = "Video Call",
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        Button(
                            onClick = onclick,
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            ),
                            modifier = Modifier
                                .height(buttonHeight)
                                .weight(1f)
                        ) {
                            Text(
                                stringResource(R.string.text_chat),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    // Mobile and tablet portrait layout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onToggleLocationClick,
                            modifier = Modifier
                                .size(buttonSize)
                                .background(Color(0xFFF5F5F5), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        IconButton(
                            onClick = onVideoCallClick,
                            modifier = Modifier
                                .size(buttonSize)
                                .background(Color(0xFFF5F5F5), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = "Video Call",
                                tint = Color(0xFF666666),
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        IconButton(
                            onClick = onclick,
                            modifier = Modifier
                                .size(buttonSize)
                                .background(Color.Black, CircleShape)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "Chat",
                                tint = Color.White,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }
                }
            }
        }
    }
}