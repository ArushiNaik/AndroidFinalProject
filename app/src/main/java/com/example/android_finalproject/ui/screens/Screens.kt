package com.example.android_finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.ui.viewmodel.MainViewModel

private val UberOrange = Color(0xFFFF6B35)
private val SurfaceBg = Color(0xFFF7F7F7)

@Composable
fun HomeScreen(vm: MainViewModel, padding: PaddingValues, openRestaurantDetail: () -> Unit) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val confirmation by vm.confirmation.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(SurfaceBg)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                Column(
                    modifier = Modifier
                        .background(Brush.horizontalGradient(listOf(UberOrange, Color(0xFFFF9A3C))))
                        .padding(16.dp)
                ) {
                    Text("Champlain Eats", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
                    Text("Fast campus delivery • Student discounts", color = Color.White)
                }
            }
        }

        item {
            confirmation?.let { msg ->
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))) {
                    Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(msg, modifier = Modifier.weight(1f))
                        Button(onClick = { vm.dismissConfirmation() }) { Text("OK") }
                    }
                }
            }
            OutlinedTextField(
                value = search,
                onValueChange = { search = it; vm.onSearchChanged(it) },
                label = { Text("Search food, restaurant, category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
        }

        item {
            Text("Popular Categories", fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Burgers", "Pizza", "Coffee", "Healthy", "Snacks")) {
                    AssistChip(onClick = {}, label = { Text(it) })
                }
            }
        }

        item { Text("Restaurants", fontWeight = FontWeight.Bold) }
        items(state.restaurants) { restaurant ->
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(14.dp)) {
                    Text("🍔 ${restaurant.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Button(onClick = { vm.selectRestaurant(restaurant.id); openRestaurantDetail() }) { Text("View Menu") }
                    Text("Open: ${restaurant.opensAt} - ${restaurant.closesAt}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(author, { author = it }, label = { Text("Name") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(comment, { comment = it }, label = { Text("Comment") }, modifier = Modifier.weight(1f))
                    }
                    Button(onClick = {
                        if (author.isNotBlank() && comment.isNotBlank()) {
                            vm.addComment(restaurant.id, author, comment)
                            comment = ""
                        }
                    }) { Text("Post Review") }
                }
            }
        }

        item { Text("Top Picks", fontWeight = FontWeight.Bold) }
        item {
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Quick Cart & Checkout", fontWeight = FontWeight.Bold)
                    Text("Items in menu: ${state.foodItems.size}. Student discount auto-applied in prices.")
                }
            }
        }
        items(state.foodItems) { food ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🍽️ ${food.name}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    val discounted = (vm.discountedPrice(food.price) * 100).roundToInt() / 100.0
                    Text("${food.category} • $${food.price}  → Student: $${discounted}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { vm.placeOrder(food.id, OrderMode.DELIVERY, food.name) }) { Text("Delivery") }
                        Button(onClick = { vm.placeOrder(food.id, OrderMode.PICKUP, food.name) }) { Text("Pickup") }
                        Button(onClick = { vm.addToCart(food) }) { Text("Add to cart") }
                    }
                }
            }
        }
    }
}



@Composable
fun CartScreen(vm: MainViewModel, padding: PaddingValues) {
    val cart by vm.cartItems.collectAsStateWithLifecycle()
    val paymentDone by vm.paymentDone.collectAsStateWithLifecycle()
    LazyColumn(Modifier.padding(padding).fillMaxSize().background(SurfaceBg).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Your Cart", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        items(cart.entries.toList()) { entry ->
            val item = entry.key
            val qty = entry.value
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column { Text("🛒 ${item.name} x$qty"); Text("$${vm.discountedPrice(item.price)} each after student discount") }
                    Button(onClick = { vm.removeFromCart(item) }) { Text("Remove") }
                }
            }
        }
        item {
            Text("Total: $${"%.2f".format(vm.cartTotal())}", fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Button(onClick = { vm.completePayment() }, enabled = cart.isNotEmpty()) { Text("Pay Now") }
        }
        if (paymentDone) item { Text("✅ Payment Completed! Order sent to kitchen.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold) }
    }
}
@Composable
fun OrdersScreen(vm: MainViewModel, padding: PaddingValues) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    LazyColumn(
        Modifier.padding(padding).fillMaxSize().background(SurfaceBg).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Live Orders", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold) }
        items(state.orders) { order ->
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${order.mode} • ${order.status}", fontWeight = FontWeight.Bold)
                    Text(orderTimeline(order.status.name))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { vm.moveOrderToNextStatus(order.id) }) { Text("Next Status") }
                        Button(onClick = { vm.cancelOrder(order.id) }) { Text("Cancel") }
                    }
                }
            }
        }
        item { Text("Community Reviews", fontWeight = FontWeight.Bold) }
        items(state.comments) { Text("• ${it.author}: ${it.message}") }
    }
}

@Composable
fun ProfileScreen(padding: PaddingValues) {
    Column(
        Modifier
            .padding(padding)
            .fillMaxSize()
            .background(SurfaceBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Champlain Student Fast Food Delivery App", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Team")
        Text("• Chin-Yi Chiu — Frontend and UI")
        Text("• Arushi Naik — Navigation and state management")
        Text("• Marwa Meskine — Backend")
        Text("• Kiana Heidarpourmaleki — Database")
        Text("Roadmap: cloud sync, farther delivery radius, schedule delivery time.")
    }
}


private fun orderTimeline(status: String): String = when (status) {
    "PENDING" -> "Timeline: Confirmed → Preparing → On the way → Delivered"
    "PREPARING" -> "Timeline: Preparing now → On the way soon"
    "ON_THE_WAY" -> "Timeline: Rider is on the way (ETA 5-10 mins)"
    "COMPLETE" -> "Timeline: Delivered ✅"
    else -> "Timeline: Cancelled"
}


@Composable
fun RestaurantDetailScreen(vm: MainViewModel, padding: PaddingValues) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val selectedId by vm.selectedRestaurantId.collectAsStateWithLifecycle()
    val restaurant = state.restaurants.firstOrNull { it.id == selectedId }
    val foods = state.foodItems.filter { it.restaurantId == selectedId }
    LazyColumn(Modifier.padding(padding).fillMaxSize().background(SurfaceBg).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(12.dp)) {
                    Text("📸 Restaurant Photo Placeholder", fontWeight = FontWeight.Bold)
                    Text(restaurant?.name ?: "Restaurant")
                    Text("Open ${restaurant?.opensAt} - ${restaurant?.closesAt}")
                    Text("Recommended dishes")
                }
            }
        }
        item { Text("Menu", fontWeight = FontWeight.Bold) }
        items(foods) { food ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column { Text("🍽️ ${food.name}"); Text(food.category) }
                    Button(onClick = { vm.addToCart(food) }) { Text("Add") }
                }
            }
        }
    }
}
