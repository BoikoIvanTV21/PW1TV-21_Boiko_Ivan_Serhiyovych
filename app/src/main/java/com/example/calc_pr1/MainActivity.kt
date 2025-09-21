package com.example.calc_pr1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuelCalculatorApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelCalculatorApp() {
    var h by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var s by remember { mutableStateOf("") }
    var n by remember { mutableStateOf("") }
    var o by remember { mutableStateOf("") }
    var w by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun parseInput(input: String): Double {
        return input.replace(",", ".").toDoubleOrNull() ?: 0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Калькулятор палива", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = h, onValueChange = { h = it }, label = { Text("H %") })
        OutlinedTextField(value = c, onValueChange = { c = it }, label = { Text("C %") })
        OutlinedTextField(value = s, onValueChange = { s = it }, label = { Text("S %") })
        OutlinedTextField(value = n, onValueChange = { n = it }, label = { Text("N %") })
        OutlinedTextField(value = o, onValueChange = { o = it }, label = { Text("O %") })
        OutlinedTextField(value = w, onValueChange = { w = it }, label = { Text("W %") })
        OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("A %") })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val H = parseInput(h)
                val C = parseInput(c)
                val S = parseInput(s)
                val N = parseInput(n)
                val O = parseInput(o)
                val W = parseInput(w)
                val A = parseInput(a)

                val kDry = if (100 - W == 0.0) 0.0 else 100.0 / (100.0 - W)
                val kCombustible = if (100 - W - A == 0.0) 0.0 else 100.0 / (100.0 - W - A)

                val H_dry = H * kDry
                val C_dry = C * kDry
                val S_dry = S * kDry
                val N_dry = N * kDry
                val O_dry = O * kDry
                val A_dry = A * kDry

                val H_comb = H * kCombustible
                val C_comb = C * kCombustible
                val S_comb = S * kCombustible
                val N_comb = N * kCombustible
                val O_comb = O * kCombustible

                val Qr = 339 * C + 1030 * H - 108.8 * (O - S) - 25 * W
                val Qr_MJ = Qr / 1000
                val Qd = Qr_MJ * kDry
                val Qg = Qr_MJ * kCombustible

                result = """
                    --- Суха маса ---
                    H = ${"%.2f".format(H_dry)}%
                    C = ${"%.2f".format(C_dry)}%
                    S = ${"%.2f".format(S_dry)}%
                    N = ${"%.2f".format(N_dry)}%
                    O = ${"%.2f".format(O_dry)}%
                    A = ${"%.2f".format(A_dry)}%
                    --- Горюча маса ---
                    H = ${"%.2f".format(H_comb)}%
                    C = ${"%.2f".format(C_comb)}%
                    S = ${"%.2f".format(S_comb)}%
                    N = ${"%.2f".format(N_comb)}%
                    O = ${"%.2f".format(O_comb)}%
                    --- Нижча теплота згоряння ---
                    Робоча маса: ${"%.3f".format(Qr_MJ)} МДж/кг
                    Суха маса: ${"%.3f".format(Qd)} МДж/кг
                    Горюча маса: ${"%.3f".format(Qg)} МДж/кг
                """.trimIndent()
            }) {
                Text("Розрахувати")
            }

            Button(onClick = {
                h = "3,8"
                c = "62,4"
                s = "3,60"
                n = "1,10"
                o = "4,30"
                w = "6,0"
                a = "18,8"
                //result = ""
            }) {
                Text("Автозаповнити (Вар.3)")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(result)
    }
}
