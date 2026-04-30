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
    private val _cartItems = MutableStateFlow<List<FoodItemEntity>>(emptyList())
    val cartItems = _cartItems.asStateFlow()
    private val _paymentDone = MutableStateFlow(false)
    val paymentDone = _paymentDone.asStateFlow()
    private val studentDiscountRate = 0.10

    val uiState: StateFlow<DashboardData> = search
        .flatMapLatest { repository.dashboard(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardData(emptyList(), emptyList(), emptyList(), emptyList()))

    init { viewModelScope.launch { repository.seedIfEmpty() } }

    fun onSearchChanged(value: String) { search.value = value }
    fun discountedPrice(price: Double): Double = price * (1.0 - studentDiscountRate)

    fun addToCart(item: FoodItemEntity) {
        _cartItems.value = _cartItems.value + item
        _confirmation.value = "Added ${item.name} to cart"
    }

    fun removeFromCart(item: FoodItemEntity) {
        _cartItems.value = _cartItems.value.toMutableList().also { it.remove(item) }
    }

    fun cartTotal(): Double = _cartItems.value.sumOf { discountedPrice(it.price) }

    fun completePayment() {
        _paymentDone.value = true
        _cartItems.value = emptyList()
        _confirmation.value = "Payment complete ✅ Your order is being prepared."
    }

    fun placeOrder(foodItemId: UUID, mode: OrderMode, itemName: String) {
        viewModelScope.launch {
            repository.placeOrder(foodItemId, mode)
            _confirmation.value = "Order confirmed for $itemName (${mode.name.lowercase()}). ETA 15-25 mins."
        }
    }

    fun dismissConfirmation() { _confirmation.value = null; _paymentDone.value = false }
    fun moveOrderToNextStatus(orderId: UUID) { viewModelScope.launch { repository.advanceOrderStatus(orderId) } }
    fun cancelOrder(orderId: UUID) { viewModelScope.launch { repository.cancelOrder(orderId) } }
    fun addComment(restaurantId: UUID, author: String, message: String) { viewModelScope.launch { repository.addComment(restaurantId, author, message) } }
}
