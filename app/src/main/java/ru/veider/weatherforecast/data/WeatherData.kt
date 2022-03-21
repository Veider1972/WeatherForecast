package ru.veider.weatherforecast.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherQuery(
    var name: String,
    var latitude: Double,
    var longitude: Double,
    var language: Language,
    val key: String = "b5eedce9-6c9a-44aa-8019-e78fa1b018e7" // ключ разработчика
) : Parcelable {
    constructor(
        name: String, latitude: Double, longitude: Double, language: String
    ) : this(
        name, latitude, longitude, Language.fromString(language)
    )
}

enum class DataLoading {
    RUSSIAN, FOREIGN
}

fun getRussianCities(): Array<WeatherQuery> {
    return arrayOf(
        WeatherQuery("Москва", 55.755826, 37.617299900000035, "ru_RU"),
        WeatherQuery("Санкт-Петербург", 59.9342802, 30.335098600000038, "ru_RU"),
        WeatherQuery("Новосибирск", 55.00835259999999, 82.93573270000002, "ru_RU"),
        WeatherQuery("Екатеринбург", 56.83892609999999, 60.60570250000001, "ru_RU"),
        WeatherQuery("Нижний Новгород", 56.2965039, 43.936059, "ru_RU"),
        WeatherQuery("Казань", 55.8304307, 49.06608060000008, "ru_RU"),
        WeatherQuery("Челябинск", 55.1644419, 61.4368432, "ru_RU"),
        WeatherQuery("Омск", 54.9884804, 73.32423610000001, "ru_RU"),
        WeatherQuery("Ростов-на-Дону", 47.2357137, 39.701505, "ru_RU"),
        WeatherQuery("Уфа", 54.7387621, 55.972055400000045, "ru_RU")
    )
}

fun getForeignCities(): Array<WeatherQuery> {
    return arrayOf(
        WeatherQuery("Лондон", 51.5085300, -0.1257400, "ru_RU"),
        WeatherQuery("Токио", 35.6895000, 139.6917100, "ru_RU"),
        WeatherQuery("Париж", 48.8534100, 2.3488000, "ru_RU"),
        WeatherQuery("Берлин", 52.52000659999999, 13.404953999999975, "ru_RU"),
        WeatherQuery("Рим", 41.9027835, 12.496365500000024, "ru_RU"),
        WeatherQuery("Минск", 53.90453979999999, 27.561524400000053, "ru_RU"),
        WeatherQuery("Стамбул", 41.0082376, 28.97835889999999, "ru_RU"),
        WeatherQuery("Вашингтон", 38.9071923, -77.03687070000001, "ru_RU"),
        WeatherQuery("Киев", 50.4501, 30.523400000000038, "ru_RU"),
        WeatherQuery("Пекин", 39.90419989999999, 116.40739630000007, "ru_RU")
    )
}

data class WeatherData(
    val now: Long,           // Время сервера в формате Unixtime. Число
    val now_dt: String,      // Время сервера в UTC. Строка
    val info: Info,          // Объект информации о населенном пункте. Объект
    val fact: Fact,          // Объект фактической информации о погоде. Объект
    val forecast: Forecast   // Объект прогнозной информации о погоде. Объект
)

fun testWeatherData(): WeatherData {
    return WeatherData(
        1470220206, "2016-08-03T10:30:06.238Z", testInfo(), testFact(), testForecast()
    )
}

data class Info(
    val latitude: Double,    // Широта (в градусах). Число
    val longitude: Double,   // Долгота (в градусах). Число
    val url: String          // Страница населенного пункта на сайте Яндекс.Погода
)

fun testInfo(): Info {
    return Info(55.833333, 37.616667, "https://yandex.ru/pogoda/moscow")
}

