package gui.export

import classes.Lesson
import classes.TimeTable
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.scene.control.Alert
import serialization.Export
import tornadofx.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO Создать интерактивное отображение файла
class ExportView() : View("Меню экспорта") {
    private val controller: ExportController by inject()
    var currentTimetable: TimeTable = TimeTable(emptyList<Lesson>().toMutableList())
    var currentPath: StringProperty = "Файл не выбран".toProperty()
    override val root = vbox {
        hbox {
            label {
                text = "Выберите файл"
            }
            button {
                action { controller.onChoicePath(ActionEvent()) }
                text = currentPath.value
            }
        }
        hbox {
            button {
                action {
                    controller.onExport(ActionEvent())
                    close()
                }
                text = "Экспортировать"
            }
            button {
                action {
                    controller.onCancel(ActionEvent())
                    close()
                }
                text = "Отмена"
            }
        }
    }
}

class ExportController : Controller() {
    private val view: ExportView = find()

    /**
     * Выбор пути файла
     */
    fun onChoicePath(actionEvent: ActionEvent) {
        val chosenDirectory = chooseDirectory(
            "Выберите путь для сохранения файла",
            File("/")
        )
        if (chosenDirectory != null) {
            view.currentPath.value = chosenDirectory.absolutePath
        }
    }

    /**
     * Экспорт файла
     */
    fun onExport(actionEvent: ActionEvent) {
        val currentDate = SimpleDateFormat("dd_M_yyyy_hh_mm_ss").format(Date())
        val fileName: String = currentDate.toString()
        // TODO Проверка файла на существование
        val selectedDirectory = File(view.currentPath.value)
        if (selectedDirectory.absolutePath != null) {
            val selectedPath = "$selectedDirectory/$fileName.json"
            Export.ExportTimetable(selectedPath, view.currentTimetable).toJSON()
        }
        tornadofx.alert(
            Alert.AlertType.INFORMATION,
            "Результат экспорта",
            "Расписание успешно экспортировано"
        )
    }

    /**
     * Закрытие окна
     */
    fun onCancel(actionEvent: ActionEvent) {
    }
}