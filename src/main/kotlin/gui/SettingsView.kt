package gui

import tornadofx.Controller
import tornadofx.View
import tornadofx.borderpane

// TODO Создать меню настроек
class SettingsView : View("My View") {
    val controller: SettingsController by inject()

    override val root = borderpane {

    }
}

class SettingsController: Controller() {
    val view: SettingsView by inject()

}