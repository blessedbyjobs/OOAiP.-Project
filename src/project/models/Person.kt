package project.models

/**
 * Пассажир
 *
 * @property [name] - имя
 * @property [desiredBusStop] - конечная остановка
 */
data class Person(private val name: String, val desiredBusStop: BusStop) {

    fun finishYourTrip() {
        println("$name добрался до точки назначения -> `${desiredBusStop.title}`")
    }
}