
import gui.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus

class MainApp : App(MainView::class, TimetableStyleSheet::class) {
    init {
        reloadStylesheetsOnFocus()
//        reloadViewsOnFocus()
    }

    override fun start(stage: Stage) {
        super.start(stage)
        stage.show()
    }
}