package gui.main

import classes.Classroom
import classes.Lesson
import classes.Teacher
import classes.TimeTable
import gui.controls.TimetableCell
import gui.controls.TimetableGrid
import gui.export.ExportView
import gui.import.ImportView
import javafx.event.ActionEvent
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import tornadofx.*
import java.io.File

class MainView : View("Редактор расписания") {
    private val controller: MainController by inject()

    private var lessonsSet: Set<Lesson> = emptySet()
    var currentTimetable: TimeTable = TimeTable(lessonsSet)
    private var TimetableCells: Set<TimetableCell> = emptySet()

    var availableLessons: Set<Lesson> = emptySet()
    var availableTeachers: Set<Teacher> = emptySet()
    var availableClassrooms: Set<Classroom> = emptySet()

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
                item("По классам") {
                    action { controller.onViewStudentClassesMenuClicked(ActionEvent()) }
                }
                item("По учителям") {
                    action { controller.onViewTeachersMenuClicked(ActionEvent()) }
                }
                item("По кабинетам") {
                    action { controller.onViewClassroomsMenuClicked(ActionEvent()) }
                }
            }
            menu("Работа с файлами") {
                item("Экспорт") {
                    action { controller.onExportMenuClicked(ActionEvent()) }
                }
                item("Импорт") {
                    action { controller.onImportMenuClicked(ActionEvent()) }
                }
            }
            menu("Настройки") {
                item("Интерфейс") {
                    action { controller.onSettingsInterfaceMenuClicked(ActionEvent()) }
                }
                item("Генерация") {
                    action { controller.onSettingsGeneratorMenuClicked(ActionEvent()) }
                }
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
                    TimetableGrid(TimetableCells)
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
                id = "lAvailableLessonsCount"
            }

        }

        addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
        addEventFilter(MouseEvent.MOUSE_DRAGGED, ::animateDrag)
        addEventFilter(MouseEvent.MOUSE_EXITED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::drop)
    }

    private fun startDrag(evt: MouseEvent) {
        controller.onStartDrag(evt)
    }

    private fun animateDrag(evt: MouseEvent) {
        controller.onAnimateDrag(evt)
    }

    private fun stopDrag(evt: MouseEvent) {
        controller.onStopDrag(evt)
    }

    private fun drop(evt: MouseEvent) {
        controller.onDrop(evt)
    }
}

class MainController : Controller() {
    val view: MainView by inject<MainView>()

    /**
     * TODO: Creating new TimetableGrid
     */
    fun onCreateMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * TODO: Opening TimetableGrid and replacing current TimetableGrid
     */
    fun onOpenMenuClicked(actionEvent: ActionEvent) {
        val timetableFile = File("current_timetable.cfg")
        val teachersFile = File("current_teachers.cfg")
        val classroomsFile = File("current_classrooms.cfg")
        val studentCLassFile = File("current_student_class.cfg")
    }

    /**
     * TODO: Saving current TimetableGrid
     */
    fun onSaveMenuClicked(actionEvent: ActionEvent) {
        val timetableFile = File("current_timetable.cfg")
        val teachersFile = File("current_teachers.cfg")
        val classroomsFile = File("current_classrooms.cfg")
        val studentCLassFile = File("current_student_class.cfg")

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
     * Opening export menu
     */
    fun onExportMenuClicked(actionEvent: ActionEvent) {
        val exportView = ExportView()
        exportView.currentTimetable = view.currentTimetable
        exportView.openModal()
    }

    /**
     * Opening import menu
     */
    fun onImportMenuClicked(actionEvent: ActionEvent) {
        val importView = ImportView()
        importView.currentTimetable = view.currentTimetable
        importView.openModal(
            owner = this.view.currentWindow,
            block = true
        )
    }

    /**
     * TODO Opening interface settings menu
     */
    fun onSettingsInterfaceMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * TODO Opening generator settings menu
     */
    fun onSettingsGeneratorMenuClicked(actionEvent: ActionEvent) {

    }

    /**
     * TODO: Changing view state
     */
    fun changeViewState(viewState : MainView.ViewState) {

    }

    /**
     * Start dragging
     */
    fun onStartDrag(evt: MouseEvent) {

        toolboxItems
            .filter {
                val mousePt: Point2D = it.sceneToLocal(evt.sceneX, evt.sceneY)
                it.contains(mousePt)
            }
            .firstOrNull()
            .apply {
                if (this != null) {
                    draggingColor = this.properties["rectColor"] as Color
                }
            }

    }

    /**
     * Animation of dragging
     */
    fun onAnimateDrag(evt: MouseEvent) {

        val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (workArea.contains(mousePt)) {

            // highlight the onDrop target (hover doesn't work)
            if (!workArea.hasClass(DraggingStyles.workAreaSelected)) {
                workArea.addClass(DraggingStyles.workAreaSelected)
            }

            // animate a rectangle so that the user can follow
            if (!inflightRect.isVisible) {
                inflightRect.isVisible = true
                inflightRect.fill = draggingColor
            }

            inflightRect.relocate(mousePt.x, mousePt.y)
        }

    }

    /**
     * Stop dragging
     */
    fun onStopDrag(evt: MouseEvent) {
        if (workArea.hasClass(DraggingStyles.workAreaSelected)) {
            workArea.removeClass(DraggingStyles.workAreaSelected)
        }
        if (inflightRect.isVisible) {
            inflightRect.isVisible = false
        }
    }

    /**
     * Drop item
     */
    fun onDrop(evt: MouseEvent) {

        val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (workArea.contains(mousePt)) {
            if (draggingColor != null) {
                val newRect = Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, draggingColor)
                workArea.add(newRect)
                newRect.relocate(mousePt.x, mousePt.y)

                inflightRect.toFront() // don't want to move cursor tracking behind added objects
            }
        }

        draggingColor = null
    }
}