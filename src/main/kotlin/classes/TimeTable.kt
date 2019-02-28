package classes

import kotlinx.serialization.*

/**
 * School timetable
 * @param[lessons] School lessons set
 */
@Serializable
abstract class TimeTable(lessons: Set<Lesson>) {
    protected var lessons : Set<Lesson> = lessons
}