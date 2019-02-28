package classes

import kotlinx.serialization.*

/**
 * School lesson
 * @param[id] // Lesson id
 * @param[subject] Subject name
 * @param[teacher] Lesson teacher
 * @param[classroom] Lesson classroom
 * @param[studentClass] Student class
 */
@Serializable
abstract class Lesson(id: Short,
                      subject: Subject,
                      teacher: Teacher,
                      classroom: Classroom,
                      studentClass: StudentClass) {
    protected var id: Short = id
    protected var subject: Subject = subject
    protected var teacher: Teacher = teacher
    protected var classroom: Classroom = classroom
    protected var studentClass: StudentClass = studentClass
}