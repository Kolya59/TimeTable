package classes

import kotlinx.serialization.Serializable

/**
 * School lesson
 * @param[id] // Lesson id
 * @param[subject] Subject name
 * @param[teacher] Lesson teacher
 * @param[classroom] Lesson classroom
 * @param[studentClass] Student class
 */
@Serializable
data class Lesson(
    internal var id: Int,
    internal var subject: Subject?,
    internal var teacher: Teacher?,
    internal var classroom: Classroom?,
    internal var studentClass: StudentClass?
)


