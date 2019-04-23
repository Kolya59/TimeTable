package gui

import tornadofx.Controller
import tornadofx.View
import tornadofx.borderpane

class DeveloperView : View("Меню разработчика") {
    val controller: DeveloperController by inject()
    override val root = borderpane {

    }
}

class DeveloperController : Controller()
