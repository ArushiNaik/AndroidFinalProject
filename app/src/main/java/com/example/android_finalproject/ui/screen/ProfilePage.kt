package com.example.android_finalproject.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCart: () -> Unit = {},
    onNavigateFavorite: () -> Unit = {},
    onNavigateProfile: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }

    var isInfoSaved by remember { mutableStateOf(false) }
    var isStudentIdLocked by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = "Profile",
                onHomeClick = onNavigateHome,
                onCartClick = onNavigateCart,
                onFavoriteClick = onNavigateFavorite,
                onProfileClick = onNavigateProfile
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8F8))
        ) {
            // Title Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PROFILE",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Content Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ProfileField(
                        label = "Name",
                        value = name,
                        placeholder = "Enter your name",
                        isSaved = isInfoSaved,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StudentIdField(
                        studentId = studentId,
                        isStudentIdLocked = isStudentIdLocked,
                        onStudentIdChange = { studentId = it },
                        onConfirmId = {
                            if (studentId.isNotBlank()) {
                                isStudentIdLocked = true
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileField(
                        label = "School Email",
                        value = email,
                        placeholder = "Enter your school email",
                        isSaved = isInfoSaved,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileField(
                        label = "Phone Number",
                        value = phone,
                        placeholder = "Enter your phone number",
                        isSaved = isInfoSaved,
                        onValueChange = { phone = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { isInfoSaved = !isInfoSaved },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isInfoSaved) "Edit Profile" else "Save Profile"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    placeholder: String,
    isSaved: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (isSaved) {
            Text(
                text = if (value.isBlank()) "Not added yet" else value,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun StudentIdField(
    studentId: String,
    isStudentIdLocked: Boolean,
    onStudentIdChange: (String) -> Unit,
    onConfirmId: () -> Unit
) {
    Column {
        Text(
            text = "Student ID",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (isStudentIdLocked) {
            Text(
                text = studentId,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            OutlinedTextField(
                value = studentId,
                onValueChange = onStudentIdChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your student ID") },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onConfirmId,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Confirm ID")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = selectedItem == "Home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selectedItem == "Cart",
            onClick = onCartClick,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )

        NavigationBarItem(
            selected = selectedItem == "Favorite",
            onClick = onFavoriteClick,
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
            label = { Text("Favorite") }
        )

        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}