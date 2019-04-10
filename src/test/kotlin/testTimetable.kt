import gui.controls.TimetableCell
import gui.controls.TimetableGrid
import tornadofx.View
import tornadofx.borderpane

class testTimetable : View("My View") {
    val data = TestData().testLessons

    fun genTest(): MutableList<TimetableCell> {
        val result = emptyList<TimetableCell>().toMutableList()
        for (lesson in data) {
            result.add(TimetableCell(lesson))
        }
        return result
    }

    override val root = borderpane {
        TimetableGrid(genTest())
    }
}
