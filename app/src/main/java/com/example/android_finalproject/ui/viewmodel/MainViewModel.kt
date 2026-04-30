package com.example.android_finalproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_finalproject.data.local.entity.FoodItemEntity
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.repository.DashboardData
import com.example.android_finalproject.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FoodRepository,
) : ViewModel() {
    private val search = MutableStateFlow("")
    private val _confirmation = MutableStateFlow<String?>(null)
    val confirmation: StateFlow<String?> = _confirmation
    private val _cartItems = MutableStateFlow<Map<FoodItemEntity, Int>>(emptyMap())
    val cartItems = _cartItems.asStateFlow()
    private val _selectedRestaurantId = MutableStateFlow<UUID?>(null)
    val selectedRestaurantId = _selectedRestaurantId.asStateFlow()
    private val _paymentDone = MutableStateFlow(false)
    val paymentDone = _paymentDone.asStateFlow()

    val uiState: StateFlow<DashboardData> = search
        .flatMapLatest { repository.dashboard(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardData(emptyList(), emptyList(), emptyList(), emptyList()))

    init { viewModelScope.launch { repository.seedIfEmpty() } }

    fun onSearchChanged(value: String) { search.value = value }
    fun discountedPrice(price: Double): Double = price * 0.9
    fun selectRestaurant(restaurantId: UUID) { _selectedRestaurantId.value = restaurantId }

    fun addToCart(item: FoodItemEntity) {
        val updated = _cartItems.value.toMutableMap(); updated[item] = (updated[item] ?: 0) + 1; _cartItems.value = updated
        _confirmation.value = "Added ${item.name} to cart"
    }
    fun removeFromCart(item: FoodItemEntity) {
        val updated = _cartItems.value.toMutableMap(); val qty = (updated[item] ?: 0) - 1
        if (qty <= 0) updated.remove(item) else updated[item] = qty
        _cartItems.value = updated
    }
    fun cartTotal(): Double = _cartItems.value.entries.sumOf { discountedPrice(it.key.price) * it.value }

    fun completePayment() { _paymentDone.value = true; _cartItems.value = emptyMap(); _confirmation.value = "Payment complete ✅" }
    fun placeOrder(foodItemId: UUID, mode: OrderMode, itemName: String) { viewModelScope.launch { repository.placeOrder(foodItemId, mode); _confirmation.value = "Order confirmed for $itemName (${mode.name.lowercase()})" } }
    fun dismissConfirmation() { _confirmation.value = null; _paymentDone.value = false }
    fun moveOrderToNextStatus(orderId: UUID) { viewModelScope.launch { repository.advanceOrderStatus(orderId) } }
    fun cancelOrder(orderId: UUID) { viewModelScope.launch { repository.cancelOrder(orderId) } }
    fun addComment(restaurantId: UUID, author: String, message: String) { viewModelScope.launch { repository.addComment(restaurantId, author, message) } }
}
