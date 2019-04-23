package gui

import classes.TimeTable
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.scene.control.Label
import javafx.stage.FileChooser
import serialization.Import
import tornadofx.*
import java.io.File

// TODO Создать интерактивное отображение файла
// TODO Исправить корректность импорта
class ImportView : View("Меню импорта") {
    private val controller: ImportController by inject()
    lateinit var currentTimetable: TimeTable
    var currentPath: StringProperty = "Файл не выбран".toProperty()

    var lPath = Label()

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
                    currentTimetable = controller.onImport(ActionEvent())
                    if (currentTimetable.lessons.isNotEmpty())
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
        view.currentPath.value = files.firstOrNull()?.absolutePath
        view.lPath.text = files.firstOrNull()?.absolutePath
    }

    /**
     * Импорт файла
     */
    fun onImport(actionEvent: ActionEvent): TimeTable {
        val selectedFile = File(view.currentPath.value)
        return Import.ImportTimetable(selectedFile).fromJSON()
    }

    /**
     * Отмена импорта
     */
    fun onCancel(actionEvent: ActionEvent) {
    }
}