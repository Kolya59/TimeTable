package classes

import kotlinx.serialization.Serializable

/**
 * School classroom
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
open class Classroom constructor(internal var id: Short, // Name of subject
                                 internal var name: String
) {
}