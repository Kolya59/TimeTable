import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class MainApp: App(gui.main.MainView:: class) {
    override fun start(stage: Stage) {
        importStylesheet("/style.css")
        super.start(stage)
    }
}