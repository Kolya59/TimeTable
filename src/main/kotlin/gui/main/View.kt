package gui.main

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import tornadofx.View

class View: View() {
    override val root: VBox by fxml()

    @FXML
    lateinit var display: Label

    init {
        this.title = "Time Table Editor"

        root.lookupAll(".button").forEach { b ->
            b.setOnMouseClicked {

            }
        }

        root.addEventFilter(KeyEvent.KEY_TYPED) {

        }

    }

    /**
     * Нажатие на элемнт меню "Экспорт"
     */
    fun onExportMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * Нажатие на элемнт меню "Импорт"
     */
    fun onImportMenuClicked(actionEvent: ActionEvent) {

    }
}