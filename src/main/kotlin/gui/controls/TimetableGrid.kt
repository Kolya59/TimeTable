package gui.controls

import javafx.scene.layout.GridPane

/**
 * TimetableGrid control
 * @param[TimetableCells] Lessons in TimetableGrid
 */
class TimetableGrid(var TimetableCells: Set<TimetableCell>) : GridPane() {
    var horizontalHeader : Set<String> = emptySet()
    var verticalHeader : Set<String> = emptySet()
    var state : TimetableState = TimetableState.CLASSIC
    init {

    }
}

enum class TimetableState{ CLASSIC, TEACHER, CLASSROOMS }