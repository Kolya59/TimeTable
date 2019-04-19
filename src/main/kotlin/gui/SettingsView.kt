package gui

import javafx.event.EventHandler
import javafx.scene.layout.VBox
import tornadofx.*

// TODO Создать меню настроек
class SettingsView : View("Настройки интерфейса") {
    val controller: SettingsController by inject()
    override val root = VBox()

    private var marginTopBottom: Double
    private var marginLeftRight: Double
//    var currentStyleSheet: Stylesheet

    init {
//        currentStyleSheet = params["stylesheet"] as Stylesheet
        marginTopBottom = 5.0
        marginLeftRight = 10.0
        setupInterface()
    }

    private fun setupInterface() {
        with(root) {
            borderpane {
                left = label("Размер шрифта")
                right = textfield {
                    filterInput { it.controlNewText.isInt() }
                    text = "12"
                    action {
                        controller.onFontSizeChange()
                    }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет шрифта заголовков")
                right = colorpicker {
                    onAction = EventHandler { controller.onHeaderFontColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет шрифта заполненных ячеек")
                right = colorpicker {
                    onAction = EventHandler { controller.onFilledCellFontColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет шрифта пустых ячеек")
                right = colorpicker {
                    onAction = EventHandler { controller.onEmptyCellFontColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет фона заголовков расписания")
                right = colorpicker {
                    onAction = EventHandler { controller.onBackgroundColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет фона заполенных ячеек")
                right = colorpicker {
                    onAction = EventHandler { controller.onFilledCellBackgroundColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            borderpane {
                left = label("Цвет фона пустых ячеек")
                right = colorpicker {
                    onAction = EventHandler { controller.onEmptyCellBackgroundColorChange() }
                }

                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            hbox {
                button {
                    text = "Сохранить"
                    action {
                        controller.onSave()
                    }
                }
                button {
                    text = "Отмена"
                    action {
                        controller.onCancel();
                    }
                }
                vboxConstraints {
                    marginTopBottom(marginTopBottom)
                    marginLeftRight(marginLeftRight)
                }
            }

            spacing = 5.0
        }
    }

}

class SettingsController: Controller() {
    val view: SettingsView by inject()

    fun onFontSizeChange() {

    }

    fun onHeaderFontColorChange() {

    }

    fun onFilledCellFontColorChange() {

    }

    fun onEmptyCellFontColorChange() {

    }

    fun onBackgroundColorChange() {

    }

    fun onFilledCellBackgroundColorChange() {

    }

    fun onEmptyCellBackgroundColorChange() {

    }

    fun onSave() {

    }

    fun onCancel() {
        view.close()
    }
}