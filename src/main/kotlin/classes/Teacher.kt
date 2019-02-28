package classes

import kotlinx.serialization.*

/**
 * School teacher
 * @param[id] Teachers id
 * @param[name] Teachers name
 * @param[availableSubjects] Subjects this teacher can teach
 */
@Serializable
abstract class Teacher constructor(id: Short, name: String, availableSubjects: Set<Subject>) {
    protected val id: Short = id
    protected var name: String = name
    protected var availableSubjects: Set<Subject> = availableSubjects
}