package gui.main

import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.input.KeyEvent
import javafx.scene.text.TextAlignment
import tornadofx.*

class MainView : View("Редактор расписания") {
    private val controller: MainController by inject()
    override val root = vbox(alignment = Pos.TOP_CENTER) {
        menubar {
            menu("Файл") {
                item("Создать")
                item("Открыть")
                item("Сохранить")
                item("Выход")
            }
            menu("Вид") {
                item("По классам")
                item("По учителям")
                item("По кабинетам")
            }
            menu("Экспорт"){
                action { controller.onExportMenuClicked(ActionEvent())}
            }
            menu("Ипорт"){
                action { controller.onImportMenuClicked(ActionEvent())}
            }
            menu("Настройки")
            menu("Выход")

        }
        splitpane {
            anchorpane {
                vbox(alignment = Pos.CENTER) {
                    label("Расписание"){
                        alignment = Pos.CENTER
                        contentDisplay = ContentDisplay.CENTER
                        textAlignment = TextAlignment.CENTER
                    }
                    gridpane{
                        id = "tvTimetable"
                        // TODO gridpane
                        columnConstraints
                        rowConstraints
                    }
                }
            }
            anchorpane {
                vbox(alignment = Pos.CENTER) {
                    label("Уроки")
                    //TODO listview()
                }
            }
        }
        hbox(alignment = Pos.TOP_RIGHT) {
            label("Свободных уроков:")
            label("Количество свободных уроков") {
                id = "lAvaibleLessonsCount"
            }

        }
    }

    init {
        this.title = "Time Table Editor"

        /*// Применение стилей
        root.lookupAll(".button").forEach { b ->
            b.setOnMouseClicked {

            }
        }*/


        root.addEventFilter(KeyEvent.KEY_TYPED) {

        }

    }
}

class MainController : Controller() {

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