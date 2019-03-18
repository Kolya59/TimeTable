package gui.develop_menu

import tornadofx.*

class DeveloperView : View("Меню разработчика") {
    val controller: DeveloperController by inject()
    override val root = borderpane {

    }
}

class DeveloperController : Controller() {

}
