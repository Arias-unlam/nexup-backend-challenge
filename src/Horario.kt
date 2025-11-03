import java.time.DayOfWeek
import java.time.LocalTime

//Representa el horario de apertura y cierre de un supermercado
data class Horario(
    val horarioApertura: LocalTime,
    val horarioCierre: LocalTime
) {
    init {
        require(horarioCierre.isAfter(horarioApertura)) {
            "La hora de cierre debe ser posterior a la hora de apertura"
        }
    }

    //Verifica si el supermercado esta abierto a una hora especifica
    fun isOpenAt(time: LocalTime): Boolean {
        return !time.isBefore(horarioApertura) && time.isBefore(horarioCierre)
    }
}

//repreesnta el horario semanal de un supermercado con diias de operacion
data class HorarioSemanal(
    val horario: Horario,
    val diasAbiertos: Set<DayOfWeek>
) {
    init {
        require(diasAbiertos.isNotEmpty()) {
            "Debe haber al menos un dia de operacion"
        }
    }

    //verifica si el supermercado esta abierto en un dia y hora especificos
    fun isOpenAt(dayOfWeek: DayOfWeek, time: LocalTime): Boolean {
        return operatingDays.contains(dayOfWeek) && schedule.isOpenAt(time)
    }
}
