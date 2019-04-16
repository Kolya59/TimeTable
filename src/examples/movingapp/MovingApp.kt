package movingapp

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*

class MovingView : View("Moving App") {

    val rectangles = mutableListOf<Rectangle>()

    var selectedRectangle: Rectangle? = null
    var selectedOffset: Point2D? = null

    val positionMessage = SimpleStringProperty("")

    enum class XFormType { NONE, SCALE, ROTATE }

    override val root = vbox {

        anchorpane {
            pane {

                fun createRectangle(startX: Double, f: Color, xform: XFormType = XFormType.NONE): Rectangle {
                    return rectangle(startX, 100.0, 50.0, 50.0) {
                        fill = f
                        stroke = Color.BLACK
                        rectangles.add(this)  // for convenience
                        layoutX = 25.0
                        layoutY = 25.0
                        when (xform) {
                            XFormType.SCALE -> {
                                scaleX = 2.0
                                scaleY = 2.0
                            }
                            XFormType.ROTATE -> {
                                rotate = 45.0
                            }
                        }
                    }
                }

                createRectangle(100.0, Color.BLUE)
                createRectangle(300.0, Color.YELLOW, XFormType.SCALE)
                createRectangle(500.0, Color.GREEN, XFormType.ROTATE)

                anchorpaneConstraints {
                    topAnchor = 0.0
                    bottomAnchor = 0.0
                    rightAnchor = 0.0
                    leftAnchor = 0.0
                }

                addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
                addEventFilter(MouseEvent.MOUSE_DRAGGED, ::drag)
                addEventFilter(MouseEvent.MOUSE_RELEASED, ::endDrag)
            }

            vboxConstraints {
                vgrow = Priority.ALWAYS
            }
        }

        label(positionMessage) {
            padding = Insets(2.0)
        }

        padding = Insets(2.0)
    }

    private fun startDrag(evt: MouseEvent) {

        rectangles
            .filter {
                val mousePt = it.sceneToLocal(evt.sceneX, evt.sceneY)
                it.contains(mousePt)
            }
            .firstOrNull()
            .apply {
                if (this != null) {

                    selectedRectangle = this

                    val mp = this.parent.sceneToLocal(evt.sceneX, evt.sceneY)
                    val vizBounds = this.boundsInParent

                    selectedOffset = Point2D(
                        mp.x - vizBounds.minX - (vizBounds.width - this.boundsInLocal.width) / 2,
                        mp.y - vizBounds.minY - (vizBounds.height - this.boundsInLocal.height) / 2
                    )
                }
            }
    }

    private fun drag(evt: MouseEvent) {

        val mousePt: Point2D = (evt.source as Pane).sceneToLocal(evt.sceneX, evt.sceneY)
        if (selectedRectangle != null && selectedOffset != null) {

            selectedRectangle!!.relocate(
                mousePt.x - selectedOffset!!.x,
                mousePt.y - selectedOffset!!.y
            )

            positionMessage.value =
                "Last Selection: Mouse (${mousePt.x}, ${mousePt.y}) Moving To (${mousePt.x - selectedOffset!!.x}, ${mousePt.y - selectedOffset!!.y})"

        }
    }

    private fun endDrag(evt: MouseEvent) {
        selectedRectangle = null
        selectedOffset = null
    }
}

class MovingApp : App(MovingView::class) {
    override fun createPrimaryScene(view: UIComponent): Scene =
        Scene(view.root, 667.0, 376.0)
}