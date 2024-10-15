package com.crissnm.registrousuarios.Navegacion

sealed class ManejoDeLasPantallasDeLaApp(val ruta: String) {
    object PantallaDeRegistro : ManejoDeLasPantallasDeLaApp("pantalla_registro")
    object PantallaDeLogin : ManejoDeLasPantallasDeLaApp("pantalla_login")
    object PantallaDeInicio : ManejoDeLasPantallasDeLaApp("pantalla_inicio")
    object PantallaDePerfil : ManejoDeLasPantallasDeLaApp("pantalla_perfil")
    object PantallaDetalleDeLaAlerta : ManejoDeLasPantallasDeLaApp("pantalla_detalle_alerta")
    object PantallaDeNotificacion : ManejoDeLasPantallasDeLaApp("pantalla_notificaciones")
    object PantallaPrincipal : ManejoDeLasPantallasDeLaApp("pantalla_principal")
}