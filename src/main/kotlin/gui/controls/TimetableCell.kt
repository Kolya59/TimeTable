package gui.controls

import classes.*
import gui.controls.TimetableCell.CellType.*
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.add

/**
 * TimetableGrid cell control
 * @param[classroom] Selected classroom
 * @param[subject] Selected subject
 * @param[lesson] Selected lesson
 * @param[studentClass] Selected student class
 * @param[teacher] Selected teacher
 * @param[otherInfo] Other info
 */
class TimetableCell() : VBox() {
    var classroom: Classroom? = null
    var lesson: Lesson? = null
    var subject: Subject? = null
    var studentClass: StudentClass? = null
    var teacher: Teacher? = null
    var otherInfo: Any? = null

    private var lClassroom: Label? = null
    private var lSubject: Label? = null
    private var lStudentClass: Label? = null
    private var lTeacher: Label? = null
    private var lOtherInfo: Label? = null

    /**
     * Types of cell content
     */
    enum class CellType { CLASSROOM, LESSON, SUBJECT, STUDENT_CLASS, TEACHER, OTHER }

    /**
     * Computing type of cell
     * @param[classroom] Selected classroom
     * @param[subject] Selected subject
     * @param[studentClass] Selected student class
     * @param[teacher] Selected teacher
     * @param[otherInfo] Other info
     */
    fun computeCellType(
        classroom: Classroom?,
        lesson: Lesson?,
        subject: Subject?,
        studentClass: StudentClass?,
        teacher: Teacher?,
        otherInfo: Any?
    ): CellType {
        if (classroom != null) return CLASSROOM
        if (lesson != null) return LESSON
        if (subject != null) return SUBJECT
        if (studentClass != null) return STUDENT_CLASS
        if (teacher != null) return TEACHER
        if (otherInfo != null) return OTHER
        return LESSON
    }

    var cellType: CellType = OTHER

    // Constructors
    /**
     * TimetableGrid cell control
     * @param[classroom] Selected classroom
     */
    constructor(classroom: Classroom) : this() {
        this.classroom = classroom; fillData()
    }

    /**
     * TimetableGrid cell control
     * @param[subject] Selected subject
     */
    constructor(lesson: Lesson) : this() {
        this.lesson = lesson; fillData()
    }

    /**
     * TimetableGrid cell control
     * @param[lesson] Selected lesson
     */
    constructor(subject: Subject) : this() {
        this.subject = subject; fillData()
    }

    /**
     * TimetableGrid cell control
     * @param[studentClass] Selected student class
     */
    constructor(studentClass: StudentClass?) : this() {
        this.studentClass = studentClass; fillData()
    }

    /**
     * TimetableGrid cell control
     * @param[teacher] Selected teacher
     */
    constructor(teacher: Teacher?) : this() {
        this.teacher = teacher; fillData()
    }

    /**
     * TimetableGrid cell control
     * @param[otherInfo] Other info
     */
    constructor(otherInfo: Any) : this() {
        this.otherInfo = otherInfo; fillData()
    }

    init {
        background = Background(
            BackgroundFill(
                Color.AQUA,
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        )
    }

    private fun fillData() {
        // Классификация ячейки
        cellType = computeCellType(classroom, lesson, subject, studentClass, teacher, otherInfo)

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
            OTHER -> {
                lOtherInfo = if (otherInfo is Label)
                    otherInfo as Label
                else
                    Label(otherInfo.toString())
            }

        }
    }

    fun fill(background: Background) {
        this.background = background
    }

    fun getItem(): Any? {
        when (cellType) {
            CLASSROOM -> return classroom
            LESSON -> return lesson
            SUBJECT -> return subject
            STUDENT_CLASS -> return studentClass
            TEACHER -> return teacher
            OTHER -> return otherInfo
            else -> return null
        }
    }

    fun setItem(value: Any) {
        when (value) {
            is Classroom -> {
                classroom = value
                lClassroom?.text = value.name
            }
            is Lesson -> {
                lesson = value
                lClassroom?.text = value.classroom?.name

            }
            is Subject -> {
                subject = value
                lSubject?.text = value.name
            }
            is StudentClass -> {
                studentClass = value
                lStudentClass?.text = value.name
            }
            is Teacher -> {
                teacher = value
                lTeacher?.text = value.name
            }
            else -> {
                otherInfo = value
                lOtherInfo?.text = value.toString()
            }
        }
    }
}
