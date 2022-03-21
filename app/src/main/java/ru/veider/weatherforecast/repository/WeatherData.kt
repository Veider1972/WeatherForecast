package ru.veider.weatherforecast.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherQuery(
    var name: String, var latitude: Double, var longitude: Double, var language: Language
) : Parcelable

@Parcelize
enum class Language(val str: String) : Parcelable {
    RU("ru_RU"),     // русский язык для домена России.
    UA_RU("ru_UA"),  // русский язык для домена Украины.
    UA_UK("uk_UA"),  // украинский язык для домена Украины.
    BY("be_BY"),     // белорусский язык для домена Беларуси.
    KZ("kk_KZ"),     // казахский язык для домена Казахстана.
    TR("tr_TR"),     // турецкий язык для домена Турции.
    US("en_US");     // международный английский.
}

enum class DataLoading {
    RUSSIAN, FOREIGN
}

fun getRussianCities(): Array<WeatherQuery> {
    return arrayOf(
        WeatherQuery("Москва", 55.755826, 37.617299900000035, Language.RU),
        WeatherQuery("Санкт-Петербург", 59.9342802, 30.335098600000038, Language.RU),
        WeatherQuery("Новосибирск", 55.00835259999999, 82.93573270000002, Language.RU),
        WeatherQuery("Екатеринбург", 56.83892609999999, 60.60570250000001, Language.RU),
        WeatherQuery("Нижний Новгород", 56.2965039, 43.936059, Language.RU),
        WeatherQuery("Казань", 55.8304307, 49.06608060000008, Language.RU),
        WeatherQuery("Челябинск", 55.1644419, 61.4368432, Language.RU),
        WeatherQuery("Омск", 54.9884804, 73.32423610000001, Language.RU),
        WeatherQuery("Ростов-на-Дону", 47.2357137, 39.701505, Language.RU),
        WeatherQuery("Уфа", 54.7387621, 55.972055400000045, Language.RU)
    )
}

fun getForeignCities(): Array<WeatherQuery> {
    return arrayOf(
        WeatherQuery("Лондон", 51.5085300, -0.1257400, Language.RU),
        WeatherQuery("Токио", 35.6895000, 139.6917100, Language.RU),
        WeatherQuery("Париж", 48.8534100, 2.3488000, Language.RU),
        WeatherQuery("Берлин", 52.52000659999999, 13.404953999999975, Language.RU),
        WeatherQuery("Рим", 41.9027835, 12.496365500000024, Language.RU),
        WeatherQuery("Минск", 53.90453979999999, 27.561524400000053, Language.RU),
        WeatherQuery("Стамбул", 41.0082376, 28.97835889999999, Language.RU),
        WeatherQuery("Вашингтон", 38.9071923, -77.03687070000001, Language.RU),
        WeatherQuery("Киев", 50.4501, 30.523400000000038, Language.RU),
        WeatherQuery("Пекин", 39.90419989999999, 116.40739630000007, Language.RU)
    )
}


data class WeatherData(
    @SerializedName("now") val now: Long?,                     // Время сервера в формате Unixtime. Число
    @SerializedName("info") val info: Info?,                   // Объект информации о населенном пункте. Объект
    @SerializedName("geo_object") val geo_object: GeoObject?,  // Местоположение
    @SerializedName("fact") val fact: Fact?                   // Объект фактической информации о погоде. Объект
)

data class Info(
    @SerializedName("lat") val latitude: Double?,    // Широта (в градусах). Число
    @SerializedName("lon") val longitude: Double?,   // Долгота (в градусах). Число
)

data class GeoObject(
    @SerializedName("district") val district: District?,    // Район
    @SerializedName("locality") val locality: Locality?,    // Область
    @SerializedName("province") val province: Province?,    // Субьект
    @SerializedName("country") val country: Country?        // Страна
)

data class District(
    val id: Int?, val name: String?
)

data class Locality(
    val id: Int?, val name: String?
)

data class Province(
    val id: Int?, val name: String?
)

data class Country(
    val id: Int?, val name: String?
)

