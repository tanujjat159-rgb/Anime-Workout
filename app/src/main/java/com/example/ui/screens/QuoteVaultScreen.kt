package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnimeQuotesRepository
import com.example.model.AnimeQuote
import com.example.model.WorkoutPhase
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
fun QuoteVaultScreen(
    onPlayQuoteAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedAnimeFilter by remember { mutableStateOf("All") }

    val seriesList = remember {
        listOf("All") + AnimeQuotesRepository.getAnimeSeriesList()
    }

    val filteredQuotes = remember(searchQuery, selectedAnimeFilter) {
        AnimeQuotesRepository.allQuotes.filter { quote ->
            val matchesSeries = selectedAnimeFilter == "All" || quote.anime == selectedAnimeFilter
            val matchesSearch = searchQuery.isBlank() ||
                quote.quote.contains(searchQuery, ignoreCase = true) ||
                quote.character.contains(searchQuery, ignoreCase = true) ||
                quote.anime.contains(searchQuery, ignoreCase = true)
            matchesSeries && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AnimeOnyx)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        // Title
        Text(
            text = "MOTIVATION VAULT",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = AnimeWhite,
            letterSpacing = 1.sp
        )
        Text(
            text = "Legendary quotes to ignite your battle spirit during workouts.",
            fontSize = 12.sp,
            color = AnimeGrey
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search character or quote...", color = AnimeGrey) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = SuperSaiyanGold)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quote_search_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FlameOrange,
                unfocusedBorderColor = AnimeCardBorder,
                focusedTextColor = AnimeWhite,
                unfocusedTextColor = AnimeWhite,
                focusedContainerColor = AnimeDarkSurface,
                unfocusedContainerColor = AnimeDarkSurface
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(seriesList) { series ->
                val isSelected = selectedAnimeFilter == series
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedAnimeFilter = series },
                    label = {
                        Text(
                            text = series,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FlameOrange,
                        selectedLabelColor = AnimeWhite,
                        containerColor = AnimeSurfaceVariant,
                        labelColor = AnimeGrey
                    ),
                    modifier = Modifier.testTag("chip_$series")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quotes List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredQuotes, key = { it.id }) { quoteItem ->
                QuoteVaultItemCard(
                    quote = quoteItem,
                    onPlayAudio = { onPlayQuoteAudio(quoteItem.quote) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun QuoteVaultItemCard(
    quote: AnimeQuote,
    onPlayAudio: () -> Unit
) {
    val phaseColor = when (quote.phase) {
        WorkoutPhase.WORK -> FlameOrange
        WorkoutPhase.REST -> AuraCyan
        else -> SuperSaiyanGold
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AnimeCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = AnimeDarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(phaseColor.copy(alpha = 0.2f))
                            .border(1.dp, phaseColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = quote.character.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = phaseColor
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${quote.anime}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuperSaiyanGold
                    )
                }

                IconButton(
                    onClick = onPlayAudio,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(phaseColor.copy(alpha = 0.15f))
                        .testTag("play_quote_${quote.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Hear Quote Voice",
                        tint = phaseColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = phaseColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "\"${quote.quote}\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = AnimeWhite,
                    lineHeight = 18.sp
                )
            }

            if (!quote.japaneseRomanji.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "🇯🇵 ${quote.japaneseRomanji}",
                    fontSize = 10.sp,
                    color = AnimeGrey,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }
    }
}