data class Fact(
    val temp: Int,              // Температура (°C)
    val feels_like: Int,        // Ощущаемая температура (°C)
    val temp_water: Int?,       // Температура воды (°C). Параметр возвращается для там, где данная информация актуальна
    val icon: String,           // Код иконки погоды по адресу https://yastatic.net/weather/i/icons/funky/dark/<знач. поля icon>.svg
    val condition: Condition,   // Код расшифровки погодного описания
    val wind_speed: Double,     // Скорость ветра (в м/с). Число
    val wind_gust: Double,      // Скорость порывов ветра (в м/с). Число
    val wind_dir: WindDir,      // Направление ветра.
    val pressure_mm: Int,       // Давление (в мм рт.ст.). Число
    val pressure_pa: Int,       // Давление (в гектопаскалях). Число
    val humidity: Int,          // Влажность воздуха (в процентах). Число
    val daytime: DayTime,       // Светлое или темное время суток.
    val polar: Boolean,         // Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
    val season: Season,         // Время года в данном населенном пункте.
    val obs_time: Long          // Время замера погодных данных в формате Unixtime. Число
) {
    constructor(
        temp: Int,
        feels_like: Int,
        temp_water: Int?,
        icon: String,
        condition: String,
        wind_speed: Double,
        wind_gust: Double,
        wind_dir: String,
        pressure_mm: Int,
        pressure_pa: Int,
        humidity: Int,
        daytime: String,
        polar: Boolean,
        season: String,
        obs_time: Long
    ) : this(
        temp,
        feels_like,
        temp_water,
        String.format("https://yastatic.net/weather/i/icons/funky/dark/%s.svg", icon),
        Condition.fromString(condition),
        wind_speed,
        wind_gust,
        WindDir.fromString(wind_dir),
        pressure_mm,
        pressure_pa,
        humidity,
        DayTime.fromString(daytime),
        polar,
        Season.fromString(season),
        obs_time
    )
}

fun testFact(): Fact {
    return Fact(
        20,
        21,
        null,
        "ovc",
        "overcast",
        2.0,
        .9,
        "nord",
        745,
        994,
        83,
        "day",
        false,
        "summer",
        1470214800
    )
}

data class Forecast(
    val date: String,            // Дата прогноза в формате ГГГГ-ММ-ДД. Строка
    val date_ts: Long,           // Дата прогноза в формате Unixtime. Число
    val week: Int,               // Порядковый номер недели. Число
    val sunrise: String,         // Время восхода Солнца, локальное время (может отсутствовать для полярных регионов). Строка
    val sunset: String,          // Время заката Солнца, локальное время (может отсутствовать для полярных регионов). Строка
    val moon_code: Int,          // Код фазы Луны.
    val moon_text: MoonText,     // Текстовый код для фазы Луны.
    val parts: Array<Parts>      // Прогнозы по времени суток
) {
    constructor(
        date: String,
        date_ts: Long,
        week: Int,
        sunrise: String,
        sunset: String,
        moon_code: Int,
        moon_text: String,
        parts: Array<Parts>
    ) : this(date, date_ts, week, sunrise, sunset, moon_code, MoonText.fromString(moon_text), parts)
}

fun testForecast(): Forecast {
    return Forecast(
        "2016-08-03",
        1522702800,
        15,
        "04:38",
        "20:31",
        1,
        "moon-code-1",
        arrayOf(testParts("day"), testParts("night"), testParts("day"), testParts("night"))
    )
}

data class Parts(
    val part_name: PartName,     // Название времени суток
    val temp_min: Int,           // Минимальная температура для времени суток (°C). Число
    val temp_max: Int,           // Максимальная температура для времени суток (°C). Число
    val temp_avg: Int,           // Средняя температура для времени суток (°C). Число
    val feels_like: Int,         // Ощущаемая температура (°C). Число
    val icon: String,            // Код иконки погоды https://yastatic.net/weather/i/icons/funky/dark/<значение поля icon>.svg. Строка
    val condition: Condition,    // Код расшифровки погодного описания
    val daytime: DayTime,        // Светлое или темное время суток
    val polar: Boolean,          // Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
    val wind_speed: Double,      // Скорость ветра (в м/с). Число
    val wind_gust: Double,       // Скорость порывов ветра (в м/с). Число
    val wind_dir: WindDir,       // Направление ветра
    val pressure_mm: Int,        // Давление (в мм рт. ст.). Число
    val pressure_pa: Int,        // Давление (в гектопаскалях). Число
    val humidity: Int,           // Влажность воздуха (в процентах). Число
    val prec_mm: Int,            // Прогнозируемое количество осадков (в мм). Число
    val prec_period: Int,        // Прогнозируемый период осадков (в минутах).	Число
    val prec_prob: Int,          // Вероятность выпадения осадков. Число
) {
    constructor(
        part_name: String,
        temp_min: Int,
        temp_max: Int,
        temp_avg: Int,
        feels_like: Int,
        icon: String,
        condition: String,
        daytime: String,
        polar: Boolean,
        wind_speed: Double,
        wind_gust: Double,
        wind_dir: String,
        pressure_mm: Int,
        pressure_pa: Int,
        humidity: Int,
        prec_mm: Int,
        prec_period: Int,
        prec_prob: Int
    ) : this(
        PartName.fromString(part_name),
        temp_min,
        temp_max,
        temp_avg,
        feels_like,
        String.format("https://yastatic.net/weather/i/icons/funky/dark/%s.svg", icon),
        Condition.fromString(condition),
        DayTime.fromString(daytime),
        polar,
        wind_speed,
        wind_gust,
        WindDir.fromString(wind_dir),
        pressure_mm,
        pressure_pa,
        humidity,
        prec_mm,
        prec_period,
        prec_prob
    )
}

