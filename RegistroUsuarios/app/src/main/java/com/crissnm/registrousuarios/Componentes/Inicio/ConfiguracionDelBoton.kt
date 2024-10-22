package com.crissnm.registrousuarios.Componentes.Inicio

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ButtonConfig(
//    var tipoDeAlerta: String = "Emergencia",
    var containerColor: Color = Color.Red,
    var contentColor: Color = Color.White,
    var widthButton: Dp = 300.dp,
    var heightButton: Dp = 100.dp,
    var paddingButton: Dp = 10.dp,
    var imageId: Int = 1,
    var sizeImage: Dp = 60.dp,
    var title: String = "Emergencia",
    var colorTitle: Color = Color.White,
    var fontSizeTitle: TextUnit = 20.sp,
    var paddingTitle: Dp = 10.dp
) {
    init {
        require(imageId != 0) { "imageId no puede estar vacío o nulo" }
        require(title.isNotEmpty()) { "title no puede estar vacío o nulo" }
    }
}

class ButtonBuilder {
    private var config = ButtonConfig()

    fun setContainerColor(color: Color) = apply { config = config.copy(containerColor = color) }
    fun setContentColor(color: Color) = apply { config = config.copy(contentColor = color) }
    fun setWidthButton(width: Dp) = apply { config = config.copy(widthButton = width) }
    fun setHeightButton(height: Dp) = apply { config = config.copy(heightButton = height) }
    fun setPaddingButton(padding: Dp) = apply { config = config.copy(paddingButton = padding) }
    fun setImageId(imageId: Int) = apply { config = config.copy(imageId = imageId) }
    fun setSizeImage(size: Dp) = apply { config = config.copy(sizeImage = size) }
    fun setTitle(title: String) = apply { config = config.copy(title = title) }
    fun setColorTitle(color: Color) = apply { config = config.copy(colorTitle = color) }
    fun setFontSizeTitle(size: TextUnit) = apply { config = config.copy(fontSizeTitle = size) }
    fun setPaddingTitle(padding: Dp) = apply { config = config.copy(paddingTitle = padding) }
    fun build() = config
}