package classes

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.*
import tornadofx.getValue
import tornadofx.setValue

/**
 * School teacher
 * @param[id] Teachers id
 * @param[name] Teachers name
 * @param[availableSubjects] Subjects this teacher can teach
 */
@Serializable
open class Teacher constructor(internal var id: Short,
                               internal var name: String,
                               internal var availableSubjects: Set<Subject>
) {
}
