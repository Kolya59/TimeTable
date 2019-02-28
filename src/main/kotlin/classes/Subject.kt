package classes

import kotlinx.serialization.*

/**
 * School subject
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
abstract class Subject constructor(id: Short, name: String) {
    protected val id: Short = id
    protected var name: String = name
}