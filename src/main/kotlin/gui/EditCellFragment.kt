package gui

import classes.*
import gui.MainView.ViewState
import gui.controls.scClassroom
import gui.controls.scStudentClass
import gui.controls.scSubject
import gui.controls.scTeacher
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.*

class EditCellFragment : Fragment("Редактрование даннных об уроке") {
    // Selected lesson
    lateinit var lesson: Lesson

    // Selected fields
    lateinit var selectedClassroom: Classroom
    lateinit var selectedStudentClass: StudentClass
    lateinit var selectedSubject: Subject
    lateinit var selectedTeacher: Teacher

    // Available values
    var classrooms: MutableList<Classroom> = emptyList<Classroom>().toMutableList()
    var studentClasses: MutableList<StudentClass> = emptyList<StudentClass>().toMutableList()
    var subjects: MutableList<Subject> = emptyList<Subject>().toMutableList()
    var teachers: MutableList<Teacher> = emptyList<Teacher>().toMutableList()

    // Controls
    lateinit var cbClassrooms: ComboBox<Classroom>
    lateinit var cbStudentClasses: ComboBox<StudentClass>
    lateinit var cbSubjects: ComboBox<Subject>
    lateinit var cbTeachers: ComboBox<Teacher>

    lateinit var viewState: ViewState

    override val root = VBox()

    init {
        setupCollections()
        setupInterface()
    }

    fun setupCollections() {
        viewState = params["state"] as ViewState

        // Заполнение коллекций
        classrooms = (params["classrooms"] as MutableList<Classroom>).toMutableList()
        studentClasses = (params["studentClasses"] as MutableList<StudentClass>).toMutableList()
        subjects = (params["subjects"] as MutableList<Subject>).toMutableList()
        teachers = (params["teachers"] as MutableList<Teacher>).toMutableList()

        // Заполнение данных об уроке
        lesson = params["lesson"] as Lesson
    }

    fun setupInterface() {
        with(root) {
            // String converters
            val scClassroom = scClassroom()
            val scStudentClass = scStudentClass()
            val scSubject = scSubject()
            val scTeacher = scTeacher()

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
                            lesson.day
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

    fun setupState(state: ViewState) {
        viewState = state
        setupCollections()
        setupInterface()
    }
}
