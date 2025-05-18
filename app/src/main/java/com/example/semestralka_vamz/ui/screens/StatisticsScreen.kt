package com.example.semestralka_vamz.ui.screens

import androidx.compose.ui.res.stringResource
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.ui.components.GameHistoryList
import com.example.semestralka_vamz.ui.components.OverviewCard
import com.example.semestralka_vamz.viewmodel.StatisticsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    onBack: () -> Unit = {}
) {
    val overview by viewModel.overview.collectAsState()
    val history by viewModel.history.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.statistics_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val isPortrait = this@BoxWithConstraints.maxWidth < 600.dp
            if (isPortrait) {
                // Портретна орієнтація
                Column {
                    OverviewCard(overview)
                    Spacer(modifier = Modifier.height(8.dp))
                    GameHistoryList(history)
                }
            } else {
                // Альбомна орієнтація
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        OverviewCard(overview)
                    }
                    Column(modifier = Modifier.weight(2f)) {
                        GameHistoryList(history)
                    }
                }
            }
        }
    }
}

