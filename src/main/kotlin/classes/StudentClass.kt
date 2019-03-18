package classes

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.serialization.Serializable
import tornadofx.getValue
import tornadofx.setValue

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
