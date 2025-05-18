package com.example.semestralka_vamz.ui.screens

// Importy potrebné pre layout, stav, UI komponenty a lokalizáciu
import androidx.compose.ui.res.stringResource
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.ui.components.GameHistoryList
import com.example.semestralka_vamz.ui.components.OverviewCard
import com.example.semestralka_vamz.viewmodel.StatisticsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,  // ViewModel obsahujúci údaje o štatistikách
    onBack: () -> Unit = {}          // Callback pre návrat späť
) {
    // Zber dát zo stavových prúdov vo ViewModeli
    val overview by viewModel.overview.collectAsState() // súhrnné štatistiky
    val history by viewModel.history.collectAsState()   // zoznam jednotlivých hier

    // Scaffold zabezpečuje základné rozloženie obrazovky vrátane top baru
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
        // BoxWithConstraints umožňuje získať informáciu o šírke kontajnera (pre responzívne zobrazenie)
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Detekcia orientácie podľa šírky
            val isPortrait = this@BoxWithConstraints.maxWidth < 600.dp

            if (isPortrait) {
                // Portrétny režim: komponenty sa zobrazujú pod sebou
                Column {
                    OverviewCard(overview)                    // komponent s prehľadom štatistík
                    Spacer(modifier = Modifier.height(8.dp)) // medzera
                    GameHistoryList(history)                 // zoznam všetkých odohraných hier
                }
            } else {
                // Krajinný režim: rozloženie do dvoch stĺpcov
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        OverviewCard(overview) // vľavo: prehľad štatistík
                    }
                    Column(modifier = Modifier.weight(2f)) {
                        GameHistoryList(history) // vpravo: detailná história
                    }
                }
            }
        }
    }
}