fun testParts(partName: String): Parts {
    return Parts(
        partName,
        20,
        21,
        21,
        23,
        "bkn_n",
        "cloudy",
        "night",
        false,
        0.9,
        4.0,
        "nord-west",
        746,
        995,
        81,
        0,
        360,
        0
    )
}

//@Parcelize
enum class Language(val str: String) {
    RU("ru_RU"),     // русский язык для домена России.
    UA_RU("ru_UA"),  // русский язык для домена Украины.
    UA_UK("uk_UA"),  // украинский язык для домена Украины.
    BY("be_BY"),     // белорусский язык для домена Беларуси.
    KZ("kk_KZ"),     // казахский язык для домена Казахстана.
    TR("tr_TR"),     // турецкий язык для домена Турции.
    US("en_US");     // международный английский.

    companion object {
        fun fromString(str: String): Language {
            return when (str) {
                "ru_RU" -> RU
                "ru_UA" -> UA_RU
                "uk_UA" -> UA_UK
                "be_BY" -> BY
                "kk_KZ" -> KZ
                "tr_TR" -> TR
                "en_US" -> US
                else -> RU
            }
        }
    }
}

enum class Condition(val str: String) {
    CLEAR("clear") {                                     // ясно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-partly-clear"
                DayTime.NIGHT -> "night-partly-clear"
            }
        }
    },
    PARTLY_CLOUDY("partly-cloudy") {                     // малооблачно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-partly-cloudy"
                DayTime.NIGHT -> "night-partly-cloudy"
            }
        }
    },
    CLOUDY("cloudy") {                                   // облачно с прояснениями
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-cloudy"
                DayTime.NIGHT -> "night-cloudy"
            }
        }
    },
    OVERCAST("overcast") {                               // пасмурно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-overcast"
                DayTime.NIGHT -> "night-overcast"
            }
        }
    },
    DRIZZLE("drizzle") {                                 // морось
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-drizzle"
                DayTime.NIGHT -> "night-drizzle"
            }
        }
    },
    LIGHT_RAIN("light-rain") {                           // небольшой дождь.
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-light-rain"
                DayTime.NIGHT -> "night-light-rain"
            }
        }
    },
    RAIN("rain") {                                       // дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-rain"
                DayTime.NIGHT -> "night-rain"
            }
        }
    },
    MODERATE_RAIN("moderate-rain") {                     // умеренно сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-moderate-rain"
                DayTime.NIGHT -> "night-moderate-rain"
            }
        }
    },
    HEAVY_RAIN("heavy-rain") {                           // сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-moderate-rain"
                DayTime.NIGHT -> "night-moderate-rain"
            }
        }
    },
    CONTINUOUS_HEAVY_RAIN("continuous-heavy-rain") {                // длительный сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-moderate-rain"
                DayTime.NIGHT -> "night-moderate-rain"
            }
        }
    },
    SHOWERS("showers") {                                  // ливень
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-shower"
                DayTime.NIGHT -> "night-shower"
            }
        }
    },
    WET_SNOW("wet-snow") {                               // дождь со снегом
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-snow"
                DayTime.NIGHT -> "night-snow"
            }
        }
    },
    LIGHT_SNOW("light-snow") {                           // небольшой снег
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-snow"
                DayTime.NIGHT -> "night-snow"
            }
        }
    },
    SNOW("snow") {                                       // снег
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-snow"
                DayTime.NIGHT -> "night-snow"
            }
        }
    },
    SNOW_SHOWERS("snow-showers") {                       // снегопад
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-snow"
                DayTime.NIGHT -> "night-snow"
            }
        }
    },
    HAIL("hail") {                                       // град
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-hail"
                DayTime.NIGHT -> "night-hail"
            }
        }
    },
    THUNDERSTORM("thunderstorm") {                       // гроза
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-thunderstorm"
                DayTime.NIGHT -> "night-thunderstorm"
            }
        }
    },
    THUNDERSTORM_WITH_RAIN("thunderstorm-with-rain") {   // дождь с грозой
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-thunderstorm-with-rain"
                DayTime.NIGHT -> "night-thunderstorm-with-rain"
            }
        }
    },
    THUNDERSTORM_WITH_HAIL("thunderstorm-with-hail") {    // гроза с градом
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day-hail"
                DayTime.NIGHT -> "night-hail"
            }
        }
    };

    abstract fun getIcon(dayTime: DayTime): String

    companion object {
        fun fromString(str: String): Condition {
            return when (str) {
                "clear" -> CLEAR
                "partly-cloudy" -> PARTLY_CLOUDY
                "cloudy" -> CLOUDY
                "overcast" -> OVERCAST
                "drizzle" -> DRIZZLE
                "light-rain" -> LIGHT_RAIN
                "rain" -> RAIN
                "moderate-rain" -> MODERATE_RAIN
                "heavy-rain" -> HEAVY_RAIN
                "continuous-heavy-rain" -> CONTINUOUS_HEAVY_RAIN
                "showers" -> SHOWERS
                "wet-snow" -> WET_SNOW
                "light-snow" -> LIGHT_SNOW
                "snow" -> SNOW
                "snow-showers" -> SNOW_SHOWERS
                "hail" -> HAIL
                "thunderstorm" -> THUNDERSTORM
                "thunderstorm-with-rain" -> THUNDERSTORM_WITH_RAIN
                "thunderstorm-with-hail" -> THUNDERSTORM_WITH_HAIL
                else -> CLEAR
            }
        }
    }
}

