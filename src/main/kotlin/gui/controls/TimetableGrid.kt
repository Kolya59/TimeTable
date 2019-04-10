package gui.controls

import javafx.scene.control.Label
import javafx.scene.layout.GridPane

/**
 * TimetableGrid control
 * @param[TimetableCells] Lessons in TimetableGrid
 */
class TimetableGrid(var TimetableCells: MutableList<TimetableCell>) : GridPane() {
    var horizontalHeader : Set<String> = emptySet()
    var verticalHeader : Set<String> = emptySet()
    var state : TimetableState = TimetableState.CLASSIC
    init {
        addColumn(0, Label("Понедельник"))
        addColumn(1)
        addColumn(2)
        addColumn(3)
        addColumn(4)
        addColumn(5)
        addColumn(6)

        addRow(0)
        addRow(1)
        addRow(2)
        addRow(3)
        addRow(4)
        addRow(5)
        addRow(6)

        for (cell in TimetableCells)
            add(cell, 0, 0)

        setGridLinesVisible(true)
    }


}

enum class TimetableState{ CLASSIC, TEACHER, CLASSROOMS }