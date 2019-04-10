package gui.controls

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
        super.addColumn(0)
        super.addColumn(1)
        super.addColumn(2)
        super.addColumn(3)
        super.addColumn(4)
        super.addColumn(5)
        super.addColumn(6)

        super.addRow(0)
        super.addRow(1)
        super.addRow(2)
        super.addRow(3)
        super.addRow(4)
        super.addRow(5)
        super.addRow(6)

        for (cell in TimetableCells)
            super.getChildren().setAll(cell)
    }


}

enum class TimetableState{ CLASSIC, TEACHER, CLASSROOMS }