enum class WindDir(val str: String) {
    NORD_WEST("nw") {    // северо-западное
        override fun getDirection() = "down-right"
    },
    NORD("n") {          // северное
        override fun getDirection() = "down"
    },
    NORD_EAST("ne") {    // северо-восточное
        override fun getDirection() = "down-left"
    },
    EAST("e") {          // восточное
        override fun getDirection() = "left"
    },
    SOUTH_EAST("se") {   // юго-восточное
        override fun getDirection() = "up-left"
    },
    SOUTH("s") {         // южное
        override fun getDirection() = "up"
    },
    SOUTH_WEST("sw") {   // юго-западное
        override fun getDirection() = "up-right"
    },
    WEST("w") {          // западное
        override fun getDirection() = "right"
    },
    CALM("с") {           // штиль
        override fun getDirection() = "calm"
    };

    abstract fun getDirection(): String

    companion object {
        fun fromString(str: String): WindDir {
            return when (str) {
                "nw" -> NORD_WEST
                "n" -> NORD
                "ne" -> NORD_EAST
                "e" -> EAST
                "se" -> SOUTH_EAST
                "s" -> SOUTH
                "sw" -> SOUTH_WEST
                "w" -> WEST
                "c" -> CALM
                else -> CALM
            }
        }
    }
}

enum class DayTime(val str: String) {
    DAY("d"),           // светлое время суток
    NIGHT("n");         // темное время суток

    companion object {
        fun fromString(str: String): DayTime {
            return when (str) {
                "d" -> DAY
                "n" -> NIGHT
                else -> DAY
            }
        }
    }
}

enum class Season(val str: String) {
    SUMMER("summer"),   // лето.
    AUTUMN("autumn"),   // осень.
    WINTER("winter"),   // зима.
    SPRING("spring");   // весна.

    companion object {
        fun fromString(str: String): Season {
            return when (str) {
                "summer" -> SUMMER
                "autumn" -> AUTUMN
                "winter" -> WINTER
                "spring" -> SPRING
                else -> SUMMER
            }
        }
    }
}

