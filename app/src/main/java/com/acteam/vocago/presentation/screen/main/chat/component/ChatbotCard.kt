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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
) {
    val deviceType = getDeviceType()

    val isCompact = deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletLandscape
    val isTabletLandscape = deviceType == DeviceType.TabletLandscape

    val nameFontSize = if (isCompact) 24.sp else 30.sp
    val statusFontSize = if (isCompact) 13.sp else 16.sp
    val jobFontSize = if (isCompact) 14.sp else 18.sp
    val paddingHorizontal = if (isCompact) 20.dp else 32.dp
    val iconSize = if (isCompact) 18.dp else 24.dp
    val buttonHeight = if (isCompact) 44.dp else 52.dp
    val sectionSpacing = if (isCompact) 6.dp else 10.dp
    val imageHeight = if (deviceType == DeviceType.TabletPortrait) 360.dp else 240.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            // Ảnh đại diện
            Box(
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                                startY = 100f
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(if (deviceType == DeviceType.TabletPortrait) sectionSpacing + 16.dp else sectionSpacing))

            Row(
                modifier = Modifier.padding(horizontal = paddingHorizontal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statusColor =
                    if (status.equals("online", ignoreCase = true)) Color(0xFF4CAF50) else Color(
                        0xFFF44336
                    )

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = status,
                    color = Color.Gray,
                    fontSize = statusFontSize,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(if (deviceType == DeviceType.TabletPortrait) sectionSpacing + 32.dp else sectionSpacing))

            Row(
                modifier = Modifier
                    .padding(horizontal = paddingHorizontal)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Name Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = name,
                            fontSize = nameFontSize,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "Job Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(iconSize)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = jobTitle,
                            color = Color.Gray,
                            fontSize = jobFontSize,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                if (isTabletLandscape) {
                    Button(
                        onClick = onclick,
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                        modifier = Modifier.height(buttonHeight)
                    ) {
                        Text(
                            "Chat",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(sectionSpacing + 16.dp))

            // Hàng nút Map, Call, Chat (ẩn nếu tablet ngang)
            if (!isTabletLandscape) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = paddingHorizontal, vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* Map action */ },
                        modifier = Modifier
                            .size(buttonHeight)
                            .background(Color(0xFFF0F0F0), shape = CircleShape)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Map",
                            tint = Color(0xFF616161),
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = { /* Call action */ },
                        modifier = Modifier
                            .size(buttonHeight)
                            .background(Color(0xFFF0F0F0), shape = CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color(0xFF616161),
                            modifier = Modifier.size(iconSize)
                        )
                    }

                    Button(
                        onClick = onclick,
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                        modifier = Modifier
                            .height(buttonHeight)
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            "Chat",
                            color = Color.White,
                            fontSize = if (deviceType == DeviceType.Mobile) 16.sp else 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
