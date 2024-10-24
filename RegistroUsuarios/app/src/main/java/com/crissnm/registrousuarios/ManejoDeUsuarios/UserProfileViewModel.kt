package com.crissnm.registrousuarios.ManejoDeUsuarios

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crissnm.registrousuarios.DepYmuni.ValidarCUI

class UserProfileViewModel(
    private val firestoreService: UserFireStoreService,
    private val validarCUI: ValidarCUI
) : ViewModel() {

    // Estado que mantiene al usuario
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    // Función para cargar el usuario por UID
    fun loadUser(uid: String) {
        // Verifica si los datos ya están cargados
        if (_user.value == null) {
            firestoreService.getUserbyUid(uid) { fetchedUser ->
                _user.value = fetchedUser
            }
        }
    }

    // Función para actualizar los datos del usuario
//    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
//        firestoreService.updateUserInFirestore(user) { success ->
//            if (success) {
//                onSuccess()
//            } else {
//                onFailure()
//            }
//        }
//    }

    // Otras funciones relacionadas con el perfil (validaciones, etc.)
}

