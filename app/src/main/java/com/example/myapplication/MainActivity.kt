package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.ProfileScreen
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
fun AppNavigation(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    val usr by authViewModel.userState.collectAsStateWithLifecycle()
    val feedback by authViewModel.authFeedback.collectAsStateWithLifecycle()

    // Exibe feedback com Toast (opcional)
    LaunchedEffect(feedback) {
        feedback?.let {
            Toast.makeText(navController.context, it, Toast.LENGTH_SHORT).show()
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (usr != null) "perfil" else "login"
    ) {
        // Tela de Cadastro
        composable("signup") {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        // Tela de Login
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }

        // Tela de Perfil
        composable("perfil") {
            if (usr != null) {
                ProfileScreen(authViewModel = authViewModel, user = usr!!)
            } else {
                // Se não houver usuário logado, volta para o login
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        }
    }
}
