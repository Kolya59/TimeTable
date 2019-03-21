package gui.controls

import classes.Lesson
import javafx.scene.control.Label
import javafx.scene.layout.VBox

/**
 * Timetable cell control
 * @param[lesson] Lesson
 */
class timetableCell(internal var lesson: Lesson) : VBox() {
    var lSubject : Label = Label(lesson.subject.name)
    var lStudentClass : Label = Label(lesson.studentClass.name)
    var lClassroom : Label = Label(lesson.classroom.name)

    init {
        super.getChildren().setAll(lSubject, lStudentClass, lClassroom)
    }
}
