package classes

import kotlinx.serialization.Serializable

/**
 * Timetable settings
 */
@Serializable
class Settings {
    /**
     * Рабочие дни
     */
    var workingDays: MutableList<String> = listOf(
        "Понедельник",
        "Вторник",
        "Среда",
        "Четверг",
        "Пятница",
        "Суббота"
    ).toMutableList()

    /**
     * TODO Время уроков
     */
    var lessonsTime: MutableList<String> = listOf(
        "08:30 - 09-15",
        "09:25 - 10:10",
        "10:20 - 11:05",
        "11:30 - 12:15",
        "12:45 - 13:30",
        "13:40 - 14:25",
        "14:30 - 15-15"
    ).toMutableList()

    /**
     * TODO Стили
     */

    /**
     * TODO Путь к конфигу
     */

    /**
     * TODO Сохранение настроек
     */

    /**
     * TODO Внешниий вид
     */

    init {
        // Рабочие дни


        // Время уроков
    }
}