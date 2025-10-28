package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

    private val _authFeedback = MutableStateFlow<String?>(null)
    val authFeedback: StateFlow<String?> = _authFeedback

    fun signUp(nome: String, email: String, senha: String) {
        viewModelScope.launch {
            _authFeedback.value = null
            try {
                auth.createUserWithEmailAndPassword(email, senha).await()
                val usr = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome).build()

                auth.currentUser?.updateProfile(usr)?.await()

            } catch (e: Exception) {
                _authFeedback.value = "Erro no cadastro - ${e.message}"
            }
        }
    }

    fun signIn() {
    }

    fun updateUser() {
    }

    fun signOut() {
        auth.signOut()
        _userState.value = null
    }
}
