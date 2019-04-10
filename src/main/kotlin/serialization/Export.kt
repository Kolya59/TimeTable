package serialization

import classes.Classroom
import classes.StudentClass
import classes.Teacher
import classes.TimeTable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Non-fields class for export
 */
open class Export {
    /**
     * Export TimetableGrid to file
     * @param[path] Path to selected file
     * @param[obj] Exported TimetableGrid
     */
    open class ExportTimetable(private val path: String, private val obj: TimeTable) {
        /**
         * Export to JSON
         * @return Export result
         */
        fun toJSON(): Boolean {
            val file = File(this.path)
            val jsonString: String = Json.unquoted.stringify(TimeTable.serializer(), obj)
            file.writeText(jsonString)
            return true
        }
    }

    /**
     * Export Classrooms to file
     * @param[path] Path to selected file
     */
    open class ExportClassroom(private val path: String, private val obj: Classroom) {
        /**
         * Export to JSON
         * @return Export result
         */
        fun toJSON(): Boolean {
            val file = File(this.path)
            val jsonString: String = Json.unquoted.stringify(Classroom.serializer(), obj)
            file.writeText(jsonString)
            return true
        }
    }

    /**
     * Export Student class to file
     * @param[path] Path to selected file
     */
    open class ExportStudentClass(private val path: String, private val obj: StudentClass) {
        /**
         * Export to JSON
         * @return Export result
         */
        fun toJSON(): Boolean {
            val file = File(this.path)
            val jsonString: String = Json.unquoted.stringify(StudentClass.serializer(), obj)
            file.writeText(jsonString)
            return true
        }
    }

    /**
     * Export Teacher to file
     * @param[path] Path to selected file
     */
    open class ExportTeacher(private val path: String, private val obj: Teacher) {
        /**
         * Export to JSON
         * @return Export result
         */
        fun toJSON(): Boolean {
            val file = File(this.path)
            val jsonString: String = Json.unquoted.stringify(Teacher.serializer(), obj)
            file.writeText(jsonString)
            return true
        }
    }
}