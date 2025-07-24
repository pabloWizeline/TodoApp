package com.pfmiranda.todoexample.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pfmiranda.todoexample.domain.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(todo: Todo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (todo.completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (todo.completed) Color(0xFF4CAF50) else Color.Gray
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (todo.completed) "Completado" else "Pendiente",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (todo.completed) Color(0xFF4CAF50) else Color.Gray,
                    modifier = Modifier
                        .background(
                            color = if (todo.completed) Color(0xFFE8F5E9) else Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            } // <- Faltaba cerrar el Row aquí

            Spacer(Modifier.height(8.dp))

            Row {
                Text("ID: ${todo.id}", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.width(16.dp))
                Text("Usuario: ${todo.userId}", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp)
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Gray.copy(alpha = 0.5f))
        Spacer(Modifier.height(16.dp))
        Text("No hay todos disponibles", fontSize = 18.sp, color = Color.Gray)
        Text("Presiona 'Cargar Todos' para obtener datos", fontSize = 14.sp, color = Color.LightGray)
    }
}

@Composable
fun ErrorState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Error, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Red.copy(alpha = 0.5f))
        Spacer(Modifier.height(16.dp))
        Text("Error de conexión", fontSize = 18.sp, color = Color.Red)
        Text("Verifica tu conexión a internet", fontSize = 14.sp, color = Color.Gray)
    }
}
