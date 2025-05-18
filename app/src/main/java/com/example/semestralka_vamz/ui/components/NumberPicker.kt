package com.example.semestralka_vamz.ui.components

// Importy pre layout, LazyColumn, snapping, štýly a výpočty
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// Komponent na výber čísla pomocou "rolovania" – ako koliesko
@Composable
fun NumberPicker(
    selected: Int,                    // aktuálne vybrané číslo
    onSelect: (Int) -> Unit,         // callback pri zmene čísla
    range: IntRange = 0..9,          // rozsah možných hodnôt
    modifier: Modifier = Modifier    // voliteľný modifier
) {
    val visibleItemsCount = 5                     // počet viditeľných položiek na výšku
    val centerItemOffset = visibleItemsCount / 2  // index centrálnej položky (pre výpočet)
    val itemHeight = 40.dp                        // výška jednej položky
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    // Vytvára sa zoznam s opakujúcimi sa číslami pre efekt "nekonečného" scrollovania
    val repeatedList = remember {
        val values = range.toList()
        List(1000) { values[it % values.size] }
    }

    // Počiatočný index nastavíme do stredu, aby sme mohli scrollovať hore aj dolu
    val initialIndex = 500 + selected
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    // Dynamické sledovanie aktuálnej hodnoty v strede pomocou derivedStateOf
    val currentIndex by remember {
        derivedStateOf {
            val exactIndex = listState.firstVisibleItemIndex +
                    centerItemOffset + (listState.firstVisibleItemScrollOffset / itemHeightPx).roundToInt()
            repeatedList[exactIndex % repeatedList.size]
        }
    }

    // Spustenie callbacku pri zmene čísla v strede
    LaunchedEffect(currentIndex) {
        onSelect(currentIndex)
    }

    // Obal s výškou podľa počtu položiek a šírkou fixne
    Box(
        modifier = modifier
            .height(itemHeight * visibleItemsCount)
            .width(64.dp),
        contentAlignment = Alignment.Center
    ) {
        // Rámček označujúci stred (vybrané číslo)
        Box(
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
        )

        // Zoznam s číslami, ktoré je možné rolovať
        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState), // snapping efekt
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(repeatedList) { _, number ->
                Box(
                    modifier = Modifier.height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
