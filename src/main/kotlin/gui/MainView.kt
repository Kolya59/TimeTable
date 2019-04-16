package gui

import TimetableStyleSheet
import classes.*
import gui.controls.TimetableCell
import javafx.event.ActionEvent
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.effect.DropShadow
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import serialization.Export
import serialization.Import
import tornadofx.*
import java.io.File
import kotlin.collections.MutableList
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.indexOf
import kotlin.collections.mapOf
import kotlin.collections.toMutableList
import kotlin.collections.firstOrNull as firstOrNull1

class MainView : View("Редактор расписания") {
    // Controller
    private val controller: MainController by inject()

    // Current timetable
    lateinit var currentTimetable: TimeTable

    // List of timetable cells
    lateinit var timetableCells: MutableList<TimetableCell>

    // Lists of timetable content
    lateinit var availableLessons: MutableList<Lesson>
    lateinit var availableTeachers: MutableList<Teacher>
    lateinit var availableClassrooms: MutableList<Classroom>
    lateinit var availableSubjects: MutableList<Subject>
    lateinit var availableStudentClasses: MutableList<StudentClass>

    var inFlightTimeTableCell: TimetableCell = TimetableCell()

    // TODO Settings
    internal var settings = Settings()

    // Selected day
    lateinit var selectedDay: String

    // Current timetable grid
    var gridTimeTable: GridPane = gridpane()

    /**
     * View statements
     */
    enum class ViewState { CLASSROOM_VIEW, STUDENT_CLASS_VIEW, TEACHER_VIEW }

    // Current timetable state
    var selectedState: ViewState

    override val root = VBox()

    init {
        loadData(true)
        selectedState = ViewState.STUDENT_CLASS_VIEW
        setupColections()
        setupInterface()
    }

