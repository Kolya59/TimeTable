package gui.controls

import classes.Classroom
import classes.Subject
import classes.Teacher
import javafx.scene.control.ListView
import java.io.InvalidClassException

/**
 * Items box for containing timetable cells
 * @param[classrooms] List which contain free classrooms
 * @param[subjects] List which contain free subject
 * @param[teachers] List which contain free teachers
 */
open class ItemBox(
    var classrooms: MutableList<Classroom>,
    var subjects: MutableList<Subject>,
    var teachers: MutableList<Teacher>
) : ListView<TimetableCell>() {
    /**
     * View states
     */
    enum class ViewState { CLASSROOM_VIEW, SUBJECTS_VIEW, TEACHER_VIEW }

    var viewState: ViewState = ViewState.SUBJECTS_VIEW

    init {
    }

    fun changeViewState(targetViewState: ViewState) {
        this.viewState = targetViewState
        this.items.clear()
        when (targetViewState) {
            ViewState.CLASSROOM_VIEW -> fillClassroomInfo()
            ViewState.SUBJECTS_VIEW -> fillSubjectInfo()
            ViewState.TEACHER_VIEW -> fillTeacherInfo()
        }
    }

    private fun fillClassroomInfo() {
        for (classroom in classrooms) {
            this.items.add(TimetableCell(classroom, null, null, null, null))
        }
    }

    private fun fillSubjectInfo() {
        for (subject in subjects) {
            this.items.add(TimetableCell(null, null, subject, null, null))
        }
    }

    private fun fillTeacherInfo() {
        for (teacher in teachers) {
            this.items.add(TimetableCell(null, null, null, null, teacher))
        }
    }

    /**
     * Add object to item box
     * @param[item] Added object
     */
    fun addItem(item: Any) {
        when (item) {
            is Classroom -> classrooms.add(item)
            is Subject -> subjects.add(item)
            is Teacher -> teachers.add(item)
            else -> throw InvalidClassException("Class can't be added to this item box")
        }
        changeViewState(this.viewState)
    }

    /**
     * Remove object from item box
     * @param[item] Removed object
     */
    fun removeItem(item: Any) {
        when (item) {
            is Classroom -> classrooms.remove(item)
            is Subject -> subjects.remove(item)
            is Teacher -> teachers.remove(item)
            else -> throw InvalidClassException("Class can't be added to this item box")
        }
        changeViewState(this.viewState)
    }
}