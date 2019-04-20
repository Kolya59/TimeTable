package gui

import TimetableStyleSheet
import classes.*
import gui.MainView.ViewState
import gui.MainView.ViewState.*
import gui.controls.TimetableCell
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
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
import kotlin.collections.contains
import kotlin.collections.emptyList
import kotlin.collections.emptySet
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.forEach
import kotlin.collections.indexOf
import kotlin.collections.last
import kotlin.collections.mapOf
import kotlin.collections.remove
import kotlin.collections.removeAll
import kotlin.collections.sortedBy
import kotlin.collections.toMutableList
import kotlin.collections.toMutableSet
import kotlin.collections.firstOrNull as firstOrNull1

class MainView : View("Редактор расписания") {
    // Controller
    private val controller: MainController by inject()

    // Current timetable
    lateinit var currentTimetable: TimeTable

    // List of timetable cells
    lateinit var timetableCells: MutableList<TimetableCell>

    var sourceTimeTableCell: TimetableCell? = TimetableCell()
    var sourceTimeTableCellCoord: Pair<Int, Int> = Pair(0, 0)
    var inFlightTimeTableCell: TimetableCell = TimetableCell()
    var targetTimeTableCell: TimetableCell? = TimetableCell()
    var targetTimeTableCellCoord: Pair<Int, Int> = Pair(0, 0)

    lateinit var paneGlobal: TabPane
    private lateinit var paneDays: TabPane

    var selectedTabFlag = true

    // TODO Settings
    internal var settings = Settings()

    var tvClassroom = TableView<Classroom>()
    var tvStudentClass = TableView<StudentClass>()
    var tvSubject = TableView<Subject>()
    var tvTeacher = TableView<Teacher>()


    // Selected day
    var selectedDayIndex: Int = 0
        get() {
            return try {
                (paneDays as? TabPane)?.selectionModel?.selectedIndex ?: 0
            } catch (e: java.lang.Exception) {
                0
            }
        }
        set(value) {
            field = value
            paneDays.selectionModel.select(value)
        }

    // Current timetable grid
    var gridTimeTable: MutableList<GridPane> = emptyList<GridPane>().toMutableList()

    /**
     * View statements
     */
    enum class ViewState { CLASSROOM_VIEW, STUDENT_CLASS_VIEW, TEACHER_VIEW }

    // Current timetable state
    var selectedState: ViewState

    override val root = VBox()

    init {
        loadData()
        selectedState = STUDENT_CLASS_VIEW
        setupInterface()
    }

    private fun loadData() {
        // Создание пустых коллекций
        timetableCells = emptyList<TimetableCell>().toMutableList()
        // Загрузка данных из конфига
        currentTimetable =
            try {
                controller.loadFromConfig("currentTimeTable.json")
            } catch (e: Exception) {
                TimeTable(
                    emptyList<Lesson>().toMutableList(),
                    emptyList<Teacher>().toMutableList(),
                    emptyList<Classroom>().toMutableList(),
                    emptyList<StudentClass>().toMutableList(),
                    emptyList<Subject>().toMutableList()
                )
            }
    }

