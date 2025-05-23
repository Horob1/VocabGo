package com.acteam.vocago.presentation.screen.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R

@Composable
fun ProfileCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp)
    ) {
        Box {
            Column {
                // 🔹 HEADER (vùng màu)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp) // chiều cao header
                        .background(Color(0xFFB4AEE8)),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = "Profile",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // 🔹 BODY
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp), // chừa chỗ cho avatar nổi lên
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Charles green",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Product photographer",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 🔹 AVATAR nổi giữa header và body
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 60.dp) // điều chỉnh cao/thấp
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.capy_vi),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardPreview() {
    ProfileCard()
}