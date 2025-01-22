package com.example.myapp04jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp04jetpackcompose.ui.theme.MyApp04JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePerson()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposePerson() {

    // Stavy pro jednotlivé textové vstupy
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var surname by remember { mutableStateOf(TextFieldValue("")) }
    var age by remember { mutableStateOf(TextFieldValue("")) }
    var place by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("") )}
    var height by remember { mutableStateOf(TextFieldValue("")) }
    var resultText by remember { mutableStateOf("") }


    // Scaffold s TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moje Aplikace - Osoba", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Moje Aplikace - Osoba", color = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->

        // Zbytek obsahu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Textová pole pro vstupy
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Příjmení") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = age,
                onValueChange = { ageInput ->
                    if (ageInput.text.all { char -> char.isDigit() }
                        && ageInput.text.toIntOrNull()?.let { it in 0..123 } == true) {
                        age = ageInput
                    }
                },
                label = { Text("Věk (hodnota menší než 123)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = place,
                onValueChange = { place = it },
                label = { Text("Bydliště") },
                modifier = Modifier.fillMaxWidth()
            )

            // Nové textové pole pro e-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = height,
                onValueChange = { heightInput ->
                    if (heightInput.text.all { char -> char.isDigit() }
                        && heightInput.text.toIntOrNull()?.let { it in 0..250 } == true) {
                        height = heightInput
                    }
                },
                label = { Text("Výška (v cm, mezi 50 a 250)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tlačítka Odeslat a Vymazat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        resultText = "Jmenuji se ${name.text} ${surname.text}. Je mi ${age.text} let, měřím ${height.text } a moje bydliště je ${place.text}. E-mailová adresa je ${email.text}"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Odeslat")
                }

                Button(
                    onClick = {
                        name = TextFieldValue("")
                        surname = TextFieldValue("")
                        age = TextFieldValue("")
                        place = TextFieldValue("")
                        height = TextFieldValue("")
                        email = TextFieldValue("")
                        resultText = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF0157),
                        contentColor = Color.White
                    )
                ) {
                    Text("Vymazat")
                }
            }

            // Zobrazení výsledku
            if (resultText.isNotEmpty()) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePerson()
}