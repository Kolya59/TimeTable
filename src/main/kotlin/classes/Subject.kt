package classes

import kotlinx.serialization.Serializable

/**
 * School subject
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
data class Subject constructor(
    internal var id: Int,
    internal var name: String
)
