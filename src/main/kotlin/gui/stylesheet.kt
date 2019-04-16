import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.px

/*TODO Создать разные стили*/
class TimetableStyleSheet() : Stylesheet() {
    companion object {
        // Timetable
        val columnHeader by cssclass()
        val rowHeader by cssclass()
        val filledCell by cssclass()
        val emptyCell by cssclass()
        val errorCell by cssclass()
        val sourceCell by cssclass()
        val targetCell by cssclass()
        val inFligthCell by cssclass()
    }

    init {
        // Timetable
        columnHeader {
            fontSize = 12.px
        }

        rowHeader {
            fontSize = 12.px
        }

        filledCell {
            fontSize = 12.px
        }

        emptyCell {

        }

        errorCell {

        }

        sourceCell {

        }

        targetCell {

        }

        inFligthCell {

        }
    }
}