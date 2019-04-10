package gui.import

import classes.TimeTable
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.stage.FileChooser
import serialization.Import
import tornadofx.*
import java.io.File

// TODO Создать интерактивное отображение файла
class ImportView : View("Меню импорта") {
    private val controller: ImportController by inject()
    var currentTimetable: TimeTable = TimeTable(emptySet())
    var currentPath: StringProperty = "Файл не выбран".toProperty()

    override val root = vbox {
        // TODO Выровнять верстку
        hbox {
            label {
                text = "Выберите путь"
            }
            button {
                id = "btFilePath"
                text = currentPath.value
                action { controller.onChoiceFilePath(ActionEvent()) }
            }
        }
        hbox {
            button {
                id = "btImport"
                action {
                    controller.onImport(ActionEvent())
                    close()
                }
                text = "Импортировать"
            }
            button {
                id = "btCancel"
                action {
                    controller.onCancel(ActionEvent())
                    close()
                }
                text = "Отмена"
            }
        }
    }
}

class ImportController : Controller() {
    private val view: ImportView = find()
    /**
     * Выбор пути файла
     */
    fun onChoiceFilePath(actionEvent: ActionEvent) {
        val files = chooseFile(
            "Выберите файл для импорта",
            arrayOf(FileChooser.ExtensionFilter("JSON", "*.json")),
            FileChooserMode.Single
        )
        if (!files.isEmpty()) {
            view.currentPath.value = files[0].absolutePath
        }
    }

    /**
     * Импорт файла
     */
    fun onImport(actionEvent: ActionEvent) {
        val flag = view.currentPath.value.matches(Regex(".json"))
        if (flag) {
            val selectedFile = File(view.currentPath.value)
            if (!selectedFile.isFile) {
                view.currentTimetable = Import.ImportTimetable(selectedFile).fromJSON()
            }
        }
    }

    /**
     * Отмена импорта
     */
    fun onCancel(actionEvent: ActionEvent) {
    }
}