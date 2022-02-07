package ru.veider.weatherforecast.data

data class WeatherQuery(
    val latitude: Double, val longitude: Double, val language: Language,
    val key: String = "b5eedce9-6c9a-44aa-8019-e78fa1b018e7" // ключ разработчика
) {
    constructor(
        latitude: Double, longitude: Double, language: String
    ) : this(
        latitude, longitude, Language.valueOf(language)
    )
}

fun testWeatherQuery(): WeatherQuery {
    return WeatherQuery(55.833333, 37.616667, "ru_RU")
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
        temp: Int, feels_like: Int, temp_water: Int?, icon: String, condition: String,
        wind_speed: Double, wind_gust: Double, wind_dir: String, pressure_mm: Int, pressure_pa: Int,
        humidity: Int, daytime: String, polar: Boolean, season: String, obs_time: Long
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
    val week: Int,               // Порядковый номер недели.	Число
    val sunrise: String,         // Время восхода Солнца, локальное время (может отсутствовать для полярных регионов). Строка
    val sunset: String,          // Время заката Солнца, локальное время (может отсутствовать для полярных регионов). Строка
    val moon_code: Int,     // Код фазы Луны.
    val moon_text: MoonText,     // Текстовый код для фазы Луны.
    var parts: Array<Parts>     // Прогнозы по времени суток
) {
    constructor(
        date: String, date_ts: Long, week: Int, sunrise: String, sunset: String, moon_code: Int,
        moon_text: String, parts: Array<Parts>
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
    val part_name: PartName,    // Название времени суток
    val temp_min: Int,           // Минимальная температура для времени суток (°C). Число
    val temp_max: Int,           // Максимальная температура для времени суток (°C). Число
    val temp_avg: Int,           // Средняя температура для времени суток (°C). Число
    val feels_like: Int,        // Ощущаемая температура (°C). Число
    val icon: String,           // Код иконки погоды https://yastatic.net/weather/i/icons/funky/dark/<значение поля icon>.svg. Строка
    val condition: Condition,    // Код расшифровки погодного описания
    val daytime: DayTime,        // Светлое или темное время суток
    val polar: Boolean,          // Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
    val wind_speed: Double,         // Скорость ветра (в м/с). Число
    val wind_gust: Double,          // Скорость порывов ветра (в м/с). Число
    val wind_dir: WindDir,       // Направление ветра
    val pressure_mm: Int,        // Давление (в мм рт. ст.). Число
    val pressure_pa: Int,        // Давление (в гектопаскалях). Число
    val humidity: Int,           // Влажность воздуха (в процентах). Число
    val prec_mm: Int,            // Прогнозируемое количество осадков (в мм). Число
    val prec_period: Int,        // Прогнозируемый период осадков (в минутах).	Число
    val prec_prob: Int,           // Вероятность выпадения осадков. Число
) {
    constructor(
        part_name: String, temp_min: Int, temp_max: Int, temp_avg: Int, feels_like: Int,
        icon: String, condition: String, daytime: String, polar: Boolean, wind_speed: Double,
        wind_gust: Double, wind_dir: String, pressure_mm: Int, pressure_pa: Int, humidity: Int,
        prec_mm: Int, prec_period: Int, prec_prob: Int
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
            when (str) {
                "ru_RU" -> return RU
                "ru_UA" -> return UA_RU
                "uk_UA" -> return UA_UK
                "be_BY" -> return BY
                "kk_KZ" -> return KZ
                "tr_TR" -> return TR
                "en_US" -> return US
            }
            return US
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
            when (str) {
                "clear" -> return CLEAR
                "partly-cloudy" -> return PARTLY_CLOUDY
                "cloudy" -> return CLOUDY
                "overcast" -> return OVERCAST
                "drizzle" -> return DRIZZLE
                "light-rain" -> return LIGHT_RAIN
                "rain" -> return RAIN
                "moderate-rain" -> return MODERATE_RAIN
                "heavy-rain" -> return HEAVY_RAIN
                "continuous-heavy-rain" -> return CONTINUOUS_HEAVY_RAIN
                "showers" -> return SHOWERS
                "wet-snow" -> return WET_SNOW
                "light-snow" -> return LIGHT_SNOW
                "snow" -> return SNOW
                "snow-showers" -> return SNOW_SHOWERS
                "hail" -> return HAIL
                "thunderstorm" -> return THUNDERSTORM
                "thunderstorm-with-rain" -> return THUNDERSTORM_WITH_RAIN
                "thunderstorm-with-hail" -> return THUNDERSTORM_WITH_HAIL
            }
            return CLEAR
        }
    }
}

enum class WindDir(val str: String) {
    NORD_WEST("nw") {    // северо-западное
        override fun getDirection(): String {
            return "down-right"
        }
    },
    NORD("n") {          // северное
        override fun getDirection(): String {
            return "down"
        }
    },
    NORD_EAST("ne") {    // северо-восточное
        override fun getDirection(): String {
            return "down-left"
        }
    },
    EAST("e") {          // восточное
        override fun getDirection(): String {
            return "left"
        }
    },
    SOUTH_EAST("se") {   // юго-восточное
        override fun getDirection(): String {
            return "up-left"
        }
    },
    SOUTH("s") {         // южное
        override fun getDirection(): String {
            return "up"
        }
    },
    SOUTH_WEST("sw") {   // юго-западное
        override fun getDirection(): String {
            return "up-right"
        }
    },
    WEST("w") {          // западное
        override fun getDirection(): String {
            return "right"
        }
    },
    CALM("с") {           // штиль
        override fun getDirection(): String {
            return "calm"
        }
    };

