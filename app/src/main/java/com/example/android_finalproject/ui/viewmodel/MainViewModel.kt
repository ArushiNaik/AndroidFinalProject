package com.example.android_finalproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_finalproject.model.OrderMode
import com.example.android_finalproject.repository.DashboardData
import com.example.android_finalproject.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FoodRepository,
) : ViewModel() {
    private val search = MutableStateFlow("")

    val uiState: StateFlow<DashboardData> = search
        .flatMapLatest { repository.dashboard(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardData(emptyList(), emptyList(), emptyList(), emptyList()),
        )

    init {
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    fun onSearchChanged(value: String) {
        search.value = value
    }

    fun placeOrder(foodItemId: UUID, mode: OrderMode) {
        viewModelScope.launch { repository.placeOrder(foodItemId, mode) }
    }

    fun moveOrderToNextStatus(orderId: UUID) {
        viewModelScope.launch { repository.advanceOrderStatus(orderId) }
    }

    fun cancelOrder(orderId: UUID) {
        viewModelScope.launch { repository.cancelOrder(orderId) }
    }

    fun addComment(restaurantId: UUID, author: String, message: String) {
        viewModelScope.launch { repository.addComment(restaurantId, author, message) }
    }
}
