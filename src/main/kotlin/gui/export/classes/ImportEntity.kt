package gui.export.classes

import classes.FileFormat
import classes.TimeTable
import kotlinx.serialization.*
import java.io.File

/**
 * Import compatible entity
 * @param[format] File format for import
 */
@Serializable
open class ImportEntity(format: FileFormat, target: TimeTable) {
    protected var target: TimeTable = target
    protected var format: FileFormat = format

/*
    */
/**
     * Loading data from file
     * @param[path] Path from which the file will be downloaded
     *//*

    fun LoadFromFile(path: String): UInt{
        //val reader: BuffrFile = File(path)
        //val buffer: FileIn
        //this.target = null
    }

    */
/**
     * Loading data from clipboard
     *//*

    fun LoadFromClipboard(): UInt{

        //this.target = null
    }
*/
}