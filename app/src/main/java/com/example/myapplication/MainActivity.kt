package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            Column {
                val id = remember { UUID.randomUUID() }
                Text("main (id=$id)")
                Button(
                    content = { Text("open details") },
                    onClick = { navController.navigate("detail") },
                )

                var eventLog by remember { mutableStateOf("") }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = object : LifecycleEventObserver {
                        override fun onStateChanged(
                            source: LifecycleOwner,
                            event: Lifecycle.Event,
                        ) {
                            eventLog += "\n$event"
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }
                Text(eventLog)
            }
        }
        composable("detail") {
            Column {
                val id = remember { UUID.randomUUID() }
                Text("detail (id=$id)")
                Button(
                    content = { Text("navigate to main with popUpTo & launchSingleTop") },
                    onClick = {
                        navController.navigate("main") {
                            popUpTo("main")
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
    }
}
