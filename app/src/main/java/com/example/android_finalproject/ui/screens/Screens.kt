package com.example.android_finalproject.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(vm: MainViewModel, padding: PaddingValues) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    LazyColumn(Modifier.padding(padding).padding(12.dp)) {
        item {
            Text("Champlain Student Fast Food Delivery App")
            OutlinedTextField(
                value = search,
                onValueChange = { search = it; vm.onSearchChanged(it) },
                label = { Text("Search food or restaurant") },
                modifier = Modifier.fillMaxWidth(),
            )
            Text("Nearby Restaurants & Schedule")
        }

        items(state.restaurants) { restaurant ->
            Text("${restaurant.name}: ${restaurant.opensAt} - ${restaurant.closesAt}")
            OutlinedTextField(author, { author = it }, label = { Text("Your name") })
            OutlinedTextField(comment, { comment = it }, label = { Text("Comment") })
            Button(onClick = {
                if (author.isNotBlank() && comment.isNotBlank()) {
                    vm.addComment(restaurant.id, author, comment)
                    comment = ""
                }
            }) { Text("Post Comment") }
        }

        item { Text("Menu") }
        items(state.foodItems) { food ->
            Card(Modifier.padding(vertical = 4.dp)) {
                Column(Modifier.padding(10.dp)) {
                    Text("${food.name} (${food.category}) - $${food.price}")
                    Button(onClick = { vm.placeOrder(food.id, OrderMode.DELIVERY) }) { Text("Order Delivery") }
                    Button(onClick = { vm.placeOrder(food.id, OrderMode.PICKUP) }) { Text("Order Pickup") }
                }
            }
        }

        item { Text("Student Comments") }
        items(state.comments) { Text("${it.author}: ${it.message}") }
    }
}

@Composable
fun OrdersScreen(vm: MainViewModel, padding: PaddingValues) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    LazyColumn(Modifier.padding(padding).padding(12.dp)) {
        item { Text("Task Manager (Add/Edit/Delete orders)") }
        items(state.orders) { order ->
            Card(Modifier.padding(vertical = 4.dp)) {
                Column(Modifier.padding(10.dp)) {
                    Text("${order.mode} - ${order.status}")
                    Button(onClick = { vm.moveOrderToNextStatus(order.id) }) { Text("Advance Status") }
                    Button(onClick = { vm.cancelOrder(order.id) }) { Text("Cancel/Delete") }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(padding: PaddingValues) {
    Column(Modifier.padding(padding).padding(12.dp)) {
        Text("Team Members")
        Text("Chin-Yi Chiu — Frontend and UI")
        Text("Arushi Naik — Navigation and state management")
        Text("Marwa Meskine — Backend")
        Text("Kiana Heidarpourmaleki — Database")
        Text("Future improvements: cloud sync, long-distance delivery, scheduled arrival time")
    }
}
