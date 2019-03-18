package gui.controls

import classes.Lesson
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.getValue
import tornadofx.setValue

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
