package gui

import classes.*
import gui.MainView.ViewState
import gui.controls.ScClassroom
import gui.controls.ScStudentClass
import gui.controls.ScSubject
import gui.controls.ScTeacher
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.*

class EditCellFragment : Fragment("Редактрование даннных об уроке") {
    // Selected lesson
    lateinit var lesson: Lesson

    // Selected fields
    private lateinit var selectedClassroom: Classroom
    private lateinit var selectedStudentClass: StudentClass
    private lateinit var selectedSubject: Subject
    private lateinit var selectedTeacher: Teacher

    // Available values
    private var classrooms: MutableList<Classroom> = emptyList<Classroom>().toMutableList()
    private var studentClasses: MutableList<StudentClass> = emptyList<StudentClass>().toMutableList()
    private var subjects: MutableList<Subject> = emptyList<Subject>().toMutableList()
    private var teachers: MutableList<Teacher> = emptyList<Teacher>().toMutableList()
    private var pinnedFlag: Boolean = false

    // Controls
    private lateinit var cbClassrooms: ComboBox<Classroom>
    private lateinit var cbStudentClasses: ComboBox<StudentClass>
    private lateinit var cbSubjects: ComboBox<Subject>
    private lateinit var cbTeachers: ComboBox<Teacher>
    lateinit var chbPinned: CheckBox

    private lateinit var viewState: ViewState

    override val root = VBox()

    init {
        setupCollections()
        setupInterface()
    }

    private fun setupCollections() {
        viewState = params["state"] as ViewState

        // Заполнение коллекций
        classrooms = (params["classrooms"] as MutableList<Classroom>).toMutableList()
        studentClasses = (params["studentClasses"] as MutableList<StudentClass>).toMutableList()
        subjects = (params["subjects"] as MutableList<Subject>).toMutableList()
        teachers = (params["teachers"] as MutableList<Teacher>).toMutableList()
        pinnedFlag = params["pinned"] as Boolean

        // Заполнение данных об уроке
        lesson = params["lesson"] as Lesson
    }

    private fun setupInterface() {
        with(root) {
            // String converters
            val scClassroom = ScClassroom()
            val scStudentClass = ScStudentClass()
            val scSubject = ScSubject()
            val scTeacher = ScTeacher()

            when (viewState) {
                ViewState.STUDENT_CLASS_VIEW -> {
                    selectedStudentClass = lesson.studentClass!!
                }
                ViewState.CLASSROOM_VIEW -> {
                    selectedClassroom = lesson.classroom!!
                }
                ViewState.TEACHER_VIEW -> {
                    selectedTeacher = lesson.teacher!!
                }
            }

            if (classrooms.isNotEmpty() && viewState != ViewState.CLASSROOM_VIEW) {
                cbClassrooms = combobox {
                    items.addAll(classrooms)
                    selectionModel.select(if (lesson.classroom != null) lesson.classroom else classrooms.first())
                    converter = scClassroom
                    onAction = EventHandler {
                        selectedClassroom = selectedItem as Classroom
                    }
                }
                selectedClassroom = cbClassrooms.selectedItem as Classroom
            } else {
                Label(lesson.classroom?.name)
            }

            if (studentClasses.isNotEmpty() && viewState != ViewState.STUDENT_CLASS_VIEW) {
                cbStudentClasses = combobox {
                    items.addAll(studentClasses)
                    selectionModel.select(if (lesson.studentClass != null) lesson.studentClass else studentClasses.first())
                    converter = scStudentClass
                    onAction = EventHandler {
                        selectedStudentClass = selectedItem as StudentClass
                    }
                }
                selectedStudentClass = cbStudentClasses.selectedItem as StudentClass
            } else {
                Label(lesson.studentClass?.name)
            }

            if (subjects.isNotEmpty()) {
                cbSubjects = combobox {
                    items.addAll(subjects)
                    selectionModel.select(if (lesson.subject != null) lesson.subject else subjects.first())
                    converter = scSubject
                    onAction = EventHandler {
                        selectedSubject = selectionModel.selectedItem as Subject
                    }
                }
                selectedSubject = cbSubjects.selectionModel.selectedItem as Subject
            } else {
                Label(lesson.subject?.name)
            }

            if (teachers.isNotEmpty() && viewState != ViewState.TEACHER_VIEW) {
                cbTeachers = combobox {
                    // TODO Отфильтровать учителей
                    items.addAll(teachers)
                    selectionModel.select(if (lesson.teacher != null) lesson.teacher else teachers.first())
                    converter = scTeacher
                    onAction = EventHandler {
                        selectedTeacher = selectionModel.selectedItem as Teacher
                    }
                }
                selectedTeacher = cbTeachers.selectionModel.selectedItem as Teacher
            } else {
                Label(lesson.teacher?.name)
            }

            hbox {
                label("Закрепить")
                chbPinned = checkbox {
                    isSelected = pinnedFlag
                    action {
                        pinnedFlag = isSelected
                    }
                }
            }

            hbox {
                button {
                    text = "Сохранить"
                    action {
                        lesson = Lesson(
                            lesson.id,
                            selectedSubject,
                            selectedTeacher,
                            selectedClassroom,
                            selectedStudentClass,
                            lesson.number,
                            lesson.day,
                            lesson.pinned
                        )
                        close()
                    }
                }
                button {
                    text = "Отмена"
                    action { close() }
                }
            }
        }
    }
}
