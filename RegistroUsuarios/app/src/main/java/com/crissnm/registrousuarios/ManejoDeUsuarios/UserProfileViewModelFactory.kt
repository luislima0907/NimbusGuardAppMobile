package com.crissnm.registrousuarios.ManejoDeUsuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crissnm.registrousuarios.DepYmuni.ValidarCUI

class UserProfileViewModelFactory(
    private val firestoreService: UserFireStoreService,
    private val validarCUI: ValidarCUI
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(firestoreService, validarCUI) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
