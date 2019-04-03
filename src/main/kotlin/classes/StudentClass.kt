package classes

import kotlinx.serialization.Serializable

/**
 * Student class
 * @param[id] Class id
 * @param[name] Classname
 */
@Serializable
data class StudentClass(
    internal var id: Short,
    internal var name: String
)
