package classes

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import tornadofx.getValue
import tornadofx.setValue

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