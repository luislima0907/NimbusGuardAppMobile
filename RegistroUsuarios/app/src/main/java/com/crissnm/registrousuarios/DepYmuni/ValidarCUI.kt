package com.crissnm.registrousuarios.DepYmuni

class ValidarCUI {
    fun obtenerMunicipioYDepartamento(cui: String): Pair<String, String> {
        val cleanCUI = cui.replace("\\s".toRegex(), "")
        val length = cleanCUI.length

        val depto = cleanCUI.substring(9, 11).toIntOrNull() ?: return Pair("", "")
        val muni = if (length == 13) {
            cleanCUI.substring(11, 13).toIntOrNull() ?: return Pair("", "")
        } else {
            cleanCUI.substring(11, 12).toIntOrNull() ?: return Pair("", "")
        }

        val municipio = obtenerNombreMunicipio(depto, muni)
        val departamento = obtenerNombreDepartamento(depto)

        return Pair(municipio, departamento)
    }

    fun obtenerNombreMunicipio(depto: Int, muni: Int): String {
        return municipio.municipiosMap[depto]?.get(muni) ?: "Desconocido"
    }

    fun obtenerNombreDepartamento(depto: Int): String {
        return departamento.departamentosMap[depto] ?: "Desconocido"
    }
}