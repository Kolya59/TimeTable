import gui.main.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class MainApp: App(MainView:: class) {
    override fun start(stage: Stage) {
        //importStylesheet("/style.css")
        super.start(stage)
    }
}