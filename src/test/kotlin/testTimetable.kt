
import gui.controls.TimetableCell
import gui.controls.TimetableGrid
import javafx.scene.control.Label
import tornadofx.View
import tornadofx.gridpane
import tornadofx.vbox

class testTimetable : View("My View") {
    val data = TestData()

    fun genTest(): MutableList<TimetableCell> {
        val result = emptyList<TimetableCell>().toMutableList()
        for (lesson in data.testLessons) {
            result.add(TimetableCell(lesson))
        }
        return result
    }

    var timetable = TimetableGrid(genTest())
    var timetableCell = TimetableCell(data.testLessons[0])

    override val root = vbox {
        gridpane {
            addColumn(0, Label("Понедельник"))
            addColumn(1, Label("Вторник"))
            addColumn(2, Label("Среда"))
            addColumn(3, Label("Четверг"))
            addColumn(4, Label("Пятница"))
            addColumn(5, Label("Суббота"))
            addColumn(6, Label("Воскресение"))

            addRow(
                1,
                timetableCell
            )
            spacing = 20.0
            isGridLinesVisible = true
        }
        spacing = 10.0
    }
}
