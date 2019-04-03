package classes

import kotlinx.serialization.Serializable

/**
 * School timetableCell
 * @param[lessons] School lessons set
 */
@Serializable
data class TimeTable(internal var lessons: Set<Lesson>)
