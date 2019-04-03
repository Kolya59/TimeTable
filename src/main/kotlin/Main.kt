import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class MainApp: App(gui.main.MainView:: class) {
    override fun shouldShowPrimaryStage() = false

    override fun start(stage: Stage) {
//        importStylesheet("/style.css")
        stage.showAndWait()
        super.start(stage)
        stage.show()
    }
}