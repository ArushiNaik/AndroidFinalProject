package com.example.android_finalproject.ui.navigation

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
import com.example.android_finalproject.ui.screens.HomeScreen
import com.example.android_finalproject.ui.screens.OrdersScreen
import com.example.android_finalproject.ui.screens.ProfileScreen
import com.example.android_finalproject.ui.viewmodel.MainViewModel

@Composable
fun ChamplainAppNavHost(vm: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val items = listOf("home", "orders", "profile")
    Scaffold(bottomBar = {
        NavigationBar { val backStack by navController.currentBackStackEntryAsState(); items.forEach { route ->
            NavigationBarItem(selected = backStack?.destination?.hierarchy?.any { it.route == route } == true, onClick = { navController.navigate(route) }, icon = {}, label = { Text(route.replaceFirstChar { it.uppercase() }) })
        } }
    }) { padding ->
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(vm, padding) }
            composable("orders") { OrdersScreen(vm, padding) }
            composable("profile") { ProfileScreen(padding) }
        }
    }
}
