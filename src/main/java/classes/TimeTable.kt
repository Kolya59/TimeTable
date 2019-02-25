package classes

import kotlinx.serialization.*

abstract class TimeTable(lessons: Set<Lesson>) {
    /**
     * School timetable
     */

    // Lessons
    protected var lessons : Set<Lesson> = lessons
}