    fun loadData(reinitFlag: Boolean) {
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
                if (reinitFlag)
                    controller.loadFromConfig(
                        "currentTimeTable.json"
                    )
                else {
                    currentTimetable
                }
            } catch (e: Exception) {
                TimeTable(
                    availableLessons,
                    availableTeachers,
                    availableClassrooms,
                    availableStudentClasses,
                    availableSubjects
                )
            }
    }

    fun setupColections() {
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

    fun setupInterface() {
        with(root) {
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
                tabpane {
                    anchorpaneConstraints {
                        topAnchor = 0.0
                        bottomAnchor = 10.0
                        rightAnchor = 0.0
                        leftAnchor = 0.0
                    }
                    tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

                    tab("Расписание") {
                        anchorpane {
                            alignment = Pos.TOP_CENTER

                            gridTimeTable = gridpane {
                                // Columns
                                addColumn(0, Label("Время"))
                                when (selectedState) {
                                    ViewState.STUDENT_CLASS_VIEW -> {
                                        for (i in 0 until availableStudentClasses.size) {
                                            val timetableCell = TimetableCell(studentClass = availableStudentClasses[i])
                                            timetableCells.add(timetableCell)
                                            addColumn(i + 1, timetableCell)
                                        }
                                        repeat(children.filter {
                                            it is TimetableCell &&
                                                    it.getItem() is StudentClass
                                        }.size) {
                                            addClass(TimetableStyleSheet.columnHeader)
                                        }
                                    }
                                    ViewState.CLASSROOM_VIEW -> {
                                        for (i in 0 until availableClassrooms.size) {
                                            val timetableCell = TimetableCell(classroom = availableClassrooms[i])
                                            timetableCells.add(timetableCell)
                                            addColumn(i + 1, timetableCell)
                                        }
                                        repeat(children.filter {
                                            it is TimetableCell &&
                                                    it.getItem() is Classroom
                                        }.size) {
                                            addClass(TimetableStyleSheet.columnHeader)
                                        }
                                    }
                                    ViewState.TEACHER_VIEW -> {
                                        for (i in 0 until availableTeachers.size) {
                                            val timetableCell = TimetableCell(teacher = availableTeachers[i])
                                            timetableCells.add(timetableCell)
                                            addColumn(i + 1, timetableCell)
                                        }
                                        repeat(children.filter {
                                            it is TimetableCell &&
                                                    it.getItem() is Teacher
                                        }.size) {
                                            addClass(TimetableStyleSheet.columnHeader)
                                        }
                                    }
                                }


                                // Rows
                                for (i in 0 until settings.lessonsTime.size) {
                                    addRow(i + 1, Label(settings.lessonsTime[i]))
                                }
                                repeat(children.filter { it is Label }.size) {
                                    addClass(TimetableStyleSheet.rowHeader)
                                }

                                // Items
                                for (i in 0 until availableLessons.filter { it.day == selectedDay }.size) {
                                    val lesson = availableLessons[i]
                                    val timetableCell = TimetableCell(lesson)
                                    timetableCells.add(timetableCell)
                                    val columnIndex = when (selectedState) {
                                        ViewState.STUDENT_CLASS_VIEW -> {
                                            availableStudentClasses.indexOf(
                                                availableStudentClasses.find {
                                                    it.name == lesson.studentClass?.name
                                                }) + 1
                                        }
                                        ViewState.CLASSROOM_VIEW -> {
                                            availableClassrooms.indexOf(availableClassrooms.find {
                                                it.name == lesson.classroom?.name
                                            }) + 1
                                        }
                                        ViewState.TEACHER_VIEW -> {
                                            availableTeachers.indexOf(availableTeachers.find {
                                                it.name == lesson.teacher?.name
                                            }) + 1

                                        }
                                    }
                                    add(
                                        timetableCell,
                                        columnIndex,
                                        lesson.number
                                    )
                                }

                                when (selectedState) {
                                    ViewState.CLASSROOM_VIEW -> repeat(children.filter {
                                        it is TimetableCell &&
                                                it.getItem() is StudentClass
                                    }.size) {
                                        addClass(TimetableStyleSheet.columnHeader)
                                    }

                                    ViewState.STUDENT_CLASS_VIEW -> repeat(children.filter {
                                        it is TimetableCell &&
                                                it.getItem() is Classroom
                                    }.size) {
                                        addClass(TimetableStyleSheet.columnHeader)
                                    }

                                    ViewState.TEACHER_VIEW -> repeat(children.filter {
                                        it is TimetableCell &&
                                                it.getItem() is Teacher
                                    }.size) {
                                        addClass(TimetableStyleSheet.columnHeader)
                                    }
                                }

                                repeat(children.filter {
                                    it is TimetableCell &&
                                            it.getItem() is Lesson
                                }.size) {
                                    addClass(TimetableStyleSheet.filledCell)
                                }

                                // Drag-and-Drop
                                inFlightTimeTableCell.visibleProperty().set(false)
                                inFlightTimeTableCell.opacity = 0.5
                                inFlightTimeTableCell.effect = DropShadow()

                                columnConstraints.add(
                                    ColumnConstraints(
                                        100.0,
                                        120.0,
                                        140.0,
                                        Priority.ALWAYS,
                                        javafx.geometry.HPos.CENTER,
                                        true
                                    )
                                )
                                vboxConstraints {
                                    vgrow = Priority.ALWAYS
                                    hgrow = Priority.ALWAYS
                                }
                                isGridLinesVisible = true
                                anchorpaneConstraints {
                                    topAnchor = 10.0
                                    bottomAnchor = 5.0
                                    rightAnchor = 3.0
                                    leftAnchor = 3.0
                                }
                            }
                        }
                    }
                    tab("Данные") {
                        hbox {
                            // TODO Cellfactory
                            // TODO Проверка нажатий
                            tableview<Classroom> {
                                items = availableClassrooms.observable()
                                column("Название", Classroom::name)
                            }
                            tableview<StudentClass> {
                                items = availableStudentClasses.observable()
                                column("Название", StudentClass::name)
                            }
                            tableview<Subject> {
                                items = availableSubjects.observable()
                                column("Название", Subject::name)
                            }
                            tableview<Teacher> {
                                items = availableTeachers.observable()
                                column("Название", Teacher::name)
                                column("Название", Teacher::availableSubjects)
                            }
                        }
                    }
                }
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
        if (mouseEvent.button == MouseButton.SECONDARY)
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
        // DEBUG
//        alert(Alert.AlertType.INFORMATION, "Cell Coord", cellCoord.toString())
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
        val headerHeight = 50.0
        val mousePt = Point2D(mouseEvent.x, mouseEvent.y - headerHeight)
        // DEBUG
//        alert(Alert.AlertType.INFORMATION, "Cell Coord", mousePt.toString())
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
        val editedLesson = view.currentTimetable.lessons.firstOrNull1 { it.id == cellFragment.lesson!!.id }
        if (editedLesson != null) {
            val editedIndex = view.currentTimetable.lessons.indexOf(editedLesson)
            view.currentTimetable.lessons[editedIndex] = cellFragment.lesson!!
            val newLesson = cellFragment.lesson!!
            val editedCell = cell
            editedCell.setItem(newLesson)
            view.gridTimeTable.children.firstOrNull1 {
                it is TimetableCell &&
                        it.getItem() != null &&
                        it.getItem() is Lesson &&
                        (it.getItem() as Lesson).id == editedLesson.id
            }!!.replaceWith(editedCell)

        }
//        Export.ExportTimetable("currentTimeTable.json", view.currentTimetable).toJSON()
    }

    /**
     * TODO: Changing view state
     */
    fun changeViewState(viewState : MainView.ViewState) {
        view.selectedState = viewState
        clearInterface()
        view.setupInterface()
    }

    /**
     * TODO Start dragging
     */
    fun onStartDrag(mouseEvent: MouseEvent) {
        val selectedCell = findLesson(mouseEvent)
        if (selectedCell != null) {
            val selectedTimeTableCell = view.gridTimeTable.children.firstOrNull1 {
                it is TimetableCell && it.getItem() == selectedCell.getItem()
            } as? TimetableCell
            if (selectedTimeTableCell != null) {
                val bf = BackgroundFill(
                    Color.GREY,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
                selectedTimeTableCell.background = Background(bf)
            }
        }
    }

    /**
     * TODO Animation of dragging
     */
    fun onAnimateDrag(mouseEvent: MouseEvent) {
        val selectedCell = findLesson(mouseEvent)
        if (selectedCell != null) {
            /*// highlight the onDrop target (hover doesn't work)
            if (!workArea.hasClass(DraggingStyles.workAreaSelected)) {
                workArea.addClass(DraggingStyles.workAreaSelected)
            }*/

            view.inFlightTimeTableCell = selectedCell
             // animate a rectangle so that the user can follow
            if (!view.inFlightTimeTableCell.isVisible) {
                view.inFlightTimeTableCell.isVisible = true
                view.inFlightTimeTableCell.fill(
                    Background(
                        BackgroundFill(
                            Color.CHOCOLATE,
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                        )
                    )
                )
             }

            view.inFlightTimeTableCell.relocate(mouseEvent.x, mouseEvent.y)
        }
    }

    /**
     * TODO Stop dragging
     */
    fun onStopDrag(mouseEvent: MouseEvent) {
        // TODO Добавить способ отмены изменений
        if (view.inFlightTimeTableCell.isVisible) {

        }
    }

    /**
     * TODO Drop item
     */
    fun onDrop(mouseEvent: MouseEvent) {
        val selectedCell = findLesson(mouseEvent)
        if (selectedCell != null) {
            val selectedTimeTableCell = view.gridTimeTable.children.firstOrNull1 {
                it is TimetableCell && it.getItem() == selectedCell.getItem()
            } as? TimetableCell
            // DEBUG
//            alert(Alert.AlertType.INFORMATION, "Cell", selectedTimeTableCell?.getItem().toString())
            if (selectedTimeTableCell != null) {
                val bf = BackgroundFill(
                    Color.GREEN,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
                selectedTimeTableCell.background = Background(bf)
            }
        }
        val sourceCell = view.gridTimeTable.children.firstOrNull1 {
            it is TimetableCell &&
                    it.background != null &&
                    it.background == Background(
                BackgroundFill(
                    Color.GREY,
                    CornerRadii.EMPTY,
                    Insets.EMPTY
                )
            )
        }

        if (sourceCell != null)
            (sourceCell as TimetableCell).fill(
                Background(
                    BackgroundFill(
                        Color.WHITE,
                        CornerRadii.EMPTY,
                        Insets.EMPTY
                    )
                )
            )
    }

    /**
     * TODO Clearing all data from timetable
     */
    fun clearAll() {

    }

    /**
     * Clearing interace
     */
    fun clearInterface() {
        view.root.children.clear()
    }

    /**
     * Clearing collections
     */
    fun clearCollections() {
        view.availableClassrooms.clear()
        view.availableLessons.clear()
        view.availableStudentClasses.clear()
        view.availableSubjects.clear()
        view.availableSubjects.clear()
    }

    /**
     * Load data from config
     * @param[pathToConfig] Path to config file
     * @return TimeTable Imported timetable
     */
    fun loadFromConfig(pathToConfig: String): TimeTable = Import.ImportTimetable(File(pathToConfig)).fromJSON()
}