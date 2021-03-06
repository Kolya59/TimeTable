package classes

import kotlinx.serialization.Serializable

/**
 * School classroom
 * @param[id] Subjects id
 * @param[name] Name of subject
 */
@Serializable
data class Classroom constructor(
    internal var id: Int,
    internal var name: String
)