    abstract fun getDirection(): String

    companion object {
        fun fromString(str: String): WindDir {
            when (str) {
                "nw" -> return NORD_WEST
                "n" -> return NORD
                "ne" -> return NORD_EAST
                "e" -> return EAST
                "se" -> return SOUTH_EAST
                "s" -> return SOUTH
                "sw" -> return SOUTH_WEST
                "w" -> return WEST
                "c" -> return CALM
            }
            return CALM
        }

    }
}

enum class DayTime(val str: String) {
    DAY("d"),           // светлое время суток
    NIGHT("n");         // темное время суток

    companion object {
        fun fromString(str: String): DayTime {
            when (str) {
                "d" -> return DAY
                "n" -> return NIGHT
            }
            return DAY
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
            when (str) {
                "summer" -> return SUMMER
                "autumn" -> return AUTUMN
                "winter" -> return WINTER
                "spring" -> return SPRING
            }
            return SUMMER
        }
    }
}


enum class MoonText(val str: String) {
    MOON_CODE_0("moon-code-0") {        //  полнолуние
        override fun getIcon(): String {
            return "moon-00"
        }
    },     // полнолуние
    MOON_CODE_1("moon-code-1") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-01"
        }
    },     // убывающая луна
    MOON_CODE_2("moon-code-2") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-02"
        }
    },     // убывающая луна
    MOON_CODE_3("moon-code-3") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-03"
        }
    },     // убывающая луна
    MOON_CODE_4("moon-code-4") {        // последняя четверть
        override fun getIcon(): String {
            return "moon-04"
        }
    },     // последняя четверть
    MOON_CODE_5("moon-code-5") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-05"
        }
    },     // убывающая луна
    MOON_CODE_6("moon-code-6") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-06"
        }
    },     // убывающая луна
    MOON_CODE_7("moon-code-7") {        // убывающая луна
        override fun getIcon(): String {
            return "moon-07"
        }
    },     // убывающая луна
    MOON_CODE_8("moon-code-8") {        // новолуние
        override fun getIcon(): String {
            return "moon-08"
        }
    },     // новолуние
    MOON_CODE_9("moon-code-9") {        // растущая луна
        override fun getIcon(): String {
            return "moon-09"
        }
    },     // растущая луна
    MOON_CODE_10("moon-code-10") {        // растущая луна
        override fun getIcon(): String {
            return "moon-10"
        }
    },   // растущая луна
    MOON_CODE_11("moon-code-11") {        // растущая луна
        override fun getIcon(): String {
            return "moon-11"
        }
    },   // растущая луна
    MOON_CODE_12("moon-code-12") {        // первая четверть
        override fun getIcon(): String {
            return "moon-12"
        }
    },   // первая четверть
    MOON_CODE_13("moon-code-13") {        // растущая луна
        override fun getIcon(): String {
            return "moon-13"
        }
    },   // растущая луна
    MOON_CODE_14("moon-code-14") {        // растущая луна
        override fun getIcon(): String {
            return "moon-14"
        }
    },   // растущая луна
    MOON_CODE_15("moon-code-15") {        // растущая луна
        override fun getIcon(): String {
            return "moon-15"
        }
    };    // растущая луна

    abstract fun getIcon(): String

    companion object {
        fun fromString(str: String): MoonText {
            when (str) {
                "moon-code-0" -> return MOON_CODE_0
                "moon-code-1" -> return MOON_CODE_1
                "moon-code-2" -> return MOON_CODE_2
                "moon-code-3" -> return MOON_CODE_3
                "moon-code-4" -> return MOON_CODE_4
                "moon-code-5" -> return MOON_CODE_5
                "moon-code-6" -> return MOON_CODE_6
                "moon-code-7" -> return MOON_CODE_7
                "moon-code-8" -> return MOON_CODE_8
                "moon-code-9" -> return MOON_CODE_9
                "moon-code-10" -> return MOON_CODE_10
                "moon-code-11" -> return MOON_CODE_11
                "moon-code-12" -> return MOON_CODE_12
                "moon-code-13" -> return MOON_CODE_13
                "moon-code-14" -> return MOON_CODE_14
                "moon-code-15" -> return MOON_CODE_15
            }
            return MOON_CODE_0
        }
    }
}

enum class PartName(val str: String) {
    NIGHT("night"){override fun getValue(): String {return "ночь"}},         // ночь.
    MORNING("morning"){override fun getValue(): String {return "утро"}},     // утро.
    DAY("day"){override fun getValue(): String {return "день"}},             // день.
    EVENING("evening"){override fun getValue(): String {return "вечер"}};      // вечер.

    abstract fun getValue():String

    companion object {
        fun fromString(str: String): PartName {
            when (str) {
                "night" -> return NIGHT
                "morning" -> return MORNING
                "day" -> return DAY
                "evening" -> return EVENING
            }
            return DAY
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
