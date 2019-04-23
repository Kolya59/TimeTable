package gui

import classes.Classroom
import classes.StudentClass
import classes.Subject
import classes.Teacher
import tornadofx.*

class CreateDataFragment : Fragment("Добавить элемент") {
    enum class DataState { CLASSROOM, STUDENT_CLASS, SUBJECT, TEACHER, OTHER }

    private var item: Any = params.getValue("item")!!
    var savedItem: Any? = null
    private var subjects: MutableList<Subject> = params.getValue("subjects") as MutableList<Subject>

    private var state: DataState

    override val root = vbox()

    init {
        state = detectState()
        setupInterface()
    }

    private fun detectState(): DataState {
        return when (item) {
            is Classroom -> DataState.CLASSROOM
            is StudentClass -> DataState.STUDENT_CLASS
            is Subject -> DataState.SUBJECT
            is Teacher -> DataState.TEACHER
            else -> DataState.OTHER
        }
    }

    private fun setupInterface() {
        with(root) {
            when (state) {
                DataState.CLASSROOM -> {
                    borderpane {
                        left = label("Название")
                        right = textfield {
                            text = (item as Classroom).name
                            action {
                                (item as Classroom).name = text
                            }
                        }
                    }
                }
                DataState.STUDENT_CLASS -> {
                    borderpane {
                        left = label("Название")
                        right = textfield {
                            text = (item as StudentClass).name
                            action {
                                (item as StudentClass).name = text
                            }
                        }
                    }
                }
                DataState.SUBJECT -> {
                    borderpane {
                        left = label("Название")
                        right = textfield {
                            text = (item as Subject).name
                            action {
                                (item as Subject).name = text
                            }
                        }
                    }
                }
                DataState.TEACHER -> {
                    vbox {
                        borderpane {
                            left = label("Имя")
                            right = textfield {
                                action {
                                    (item as Teacher).name = text
                                }
                            }
                        }
                        borderpane {
                            left = label("Уроки")
                            right {
                                for (subject in subjects) {
                                    hbox {
                                        label(subject.name)
                                        checkbox {
                                            isSelected = (item as Teacher).availableSubjects.contains(subject)
                                            action {
                                                if (isSelected)
                                                    (item as Teacher).availableSubjects.add(subject)
                                                else
                                                    (item as Teacher).availableSubjects.remove(subject)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                DataState.OTHER -> {
                }
            }
            hbox {
                button {
                    text = "Сохранить"
                    action {
                        savedItem = item
                        close()
                    }
                }
                button {
                    text = "Отменить"
                    action {
                        close()
                    }
                }
            }
        }
    }
}
