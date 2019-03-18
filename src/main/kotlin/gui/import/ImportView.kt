package gui.import

import javafx.event.ActionEvent
import tornadofx.*

class ImportView : View("Меню импорта") {
    private val controller: ImportController by inject()
    override val root = anchorpane {
        prefHeight = 142.0
        prefWidth = 400.0

        /*choicebox {
            id="cbFileFormat"
            layoutX=236.0
            layoutY=63.0
            onContextMenuRequested=controller.onChoiceFileFormat(ActionEvent())
            prefWidth=150.0

        }*/
        button {
            id="btCancel"
            layoutX=322.0
            layoutY=111.0
            action { controller.onCancel(ActionEvent()) }
            text="Отмена"
        }
        button {
            id="btImport"
            layoutX=176.0
            layoutY=111.0
            action { controller.onImport(ActionEvent()) }
            text="Импортировать"
        }
        /*label {
            layoutX=14.0
            layoutY=68.0
            text="Выберите формат для импорта"
        }*/
        label {
            layoutX=14.0
            layoutY=28.0
            text="Выберите путь"
        }
        textfield {
            id="tfFilePath"
            layoutX=117.0
            layoutY=23.0
            action {controller.onChoiceFilePath(ActionEvent()) }
            prefHeight=28.0
            prefWidth=267.0
        }
    }
}

class ImportController : Controller() {
//    /**
//     * Выбор формата файла
//     */
//    fun onChoiceFileFormat(contextMenuEvent: ContextMenuEvent) {
//
//    }

    /**
     * Выбор пути файла
     */
    fun onChoiceFilePath(actionEvent: ActionEvent) {

    }

    /**
     * Импорт файла
     */
    fun onImport(actionEvent: ActionEvent) {

    }

    /**
     * Отмена импорта
     */
    fun onCancel(actionEvent: ActionEvent) {

    }
}