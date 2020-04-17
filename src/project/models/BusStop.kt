package project.models

/**
 * Остановка
 */
data class BusStop(
    val title: String = "",
    val waiters: MutableList<Person> = mutableListOf()
) {

    // человек пришел на остановку
    fun personStartedWaitingTaxi(person: Person) {
        waiters.add(person)
    }

    // человек сел в такси
    fun personStartedHisTrip(person: Person) {
        waiters.remove(person)
    }

    override fun toString(): String = title
}