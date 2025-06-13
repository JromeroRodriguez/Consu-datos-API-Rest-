package com.example.aservicer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.aservicer.model.Post
import com.example.aservicer.network.Retrofit
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PostViewModel : ViewModel() {

    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> get() = _uiState

    init {
        fetchPost()
    }

    private fun fetchPost() {
        viewModelScope.launch {
            _uiState.value = try {
                val response = Retrofit.apiService.getPosts()
                if (response.isSuccessful) {
                    UiState.Success(posts = response.body() ?: emptyList())
                } else {
                    UiState.Error("Error de la API: Código ${response.code()}")
                }
            } catch (e: IOException) {
                UiState.Error("Error de red: ${e.message ?: "Conexión fallida"}")
            } catch (e: HttpException) {
                UiState.Error("Error HTTP: Código ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                UiState.Error("Error inesperado: ${e.message ?: "Desconocido"}")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val posts: List<Post>) : UiState()
    data class Error(val message: String) : UiState()
}
