package classes

import javafx.beans.property.SimpleObjectProperty
import kotlinx.serialization.*
import tornadofx.getValue
import tornadofx.setValue

/**
 * School timetableCell
 * @param[lessons] School lessons set
 */
@Serializable
open class TimeTable(internal var lessons: Set<Lesson>) {
}
