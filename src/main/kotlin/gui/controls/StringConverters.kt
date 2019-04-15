package gui.controls

import classes.Classroom
import classes.StudentClass
import classes.Subject
import classes.Teacher
import javafx.util.StringConverter

class scClassroom() : StringConverter<Classroom>() {
    override fun toString(`object`: Classroom?): String {
        return `object`?.name.toString()
    }

    override fun fromString(string: String?): Classroom {
        return Classroom(0, string.toString())
    }
}

class scStudentClass() : StringConverter<StudentClass>() {
    override fun toString(`object`: StudentClass?): String {
        return `object`?.name.toString()
    }

    override fun fromString(string: String?): StudentClass {
        return StudentClass(0, string.toString())
    }
}

class scSubject() : StringConverter<Subject>() {
    override fun toString(`object`: Subject?): String {
        return `object`?.name.toString()
    }

    override fun fromString(string: String?): Subject {
        return Subject(0, string.toString())
    }
}

class scTeacher() : StringConverter<Teacher>() {
    override fun toString(`object`: Teacher?): String {
        return `object`?.name.toString()
    }

    override fun fromString(string: String?): Teacher {
        return Teacher(0, string.toString(), emptySet())
    }
}