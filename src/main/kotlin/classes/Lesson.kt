package classes

import kotlinx.serialization.*

@Serializable
open class Lesson(id: Short,
                  subject: Subject,
                  teacher: Teacher,
                  classroom: Classroom,
                  studentClass: StudentClass) {
    /**
     * School lesson
     */

    // Lesson id
    protected var id: Short = id

    // Subject name
    protected var subject: Subject = subject

    // Teacher
    protected var teacher: Teacher = teacher

    // Classroom
    protected var classroom: Classroom = classroom

    // Student class
    protected var studentClass: StudentClass = studentClass
}