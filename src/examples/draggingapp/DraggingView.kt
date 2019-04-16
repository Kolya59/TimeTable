package draggingapp

import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.effect.DropShadow
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*

class DraggingView : View("Dragging App") {

    private val RECTANGLE_HEIGHT = 50.0
    private val RECTANGLE_WIDTH = 50.0

    private var draggingColor: Color? = null

    private val toolboxItems = mutableListOf<Node>()

    private var inflightRect: Rectangle by singleAssign()

    private var toolbox: Parent by singleAssign()

    private var workArea: Pane by singleAssign()

    override val root = hbox {

        addClass(DraggingStyles.wrapper)

        toolbox = vbox {

            fun createToolboxItem(c: Color): Rectangle {
                return rectangle(width = RECTANGLE_WIDTH, height = RECTANGLE_HEIGHT) {
                    fill = c
                    properties["rectColor"] = c
                    addClass(DraggingStyles.toolboxItem)
                }
            }

            add(createToolboxItem(Color.RED))
            add(createToolboxItem(Color.BLUE))
            add(createToolboxItem(Color.YELLOW))

            spacing = 10.0
            padding = Insets(10.0)
            alignment = Pos.CENTER

            hboxConstraints {
                hgrow = Priority.NEVER
            }
        }
        anchorpane {
            workArea = pane {

                addClass(DraggingStyles.workArea)

                anchorpaneConstraints {
                    leftAnchor = 0.0
                    topAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }

                inflightRect = rectangle(0, 0, RECTANGLE_WIDTH, RECTANGLE_HEIGHT) {
                    isVisible = false
                    opacity = 0.7
                    effect = DropShadow()
                }

                add(inflightRect)
            }

            hboxConstraints {
                hgrow = Priority.ALWAYS
            }

        }
        vboxConstraints {
            vgrow = Priority.ALWAYS
        }

        padding = Insets(10.0)
        spacing = 10.0

        addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
        addEventFilter(MouseEvent.MOUSE_DRAGGED, ::animateDrag)
        addEventFilter(MouseEvent.MOUSE_EXITED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::stopDrag)
        addEventFilter(MouseEvent.MOUSE_RELEASED, ::drop)

    }

    init {
        toolboxItems.addAll(toolbox.childrenUnmodifiable)
    }

    private fun startDrag(evt: MouseEvent) {

        toolboxItems
            .filter {
                val mousePt: Point2D = it.sceneToLocal(evt.sceneX, evt.sceneY)
                it.contains(mousePt)
            }
            .firstOrNull()
            .apply {
                if (this != null) {
                    draggingColor = this.properties["rectColor"] as Color
                }
            }

    }

    private fun animateDrag(evt: MouseEvent) {

        val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (workArea.contains(mousePt)) {

            // highlight the drop target (hover doesn't work)
            if (!workArea.hasClass(DraggingStyles.workAreaSelected)) {
                workArea.addClass(DraggingStyles.workAreaSelected)
            }

            // animate a rectangle so that the user can follow
            if (!inflightRect.isVisible) {
                inflightRect.isVisible = true
                inflightRect.fill = draggingColor
            }

            inflightRect.relocate(mousePt.x, mousePt.y)
        }

    }

    private fun stopDrag(evt: MouseEvent) {
        if (workArea.hasClass(DraggingStyles.workAreaSelected)) {
            workArea.removeClass(DraggingStyles.workAreaSelected)
        }
        if (inflightRect.isVisible) {
            inflightRect.isVisible = false
        }
    }

    private fun drop(evt: MouseEvent) {

        val mousePt = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (workArea.contains(mousePt)) {
            if (draggingColor != null) {
                val newRect = Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, draggingColor)
                workArea.add(newRect)
                newRect.relocate(mousePt.x, mousePt.y)

                inflightRect.toFront() // don't want to move cursor tracking behind added objects
            }
        }

        draggingColor = null
    }
}

class DraggingStyles : Stylesheet() {

    companion object {
        val wrapper by cssclass()
        val toolboxItem by cssclass()
        val workAreaSelected by cssclass()
        val workArea by cssclass()
    }

    init {
        wrapper {
            backgroundColor += Color.WHITE
        }
        workArea {
            backgroundColor += Color.LIGHTGRAY
            borderColor += box(Color.BLACK)
            borderWidth += box(1.px)
        }
        toolboxItem {
            padding = box(4.px)
            stroke = Color.BLACK
            strokeWidth = 1.px
            and(hover) {
                opacity = 0.7
            }
        }
        workAreaSelected {
            borderColor += box(Color.BLACK)
            borderWidth += box(3.px)
        }
    }
}

class DraggingApp : App(DraggingView::class, DraggingStyles::class) {
    override fun createPrimaryScene(view: UIComponent) =
        Scene(view.root, 1280.0, 800.0)
}