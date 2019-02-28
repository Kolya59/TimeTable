package classes

import kotlinx.serialization.Serializable

/**
 * Student class
 * @param[id] Class id
 * @param[name] Classname
 */
@Serializable
abstract class StudentClass(id: Short, name: String) {
    protected val id: Short = id
    protected var name: String = name
}