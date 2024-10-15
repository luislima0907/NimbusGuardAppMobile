package com.crissnm.registrousuarios.Validaciones

object Validaciones {

    private val nombreRegex = Regex("^[A-ZÁÉÍÓÚ][a-záéíóú]+(?: [A-ZÁÉÍÓÚ][a-záéíóú]+)*$")

    private val telefonoRegex = Regex("^\\d{4} \\d{4}\$")

    private val correoRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    fun isValidName(name: String): Boolean {
        return nombreRegex.matches(name)
    }

    fun isValidPhone(phone: String): Boolean {
        return telefonoRegex.matches(phone)
    }

    fun isValidCorreo(correo: String): Boolean {
        return correoRegex.matches(correo)
    }

    fun isValidCUI(cui: String): Boolean {
        if (cui.isBlank()) {
            println("CUI vacío")
            return false
        }

        val cuiRegExp = Regex("^[0-9]{4}\\s?[0-9]{5}\\s?[0-9]{3,4}$")
        if (!cuiRegExp.matches(cui)) {
            println("CUI con formato inválido")
            return false
        }

        val cleanCUI = cui.replace("\\s".toRegex(), "")
        val length = cleanCUI.length

        if (length != 12 && length != 13) {
            println("CUI debe tener 12 o 13 dígitos")
            return false
        }

        val numero = cleanCUI.substring(0, 8)
        val verificador = cleanCUI.substring(8, 9).toIntOrNull() ?: run {
            println("Verificador inválido")
            return false
        }

        val depto = try {
            cleanCUI.substring(9, 11).toInt()
        } catch (e: Exception) {
            println("Error al extraer departamento: ${e.message}")
            return false
        }

        val muni = try {
            if (length == 13) {
                cleanCUI.substring(11, 13).toInt()
            } else { // length == 12
                cleanCUI.substring(11, 12).toInt()
            }
        } catch (e: Exception) {
            println("Error al extraer municipio: ${e.message}")
            return false
        }

        val munisPorDepto = listOf(
            17, 8, 16, 16, 13, 14, 19, 8, 24, 21, 9, 30,
            32, 21, 8, 17, 14, 5, 11, 11, 7, 17
        )

        if (depto == 0 || muni == 0) {
            println("CUI con código de municipio o departamento inválido.")
            return false
        }

        if (depto > munisPorDepto.size) {
            println("CUI con código de departamento inválido.")
            return false
        }

        if (muni > munisPorDepto[depto - 1]) {
            println("CUI con código de municipio inválido.")
            return false
        }

        var total = 0
        for (i in numero.indices) {
            val digit = numero[i].toString().toIntOrNull() ?: run {
                println("Número inválido en posición $i")
                return false
            }
            total += digit * (i + 2)
        }
        val modulo = total % 11
        println("CUI con módulo: $modulo")

        return modulo == verificador
    }

}