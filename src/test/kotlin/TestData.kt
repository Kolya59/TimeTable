import classes.*

class TestData() {
    /**
     * Test data
     */
    val testSubjects: List<Subject> = listOf(
        Subject(0, "Математика"),
        Subject(1, "Физика"),
        Subject(2, "Русский язык"),
        Subject(3, "История"),
        Subject(4, "Литература")
    )
    val testTeachers: List<Teacher> = listOf(
        Teacher(0, "Иванов Олег Александрович", setOf(testSubjects[0], testSubjects[1])),
        Teacher(1, "Петрова Лидия Александровна", setOf(testSubjects[2], testSubjects[4])),
        Teacher(2, "Алексеев Генадий Александрович", setOf(testSubjects[3], testSubjects[4]))
    )
    val testClassrooms: List<Classroom> = listOf(
        Classroom(0, "237"),
        Classroom(1, "239"),
        Classroom(2, "240")
    )
    val testStudentClasses: List<StudentClass> = listOf(
        StudentClass(0, "5А"),
        StudentClass(1, "6А"),
        StudentClass(2, "7А"),
        StudentClass(3, "8А")
    )
    val testLessons: List<Lesson> = listOf(
        Lesson(0, testSubjects[0], testTeachers[0], testClassrooms[0], testStudentClasses[0]),
        Lesson(1, testSubjects[0], testTeachers[0], testClassrooms[1], testStudentClasses[1]),
        Lesson(2, testSubjects[0], testTeachers[0], testClassrooms[2], testStudentClasses[2]),
        Lesson(3, testSubjects[1], testTeachers[0], testClassrooms[0], testStudentClasses[3]),
        Lesson(4, testSubjects[1], testTeachers[0], testClassrooms[1], testStudentClasses[0]),
        Lesson(5, testSubjects[1], testTeachers[0], testClassrooms[2], testStudentClasses[1]),
        Lesson(6, testSubjects[2], testTeachers[1], testClassrooms[0], testStudentClasses[2]),
        Lesson(7, testSubjects[2], testTeachers[1], testClassrooms[1], testStudentClasses[3]),
        Lesson(8, testSubjects[2], testTeachers[1], testClassrooms[2], testStudentClasses[0]),
        Lesson(9, testSubjects[3], testTeachers[2], testClassrooms[0], testStudentClasses[1]),
        Lesson(10, testSubjects[3], testTeachers[2], testClassrooms[1], testStudentClasses[2]),
        Lesson(11, testSubjects[4], testTeachers[2], testClassrooms[2], testStudentClasses[3])
    )
    val testTimeTable: TimeTable = TimeTable(testLessons.toMutableList())
}