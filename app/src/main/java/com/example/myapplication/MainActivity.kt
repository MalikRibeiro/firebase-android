 package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.SignUpScreen
import com.example.myapplication.viewmodel.AuthViewModel

 class MainActivity : ComponentActivity() {
     private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(authViewModel)
            }
        }
    }

 @Composable
 fun AppNavigation(authViewModel: AuthViewModel){
     val navController = rememberNavController()

     NavHost(navController = navController, startDestination = "signup") {
         composable("signup"){
             SignUpScreen(
                 authViewModel = authViewModel,
                 onNavigateToLogin = {})
             }
         }
     }
