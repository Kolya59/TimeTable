package selectingapp

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.lang.Math.abs

/**
 * @author carl
 */

class SelectingView : View("Selecting App") {

    var workArea: Pane by singleAssign()

    val circles = mutableListOf<Circle>()

    val selectedCircles = FXCollections.observableArrayList<String>()

    var lassoRect: Rectangle by singleAssign()

    var initDrag = false
    var dragStart: Point2D? = null

    override val root = hbox {
        anchorpane {

            workArea = pane {

                fun createCircle(centerX: Int, centerY: Int, c: Color, prop: String): Circle {
                    return circle(centerX, centerY, 50) {
                        fill = c
                        addClass(SelectingStyles.circleItem)
                        circles.add(this)
                        properties["circleColor"] = prop
                    }
                }

                addClass(SelectingStyles.workArea)

                createCircle(100, 100, Color.RED, "RED")
                createCircle(220, 100, Color.BLUE, "BLUE")
                createCircle(100, 220, Color.GREEN, "GREEN")
                createCircle(220, 220, Color.YELLOW, "YELLOW")

                label("Ctrl, Shift, or Drag to Multi-Select") {
                    padding = Insets(2.0)
                }

                lassoRect = rectangle {
                    isVisible = false
                    addClass(SelectingStyles.lassoRect)
                }

                anchorpaneConstraints {
                    topAnchor = 0.0
                    bottomAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                }

                addEventFilter(MouseEvent.MOUSE_PRESSED, ::press)
                addEventFilter(MouseEvent.MOUSE_DRAGGED, ::lasso)
                addEventFilter(MouseEvent.MOUSE_RELEASED, ::stopLasso)
            }

            padding = Insets(10.0)

            hboxConstraints {
                hgrow = Priority.ALWAYS
            }
        }

        vbox {
            listview(selectedCircles)

            padding = Insets(10.0)
        }
    }

    private fun lasso(evt: MouseEvent) {
        if (!initDrag) {
            lassoRect.isVisible = true
            dragStart = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
            if (dragStart != null) {
                lassoRect.relocate(dragStart!!.x, dragStart!!.y)
            }
            initDrag = true
        }

        // find width and height; negatives ok?
        val currPos = workArea.sceneToLocal(evt.sceneX, evt.sceneY)
        if (dragStart != null) {

            val w = currPos.x - dragStart!!.x
            val y = currPos.y - dragStart!!.y

            lassoRect.width = abs(w)
            lassoRect.height = abs(y)

            if (w < 0 || y < 0) { // dragging left or up
                lassoRect.relocate(currPos.x, currPos.y)
            }
        }
    }

    private fun stopLasso(evt: MouseEvent) {
        if (initDrag) {  // in drag operation

            lassoRect.isVisible = false

            val lassoBounds = lassoRect.boundsInParent

            circles
                .filter {
                    lassoBounds.contains(it.boundsInParent)
                }
                .forEach {
                    selectCircle(it)
                }
        }

        initDrag = false
        dragStart = null
    }

    private fun press(evt: MouseEvent) {

        if (!evt.isControlDown && !evt.isShiftDown) {

            circles
                .forEach {
                    if (it.hasClass(SelectingStyles.selected)) {
                        it.removeClass(SelectingStyles.selected)
                    }
                }

            selectedCircles.clear()
        }

        circles
            .filter {
                val mousePt: Point2D = it.sceneToLocal(evt.sceneX, evt.sceneY)
                it.contains(mousePt)
            }
            .firstOrNull()
            .apply {
                if (this != null) {
                    if (this.hasClass(SelectingStyles.selected)) {
                        this.removeClass(SelectingStyles.selected)
                        selectedCircles.remove(this.properties["circleColor"] as String)
                    } else {
                        selectCircle(this)
                    }
                }
            }
    }

    private fun selectCircle(circ: Circle) {
        circ.addClass(SelectingStyles.selected)
        selectedCircles.add(circ.properties["circleColor"] as String)
    }
}

class SelectingStyles : Stylesheet() {

    companion object {
        val workArea by cssclass()
        val selected by cssclass()
        val circleItem by cssclass()
        val lassoRect by cssclass()
    }

    init {
        workArea {
            backgroundColor += Color.LIGHTGRAY
            borderColor += box(Color.BLACK)
            borderWidth += box(1.px)
        }

        selected {
            stroke = Color.BLACK
            strokeWidth = 2.px
        }

        circleItem {
            hover {
                fill = Color.WHITE
            }
        }

        lassoRect {
            fill = Color.TRANSPARENT
            stroke = Color.BLACK
            strokeWidth = 1.px
        }
    }
}

class SelectingApp : App(SelectingView::class, SelectingStyles::class) {
    override fun createPrimaryScene(view: UIComponent) =
        Scene(view.root, 667.0, 376.0)
}

