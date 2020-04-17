package project.models


const val NORMAL_DAY_PEOPLE_AMOUNT = 500

/**
 * Класс маршрутного такси
 *
 * @property [maxPeopleAmount] - максимальная вместимость, по умолчанию - 20
 * @property [passengers] - список пассажиров в такси, mutableList т.к. по ходу движения меняется
 * @property [mainRoute] - основной маршрут следования, mutableList т.к. разворачивается при достижении конечной
 */
data class Taxi(
    private val maxPeopleAmount: Int = 20,
    private val passengers: MutableList<Person> = mutableListOf(),
    private val mainRoute: MutableList<BusStop> = mutableListOf()
) {

    // количество остановок на маршруте
    private val stopsAmount = mainRoute.size

    // количество перевезенных пассажиров за день
    private var passengersAmountForToday = 0

    // отслеживание текущего положения на маршруте
    private lateinit var currentBusStop: BusStop
    private var currentBusStopPosition = 0

    // чисто статистика
    private var mainRouteIsPassedCount = 0

    /**
     * Метод "сажающий" пассажира
     */
    fun getOnBoard(person: Person) {
        passengers.add(person)
    }

    /**
     * Метод "вытаскивающий" пассажира и оповещающий, что он доехал
     */
    fun getOffBoard(person: Person) {
        passengers.remove(person)
        person.finishYourTrip()
    }

    private fun leaveBusStop() {
        println("Такси уехало с остановки `${currentBusStop.title}`, текущее число пассажиров = ${passengers.size}")
    }

    /**
     * При достижении остановки необходимо высадить всех пасажиров кто хотел здесь выйти
     */
    private fun removeAllSuccessfulTrippers() {
        val successfulTrippers = passengers.filter {
            currentBusStop == it.desiredBusStop
        }

        successfulTrippers.forEach {
            getOffBoard(it)
        }
    }

    /**
     * Посадить всех желающих людей в такси по максимуму
     *
     * Пример, пассажир хочет уехать с политеха на памятник, стоя при этом на остановке по направлению в ВГУ
     * В списке позиция памятника меньше текущей остановки, значит он будет подобран на обратном маршруте,
     * т.к. список реверсируется
     */
    private fun getOnBoardAllPersons() {
        val possibleClients = currentBusStop.waiters.filter {
            it.desiredBusStop in mainRoute && mainRoute.indexOf(it.desiredBusStop) > mainRoute.indexOf(currentBusStop)
        }

        if (possibleClients.isNotEmpty()) {
            possibleClients.forEach {
                if (passengers.size < maxPeopleAmount) {
                    getOnBoard(it)
                    currentBusStop.personStartedHisTrip(it)
                    passengersAmountForToday++
                }
            }
        }
    }

    /**
     * Прибытие на остановку. Высаживаем, принимаем новых
     */
    private fun arriveBusStop() {
        println("Такси приехало на остановку `${currentBusStop.title}`, текущее число пассажиров = ${passengers.size}")

        removeAllSuccessfulTrippers()

        getOnBoardAllPersons()
    }

    /**
     * Начало работы такси
     *
     * Работает до тех пор пока не выполнит норму или пока еще есть пассажиры
     */
    fun startWorkingDay() {

        while (passengersAmountForToday < NORMAL_DAY_PEOPLE_AMOUNT || passengers.isNotEmpty()) {
            work()
        }

        println("Хватит работать на сегодня. Норма выполнена. Маршрут сегодня пройден: $mainRouteIsPassedCount раз")
    }

    /**
     * Сам процесс работы
     *
     * По достижению последней остановки, реверсируем маршрут, т.е. движемся по обратному пути
     */
    fun work() {
        if (currentBusStopPosition == stopsAmount) {
            mainRoute.reverse()
            currentBusStopPosition = 0
            mainRouteIsPassedCount++
        }

        currentBusStop = mainRoute[currentBusStopPosition]
        currentBusStopPosition++

        arriveBusStop()
        leaveBusStop()
    }
}