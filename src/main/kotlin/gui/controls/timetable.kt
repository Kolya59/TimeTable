package gui.controls

import javafx.scene.layout.GridPane

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