enum class MoonText(val str: String) {
    MOON_CODE_0("moon-code-0") {        //  полнолуние
        override fun getIcon() = "moon-00"
    },
    MOON_CODE_1("moon-code-1") {        // убывающая луна
        override fun getIcon() = "moon-01"
    },
    MOON_CODE_2("moon-code-2") {        // убывающая луна
        override fun getIcon() = "moon-02"
    },
    MOON_CODE_3("moon-code-3") {        // убывающая луна
        override fun getIcon() = "moon-03"
    },
    MOON_CODE_4("moon-code-4") {        // последняя четверть
        override fun getIcon() = "moon-04"
    },
    MOON_CODE_5("moon-code-5") {        // убывающая луна
        override fun getIcon() = "moon-05"
    },
    MOON_CODE_6("moon-code-6") {        // убывающая луна
        override fun getIcon() = "moon-06"
    },
    MOON_CODE_7("moon-code-7") {        // убывающая луна
        override fun getIcon() = "moon-07"
    },
    MOON_CODE_8("moon-code-8") {        // новолуние
        override fun getIcon() = "moon-08"
    },
    MOON_CODE_9("moon-code-9") {        // растущая луна
        override fun getIcon() = "moon-09"
    },
    MOON_CODE_10("moon-code-10") {        // растущая луна
        override fun getIcon() = "moon-10"
    },
    MOON_CODE_11("moon-code-11") {        // растущая луна
        override fun getIcon() = "moon-11"
    },
    MOON_CODE_12("moon-code-12") {        // первая четверть
        override fun getIcon() = "moon-12"
    },
    MOON_CODE_13("moon-code-13") {        // растущая луна
        override fun getIcon() = "moon-13"
    },
    MOON_CODE_14("moon-code-14") {        // растущая луна
        override fun getIcon() = "moon-14"
    },
    MOON_CODE_15("moon-code-15") {        // растущая луна
        override fun getIcon() = "moon-15"
    };

    abstract fun getIcon(): String

    companion object {
        fun fromString(str: String): MoonText {
            return when (str) {
                "moon-code-0" -> MOON_CODE_0
                "moon-code-1" -> MOON_CODE_1
                "moon-code-2" -> MOON_CODE_2
                "moon-code-3" -> MOON_CODE_3
                "moon-code-4" -> MOON_CODE_4
                "moon-code-5" -> MOON_CODE_5
                "moon-code-6" -> MOON_CODE_6
                "moon-code-7" -> MOON_CODE_7
                "moon-code-8" -> MOON_CODE_8
                "moon-code-9" -> MOON_CODE_9
                "moon-code-10" -> MOON_CODE_10
                "moon-code-11" -> MOON_CODE_11
                "moon-code-12" -> MOON_CODE_12
                "moon-code-13" -> MOON_CODE_13
                "moon-code-14" -> MOON_CODE_14
                "moon-code-15" -> MOON_CODE_15
                else -> MOON_CODE_0
            }
        }
    }
}

enum class PartName(val str: String) {
    NIGHT("night") {
        override fun getValue() = "ночь"
    },         // ночь.
    MORNING("morning") {
        override fun getValue() = "утро"
    },     // утро.
    DAY("day") {
        override fun getValue() = "день"
    },             // день.
    EVENING("evening") {
        override fun getValue() = "вечер"
    };      // вечер.

    abstract fun getValue(): String

    companion object {
        fun fromString(str: String): PartName {
            return when (str) {
                "night" -> NIGHT
                "morning" -> MORNING
                "day" -> DAY
                "evening" -> EVENING
                else -> DAY
            }
        }
    }
}


// Ответ:
//
//{
//  "now": 1470220206,
//  "now_dt": "2016-08-03T10:30:06.238Z",
//  "info": {
//    "lat": 55.833333,
//    "lon": 37.616667,
//    "url": "https://yandex.ru/pogoda/moscow"
//  },
//  "fact": {
//    "temp": 20,
//    "feels_like": 21,
//    "icon": "ovc",
//    "condition": "overcast",
//    "wind_speed": 2,
//    "wind_gust": 5.9,
//    "wind_dir": "n",
//    "pressure_mm": 745,
//    "pressure_pa": 994,
//    "humidity": 83,
//    "daytime": "d",
//    "polar": false,
//    "season": "summer",
//    "obs_time": 1470214800
//  },
//  "forecast": {
//    "date": "2016-08-03",
//    "date_ts": 1522702800,
//    "week": 15,
//    "sunrise": "04:38",
//    "sunset": "20:31",
//    "moon_code": 1,
//    "moon_text": "moon-code-1",
//    "parts": [
//      {
//        "part_name": "day",
//        "temp_min": 20,
//        "temp_max": 21,
//        "temp_avg": 21,
//        "feels_like": 23,
//        "icon": "bkn_n",
//        "condition": "cloudy",
//        "daytime": "n",
//        "polar": false,
//        "wind_speed": 0.9,
//        "wind_gust": 4,
//        "wind_dir": "nw",
//        "pressure_mm": 746,
//        "pressure_pa": 995,
//        "humidity": 81,
//        "prec_mm": 0,
//        "prec_period": 360,
//        "prec_prob": 0
//      },
//      {
//        "part_name": "evening",
//        ...
//      },
//      {...}
//    ]
//  }
//}
