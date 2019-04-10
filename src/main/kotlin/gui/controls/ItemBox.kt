package gui.controls

import classes.Classroom
import classes.Lesson
import classes.Teacher
import javafx.scene.control.ListView

open class ItemBox(
    var availableLessons: MutableList<Lesson>,
    var availableTeachers: MutableList<Teacher>,
    var availableClassroom: MutableList<Classroom>
) : ListView<TimetableCell>() {
    init {
        for (lesson in availableLessons) {
            items.add(TimetableCell(lesson))
        }
    }
}