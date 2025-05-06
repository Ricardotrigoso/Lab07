package com.example.lab07

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = remember { UserDatabase.getDatabase(context) }
    val dao = db.userDao()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios") },
                actions = {
                    IconButton(onClick = {
                        val user = User(firstName = name, lastName = email)
                        coroutineScope.launch {
                            dao.insert(user)
                            userList = dao.getAll()
                            snackbarHostState.showSnackbar("Usuario agregado")
                        }
                        name = ""
                        email = ""
                    }) {
                        Text("Agregar")
                    }

                    IconButton(onClick = {
                        coroutineScope.launch {
                            userList = dao.getAll()
                            snackbarHostState.showSnackbar("Lista actualizada")
                        }
                    }) {
                        Text("Listar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
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

            Button(
                onClick = {
                    coroutineScope.launch {
                        dao.deleteLastUser()
                        userList = dao.getAll()
                        snackbarHostState.showSnackbar("Último usuario eliminado")
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
                val first = it.firstName.orEmpty()
                val last = it.lastName.orEmpty()
                if (first.isNotBlank() || last.isNotBlank()) {
                    Text("- $first $last")
                }
            }
        }
    }
}