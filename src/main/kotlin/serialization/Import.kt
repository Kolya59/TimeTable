package serialization

import classes.*
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Non-fields class for import
 */
open class Import() {
    /**
     * Import TimetableGrid from file
     * @param[file] Selected file
     */
    open class ImportTimetable(val file: File) {
        /**
         * Import from JSON
         * @return Imported TimetableGrid
         */
        fun fromJSON(): TimeTable {
            return try {
                val jsonString: String = file.readText(Charsets.UTF_8)
                Json.unquoted.parse(TimeTable.serializer(), jsonString)
            } catch (e: Exception) {
                TimeTable(
                    emptyList<Lesson>().toMutableList(),
                    emptyList<Teacher>().toMutableList(),
                    emptyList<Classroom>().toMutableList(),
                    emptyList<StudentClass>().toMutableList(),
                    emptyList<Subject>().toMutableList()
                )
            }
        }
    }

    /**
     * Import Classrooms from file
     * @param[file] Selected file
     */
    open class ImportClassroom(val file: File) {
        /**
         * Import from JSON
         * @return Imported classroom
         */
        fun fromJSON(): Classroom {
            val jsonString: String = file.readText(Charsets.UTF_8)
            return Json.unquoted.parse(Classroom.serializer(), jsonString)
        }
    }

    /**
     * Import Student class from file
     * @param[file] Selected file
     */
    open class ImportStudentClass(val file: File) {
        /**
         * Import from JSON
         * @return Imported student class
         */
        fun fromJSON(): StudentClass {
            val jsonString: String = file.readText(Charsets.UTF_8)
            return Json.unquoted.parse(StudentClass.serializer(), jsonString)
        }
    }

    /**
     * Import Teacher from file
     * @param[file] Selected file
     */
    open class ImportTeacher(val file: File) {
        /**
         * Import from JSON
         * @return Imported teacher
         */
        fun fromJSON(): Teacher {
            val jsonString: String = file.readText(Charsets.UTF_8)
            return Json.unquoted.parse(Teacher.serializer(), jsonString)
        }
    }

}