package com.crissnm.registrousuarios.Navegacion

sealed class ManejoDeLasPantallasDeLaApp(val ruta: String) {
    object PantallaDeBienvenida : ManejoDeLasPantallasDeLaApp("pantalla_bienvenida")
    object PantallaDeRegistro : ManejoDeLasPantallasDeLaApp("pantalla_registro")
    object PantallaDeLogin : ManejoDeLasPantallasDeLaApp("pantalla_login")
    object PantallaDeInicio : ManejoDeLasPantallasDeLaApp("pantalla_inicio")
    object PantallaDePerfil : ManejoDeLasPantallasDeLaApp("pantalla_perfil")
    object PantallaDeNotificacion : ManejoDeLasPantallasDeLaApp("pantalla_notificaciones")
    object PantallaPrincipal : ManejoDeLasPantallasDeLaApp("pantalla_principal")
    object PantallaConInfoApp: ManejoDeLasPantallasDeLaApp("pantalla_con_info_app")
}