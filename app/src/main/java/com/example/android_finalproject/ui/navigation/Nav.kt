package com.example.android_finalproject.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android_finalproject.ui.screens.CartScreen
import com.example.android_finalproject.ui.screens.HomeScreen
import com.example.android_finalproject.ui.screens.OrdersScreen
import com.example.android_finalproject.ui.screens.ProfileScreen
import com.example.android_finalproject.ui.viewmodel.MainViewModel

data class BottomNavItem(val route: String, val label: String, val icon: @Composable () -> Unit)

@Composable
fun ChamplainAppNavHost(vm: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("home", "Home", { Icon(Icons.Default.Home, null) }),
        BottomNavItem("cart", "Cart", { Icon(Icons.Default.ShoppingBag, null) }),
        BottomNavItem("orders", "Orders", { Icon(Icons.Default.ShoppingCart, null) }),
        BottomNavItem("profile", "Profile", { Icon(Icons.Default.Person, null) }),
    )
    Scaffold(bottomBar = {
        NavigationBar {
            val backStack by navController.currentBackStackEntryAsState()
            items.forEach { item ->
                NavigationBarItem(
                    selected = backStack?.destination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = { navController.navigate(item.route) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                    icon = item.icon,
                    label = { Text(item.label) },
                )
            }
        }
    }) { padding ->
        NavHost(navController = navController, startDestination = "home") {
            composable("home", enterTransition = { enterFromRight() }, exitTransition = { exitToLeft() }) { HomeScreen(vm, padding) }
            composable("cart", enterTransition = { enterFromRight() }, exitTransition = { exitToLeft() }) { CartScreen(vm, padding) }
            composable("orders", enterTransition = { enterFromRight() }, exitTransition = { exitToLeft() }) { OrdersScreen(vm, padding) }
            composable("profile", enterTransition = { enterFromRight() }, exitTransition = { exitToLeft() }) { ProfileScreen(padding) }
        }
    }
}

private fun enterFromRight(): EnterTransition = slideInHorizontally(initialOffsetX = { it / 3 }) + fadeIn()
private fun exitToLeft(): ExitTransition = slideOutHorizontally(targetOffsetX = { -it / 4 }) + fadeOut()
