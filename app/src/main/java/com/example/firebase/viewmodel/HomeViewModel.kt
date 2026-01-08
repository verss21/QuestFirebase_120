package com.example.firebase.viewmodel

import RepositorySiswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase.modeldata.Siswa
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface StatusUiSiswa {
    data class Success(val siswa: List<Siswa>) : StatusUiSiswa // Lepas default value agar lebih pasti
    object Error : StatusUiSiswa
    object Loading : StatusUiSiswa
}

class HomeViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var statusUiSiswa: StatusUiSiswa by mutableStateOf(StatusUiSiswa.Loading)
        private set

    init {
        loadSiswa()
    }

    fun loadSiswa() {
        viewModelScope.launch {
            statusUiSiswa = StatusUiSiswa.Loading
            try {
                // Ambil data terlebih dahulu
                val dataSiswa = repositorySiswa.getDataSiswa()
                // Update state dengan data yang didapat
                statusUiSiswa = StatusUiSiswa.Success(dataSiswa)
            } catch (e: IOException) {
                statusUiSiswa = StatusUiSiswa.Error
            } catch (e: Exception) {
                statusUiSiswa = StatusUiSiswa.Error
            }
        }
    }
}