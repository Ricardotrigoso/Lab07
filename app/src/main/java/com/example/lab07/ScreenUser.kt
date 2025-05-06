package com.example.lab07

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ScreenUser(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val dao = db.userDao()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar usuario
        Button(
            onClick = {
                val user = User(firstName = name, lastName = email)
                coroutineScope.launch {
                    dao.insert(user)
                    userList = dao.getAll()
                }
                name = ""
                email = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Usuario")
        }

        Spacer(modifier = Modifier.height(8.dp))

        //boton para quitar usuario
        Button(
            onClick = {
                coroutineScope.launch {
                    dao.deleteLastUser()
                    userList = dao.getAll()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Eliminar Último Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Lista de usuarios:", style = MaterialTheme.typography.titleMedium)

        userList.forEach {
            Text("- ${it.firstName} ${it.lastName}")
        }
    }
}