package gui

import classes.*
import gui.controls.scClassroom
import gui.controls.scStudentClass
import gui.controls.scSubject
import gui.controls.scTeacher
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import tornadofx.*

class EditCellFragment : Fragment("Редактрование даннных об уроке") {
    // Selected lesson
    var lesson: Lesson?

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

    init {
        try {
            // Заполнение коллекций
            classrooms = (params["classrooms"] as MutableList<Classroom>).toMutableList()
            studentClasses = (params["studentClasses"] as MutableList<StudentClass>).toMutableList()
            subjects = (params["subjects"] as MutableList<Subject>).toMutableList()
            teachers = (params["teachers"] as MutableList<Teacher>).toMutableList()

            // Заполнение данных об уроке
            lesson = params["lesson"] as Lesson
        } catch (e: Exception) {
            lesson = null
        }
    }

    override val root = vbox {
        // String converters
        val scClassroom = scClassroom()
        val scStudentClass = scStudentClass()
        val scSubject = scSubject()
        val scTeacher = scTeacher()

        selectedClassroom = lesson!!.classroom!!
        selectedStudentClass = lesson!!.studentClass!!
        selectedSubject = lesson!!.subject!!
        selectedTeacher = lesson!!.teacher!!

        if (!classrooms.isEmpty()) {
            cbClassrooms = combobox<Classroom> {
                items.addAll(classrooms)
                selectionModel.select(lesson!!.classroom)
                converter = scClassroom
                onAction = EventHandler {
                    selectedClassroom = selectedItem as Classroom
                }
            }
        } else {
            Label(lesson!!.classroom?.name)
        }

        if (!studentClasses.isEmpty()) {
            cbStudentClasses = combobox<StudentClass> {
                items.addAll(studentClasses)
                selectionModel.select(lesson!!.studentClass)
                converter = scStudentClass
                onAction = EventHandler {
                    selectedStudentClass = selectedItem as StudentClass
                }
            }
        } else {
            Label(lesson!!.studentClass?.name)
        }

        if (!subjects.isEmpty()) {
            cbSubjects = combobox<Subject> {
                items.addAll(subjects)
                selectionModel.select(lesson!!.subject)
                converter = scSubject
                onAction = EventHandler {
                    selectedSubject = selectionModel.selectedItem as Subject
                }
            }
        } else {
            Label(lesson!!.subject?.name)
        }

        if (!teachers.isEmpty()) {
            cbTeachers = combobox<Teacher> {
                items.addAll(teachers)
                selectionModel.select(lesson!!.teacher)
                converter = scTeacher
                onAction = EventHandler {
                    selectedTeacher = selectionModel.selectedItem as Teacher
                }
            }
        } else {
            Label(lesson!!.teacher?.name)
        }

        hbox {
            button {
                text = "Сохранить"
                action {
                    lesson = Lesson(
                        lesson!!.id,
                        selectedSubject,
                        selectedTeacher,
                        selectedClassroom,
                        selectedStudentClass,
                        lesson!!.number,
                        lesson!!.day
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
