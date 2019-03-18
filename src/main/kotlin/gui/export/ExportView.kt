package gui.export

import javafx.event.ActionEvent
import tornadofx.*

class ExportView : View("Меню экспорта") {
    private val controller: ExportController by inject()
    override val root = anchorpane {
        /*choicebox {
            id="cbFileFormat"
            layoutX=236.0
            layoutY=63.0
            onContextMenuRequested=controller.onChoiceFileFormat(ActionEvent())
            prefWidth=150.0
        }*/
        button {
            layoutX=322.0
            layoutY=111.0
            action { controller.onCancel(ActionEvent()) }
            text="Отмена"
        }
        button {
            layoutX=176.0
            layoutY=111.0
            action { controller.onExport(ActionEvent()) }
            text="Экспортировать"
        }
        label {
            layoutX=14.0
            layoutY=68.0
            text="Выберите формат для экспорта"
        }
        label {
            layoutX=14.0
            layoutY=28.0
            text="Выберите путь"
        }
        textfield {
            id="tfFilePath"
            layoutX=117.0
            layoutY=23.0
            action { controller.onChoiceFilePath(ActionEvent()) }
            prefHeight=28.0
            prefWidth=267.0
        }
    }
}

class ExportController : Controller() {
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
     * Экспорт файла
     */
    fun onExport(actionEvent: ActionEvent) {

    }

    /**
     * Закрытие окна
     */
    fun onCancel(actionEvent: ActionEvent) {

    }
}