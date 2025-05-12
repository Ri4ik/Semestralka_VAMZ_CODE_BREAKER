package com.example.semestralka_vamz.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semestralka_vamz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(onBack: () -> Unit =  {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RULES") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.rules_guess_code),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(stringResource(R.string.rules_feedback_info))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(R.string.rules_feedback_green), fontSize = 16.sp)
                Text(text = stringResource(R.string.rules_feedback_yellow), fontSize = 16.sp)
                Text(text = stringResource(R.string.rules_feedback_blue), fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.rules),
                contentDescription = "Pravidlá ilustrácia",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("OK")
            }
        }
    }
}