data class Fact(
    @SerializedName("temp") val temp: Int?,                 // Температура (°C)
    @SerializedName("feels_like") val feels_like: Int?,     // Ощущаемая температура (°C)
    @SerializedName("temp_water") val temp_water: Int?,     // Температура воды (°C). Параметр возвращается для там, где данная информация актуальна
    @SerializedName("condition") val condition: Condition?, // Код расшифровки погодного описания
    @SerializedName("wind_speed") val wind_speed: Double?,  // Скорость ветра (в м/с). Число
    @SerializedName("wind_dir") val wind_dir: WindDir?,     // Направление ветра.
    @SerializedName("pressure_mm") val pressure_mm: Int?,   // Давление (в мм рт.ст.). Число
    @SerializedName("humidity") val humidity: Int?,         // Влажность воздуха (в процентах). Число
    @SerializedName("daytime") val daytime: DayTime?,       // Светлое или темное время суток.
)

enum class Condition {
    @SerializedName("clear")
    CLEAR {                                     // ясно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_clear"
                DayTime.NIGHT -> "night_clear"
            }
        }
    },

    @SerializedName("partly-cloudy")
    PARTLY_CLOUDY {                     // малооблачно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_partly_cloudy"
                DayTime.NIGHT -> "night_partly_cloudy"
            }
        }
    },

    @SerializedName("cloudy")
    CLOUDY {                                   // облачно с прояснениями
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_cloudy"
                DayTime.NIGHT -> "night_cloudy"
            }
        }
    },

    @SerializedName("overcast")
    OVERCAST {                               // пасмурно
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_overcast"
                DayTime.NIGHT -> "night_overcast"
            }
        }
    },

    @SerializedName("drizzle")
    DRIZZLE {                                 // морось
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_drizzle"
                DayTime.NIGHT -> "night_drizzle"
            }
        }
    },

    @SerializedName("light-rain")
    LIGHT_RAIN {                           // небольшой дождь.
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_light_rain"
                DayTime.NIGHT -> "night_light_rain"
            }
        }
    },

    @SerializedName("rain")
    RAIN {                                       // дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_rain"
                DayTime.NIGHT -> "night_rain"
            }
        }
    },

    @SerializedName("moderate-rain")
    MODERATE_RAIN {                     // умеренно сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_moderate_rain"
                DayTime.NIGHT -> "night_moderate_rain"
            }
        }
    },

    @SerializedName("heavy-rain")
    HEAVY_RAIN {                           // сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_moderate_rain"
                DayTime.NIGHT -> "night_moderate_rain"
            }
        }
    },

    @SerializedName("continuous-heavy-rain")
    CONTINUOUS_HEAVY_RAIN {                // длительный сильный дождь
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_moderate_rain"
                DayTime.NIGHT -> "night_moderate_rain"
            }
        }
    },

    @SerializedName("showers")
    SHOWERS {                                  // ливень
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_shower"
                DayTime.NIGHT -> "night_shower"
            }
        }
    },

    @SerializedName("wet-snow")
    WET_SNOW {                               // дождь со снегом
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_snow"
                DayTime.NIGHT -> "night_snow"
            }
        }
    },

    @SerializedName("light-snow")
    LIGHT_SNOW {                           // небольшой снег
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_snow"
                DayTime.NIGHT -> "night_snow"
            }
        }
    },

    @SerializedName("snow")
    SNOW {                                       // снег
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_snow"
                DayTime.NIGHT -> "night_snow"
            }
        }
    },

    @SerializedName("snow-showers")
    SNOW_SHOWERS {                       // снегопад
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_snow"
                DayTime.NIGHT -> "night_snow"
            }
        }
    },

    @SerializedName("hail")
    HAIL {                                       // град
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_hail"
                DayTime.NIGHT -> "night_hail"
            }
        }
    },

    @SerializedName("thunderstorm")
    THUNDERSTORM {                       // гроза
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_thunderstorm"
                DayTime.NIGHT -> "night_thunderstorm"
            }
        }
    },

    @SerializedName("thunderstorm-with-rain")
    THUNDERSTORM_WITH_RAIN {   // дождь с грозой
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_thunderstorm_with_rain"
                DayTime.NIGHT -> "night_thunderstorm_with_rain"
            }
        }
    },

    @SerializedName("thunderstorm-with-hail")
    THUNDERSTORM_WITH_HAIL {    // гроза с градом
        override fun getIcon(dayTime: DayTime): String {
            return when (dayTime) {
                DayTime.DAY -> "day_hail"
                DayTime.NIGHT -> "night_hail"
            }
        }
    };

    abstract fun getIcon(dayTime: DayTime): String
}

