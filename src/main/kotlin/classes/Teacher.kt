package classes

import kotlinx.serialization.Serializable

/**
 * School teacher
 * @param[id] Teachers id
 * @param[name] Teachers name
 * @param[availableSubjects] Subjects this teacher can teach
 */
@Serializable
data class Teacher constructor(
    internal var id: Short,
    internal var name: String,
    internal var availableSubjects: Set<Subject>
)
