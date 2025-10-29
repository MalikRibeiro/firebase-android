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

    // 🧩 CADASTRO (Sign Up)
    fun signUp(nome: String, email: String, senha: String) {
        viewModelScope.launch {
            _authFeedback.value = null
            try {
                auth.createUserWithEmailAndPassword(email, senha).await()

                val usr = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()

                auth.currentUser?.updateProfile(usr)?.await()

                _userState.value = auth.currentUser
                _authFeedback.value = "Cadastro realizado com sucesso!"
            } catch (e: Exception) {
                _authFeedback.value = "Erro no cadastro - ${e.message}"
            }
        }
    }

    // 🔐 LOGIN (Sign In)
    fun signIn(email: String, senha: String) {
        viewModelScope.launch {
            _authFeedback.value = null
            try {
                auth.signInWithEmailAndPassword(email, senha).await()
                _userState.value = auth.currentUser
                _authFeedback.value = "Login realizado com sucesso!"
            } catch (e: Exception) {
                _authFeedback.value = "Erro ao fazer login - ${e.message}"
            }
        }
    }

    // 🧱 ATUALIZAR PERFIL (Update User)
    fun updateUser(nome: String? = null) {
        viewModelScope.launch {
            _authFeedback.value = null
            try {
                val user = auth.currentUser
                if (user != null && nome != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nome)
                        .build()
                    user.updateProfile(profileUpdates).await()

                    _userState.value = auth.currentUser
                    _authFeedback.value = "Usuário atualizado com sucesso!"
                } else {
                    _authFeedback.value = "Nenhum usuário logado ou nome inválido."
                }
            } catch (e: Exception) {
                _authFeedback.value = "Erro ao atualizar usuário - ${e.message}"
            }
        }
    }

    // 🚪 SAIR (Sign Out)
    fun signOut() {
        auth.signOut()
        _userState.value = null
        _authFeedback.value = "Usuário desconectado."
    }
}
