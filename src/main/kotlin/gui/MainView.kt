package gui

import classes.*
import gui.controls.TimetableCell
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
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
    internal var timetableCells: MutableList<TimetableCell>

    // Lists of timetable content
    var availableLessons: MutableList<Lesson>
    var availableTeachers: MutableList<Teacher>
    var availableClassrooms: MutableList<Classroom>
    var availableSubjects: MutableList<Subject>
    var availableStudentClasses: MutableList<StudentClass>

    // TODO Settings
    internal var settings = Settings()
    /* // Current itembox
     private var currentItemBox: ItemBox*/
    // Selected day
    var selectedDay: String

    // Current timetable grid
    var gridTimeTable: GridPane = GridPane()

    /**
     * View statements
     */
    enum class ViewState { CLASSROOM_VIEW, STUDENT_CLASS_VIEW, TEACHER_VIEW }

    // Current timetable state
    var selectedState: ViewState = ViewState.STUDENT_CLASS_VIEW

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
                            val timetableCell = TimetableCell(studentClass = availableStudentClasses[i])
                            timetableCells.add(timetableCell)
                            addColumn(i + 1, timetableCell)
                        }

                        // Rows
                        for (i in 0 until settings.lessonsTime.size) {
                            addRow(i + 1, Label(settings.lessonsTime[i]))
                        }

                        // Items
                        for (i in 0 until availableLessons.filter { it.day == selectedDay }.size) {
                            val lesson = availableLessons[i]
                            val timetableCell = TimetableCell(lesson)
                            timetableCells.add(timetableCell)
                            add(
                                timetableCell,
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

                /*anchorpane {
                    vbox {
                        alignment = Pos.CENTER

                        label("Уроки")
                        currentItemBox = ItemBox(availableClassrooms, availableSubjects, availableTeachers)
                    }
                }*/
        }

        // Drag and Drop event filters
        addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
        addEventFilter(MouseEvent.MOUSE_DRAGGED, ::animateDrag)
        addEventFilter(MouseEvent.MOUSE_EXITED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::drop)

        // Clicks
        addEventFilter(MouseEvent.MOUSE_CLICKED, ::changeCell)
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

    // Change cell data
    private fun changeCell(mouseEvent: MouseEvent) {
        // TODO Double click
        if (mouseEvent.button == MouseButton.PRIMARY)
            controller.onClick(mouseEvent)
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
        find<ExportView>(map).openModal(
            owner = view.currentWindow,
            block = true
        )
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
     * Mouse click
     */
    fun onClick(mouseEvent: MouseEvent) {
        val selectedCell = findLesson(mouseEvent)
        if (selectedCell != null)
            editCell(selectedCell)
    }

    /**
     * Find lesson by cell coord
     */
    fun findLesson(mouseEvent: MouseEvent): TimetableCell? {
        val cellCoord = findCellCoord(mouseEvent)
        return view.timetableCells
            .filter { it.cellType == TimetableCell.CellType.LESSON }
            .find {
                (it.getItem() as Lesson).number == cellCoord.second &&
                        (it.getItem() as Lesson).day == view.selectedDay && ((
                        view.selectedState == MainView.ViewState.CLASSROOM_VIEW &&
                                (it.getItem() as Lesson).classroom == view.availableClassrooms[cellCoord.first - 1]
                        ) || (
                        view.selectedState == MainView.ViewState.STUDENT_CLASS_VIEW &&
                                (it.getItem() as Lesson).studentClass == view.availableStudentClasses[cellCoord.first - 1]
                        ) || (
                        view.selectedState == MainView.ViewState.TEACHER_VIEW &&
                                (it.getItem() as Lesson).teacher == view.availableTeachers[cellCoord.first - 1]
                        ))
            }
    }

    /**
     * Find cell by mouse coord
     * @param[mouseEvent] Mouse event
     */
    fun findCellCoord(mouseEvent: MouseEvent): Pair<Int, Int> {
        // TODO Сделать адаптивно вычисляемый размер заголовка
        val headerHeight = 90.0
        val mousePt = view.gridTimeTable.localToScene(mouseEvent.x, mouseEvent.y - headerHeight)
        // Поиск ячейки
        if (view.gridTimeTable.contains(mousePt)) {
            for (i in 1 until view.gridTimeTable.columnCount) {
                for (j in 1 until view.gridTimeTable.rowCount) {
                    if (view.gridTimeTable.getCellBounds(i, j).contains(mousePt))
                        return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1)
    }

    /**
     * EditCell
     */
    fun editCell(cell: TimetableCell) {
        // TODO Добавить изменяемый урок
        val parameters = mapOf(
            "classrooms" to view.availableClassrooms,
            "lesson" to cell.lesson,
            "studentClasses" to view.availableStudentClasses,
            "subjects" to view.availableSubjects,
            "teachers" to view.availableTeachers
        )
        val cellFragment = find<EditCellFragment>(parameters)
        cellFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        val editedLesson = view.availableLessons.filter { it.id == cellFragment.lesson!!.id }
        val editedIndex = view.availableLessons.indexOf(editedLesson[0])
        view.availableLessons[editedIndex] = cellFragment.lesson!!
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