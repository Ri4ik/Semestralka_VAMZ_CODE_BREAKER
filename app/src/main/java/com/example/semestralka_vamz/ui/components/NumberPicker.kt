package com.example.semestralka_vamz.ui.components

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

@Composable
fun NumberPicker(
    selected: Int,
    onSelect: (Int) -> Unit,
    range: IntRange = 0..9
) {
    val visibleItemsCount = 5
    val centerItemOffset = visibleItemsCount / 2
    val itemHeight = 40.dp
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    val repeatedList = remember {
        val values = range.toList()
        List(1000) { values[it % values.size] }
    }

    val initialIndex = 500 + selected
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    val currentIndex by remember {
        derivedStateOf {
            val exactIndex = listState.firstVisibleItemIndex +
                    centerItemOffset + (listState.firstVisibleItemScrollOffset / itemHeightPx).roundToInt()
            repeatedList[exactIndex % repeatedList.size]
        }
    }

    LaunchedEffect(currentIndex) {
        onSelect(currentIndex)
    }

    Box(
        modifier = Modifier
            .height(itemHeight * visibleItemsCount)
            .width(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth()
                .border(2.dp, Color.Gray)
        )

        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(repeatedList) { _, number ->
                Box(
                    modifier = Modifier
                        .height(itemHeight),
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