enum class WindDir {
    @SerializedName("nw")
    NORD_WEST {    // северо-западное
        override fun getDirection() = "direction_down_right"
    },

    @SerializedName("n")
    NORD {          // северное
        override fun getDirection() = "direction_down"
    },

    @SerializedName("ne")
    NORD_EAST {    // северо-восточное
        override fun getDirection() = "direction_down_left"
    },

    @SerializedName("e")
    EAST {          // восточное
        override fun getDirection() = "direction_left"
    },

    @SerializedName("se")
    SOUTH_EAST {   // юго-восточное
        override fun getDirection() = "direction_up_left"
    },

    @SerializedName("s")
    SOUTH {         // южное
        override fun getDirection() = "direction_up"
    },

    @SerializedName("sw")
    SOUTH_WEST {   // юго-западное
        override fun getDirection() = "direction_up_right"
    },

    @SerializedName("w")
    WEST {          // западное
        override fun getDirection() = "direction_right"
    },

    @SerializedName("c")
    CALM {           // штиль
        override fun getDirection() = "direction_calm"
    };

    abstract fun getDirection(): String
}

enum class DayTime {
    @SerializedName("d")
    DAY,           // светлое время суток

    @SerializedName("n")
    NIGHT;         // темное время суток
}

//
// Запрос: https://api.weather.yandex.ru/v2/forecast?lat=55.75396&lon=37.620393&limit=1&hours=false&extra=false
// Ответ:
// {
//    "now": 1646065329,
//    "now_dt": "2022-02-28T16:22:09.920314Z",
//    "info": {
//        "n": true,
//        "geoid": 213,
//        "url": "https://yandex.ru/pogoda/213?lat=55.75396&lon=37.620393",
//        "lat": 55.75396,
//        "lon": 37.620393,
//        "tzinfo": {
//            "name": "Europe/Moscow",
//            "abbr": "MSK",
//            "dst": false,
//            "offset": 10800
//        },
//        "def_pressure_mm": 745,
//        "def_pressure_pa": 993,
//        "slug": "213",
//        "zoom": 10,
//        "nr": true,
//        "ns": true,
//        "nsr": true,
//        "p": false,
//        "f": true,
//        "_h": false
//    },
//    "geo_object": {
//        "district": {
//            "id": 120540,
//            "name": "Тверской район"
//        },
//        "locality": {
//            "id": 213,
//            "name": "Москва"
//        },
//        "province": {
//            "id": 213,
//            "name": "Москва"
//        },
//        "country": {
//            "id": 225,
//            "name": "Россия"
//        }
//    },
//    "yesterday": {
//        "temp": -1
//    },
//    "fact": {
//        "obs_time": 1646064000,
//        "uptime": 1646065329,
//        "temp": -2,
//        "feels_like": -6,
//        "icon": "skc_n",
//        "condition": "clear",
//        "cloudness": 0,
//        "prec_type": 0,
//        "prec_prob": 0,
//        "prec_strength": 0,
//        "is_thunder": false,
//        "wind_speed": 1,
//        "wind_dir": "nw",
//        "pressure_mm": 761,
//        "pressure_pa": 1014,
//        "humidity": 54,
//        "daytime": "n",
//        "polar": false,
//        "season": "winter",
//        "source": "station",
//        "accum_prec": {
//            "7": 8.683477,
//            "3": 0.09985932,
//            "1": 0
//        },
//        "soil_moisture": 0.37,
//        "soil_temp": 0,
//        "uv_index": 0,
//        "wind_gust": 3.9
//    },
//    "forecasts": [
//        {
//            "date": "2022-02-28",
//            "date_ts": 1645995600,
//            "week": 9,
//            "sunrise": "07:24",
//            "sunset": "18:00",
//            "rise_begin": "06:46",
//            "set_end": "18:37",
//            "moon_code": 6,
//            "moon_text": "moon-code-6",
//            "parts": {
//                "night": {
//                    "_source": "0,1,2,3,4,5",
//                    "temp_min": -5,
//                    "temp_avg": -4,
//                    "temp_max": -4,
//                    "wind_speed": 2.8,
//                    "wind_gust": 5.3,
//                    "wind_dir": "n",
//                    "pressure_mm": 763,
//                    "pressure_pa": 1017,
//                    "humidity": 86,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 360,
//                    "cloudness": 0,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "skc_n",
//                    "condition": "clear",
//                    "uv_index": 0,
//                    "feels_like": -9,
//                    "daytime": "n",
//                    "polar": false,
//                    "fresh_snow_mm": 0
//                },
//                "evening": {
//                    "_source": "18,19,20,21,22,23",
//                    "temp_min": -5,
//                    "temp_avg": -3,
//                    "temp_max": -1,
//                    "wind_speed": 2.4,
//                    "wind_gust": 4.8,
//                    "wind_dir": "n",
//                    "pressure_mm": 762,
//                    "pressure_pa": 1015,
//                    "humidity": 83,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 360,
//                    "cloudness": 0,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "skc_n",
//                    "condition": "clear",
//                    "uv_index": 0,
//                    "feels_like": -7,
//                    "daytime": "n",
//                    "polar": false,
//                    "fresh_snow_mm": 0
//                },
//                "day_short": {
//                    "_source": "6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21",
//                    "temp": 0,
//                    "temp_min": -5,
//                    "wind_speed": 4.1,
//                    "wind_gust": 7.6,
//                    "wind_dir": "n",
//                    "pressure_mm": 762,
//                    "pressure_pa": 1015,
//                    "humidity": 81,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 960,
//                    "cloudness": 0.5,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "bkn_d",
//                    "condition": "cloudy",
//                    "uv_index": 1,
//                    "feels_like": -7,
//                    "daytime": "d",
//                    "polar": false,
//                    "fresh_snow_mm": 0
//                },
//                "day": {
//                    "_source": "12,13,14,15,16,17",
//                    "temp_min": -1,
//                    "temp_avg": 0,
//                    "temp_max": 0,
//                    "wind_speed": 4.1,
//                    "wind_gust": 7.6,
//                    "wind_dir": "n",
//                    "pressure_mm": 762,
//                    "pressure_pa": 1015,
//                    "humidity": 77,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 360,
//                    "cloudness": 1,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "ovc",
//                    "condition": "overcast",
//                    "uv_index": 1,
//                    "feels_like": -5,
//                    "daytime": "d",
//                    "polar": false
//                },
//                "morning": {
//                    "_source": "6,7,8,9,10,11",
//                    "temp_min": -5,
//                    "temp_avg": -3,
//                    "temp_max": -1,
//                    "wind_speed": 3.3,
//                    "wind_gust": 6,
//                    "wind_dir": "n",
//                    "pressure_mm": 762,
//                    "pressure_pa": 1015,
//                    "humidity": 86,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 360,
//                    "cloudness": 0.5,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "bkn_d",
//                    "condition": "cloudy",
//                    "uv_index": 1,
//                    "feels_like": -8,
//                    "daytime": "d",
//                    "polar": false,
//                    "fresh_snow_mm": 0
//                },
//                "night_short": {
//                    "_source": "0,1,2,3,4,5",
//                    "temp": -5,
//                    "wind_speed": 2.8,
//                    "wind_gust": 5.3,
//                    "wind_dir": "n",
//                    "pressure_mm": 763,
//                    "pressure_pa": 1017,
//                    "humidity": 86,
//                    "soil_temp": 0,
//                    "soil_moisture": 0.37,
//                    "prec_mm": 0,
//                    "prec_prob": 0,
//                    "prec_period": 360,
//                    "cloudness": 0,
//                    "prec_type": 0,
//                    "prec_strength": 0,
//                    "icon": "skc_n",
//                    "condition": "clear",
//                    "uv_index": 0,
//                    "feels_like": -9,
//                    "daytime": "n",
//                    "polar": false,
//                    "fresh_snow_mm": 0
//                }
//            },
//            "hours": [],
//            "biomet": {
//                "index": 0,
//                "condition": "magnetic-field_0"
//            }
//        }
//    ]
//}