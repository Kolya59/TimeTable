package gui

import classes.*
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.stage.FileChooser
import serialization.Import
import tornadofx.*
import java.io.File

// TODO Создать интерактивное отображение файла
// TODO Исправить корректность импорта
class ImportView : View("Меню импорта") {
    private val controller: ImportController by inject()
    var currentTimetable: TimeTable = TimeTable(
        emptyList<Lesson>().toMutableList(),
        emptyList<Teacher>().toMutableList(),
        emptyList<Classroom>().toMutableList(),
        emptyList<StudentClass>().toMutableList(),
        emptyList<Subject>().toMutableList()
    )
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
                    currentTimetable = controller.onImport(ActionEvent())
                    if (!currentTimetable.lessons.isEmpty())
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