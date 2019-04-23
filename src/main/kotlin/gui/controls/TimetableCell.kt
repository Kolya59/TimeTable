package gui.controls

import TimetableStyleSheet
import classes.*
import gui.controls.TimetableCell.CellType.*
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.VBox
import tornadofx.add
import tornadofx.addClass
import tornadofx.removeClass

/**
 * TimetableGrid cell control
 */
class TimetableCell() : VBox() {
    var classroom: Classroom? = null
    var lesson: Lesson? = null
    var subject: Subject? = null
    private var studentClass: StudentClass? = null
    var teacher: Teacher? = null
    private var otherInfo: Any? = null

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
    private fun computeCellType(
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
     * @param[lesson] Selected lesson
     */
    constructor(lesson: Lesson) : this() {
        this.lesson = lesson
        fillData()
        if (lesson.pinned) {
            this.addClass(TimetableStyleSheet.pinnedCell)
        } else {
            this.removeClass(TimetableStyleSheet.pinnedCell)
        }
    }

    /**
     * TimetableGrid cell control
     * @param[subject] Selected subject
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

    private fun fillData() {
        // Классификация ячейки
        cellType = computeCellType(classroom, lesson, subject, studentClass, teacher, otherInfo)

        when (cellType) {
            CLASSROOM -> {
                lClassroom = Label(classroom?.name)
                this.add(lClassroom!!)
                children[0] = lClassroom
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

                children.setAll(lClassroom, lSubject, lStudentClass, lTeacher)
            }
            SUBJECT -> {
                lSubject = Label(subject?.name)
                this.add(lSubject!!)
                children[0] = lSubject
            }
            STUDENT_CLASS -> {
                lStudentClass = Label(studentClass?.name)
                this.add(lStudentClass!!)
                children[0] = lStudentClass
            }
            TEACHER -> {
                lTeacher = Label(teacher?.name)
                this.add(lTeacher!!)
                children[0] = lTeacher
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
        return when (cellType) {
            CLASSROOM -> classroom
            LESSON -> lesson
            SUBJECT -> subject
            STUDENT_CLASS -> studentClass
            TEACHER -> teacher
            OTHER -> otherInfo
            else -> null
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
