package gui.controls

import classes.Classroom
import classes.Subject
import classes.Teacher
import javafx.scene.control.ListView

open class ItemBox(
    var availableSubjects: MutableList<Subject>,
    var availableTeachers: MutableList<Teacher>,
    var availableClassroom: MutableList<Classroom>
) : ListView<TimetableCell>() {
    init {
        for (lesson in availableSubjects) {
            items.add(TimetableCell(lesson))
        }
    }
}