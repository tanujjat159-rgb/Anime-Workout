package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun ScouterHudOverlay(
    currentPowerLevel: Int,
    burnedCalories: Int,
    isMuted: Boolean,
    isVoiceEnabled: Boolean,
    onToggleMute: () -> Unit,
    onToggleVoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AnimeDarkSurface.copy(alpha = 0.85f))
            .border(1.dp, SuperSaiyanGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Scouter Power Gauge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Power Level",
                    tint = SuperSaiyanGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "SCOUTER POWER",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AnimeGrey,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = if (currentPowerLevel >= 9000) "OVER 9000! ($currentPowerLevel)" else "$currentPowerLevel",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace,
                        color = SuperSaiyanGold
                    )
                }
            }

            // Center: Calories Burned
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Calories",
                    tint = FlameOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "BURN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AnimeGrey
                    )
                    Text(
                        text = "$burnedCalories kcal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AnimeWhite
                    )
                }
            }

            // Right: Audio / Voice Toggles
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onToggleVoice,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("toggle_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Toggle Anime Voice",
                        tint = if (isVoiceEnabled) SuperSaiyanGold else AnimeGrey.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("toggle_sound_button")
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Toggle Sound",
                        tint = if (!isMuted) FlameOrange else AnimeGrey.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
