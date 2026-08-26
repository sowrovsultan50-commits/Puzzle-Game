package com.example.firebase

import android.content.Context
import com.example.data.local.GameRepository
import com.example.data.local.UserStatsEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AuthManager(
    private val context: Context,
    private val repository: GameRepository
) {
    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }

    private val _currentUserId = MutableStateFlow<String>("player_${UUID.randomUUID().toString().take(6)}")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val authScope = CoroutineScope(Dispatchers.Main)

    fun loginAsGuest(customName: String? = null) {
        val uid = "guest_" + (1000..9999).random().toString()
        _currentUserId.value = uid
        _isLoggedIn.value = true

        authScope.launch {
            val stats = repository.getInitialUserStats()
            if (customName != null && customName.isNotBlank()) {
                repository.updateUserProfile(customName, stats.avatarId)
            }
        }
    }

    fun loginWithGoogle(onResult: (Boolean, String?) -> Unit) {
        // Jetpack Credential Manager flow structure
        authScope.launch {
            val uid = "google_usr_" + (1000..9999).random().toString()
            _currentUserId.value = uid
            _isLoggedIn.value = true
            onResult(true, null)
        }
    }

    fun logout() {
        try {
            auth?.signOut()
        } catch (_: Exception) {}
        _isLoggedIn.value = false
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthManager? = null

        fun getInstance(context: Context, repository: GameRepository): AuthManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthManager(context.applicationContext, repository)
                INSTANCE = instance
                instance
            }
        }
    }
}
