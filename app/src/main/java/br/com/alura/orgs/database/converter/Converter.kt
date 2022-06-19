package br.com.alura.orgs.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converter {
    @TypeConverter
    fun fromDouble(valor : Double?) : BigDecimal {
       return valor?.let {valor.toString().toBigDecimal()} ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalToDouble(valor : BigDecimal?): Double? {
        return valor?.let{valor.toDouble()}
    }
}