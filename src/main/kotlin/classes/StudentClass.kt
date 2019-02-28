package classes

import kotlinx.serialization.*

@Serializable
open class StudentClass(id: Short, name: String) {
    /**
     * Student class
     */

    // Class id
    protected val id: Short = id
    // Classname
    protected var name: String = name
}