    fun setupInterface() {
        with(root) {
            alignment = Pos.TOP_CENTER

            menubar {
                menu("Файл") {
                    item("Создать") {
                        action { controller.onCreateMenuClicked() }
                    }
                    item("Открыть") {
                        action { controller.onImportMenuClicked() }
                    }
                    item("Сохранить") {
                        action { controller.onSaveMenuClicked() }
                    }
                    item("Сохранить как") {
                        action { controller.onExportMenuClicked() }
                    }
                    item("Выход") {
                        action { controller.onExitMenuClicked() }
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
                menu("Настройки") {
                    item("Интерфейс") {
                        action { controller.onSettingsInterfaceMenuClicked() }
                    }
                    /*item("Генерация") {
                        action { controller.onSettingsGeneratorMenuClicked() }
                    }*/
                }
            }
            anchorpane {
                // TODO Поправить верстку см 100 стр
                paneGlobal = tabpane {
                    anchorpaneConstraints {
                        topAnchor = 0.0
                        bottomAnchor = 10.0
                        rightAnchor = 0.0
                        leftAnchor = 0.0
                    }

                    tab("Расписание") {
                        paneDays = tabpane {
                            for (day in settings.workingDays) {
                                tab(day) {
                                    borderpane {
                                        gridTimeTable.add(gridpane {
                                            // Columns
                                            addColumn(0, Label("Время"))
                                            when (selectedState) {
                                                STUDENT_CLASS_VIEW -> {
                                                    for (i in 0 until currentTimetable.studentClasses.size) {
                                                        val timetableCell =
                                                            TimetableCell(studentClass = currentTimetable.studentClasses[i])
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
                                                CLASSROOM_VIEW -> {
                                                    for (i in 0 until currentTimetable.classrooms.size) {
                                                        val timetableCell =
                                                            TimetableCell(classroom = currentTimetable.classrooms[i])
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
                                                TEACHER_VIEW -> {
                                                    for (i in 0 until currentTimetable.teachers.size) {
                                                        val timetableCell =
                                                            TimetableCell(teacher = currentTimetable.teachers[i])
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
                                            for (i in 0 until currentTimetable.lessons.filter { it.day == day }.size) {
                                                val lesson = currentTimetable.lessons.filter { it.day == day }[i]
                                                val timetableCell = TimetableCell(lesson)
                                                timetableCells.add(timetableCell)
                                                val columnIndex = when (selectedState) {
                                                    STUDENT_CLASS_VIEW -> {
                                                        currentTimetable.studentClasses.indexOf(
                                                            currentTimetable.studentClasses.find {
                                                                it.name == lesson.studentClass?.name
                                                            }) + 1
                                                    }
                                                    CLASSROOM_VIEW -> {
                                                        currentTimetable.classrooms.indexOf(currentTimetable.classrooms.find {
                                                            it.name == lesson.classroom?.name
                                                        }) + 1
                                                    }
                                                    TEACHER_VIEW -> {
                                                        currentTimetable.teachers.indexOf(currentTimetable.teachers.find {
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
                                                CLASSROOM_VIEW -> repeat(children.filter {
                                                    it is TimetableCell &&
                                                            it.getItem() is StudentClass
                                                }.size) {
                                                    addClass(TimetableStyleSheet.columnHeader)
                                                }

                                                STUDENT_CLASS_VIEW -> repeat(children.filter {
                                                    it is TimetableCell &&
                                                            it.getItem() is Classroom
                                                }.size) {
                                                    addClass(TimetableStyleSheet.columnHeader)
                                                }

                                                TEACHER_VIEW -> repeat(children.filter {
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
                                        })
                                        center = gridTimeTable.last()
                                    }
                                }
                            }

                            selectionModel.select(selectedDayIndex)
                            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                        }
                    }
                    tab("Данные") {
                        hbox {
                            vbox {
                                tvClassroom = tableview<Classroom> {
                                    items = currentTimetable.classrooms.observable()
                                    column("Кабинет", Classroom::name)
                                    columnResizePolicy = CONSTRAINED_RESIZE_POLICY
                                    maxWidth = 80.0
                                }
                                vbox {
                                    button {
                                        text = "Добавить"
                                        action {
                                            controller.onCreateDataClassroom()
                                        }
                                    }
                                    button {
                                        text = "Изменить"
                                        action {
                                            controller.onUpdateDataClassroom()
                                        }
                                    }
                                    button {
                                        text = "Удалить"
                                        action {
                                            controller.onDeleteDataClassroom()
                                        }
                                    }
                                }
                            }
                            vbox {
                                tvStudentClass = tableview<StudentClass> {
                                    items = currentTimetable.studentClasses.observable()
                                    column("Класс", StudentClass::name)
                                    columnResizePolicy = CONSTRAINED_RESIZE_POLICY
                                    maxWidth = 80.0
                                }
                                vbox {
                                    button {
                                        text = "Добавить"
                                        action {
                                            controller.onCreateDataStudentClass()
                                        }
                                    }
                                    button {
                                        text = "Изменить"
                                        action {
                                            controller.onUpdateDataStudentClass()
                                        }
                                    }
                                    button {
                                        text = "Удалить"
                                        action {
                                            controller.onDeleteDataStudentClass()
                                        }
                                    }
                                }
                            }
                            vbox {
                                tvSubject = tableview<Subject> {
                                    items = currentTimetable.subjects.observable()
                                    column("Предмет", Subject::name)
                                    columnResizePolicy = CONSTRAINED_RESIZE_POLICY
                                }
                                vbox {
                                    button {
                                        text = "Добавить"
                                        action {
                                            controller.onCreateDataSubject()
                                        }
                                    }
                                    button {
                                        text = "Изменить"
                                        action {
                                            controller.onUpdateDataSubject()
                                        }
                                    }
                                    button {
                                        text = "Удалить"
                                        action {
                                            controller.onDeleteDataSubject()
                                        }
                                    }
                                }
                            }
                            vbox {
                                tvTeacher = tableview<Teacher> {
                                    items = currentTimetable.teachers.observable()
                                    column("Название", Teacher::name)
                                    column("Доступные предметы", Teacher::availableSubjects)
                                }
                                vbox {
                                    button {
                                        text = "Добавить"
                                        action {
                                            controller.onCreateDataTeacher()
                                        }
                                    }
                                    button {
                                        text = "Изменить"
                                        action {
                                            controller.onUpdateDataTeacher()
                                        }
                                    }
                                    button {
                                        text = "Удалить"
                                        action {
                                            controller.onDeleteDataTeacher()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
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
        controller.onStopDrag()
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
    private val view: MainView by inject()

    /**
     * TODO: Creating new TimetableGrid
     */
    fun onCreateMenuClicked() {
        clearAll()
    }

    /**
     * TODO: Saving current TimetableGrid
     */
    fun onSaveMenuClicked() {
        Export.ExportTimetable("currentTimeTable.json", view.currentTimetable).toJSON()
        alert(Alert.AlertType.INFORMATION, "Информация о сохранении", "Расписание успешно сохранено")
    }

    /**
     * Closing view
     */
    fun onExitMenuClicked() {
        view.close()
    }

    /**
     * Setting view to student class view state
     */
    fun onViewStudentClassesMenuClicked() {
        changeViewState(STUDENT_CLASS_VIEW)
    }

    /**
     * Setting view to teachers view state
     */
    fun onViewTeachersMenuClicked() {
        changeViewState(TEACHER_VIEW)
    }

    /**
     * Setting view to classrooms view state
     */
    fun onViewClassroomsMenuClicked() {
        changeViewState(CLASSROOM_VIEW)
    }

    /**
     * Opening export menu
     */
    fun onExportMenuClicked() {
        val map = mapOf("currentTimetable" to view.currentTimetable)
        find<ExportView>(map).openModal(
            owner = view.currentWindow,
            block = true,
            resizable = false
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
            block = true,
            resizable = false
        )
    }

    /**
     * Opening interface settings menu
     */
    fun onSettingsInterfaceMenuClicked() {
        val settingsView = SettingsView()
        settingsView.openModal(
            owner = this.view.currentWindow,
            block = true,
            resizable = false
        )
    }

    /**
     * TODO Opening generator settings menu
     */
    /*fun onSettingsGeneratorMenuClicked() {}*/

    /**
     * Mouse click
     */
    fun onClick(mouseEvent: MouseEvent) {
        if (view.selectedState != STUDENT_CLASS_VIEW || view.paneGlobal.selectionModel.selectedIndex != 0)
            return
        val selectedCell = findLessonCell(mouseEvent)
        if (selectedCell != null)
            editCell(selectedCell)
        else if (findCellCoord(mouseEvent) != Pair(-1, -1)) {
            val coord = findCellCoord(mouseEvent)
            createCell(view.settings.workingDays[view.selectedDayIndex], coord.first, coord.second)
        }
    }

    /**
     * Generating lesson by some data
     * @param[args] Input data
     * @return Generated lesson
     */
    private fun generateLesson(vararg args: Any): Lesson {
        var newId = 0
        view.currentTimetable.lessons.sortedBy { it.id }.forEach { if (it.id == newId) newId++ }

        val newSubject: Subject? = args.firstOrNull1 { it is Subject } as? Subject
        val newTeacher: Teacher? = args.firstOrNull1 { it is Teacher } as? Teacher
        val newClassroom: Classroom? = args.firstOrNull1 { it is Classroom } as? Classroom
        val newStudentClass: StudentClass? = args.firstOrNull1 { it is StudentClass } as? StudentClass
        val newNumber: Int = args.firstOrNull1 { it is Int } as Int
        val newDay: String = args.firstOrNull1 { it is String } as String
        val newPinned: Boolean = (args.firstOrNull1 { it is Boolean }) != null

        return Lesson(
            newId,
            newSubject,
            newTeacher,
            newClassroom,
            newStudentClass,
            newNumber,
            newDay,
            newPinned
        )
    }

    /**
     * Generating classroom by some data
     * @param[args] Input data
     * @return Generated classroom
     */
    private fun generateClassroom(vararg args: Any): Classroom {
        var newId = 0
        view.currentTimetable.classrooms.sortedBy { it.id }.forEach { if (it.id == newId) newId++ }

        val newName: String = args.firstOrNull1 { it is String }.toString()

        return Classroom(newId, newName)
    }

    /**
     * Generating student class by some data
     * @param[args] Input data
     * @return Generated student class
     */
    private fun generateStudentClass(vararg args: Any): StudentClass {
        var newId = 0
        view.currentTimetable.studentClasses.sortedBy { it.id }.forEach { if (it.id == newId) newId++ }

        val newName: String = args.firstOrNull1 { it is String }.toString()

        return StudentClass(newId, newName)
    }

    /**
     * Generating subject by some data
     * @param[args] Input data
     * @return Generated subject
     */
    private fun generateSubject(vararg args: Any): Subject {
        var newId = 0
        view.currentTimetable.subjects.sortedBy { it.id }.forEach { if (it.id == newId) newId++ }

        val newName: String = args.firstOrNull1 { it is String }.toString()

        return Subject(newId, newName)
    }

    /**
     * Generating teacher by some data
     * @param[args] Input data
     * @return Generated teacher
     */
    private fun generateTeacher(vararg args: Any): Teacher {
        var newId = 0
        view.currentTimetable.lessons.sortedBy { it.id }.forEach { if (it.id == newId) newId++ }

        val newName: String = args.firstOrNull1 { it is String }.toString()

        return Teacher(
            newId,
            newName,
            emptySet<Subject>().toMutableSet()
        )
    }

    /**
     * Find lesson by cell coord
     */
    private fun findLessonCell(mouseEvent: MouseEvent): TimetableCell? {
        val cellCoord = findCellCoord(mouseEvent)
        // DEBUG
//        alert(Alert.AlertType.INFORMATION, "Cell Coord", cellCoord.toString())
        return view.timetableCells
            .filter { it.cellType == TimetableCell.CellType.LESSON && view.settings.workingDays.indexOf((it.getItem() as Lesson).day) == view.selectedDayIndex }
            .find {
                (it.getItem() as Lesson).number == cellCoord.second &&
                        ((
                                view.selectedState == CLASSROOM_VIEW &&
                                        (it.getItem() as Lesson).classroom == view.currentTimetable.classrooms[cellCoord.first - 1]
                                ) || (
                                view.selectedState == STUDENT_CLASS_VIEW &&
                                        (it.getItem() as Lesson).studentClass == view.currentTimetable.studentClasses[cellCoord.first - 1]
                                ) || (
                                view.selectedState == TEACHER_VIEW &&
                                        (it.getItem() as Lesson).teacher == view.currentTimetable.teachers[cellCoord.first - 1]
                                ))
            }
    }

    /**
     * Find cell by mouse coord
     * @param[mouseEvent] Mouse event
     */
    private fun findCellCoord(mouseEvent: MouseEvent): Pair<Int, Int> {
        // TODO Сделать адаптивно вычисляемый размер заголовка
        val headerHeight = 95.0
        val mousePt = Point2D(mouseEvent.x, mouseEvent.y - headerHeight)

        // DEBUG
//        alert(Alert.AlertType.INFORMATION, "Cell Coord", mousePt.toString())
//        var logError = "$mousePt \n"
        // Поиск ячейки
        if (view.gridTimeTable[view.selectedDayIndex].contains(mousePt)) {
            for (i in 1 until view.gridTimeTable[view.selectedDayIndex].columnCount) {
                for (j in 1 until view.gridTimeTable[view.selectedDayIndex].rowCount) {
                    val bounds = view.gridTimeTable[view.selectedDayIndex].getCellBounds(i, j)
                    val containFlag = bounds.contains(mousePt)
//                    logError += "${bounds.minX}, ${bounds.minY}, ${bounds.maxX}, ${bounds.maxY}, $containFlag \n"
                    if (containFlag)
                        return Pair(i, j)
                }
            }
        }
//        alert(Alert.AlertType.INFORMATION, "", logError)
        return Pair(-1, -1)
    }

    /**
     * Edit cell
     */
    private fun editCell(cell: TimetableCell) {
        val parameters = mapOf(
            "classrooms" to view.currentTimetable.classrooms,
            "lesson" to cell.lesson,
            "studentClasses" to view.currentTimetable.studentClasses,
            "subjects" to view.currentTimetable.subjects,
            "teachers" to view.currentTimetable.teachers,
            "state" to view.selectedState,
            "pinned" to cell.lesson!!.pinned
        )
        val cellFragment = find<EditCellFragment>(parameters)
        cellFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        val editedLesson = view.currentTimetable.lessons.firstOrNull1 { it.id == cellFragment.lesson.id }
        if (editedLesson != null) {
            val editedIndex = view.currentTimetable.lessons.indexOf(editedLesson)
            view.currentTimetable.lessons[editedIndex] = cellFragment.lesson
            val newLesson = cellFragment.lesson
            cell.setItem(newLesson)
            view.gridTimeTable[view.selectedDayIndex].children.firstOrNull1 {
                it is TimetableCell &&
                        it.getItem() != null &&
                        it.getItem() is Lesson &&
                        (it.getItem() as Lesson).id == editedLesson.id
            }!!.replaceWith(cell)

        }
//        Export.ExportTimetable("currentTimeTable.json", view.currentTimetable).toJSON()
    }

    /**
     * Create cell
     */
    private fun createCell(day: String, columnIndex: Int, rowIndex: Int) {
        val transferParameter: Any
        when (view.selectedState) {
            STUDENT_CLASS_VIEW -> {
                transferParameter = view.currentTimetable.studentClasses[columnIndex - 1]
            }
            CLASSROOM_VIEW -> {
                transferParameter = view.currentTimetable.classrooms[columnIndex - 1]
            }
            TEACHER_VIEW -> {
                transferParameter = view.currentTimetable.teachers[columnIndex - 1]
            }
        }
        val generatedLesson = generateLesson(
            day, rowIndex, when (view.selectedState) {
                STUDENT_CLASS_VIEW -> transferParameter as StudentClass
                CLASSROOM_VIEW -> transferParameter as Classroom
                TEACHER_VIEW -> transferParameter as Teacher
            }
        )
        val parameters = mapOf(
            "classrooms" to view.currentTimetable.classrooms,
            "lesson" to generatedLesson,
            "studentClasses" to view.currentTimetable.studentClasses,
            "subjects" to view.currentTimetable.subjects,
            "teachers" to view.currentTimetable.teachers,
            "state" to view.selectedState,
            "pinned" to generatedLesson.pinned
        )
        val cellFragment = find<EditCellFragment>(parameters)
        cellFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (cellFragment.lesson != generatedLesson) {
            val createdLesson = cellFragment.lesson
            view.currentTimetable.lessons.add(createdLesson)
            view.currentTimetable.lessons.add(createdLesson)
            clearInterface()
            view.setupInterface()
        }
    }

    /**
     * Changing view state
     */
    private fun changeViewState(viewState: ViewState) {
        view.selectedState = viewState
        clearInterface()
        view.setupInterface()
    }

    /**
     * TODO Start dragging
     */
    fun onStartDrag(mouseEvent: MouseEvent) {
        view.sourceTimeTableCell = findLessonCell(mouseEvent)
        view.sourceTimeTableCellCoord = findCellCoord(mouseEvent)
        if (view.sourceTimeTableCell != null) {
            val selectedTimeTableCell = view.gridTimeTable[view.selectedDayIndex].children.firstOrNull1 {
                it is TimetableCell && it.getItem() == (view.sourceTimeTableCell)!!.getItem()
            } as? TimetableCell
            if (selectedTimeTableCell != null && !selectedTimeTableCell.hasClass(TimetableStyleSheet.sourceCell)) {
                selectedTimeTableCell.addClass(TimetableStyleSheet.sourceCell)
            }
        }
    }

    /**
     * TODO Animation of dragging
     */
    fun onAnimateDrag(mouseEvent: MouseEvent) {
        if (view.selectedState != STUDENT_CLASS_VIEW || view.paneGlobal.selectionModel.selectedIndex != 0)
            return
        val selectedCell = findLessonCell(mouseEvent)
        if (selectedCell != null) {
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
    fun onStopDrag() {
        if (view.selectedState != STUDENT_CLASS_VIEW || view.paneGlobal.selectionModel.selectedIndex != 0)
            return
        if (view.inFlightTimeTableCell.isVisible) {
//            val targetCell = findLessonCell(mouseEvent)
//            if (!targetCell!!.hasClass(TimetableStyleSheet.targetCell))
//                targetCell.addClass(TimetableStyleSheet.targetCell)
        }
    }

    /**
     * TODO Drop item
     */
    fun onDrop(mouseEvent: MouseEvent) {
        if (view.selectedState != STUDENT_CLASS_VIEW || view.paneGlobal.selectionModel.selectedIndex != 0)
            return
        if (view.inFlightTimeTableCell.isVisible) {
            view.targetTimeTableCell = findLessonCell(mouseEvent)
            view.targetTimeTableCellCoord = findCellCoord(mouseEvent)
            if (view.targetTimeTableCell != null) {
                view.targetTimeTableCell = view.gridTimeTable[view.selectedDayIndex].children.firstOrNull1 {
                    it is TimetableCell && it.getItem() == (view.targetTimeTableCell)!!.getItem()
                } as? TimetableCell
                // DEBUG
//            alert(Alert.AlertType.INFORMATION, "Cell", selectedTimeTableCell?.getItem().toString())
//                if (view.targetTimeTableCell != null && view.targetTimeTableCell!!.hasClass(TimetableStyleSheet.targetCell)) {
//                    view.targetTimeTableCell!!.addClass((TimetableStyleSheet.targetCell))
//                }
            }
            swapCells(view.sourceTimeTableCell, view.targetTimeTableCell)
//            if (view.sourceTimeTableCell!!.hasClass(TimetableStyleSheet.sourceCell))
//                view.sourceTimeTableCell!!.removeClass(TimetableStyleSheet.sourceCell)
//
//            if (view.targetTimeTableCell!!.hasClass(TimetableStyleSheet.sourceCell))
//                view.targetTimeTableCell!!.removeClass(TimetableStyleSheet.targetCell)
//            view.inFlightTimeTableCell.isVisible = false
            clearInterface()
            view.setupInterface()
        }
    }

    /**
     * Swap two cells
     */
    fun swapCells(source: TimetableCell?, target: TimetableCell?) {
        if (source == null) {
            when (view.selectedState) {
                CLASSROOM_VIEW -> {
                    (target!!.getItem() as Lesson).classroom =
                        view.currentTimetable.classrooms[view.sourceTimeTableCellCoord.first - 1]
                }
                STUDENT_CLASS_VIEW -> {
                    (target!!.getItem() as Lesson).studentClass =
                        view.currentTimetable.studentClasses[view.sourceTimeTableCellCoord.first - 1]
                }
                TEACHER_VIEW -> {
                    (target!!.getItem() as Lesson).teacher =
                        view.currentTimetable.teachers[view.sourceTimeTableCellCoord.first - 1]
                }
            }
            (target.getItem() as Lesson).number = view.sourceTimeTableCellCoord.second

            return
        }

        if (target == null) {
            when (view.selectedState) {
                CLASSROOM_VIEW -> {
                    (source.getItem() as Lesson).classroom =
                        view.currentTimetable.classrooms[view.targetTimeTableCellCoord.first - 1]
                }
                STUDENT_CLASS_VIEW -> {
                    (source.getItem() as Lesson).studentClass =
                        view.currentTimetable.studentClasses[view.targetTimeTableCellCoord.first - 1]
                }
                TEACHER_VIEW -> {
                    (source.getItem() as Lesson).teacher =
                        view.currentTimetable.teachers[view.targetTimeTableCellCoord.first - 1]
                }
            }
            (source.getItem() as Lesson).number = view.targetTimeTableCellCoord.second

            return
        }

        var tmp: Any?
        when (view.selectedState) {
            CLASSROOM_VIEW -> {
                tmp = (source.getItem() as Lesson).classroom
                (source.getItem() as Lesson).classroom = (target.getItem() as Lesson).classroom
                (target.getItem() as Lesson).classroom = tmp
            }
            STUDENT_CLASS_VIEW -> {
                tmp = (source.getItem() as Lesson).studentClass
                (source.getItem() as Lesson).studentClass = (target.getItem() as Lesson).studentClass
                (target.getItem() as Lesson).studentClass = tmp
            }
            TEACHER_VIEW -> {
                tmp = (source.getItem() as Lesson).teacher
                (source.getItem() as Lesson).teacher = (target.getItem() as Lesson).teacher
                (target.getItem() as Lesson).teacher = tmp
            }
        }

        tmp = (source.getItem() as Lesson).number
        (source.getItem() as Lesson).number = (target.getItem() as Lesson).number
        (target.getItem() as Lesson).number = tmp
    }

    /**
     * Clearing all data from timetable
     */
    private fun clearAll() {
        clearCollections()
        clearInterface()
    }

    /**
     * Clearing interface
     */
    private fun clearInterface() = view.root.clear()

    /**
     * Clearing collections
     */
    private fun clearCollections() {
        view.currentTimetable.classrooms.clear()
        view.currentTimetable.lessons.clear()
        view.currentTimetable.studentClasses.clear()
        view.currentTimetable.subjects.clear()
    }

    /**
     * Load data from config
     * @param[pathToConfig] Path to config file
     * @return TimeTable Imported timetable
     */
    fun loadFromConfig(pathToConfig: String): TimeTable = Import.ImportTimetable(File(pathToConfig)).fromJSON()

    /**
     * CRUID classrooms
     */
    fun onCreateDataClassroom() {
        val classroom = generateClassroom()
        val params = mapOf(
            "item" to classroom,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataFragment = find<CreateDataFragment>(params)
        createDataFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataFragment.savedItem != null) {
            view.currentTimetable.classrooms.add(createDataFragment.savedItem as Classroom)
            clearInterface()
            view.setupInterface()
        }
    }

    fun onUpdateDataClassroom() {
        val selectedClassroom = view.tvClassroom.selectedItem
        val params = mapOf(
            "item" to selectedClassroom,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataFragment = find<CreateDataFragment>(params)
        createDataFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataFragment.savedItem != null) {
            view.currentTimetable.classrooms.remove(selectedClassroom)
            view.currentTimetable.classrooms.add(createDataFragment.savedItem as Classroom)
            clearInterface()
            view.setupInterface()
            view.paneGlobal.selectionModel.select(1)
        }
    }

    fun onDeleteDataClassroom() {
        if (view.currentTimetable.classrooms.size == 1) {
            alert(Alert.AlertType.ERROR, "Ошибка удаления", "Нельзя удалить все кабинеты")
            return
        }
        val selectedClassroom = view.tvClassroom.selectedItem
        if (view.currentTimetable.classrooms.contains(selectedClassroom)) {
            view.currentTimetable.classrooms.remove(selectedClassroom)
            view.currentTimetable.lessons.removeAll { it.classroom == selectedClassroom }
            clearInterface()
            view.setupInterface()
            view.tvClassroom.selectionModel.select(view.currentTimetable.classrooms.firstOrNull1())
            view.paneGlobal.selectionModel.select(1)
        }
    }

    /**
     * CRUID student classes
     */
    fun onCreateDataStudentClass() {
        val studentClass = generateStudentClass()
        val params = mapOf(
            "item" to studentClass,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataView = find<CreateDataFragment>(params)
        createDataView.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataView.savedItem != null) {
            view.currentTimetable.studentClasses.add(createDataView.savedItem as StudentClass)
        }
    }

    fun onUpdateDataStudentClass() {
        val selectedStudentClass = view.tvStudentClass.selectedItem
        val params = mapOf(
            "item" to selectedStudentClass,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataFragment = find<CreateDataFragment>(params)
        createDataFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataFragment.savedItem != null) {
            view.currentTimetable.studentClasses.remove(selectedStudentClass)
            view.currentTimetable.studentClasses.add(createDataFragment.savedItem as StudentClass)
            clearInterface()
            view.setupInterface()
            view.paneGlobal.selectionModel.select(1)
        }
    }

    fun onDeleteDataStudentClass() {
        if (view.currentTimetable.studentClasses.size == 1) {
            alert(Alert.AlertType.ERROR, "Ошибка удаления", "Нельзя удалить все классы")
            return
        }
        val selectedStudentClass = view.tvStudentClass.selectedItem
        if (view.currentTimetable.studentClasses.contains(selectedStudentClass)) {
            view.currentTimetable.studentClasses.remove(selectedStudentClass)
            view.currentTimetable.lessons.removeAll { it.studentClass == selectedStudentClass }
            clearInterface()
            view.setupInterface()
            view.tvStudentClass.selectionModel.select(view.currentTimetable.studentClasses.firstOrNull1())
            view.paneGlobal.selectionModel.select(1)
        }
    }

    /**
     * CRUID subjects
     */
    fun onCreateDataSubject() {
        val subject = generateSubject()
        val params = mapOf(
            "item" to subject,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataView = find<CreateDataFragment>(params)
        createDataView.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataView.savedItem != null) {
            view.currentTimetable.subjects.add(createDataView.savedItem as Subject)
        }
    }

    fun onUpdateDataSubject() {
        val selectedSubject = view.tvSubject.selectedItem
        val params = mapOf(
            "item" to selectedSubject,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataFragment = find<CreateDataFragment>(params)
        createDataFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataFragment.savedItem != null) {
            view.currentTimetable.subjects.remove(selectedSubject)
            view.currentTimetable.subjects.add(createDataFragment.savedItem as Subject)
            clearInterface()
            view.setupInterface()
            view.paneGlobal.selectionModel.select(1)
        }
    }

    fun onDeleteDataSubject() {
        if (view.currentTimetable.subjects.size == 1) {
            alert(Alert.AlertType.ERROR, "Ошибка удаления", "Нельзя удалить все предметы")
            return
        }
        val selectedSubject = view.tvSubject.selectedItem
        if (view.currentTimetable.subjects.contains(selectedSubject)) {
            view.currentTimetable.subjects.remove(selectedSubject)
            view.currentTimetable.lessons.removeAll { it.subject == selectedSubject }
            clearInterface()
            view.setupInterface()
            view.tvSubject.selectionModel.select(view.currentTimetable.subjects.firstOrNull1())
            view.paneGlobal.selectionModel.select(1)
        }
    }

    /**
     * CRUID teachers
     */
    fun onCreateDataTeacher() {
        val classroom = generateClassroom()
        val params = mapOf(
            "item" to classroom,
            "subjects" to view.currentTimetable.subjects
        )
        val createDataView = find<CreateDataFragment>(params)
        createDataView.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataView.savedItem != null) {
            view.currentTimetable.teachers.add(createDataView.savedItem as Teacher)
        }
    }

    fun onUpdateDataTeacher() {
        val selectedTeacher = view.tvTeacher.selectedItem
        val params = mapOf(
            "item" to selectedTeacher,
            "subjects" to emptyList<Subject>().toMutableList()
        )
        val createDataFragment = find<CreateDataFragment>(params)
        createDataFragment.openModal(
            owner = view.currentWindow,
            block = true
        )
        if (createDataFragment.savedItem != null) {
            view.currentTimetable.teachers.remove(selectedTeacher)
            view.currentTimetable.teachers.add(createDataFragment.savedItem as Teacher)
            clearInterface()
            view.setupInterface()
            view.paneGlobal.selectionModel.select(1)
        }
    }

    fun onDeleteDataTeacher() {
        if (view.currentTimetable.teachers.size == 1) {
            alert(Alert.AlertType.ERROR, "Ошибка удаления", "Нельзя удалить всех учителей")
            return
        }
        val selectedTeacher = view.tvTeacher.selectedItem
        if (view.currentTimetable.teachers.contains(selectedTeacher)) {
            view.currentTimetable.teachers.remove(selectedTeacher)
            view.currentTimetable.lessons.removeAll { it.teacher == selectedTeacher }
            clearInterface()
            view.setupInterface()
            view.tvTeacher.selectionModel.select(view.currentTimetable.teachers.firstOrNull1())
            view.paneGlobal.selectionModel.select(1)
        }
    }
}