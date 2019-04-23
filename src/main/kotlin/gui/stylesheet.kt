import javafx.scene.paint.Paint
import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.px

class TimetableStyleSheet : Stylesheet() {
    companion object {
        // Timetable
        val columnHeader by cssclass()
        val rowHeader by cssclass()
        val filledCell by cssclass()
        val emptyCell by cssclass()
        val errorCell by cssclass()
        val sourceCell by cssclass()
        val targetCell by cssclass()
        val pinnedCell by cssclass()
        val inFligthCell by cssclass()
    }

    init {
        // Заголовок столбца
        columnHeader {
            fontSize = 12.px
//            backgroundColor = MultiValue(Paint(Color.BLACK))

        }

        // Заголовок строки
        rowHeader {
            fontSize = 12.px
        }

        // Заполненные ячейки
        filledCell {
            fontSize = 12.px
        }

        // Пустые ячейки
        emptyCell {

        }

        // Ячейки, в которых есть ошибки
        errorCell {
            backgroundColor.add(Paint.valueOf("red"))
        }

        // Ячейка, из которой перетаскивают
        sourceCell {

        }

        // Ячейка, в которую перетаксивают
        targetCell {

        }

        // Закрепленная ячейка
        pinnedCell {

        }

        // Перетаскиваемая ячейка
        inFligthCell {

        }
    }
}