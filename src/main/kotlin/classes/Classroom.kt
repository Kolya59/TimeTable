package classes

import kotlinx.serialization.*

/**
 * School classroom
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
abstract class Classroom constructor(id: Short, name: String) {
    protected val id: Short = id
    protected var name: String = name
}