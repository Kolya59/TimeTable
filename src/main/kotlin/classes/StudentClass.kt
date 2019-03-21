package classes

import kotlinx.serialization.Serializable

/**
 * Student class
 * @param[id] Class id
 * @param[name] Classname
 */
@Serializable
open class StudentClass(internal var id: Short,
                        internal var name: String
) {
}
