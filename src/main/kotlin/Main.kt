import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet
import tornadofx.reloadStylesheetsOnFocus
import tornadofx.reloadViewsOnFocus

class MainApp: App(gui.main.MainView:: class) {
    init {
        reloadStylesheetsOnFocus()
        reloadViewsOnFocus()
    }

    override fun start(stage: Stage) {
        importStylesheet("/style.css")
        super.start(stage)
        stage.show()
    }
}