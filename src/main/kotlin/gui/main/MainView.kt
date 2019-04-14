package gui.main

import classes.*
import gui.controls.ItemBox
import gui.controls.TimetableCell
import gui.export.ExportView
import gui.import.ImportView
import gui.settings.SettingsView
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.text.TextAlignment
import serialization.Export
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
    // Selected day
    var selectedDay: String

    // Current timetable grid
    private var gridTimeTable: GridPane = GridPane()

    /**
     * View statements
     */
    enum class ViewState { CLASSROOM_VIEW, STUDENT_CLASS_VIEW, TEACHER_VIEW }

    init {
        // Создание пустых коллекций
        timetableCells = emptyList<TimetableCell>().toMutableList()
        availableClassrooms = emptyList<Classroom>().toMutableList()
        availableSubjects = emptyList<Subject>().toMutableList()
        availableLessons = emptyList<Lesson>().toMutableList()
        availableTeachers = emptyList<Teacher>().toMutableList()
        availableStudentClasses = emptyList<StudentClass>().toMutableList()

        // Загрузка данных из конфига
        currentTimetable =
            try {
                controller.loadFromConfig(
                    "currentTimeTable.json"
                )
            } catch (e: Exception) {
                TimeTable(
                    availableLessons,
                    availableTeachers,
                    availableClassrooms,
                    availableStudentClasses,
                    availableSubjects
                )
            }

        // TODO Проверка на выбор понедельника в списке дней недели
        selectedDay = "Понедельник"
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
                item("Сохранить") {
                    action { controller.onSaveMenuClicked(ActionEvent()) }
                }
                item("Выход") {
                    action { controller.onExitMenuClicked(ActionEvent()) }
                }
            }
            menu("Вид") {
                item("По классам") {
                    action { controller.onViewStudentClassesMenuClicked() }
                }
                item("По учителям") {
                    action { controller.onViewTeachersMenuClicked() }
                }
                item("По кабинетам") {
                    action { controller.onViewClassroomsMenuClicked() }
                }
            }
            menu("Работа с файлами") {
                item("Экспорт") {
                    action { controller.onExportMenuClicked() }
                }
                item("Импорт") {
                    action { controller.onImportMenuClicked() }
                }
            }
            menu("Настройки") {
                item("Интерфейс") {
                    action { controller.onSettingsInterfaceMenuClicked() }
                }
                item("Генерация") {
                    action { controller.onSettingsGeneratorMenuClicked() }
                }
            }

        }
        splitpane {
            anchorpane {
                // TODO Поправить верстку см 100 стр
                vbox {
                    alignment = Pos.CENTER

                    label("Расписание"){
                        alignment = Pos.CENTER
                        contentDisplay = ContentDisplay.CENTER
                        textAlignment = TextAlignment.CENTER
                    }
                    gridTimeTable = gridpane {
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
                        for (i in 0 until availableLessons.filter { it.day == selectedDay }.size) {
                            val lesson = availableLessons[i]
                            add(
                                TimetableCell(lesson),
                                availableStudentClasses.indexOf(availableStudentClasses.find {
                                    it.name == lesson.studentClass?.name
                                }) + 1,
                                lesson.number
                            )
                        }

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
    val view: MainView by inject()

    /**
     * TODO: Creating new TimetableGrid
     */
    fun onCreateMenuClicked(actionEvent: ActionEvent) {
        clearAll()
    }

    /**
     * TODO: Saving current TimetableGrid
     */
    fun onSaveMenuClicked(actionEvent: ActionEvent) {
        Export.ExportTimetable("currentTimeTable.json", view.currentTimetable).toJSON()
        alert(Alert.AlertType.INFORMATION, "Информация о сохранении", "Расписание успешно сохранено")
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
    fun onViewStudentClassesMenuClicked() {
        changeViewState(MainView.ViewState.STUDENT_CLASS_VIEW)
    }

    /**
     * Setting view to teachers view state
     */
    fun onViewTeachersMenuClicked() {
        changeViewState(MainView.ViewState.TEACHER_VIEW)
    }

    /**
     * Setting view to classrooms view state
     */
    fun onViewClassroomsMenuClicked() {
        changeViewState(MainView.ViewState.CLASSROOM_VIEW)
    }

    /**
     * Opening export menu
     */
    fun onExportMenuClicked() {
        val map = mapOf("currentTimetable" to view.currentTimetable)
        find<ExportView>(map).openWindow()
    }

    /**
     * Opening import menu
     */
    fun onImportMenuClicked() {
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
    fun onSettingsInterfaceMenuClicked() {
        val settingsView = SettingsView()
        settingsView.openModal(
            owner = this.view.currentWindow,
            block = true
        )
    }

    /**
     * TODO Opening generator settings menu
     */
    fun onSettingsGeneratorMenuClicked() {

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