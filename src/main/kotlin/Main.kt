import javafx.stage.Stage
import tornadofx.App
import tornadofx.importStylesheet

class Main: App() {
    override val primaryView = gui.main.View::class

    override fun start(stage: Stage) {
        importStylesheet("/style.css")
        super.start(stage)
    }
}