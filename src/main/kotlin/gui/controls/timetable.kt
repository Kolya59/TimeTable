package gui.controls

import classes.Classroom
import classes.Lesson
import classes.StudentClass
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import tornadofx.*

/**
 * Timetable control
 * @param[timetableCells] Lessons in timetable
 */
class timetable(var timetableCells: Set<timetableCell>) : GridPane() {
    var horizontalHeader : Set<String> = emptySet()
    var verticalHeader : Set<String> = emptySet()
    var state : TimetableState = TimetableState.CLASSIC
    init {

    }
}

enum class TimetableState{ CLASSIC, TEACHER, CLASSROOMS }