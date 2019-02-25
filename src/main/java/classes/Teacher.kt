package classes

open class Teacher constructor(id: Short, name: String, availableSubjects: Set<Subject>) {
    /**
     * School teacher
     */

    // Teacher id
    protected val id: Short = id
    // Teacher name
    protected var name: String = name
    // Subjects this teacher can teach
    protected var availableSubjects: Set<Subject> = availableSubjects
}