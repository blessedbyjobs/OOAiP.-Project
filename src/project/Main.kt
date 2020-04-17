package project

import project.models.Taxi
import project.models.BusStop
import project.models.Person


fun main() {
    val testingBusStops = mutableListOf(
        BusStop("Московский проспект"),
        BusStop("Кафе `Рай`"),
        BusStop("Памятник Славы"),
        BusStop("45 Стрелковой дивизии"),
        BusStop("Автовокзал"),
        BusStop("Рабочий проспект"),
        BusStop("Политех"),
        BusStop("Проспект труда"),
        BusStop("Площадь `Застава`"),
        BusStop("Галерея Чижова"),
        BusStop("Сбербанк"),
        BusStop("Гостиница Брно"),
        BusStop("ВГУ")
    )

    val testingPersons = mutableListOf<Person>()

    val taxi = Taxi(mainRoute = testingBusStops)

    // инициализация пассажиров
    while (testingPersons.size < 501) {
        var busStop: BusStop
        var desiredBusStop: BusStop
        do {
            busStop = testingBusStops.random()
            desiredBusStop = testingBusStops.random()
        } while (busStop == desiredBusStop)

        val person = Person("Пассажир ${testingPersons.size}", desiredBusStop)

        busStop.personStartedWaitingTaxi(person)
        testingPersons.add(person)
    }

    // запуск работы такси
    taxi.startWorkingDay()
}