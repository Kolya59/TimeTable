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
        addColumn(1, Label("Вторник"))
        addColumn(2, Label("Среда"))
        addColumn(3, Label("Четверг"))
        addColumn(4, Label("Пятница"))
        addColumn(5, Label("Суббота"))
        addColumn(6, Label("Воскресение"))

        for (i in 1..TimetableCells.size)
            addRow(i, TimetableCells[i - 1])

        width = 20.0
        height = 20.0
        isGridLinesVisible = true
    }


}

enum class TimetableState{ CLASSIC, TEACHER, CLASSROOMS }