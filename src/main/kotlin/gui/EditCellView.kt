package gui

import classes.*
import javafx.collections.ObservableList
import javafx.scene.control.Label
import tornadofx.Controller
import tornadofx.View
import tornadofx.combobox
import tornadofx.vbox

class EditCellView() : View("Редактрование даннных об уроке") {
    val controller: EditController by inject()

    // Selected lesson
    var lesson: Lesson

    // Selected fields
    lateinit var selectedClassroom: Classroom
    lateinit var selectedStudentClass: StudentClass
    lateinit var selectedSubject: Subject
    lateinit var selectedTeacher: Teacher

    // Available values
    val classrooms: MutableList<Classroom> = emptyList<Classroom>() as MutableList<Classroom>
    val studentClasses: MutableList<StudentClass> = emptyList<StudentClass>() as MutableList<StudentClass>
    val subjects: MutableList<Subject> = emptyList<Subject>() as MutableList<Subject>
    val teachers: MutableList<Teacher> = emptyList<Teacher>() as MutableList<Teacher>

    init {
        // Заполнение коллекций
        (params["classrooms"] as MutableList<Classroom>).map { it -> classrooms.add(it) }
        (params["studentClasses"] as MutableList<StudentClass>).map { it -> studentClasses.add(it) }
        (params["subjects"] as MutableList<Subject>).map { it -> subjects.add(it) }
        (params["teachers"] as MutableList<Teacher>).map { it -> teachers.add(it) }

        // Запонение данных об уроке
        lesson = params["lesson"] as Lesson
    }

    override val root = vbox {
        if (classrooms.isEmpty())
            combobox<Any> {
                items = classrooms as ObservableList<Any>
            }
        else
            Label(lesson.classroom?.name)

        if (studentClasses.isEmpty())
            combobox<Any> {
                items = studentClasses as ObservableList<Any>
            }
        else
            Label(lesson.studentClass?.name)

        if (subjects.isEmpty())
            combobox<Any> {
                items = subjects as ObservableList<Any>
            }
        else
            Label(lesson.subject?.name)

        if (teachers.isEmpty())
            combobox<Any> {
                items = teachers as ObservableList<Any>
            }
        else
            Label(lesson.teacher?.name)

    }
}

// TODO Обработчики событий
class EditController : Controller() {

}
