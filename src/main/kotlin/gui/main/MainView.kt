package gui.main

import classes.*
import gui.controls.ItemBox
import gui.controls.TimetableCell
import gui.export.ExportView
import gui.import.ImportView
import gui.settings.SettingsView
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.text.TextAlignment
import serialization.Import
import tornadofx.*
import java.io.File

class MainView : View("Редактор расписания") {
    // Controller
    private val controller: MainController by inject()

    // Current timetable
    var currentTimetable: TimeTable

    // List of timetable cells
    private var timetableCells: MutableList<TimetableCell>

    // Lists of timetable content
    private var availableLessons: MutableList<Lesson>
    private var availableTeachers: MutableList<Teacher>
    private var availableClassrooms: MutableList<Classroom>
    private var availableSubjects: MutableList<Subject>
    private var availableStudentClasses: MutableList<StudentClass>

    // TODO Settings
    private var settings = Settings()
    // Current itembox
    private var currentItemBox: ItemBox

    // Current timetable grid
    private var gridTimeTable: GridPane = GridPane()

    /**
     * View statements
     */
    enum class ViewState { CLASSROOM_VIEW, STUDENT_CLASS_VIEW, TEACHER_VIEW }

    init {
        // Загрузка данных из конфига
        currentTimetable =
            controller.loadFromConfig(
                "/Users/kolya59/Yandex.Disk.localized/Универ/Курсовая/Timetable/src/main/resources/config.json"
            )
        // Создание пустых коллекций
        timetableCells = emptyList<TimetableCell>().toMutableList()
        availableClassrooms = emptyList<Classroom>().toMutableList()
        availableSubjects = emptyList<Subject>().toMutableList()
        availableLessons = emptyList<Lesson>().toMutableList()
        availableTeachers = emptyList<Teacher>().toMutableList()
        availableStudentClasses = emptyList<StudentClass>().toMutableList()

        // Сортировка содержимого конфига по коллекциям
        // Загрузка уроков
        for (lesson in currentTimetable.lessons) {
            availableLessons.add(lesson)
        }

        // Загрузка списка школьных предметов
        for (subject in currentTimetable.subjects) {
            availableSubjects.add(subject)
        }

        // Загрузка списка классов
        for (studentClass in currentTimetable.studentClasses) {
            availableStudentClasses.add(studentClass)
        }

        // Загрузка списка учителей
        for (teacher in currentTimetable.teachers) {
            availableTeachers.add(teacher)
        }

        // Загрузка списка кабнетов
        for (classroom in currentTimetable.classrooms) {
            availableClassrooms.add(classroom)
        }

        // Создание пула свободных уроков
        currentItemBox = ItemBox(availableClassrooms, availableSubjects, availableTeachers)
    }

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
                // TODO Поправить верстку
                vbox {
                    alignment = Pos.CENTER

                    label("Расписание"){
                        alignment = Pos.CENTER
                        contentDisplay = ContentDisplay.CENTER
                        textAlignment = TextAlignment.CENTER
                    }
                    gridpane {
                        // Columns
                        addColumn(0, Label("Время"))
                        for (i in 0 until availableStudentClasses.size) {
                            addColumn(i + 1, TimetableCell(studentClass = availableStudentClasses[i]))
                        }

                        // Rows
                        for (i in 0 until settings.lessonsTime.size) {
                            addRow(i + 1, Label(settings.lessonsTime[i]))
                        }

                        // Items
                        //for (i in 0 until availableLessons.size) {
                        add(TimetableCell(availableLessons[0]), 1, 1)
                        //}
                        add(javafx.scene.control.Label("Что-то"), 3, 1)
                        add(javafx.scene.control.Label("Еще что-то"), 2, 1)

                        alignment = Pos.CENTER
                        columnConstraints.add(
                            javafx.scene.layout.ColumnConstraints(
                                20.0,
                                60.0,
                                100.0,
                                javafx.scene.layout.Priority.ALWAYS,
                                javafx.geometry.HPos.CENTER,
                                true
                            )
                        )
                        isGridLinesVisible = true
                    }
                }
            }
            anchorpane {
                vbox {
                    alignment = Pos.CENTER

                    label("Уроки")
                    currentItemBox = ItemBox(availableClassrooms, availableSubjects, availableTeachers)
                }
            }
        }

        // Drag and Drop event filters
        addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
        addEventFilter(MouseEvent.MOUSE_DRAGGED, ::animateDrag)
        addEventFilter(MouseEvent.MOUSE_EXITED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::drop)
    }


    // Drag and Drop
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
        // TODO Экспорт
        clearAll()
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
        val map = mapOf("currentTimetable" to view.currentTimetable)
        find<ExportView>(map).openWindow()
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
        val settingsView = SettingsView()
        settingsView.openModal(
            owner = this.view.currentWindow,
            block = true
        )
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
     * TODO Start dragging
     */
    fun onStartDrag(mouseEvent: MouseEvent) {
        /*toolboxItems
            .filter {
                val mousePt: Point2D = it.sceneToLocal(evt.sceneX, evt.sceneY)
                it.contains(mousePt)
            }
            .firstOrNull()
            .apply {
                if (this != null) {
                    draggingColor = this.properties["rectColor"] as Color
                }
            }*/
    }

    /**
     * TODO Animation of dragging
     */
    fun onAnimateDrag(mouseEvent: MouseEvent) {
        /* val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
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
         }*/
    }

    /**
     * TODO Stop dragging
     */
    fun onStopDrag(mouseEvent: MouseEvent) {
        /*if (workArea.hasClass(DraggingStyles.workAreaSelected)) {
            workArea.removeClass(DraggingStyles.workAreaSelected)
        }
        if (inflightRect.isVisible) {
            inflightRect.isVisible = false
        }*/
    }

    /**
     * TODO Drop item
     */
    fun onDrop(mouseEvent: MouseEvent) {
        /*val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (workArea.contains(mousePt)) {
            if (draggingColor != null) {
                val newRect = Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, draggingColor)
                workArea.add(newRect)
                newRect.relocate(mousePt.x, mousePt.y)

                inflightRect.toFront() // don't want to move cursor tracking behind added objects
            }
        }

        draggingColor = null*/
    }

    /**
     * TODO Clearing all data from timetable
     */
    fun clearAll() {

    }

    /**
     * Load data from config
     * @param[pathToConfig] Path to config file
     * @return TimeTable Imported timetable
     */
    fun loadFromConfig(pathToConfig: String): TimeTable = Import.ImportTimetable(File(pathToConfig)).fromJSON()
}