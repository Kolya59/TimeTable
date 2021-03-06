package gui.controls

import classes.Classroom
import classes.Subject
import classes.Teacher
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import java.io.InvalidClassException

/**
 * Items box for containing timetable cells
 * @param[classrooms] List which contain free classrooms
 * @param[subjects] List which contain free subject
 * @param[teachers] List which contain free teachers
 */
open class ItemBox(
    private var classrooms: MutableList<Classroom>,
    private var subjects: MutableList<Subject>,
    private var teachers: MutableList<Teacher>
) : ListView<TimetableCell>() {
    /**
     * View states
     */
    enum class ViewState { CLASSROOM_VIEW, SUBJECTS_VIEW, TEACHER_VIEW }

    private var viewState: ViewState = ViewState.SUBJECTS_VIEW

    init {
        changeViewState(ViewState.SUBJECTS_VIEW)
        orientation = Orientation.VERTICAL
        super.setItems(items)
    }

    private fun changeViewState(targetViewState: ViewState) {
        viewState = targetViewState
        items.clear()
        when (targetViewState) {
            ViewState.CLASSROOM_VIEW -> fillClassroomInfo()
            ViewState.SUBJECTS_VIEW -> fillSubjectInfo()
            ViewState.TEACHER_VIEW -> fillTeacherInfo()
        }
    }

    private fun fillClassroomInfo() {
        for (classroom in classrooms) {
            items.add(TimetableCell(classroom))
        }
        children.setAll(items)
    }

    private fun fillSubjectInfo() {
        for (subject in subjects) {
            items.add(TimetableCell(subject))
        }
        children.setAll(items)
    }

    private fun fillTeacherInfo() {
        for (teacher in teachers) {
            items.add(TimetableCell(teacher = teacher))
        }
        children.setAll(items)
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
        changeViewState(viewState)
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
        changeViewState(viewState)
    }
}