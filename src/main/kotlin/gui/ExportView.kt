package gui

import classes.TimeTable
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.scene.control.Alert
import javafx.scene.control.Label
import serialization.Export
import tornadofx.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ExportView : View("Меню экспорта") {
    private val controller: ExportController by inject()
    var currentTimetable: TimeTable = params["currentTimetable"] as TimeTable
    var currentPath: StringProperty = "Директория не выбрана".toProperty()
    var lPath: Label = Label()

    override val root = borderpane {
        top = hbox {
            lPath = label {
                text = "Выберите директорию"
            }
            button {
                action { controller.onChoicePath(ActionEvent()) }
                text = currentPath.value
            }
        }
        bottom = hbox {
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
    private val view: ExportView by inject()

    /**
     * Выбор пути файла
     */
    fun onChoicePath(actionEvent: ActionEvent) {
        val chosenDirectory = chooseDirectory(
            "Выберите путь для сохранения файла",
            File("/")
        )
        if (chosenDirectory != null && chosenDirectory.exists()) {
            view.currentPath.value = chosenDirectory.absolutePath
            view.lPath.text = chosenDirectory.absolutePath
        }
    }

    /**
     * Экспорт файла
     */
    fun onExport(actionEvent: ActionEvent) {
        val currentDate = SimpleDateFormat("dd_M_yyyy_hh_mm_ss").format(Date())
        val fileName: String = currentDate.toString()
        val selectedDirectory = File(view.currentPath.value)
        if (selectedDirectory.absolutePath != null) {
            val selectedPath = "$selectedDirectory/$fileName.json"
            Export.ExportTimetable(selectedPath, view.currentTimetable).toJSON()
        }
        alert(
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