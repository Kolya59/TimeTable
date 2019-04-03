package gui.main

import classes.Lesson
import classes.TimeTable
import gui.controls.timetable
import gui.controls.timetableCell
import gui.export.ExportView
import gui.import.ImportView
import gui.settings.SettingsView
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.text.TextAlignment
import tornadofx.*

class MainView : View("Редактор расписания") {
    private val controller: MainController by inject()

    private var lessonsSet: Set<Lesson> = emptySet()
    private var timetable: TimeTable = TimeTable(lessonsSet)
    private var timetableCells: Set<timetableCell> = emptySet()

    /**
     * View statements
     */
    enum class ViewState {STUDENT_CLASS_VIEW, TEACHER_VIEW, CLASSROOM_VIEW}

    override val root = vbox() {
        alignment = Pos.TOP_CENTER

        menubar {
            menu("Файл") {
                item("Создать") {
                    action { controller.onCreateMenuClicked(ActionEvent()) }
                }
                item("Открыть") {
                    action { controller.onOpenMenuClicked(ActionEvent()) }
                }
                item("Сохранить") {
                    action { controller.onSaveMenuClicked(ActionEvent()) }
                }
                item("Выход") {
                    action { controller.onExitMenuClicked(ActionEvent()) }
                }
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
            menu("Настройки") {
                action { controller.onSettingsMenuClicked(ActionEvent())}
            }

        }
        splitpane {
            anchorpane {
                vbox(alignment = Pos.CENTER) {
                    label("Расписание"){
                        alignment = Pos.CENTER
                        contentDisplay = ContentDisplay.CENTER
                        textAlignment = TextAlignment.CENTER
                    }
                    timetable(timetableCells)
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


        /*root.addEventFilter(KeyEvent.KEY_TYPED) {

        }*/

    }
}

class MainController : Controller() {
    /**
     * TODO: Creating new timetable
     */
    fun onCreateMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * TODO: Opening timetable and replacing current timetable
     */
    fun onOpenMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * TODO: Saving current timetable
     */
    fun onSaveMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * Closing view
     */
    fun onExitMenuClicked(actionEvent: ActionEvent) {
        this.primaryStage.close()
    }

    /**
     * Setting view to student class view state
     */
    fun onViewStudentClassesMenuClicked(actionEvent: ActionEvent) {
        changeViewState(MainView.ViewState.STUDENT_CLASS_VIEW)
    }

    /**
     * Setting view to teachers view state
     */
    fun onViewTeachersMenuClicked(actionEvent: ActionEvent) {
        changeViewState(MainView.ViewState.TEACHER_VIEW)
    }

    /**
     * Setting view to classrooms view state
     */
    fun onViewClassroomsMenuClicked(actionEvent: ActionEvent) {
        changeViewState(MainView.ViewState.CLASSROOM_VIEW)
    }

    /**
     * TODO: Opening export menu
     */
    fun onExportMenuClicked(actionEvent: ActionEvent) {
        find<ExportView>().openModal()
    }

    /**
     * TODO: Opening import menu
     */
    fun onImportMenuClicked(actionEvent: ActionEvent) {
        find<ImportView>().openModal()
    }

    /**
     * Opening settings menu
     */
    fun onSettingsMenuClicked(actionEvent: ActionEvent) {
        find<SettingsView>().openModal()
    }


    /**
     * TODO: Changing view state
     */
    fun changeViewState(viewState : MainView.ViewState) {

    }
}