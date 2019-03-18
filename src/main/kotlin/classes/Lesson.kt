package classes

import javafx.beans.property.SimpleObjectProperty
import kotlinx.serialization.*
import tornadofx.getValue
import tornadofx.setValue

/**
 * School lesson
 * @param[id] // Lesson id
 * @param[subject] Subject name
 * @param[teacher] Lesson teacher
 * @param[classroom] Lesson classroom
 * @param[studentClass] Student class
 */
@Serializable
open class Lesson(internal var id: Short,
                  internal var subject: Subject,
                  internal var teacher: Teacher,
                  internal var classroom: Classroom,
                  internal var studentClass: StudentClass
) {
}

