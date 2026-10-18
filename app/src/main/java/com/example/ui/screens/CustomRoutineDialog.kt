package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.WorkoutIntensity
import com.example.ui.theme.AnimeCardBorder
import com.example.ui.theme.AnimeDarkSurface
import com.example.ui.theme.AnimeGrey
import com.example.ui.theme.AnimeOnyx
import com.example.ui.theme.AnimeSurfaceVariant
import com.example.ui.theme.AnimeWhite
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuperSaiyanGold

@Composable
fun CustomRoutineDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, workSecs: Int, restSecs: Int, rounds: Int, animeTag: String, intensity: WorkoutIntensity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var animeTag by remember { mutableStateOf("Custom Arc") }
    var workSecs by remember { mutableIntStateOf(30) }
    var restSecs by remember { mutableIntStateOf(15) }
    var rounds by remember { mutableIntStateOf(6) }
    var selectedIntensity by remember { mutableStateOf(WorkoutIntensity.STANDARD) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, FlameOrange.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "FORGE ANIME ROUTINE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = SuperSaiyanGold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine Name (e.g. Bankai Rush)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_routine_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlameOrange,
                        unfocusedBorderColor = AnimeCardBorder,
                        focusedTextColor = AnimeWhite,
                        unfocusedTextColor = AnimeWhite,
                        focusedLabelColor = FlameOrange,
                        unfocusedLabelColor = AnimeGrey
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = animeTag,
                    onValueChange = { animeTag = it },
                    label = { Text("Anime Inspiration (e.g. Bleach)") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_routine_anime_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuraCyan,
                        unfocusedBorderColor = AnimeCardBorder,
                        focusedTextColor = AnimeWhite,
                        unfocusedTextColor = AnimeWhite,
                        focusedLabelColor = AuraCyan,
                        unfocusedLabelColor = AnimeGrey
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Intensity Tier Selection
                Text(
                    text = "TRAINING INTENSITY & XP MULTIPLIER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AnimeGrey,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WorkoutIntensity.entries.forEach { intensity ->
                        val isSelected = selectedIntensity == intensity
                        val intensityColor = Color(intensity.colorHex)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedIntensity = intensity },
                            label = {
                                Text(
                                    text = "${intensity.label} (${intensity.multiplier}x)",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = intensityColor,
                                selectedLabelColor = AnimeOnyx,
                                containerColor = AnimeSurfaceVariant,
                                labelColor = AnimeGrey
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Work Seconds Selector
                NumberSelectorRow(
                    label = "Work (Seconds)",
                    value = workSecs,
                    min = 10,
                    max = 180,
                    step = 5,
                    color = FlameOrange,
                    onValueChange = { workSecs = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rest Seconds Selector
                NumberSelectorRow(
                    label = "Rest (Seconds)",
                    value = restSecs,
                    min = 5,
                    max = 120,
                    step = 5,
                    color = AuraCyan,
                    onValueChange = { restSecs = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rounds Selector
                NumberSelectorRow(
                    label = "Total Rounds",
                    value = rounds,
                    min = 1,
                    max = 30,
                    step = 1,
                    color = SuperSaiyanGold,
                    onValueChange = { rounds = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Estimated XP preview
                val estDuration = workSecs * rounds
                val estXp = (estDuration * 4 * selectedIntensity.multiplier).toInt() + (rounds * 40) + selectedIntensity.clearBonusXp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AnimeSurfaceVariant, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = SuperSaiyanGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Est. Power XP:", fontSize = 11.sp, color = AnimeGrey)
                    }
                    Text("~$estXp XP", fontSize = 12.sp, fontWeight = FontWeight.Black, color = SuperSaiyanGold)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("cancel_custom_routine_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AnimeGrey)
                    ) {
                        Text("CANCEL")
                    }

                    Button(
                        onClick = {
                            val routineTitle = if (title.isNotBlank()) title else "Anime Power Workout"
                            onSave(routineTitle, workSecs, restSecs, rounds, animeTag, selectedIntensity)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_custom_routine_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FlameOrange,
                            contentColor = AnimeWhite
                        )
                    ) {
                        Text("FORGE ROUTINE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberSelectorRow(
    label: String,
    value: Int,
    min: Int,
    max: Int,
    step: Int,
    color: Color,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AnimeSurfaceVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, fontSize = 12.sp, color = AnimeGrey)
            Text(text = "$value", fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilledIconButton(
                onClick = { if (value - step >= min) onValueChange(value - step) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = AnimeDarkSurface),
                modifier = Modifier.testTag("decrease_${label.replace(" ", "_")}")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AnimeWhite)
            }

            FilledIconButton(
                onClick = { if (value + step <= max) onValueChange(value + step) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = color),
                modifier = Modifier.testTag("increase_${label.replace(" ", "_")}")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase", tint = AnimeOnyx)
            }
        }
    }
}
