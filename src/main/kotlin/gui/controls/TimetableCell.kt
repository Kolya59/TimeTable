package gui.controls

import classes.*
import gui.controls.TimetableCell.CellType.*
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.add
import java.io.InvalidClassException

/**
 * TimetableGrid cell control
 * @param[classroom] Selected classroom
 * @param[subject] Selected subject
 * @param[lesson] Selected lesson
 * @param[studentClass] Selected student class
 * @param[teacher] Selected teacher
 */
class TimetableCell(
    internal var classroom: Classroom?,
    internal var lesson: Lesson?,
    internal var subject: Subject?,
    internal var studentClass: StudentClass?,
    internal var teacher: Teacher?
) : VBox() {
    private var lClassroom: Label? = null
    private var lSubject: Label? = null
    private var lStudentClass: Label? = null
    private var lTeacher: Label? = null

    /**
     * Types of cell content
     */
    enum class CellType { CLASSROOM, LESSON, SUBJECT, STUDENT_CLASS, TEACHER }

    /**
     * Computing type of cell
     * @param[classroom] Selected classroom
     * @param[subject] Selected subject
     * @param[studentClass] Selected student class
     * @param[teacher] Selected teacher
     */
    fun computeCellType(
        classroom: Classroom?,
        lesson: Lesson?,
        subject: Subject?,
        studentClass: StudentClass?,
        teacher: Teacher?
    ): CellType {
        if (classroom != null) return CLASSROOM
        if (lesson != null) return LESSON
        if (subject != null) return SUBJECT
        if (studentClass != null) return STUDENT_CLASS
        if (teacher != null) return TEACHER
        return LESSON
    }

    var cellType: CellType

    init {
        // Классификация ячейки
        cellType = computeCellType(classroom, lesson, subject, studentClass, teacher)

        when (cellType) {
            CLASSROOM -> {
                lClassroom = Label(classroom?.name)
                this.add(lClassroom!!)
            }
            LESSON -> {
                lClassroom = Label(lesson?.classroom?.name)
                lSubject = Label(lesson?.subject?.name)
                lStudentClass = Label(lesson?.studentClass?.name)
                lTeacher = Label(lesson?.teacher?.name)

                this.add(lClassroom!!)
                this.add(lSubject!!)
                this.add(lStudentClass!!)
                this.add(lTeacher!!)
            }
            SUBJECT -> {
                lSubject = Label(subject?.name)
                this.add(lSubject!!)
            }
            STUDENT_CLASS -> {
                lStudentClass = Label(studentClass?.name)
                this.add(lStudentClass!!)
            }
            TEACHER -> {
                lTeacher = Label(teacher?.name)
                this.add(lTeacher!!)
            }
        }
    }

    fun getItem(): Any? {
        when (cellType) {
            CLASSROOM -> return classroom
            LESSON -> return lesson
            SUBJECT -> return subject
            STUDENT_CLASS -> return studentClass
            TEACHER -> return teacher
            else -> return null
        }
    }

    fun setItem(value: Any) {
        when (value) {
            is Classroom -> classroom = value
            is Lesson -> lesson = value
            is Subject -> subject = value
            is StudentClass -> studentClass = value
            is Teacher -> teacher = value
            else -> throw InvalidClassException("Class can't be set in timetable cell")
        }
    }
}
