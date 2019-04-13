package gui.controls

import classes.Classroom
import classes.StudentClass
import classes.Subject
import classes.Teacher
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.add

/**
 * TimetableGrid cell control
 * @param[classroom] Selected classroom
 * @param[subject] Selected subject
 * @param[studentClass] Selected student class
 * @param[teacher] Selected teacher
 */
class TimetableCell(
    internal var classroom: Classroom?,
    internal var subject: Subject?,
    internal var studentClass: StudentClass?,
    internal var teacher: Teacher?
) : VBox() {
    var lClassroom: Label?
    var lSubject: Label?
    var lStudentClass: Label?
    var lTeacher: Label?

    init {
        // Добавление кабинета
        if (classroom != null) {
            lClassroom = Label(classroom?.name)
            this.add(lClassroom!!)
        } else {
            lClassroom = null
        }

        // Добавление предмета
        if (subject != null) {
            lSubject = Label(subject?.name)
            this.add(lSubject!!)
        } else {
            lSubject = null
        }

        // Добавление класса
        if (studentClass != null) {
            lStudentClass = Label(studentClass?.name)
            this.add(lStudentClass!!)
        } else {
            lStudentClass = null
        }

        // Добавление учителя
        if (teacher != null) {
            lTeacher = Label(teacher?.name)
            this.add(lTeacher!!)
        } else {
            lTeacher = null
        }
    }
}
