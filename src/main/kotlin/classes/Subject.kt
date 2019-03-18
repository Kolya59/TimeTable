package classes

import kotlinx.serialization.Serializable

/**
 * School subject
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
open class Subject constructor(internal var id: Short,
                               internal var name: String
) {
}
