package com.example.semestralka_vamz.ui.screens

// Importy potrebné pre rozloženie, ikony, štýly a zobrazenie obrázka
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.data.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    onBack: () -> Unit = {}, // Callback na návrat späť
    theme: AppTheme          // Aktuálna téma (tmavá / svetlá)
) {
    // Scaffold zabezpečuje základné rozloženie obrazovky s TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.rules)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Hlavný obsah obrazovky, scrollovateľný stĺpec
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nadpis sekcie pravidiel
            Text(
                text = stringResource(R.string.rules_guess_code),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Vysvetlenie základného princípu spätnej väzby
            Text(stringResource(R.string.rules_feedback_info))

            // Popis farebného kódu spätnej väzby
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(R.string.rules_feedback_green), fontSize = 16.sp)
                Text(text = stringResource(R.string.rules_feedback_yellow), fontSize = 16.sp)
                Text(text = stringResource(R.string.rules_feedback_blue), fontSize = 16.sp)

                // Ikona označujúca úspešné uhádnutie
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Correct",
                        tint = Color(0xFF4CAF50)
                    )
                    Text(
                        text = stringResource(R.string.rules_feedback_checkmark),
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Obrázok pravidiel, mení sa podľa aktuálnej témy
            val imageRes = when (theme) {
                AppTheme.Dark -> R.drawable.rules_dark
                AppTheme.Light -> R.drawable.rules_light
            }
            Image(
                painter = painterResource(imageRes),
                contentDescription = "Pravidlá ilustrácia",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tlačidlo OK na zatvorenie obrazovky
            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    }
}
