package classes

import kotlinx.serialization.Serializable

/**
 * School TimetableCell
 * @param[lessons] School lessons set
 * @param[teachers] List of teachers, which dont teach lessons
 * @param[classrooms] List of classrooms, which dont used
 * @param[studentClasses] List of student classes, which dont study
 */
@Serializable
data class TimeTable(
    internal var lessons: MutableList<Lesson>,
    internal var teachers: MutableList<Teacher>,
    internal var classrooms: MutableList<Classroom>,
    internal var studentClasses: MutableList<StudentClass>
)
