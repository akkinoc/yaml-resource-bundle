package example

import dev.akkinoc.util.YamlResourceBundle
import java.util.ResourceBundle

fun main(vararg args: String) {

    // Gets the resource bundle
    // YamlResourceBundle.Control is specified for ResourceBundle.Control
    val bundle = ResourceBundle.getBundle("resource", YamlResourceBundle.Control)

    // Gets the map values
    println(bundle.getString("fruits.apple"))   // => "Apple" or localized value
    println(bundle.getString("fruits.orange"))  // => "Orange" or localized value
    println(bundle.getString("fruits.grape"))   // => "Grape" or localized value

    // Gets the list values
    println(bundle.getString("colors[0]"))      // => "Red" or localized value
    println(bundle.getString("colors[1]"))      // => "Orange" or localized value
    println(bundle.getString("colors[2]"))      // => "Purple" or localized value

    // Gets the list values as an array
    println(bundle.getStringArray("colors").contentToString())
    // => "[Red, Orange, Purple]" or localized values

    // Gets all the keys
    println(bundle.keySet())
    // => "[fruits.apple, fruits.orange, fruits.grape, colors, colors[0], colors[1], colors[2]]" (not sorted)

}
