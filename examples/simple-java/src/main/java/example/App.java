package example;

import dev.akkinoc.util.YamlResourceBundle;
import java.util.Arrays;
import java.util.ResourceBundle;

public class App {

    public static void main(String... args) {

        // Gets the resource bundle
        // YamlResourceBundle.Control is specified for ResourceBundle.Control
        ResourceBundle bundle = ResourceBundle.getBundle("resource", YamlResourceBundle.Control.INSTANCE);

        // Gets the map values
        System.out.println(bundle.getString("fruits.apple"));   // => "Apple" or localized value
        System.out.println(bundle.getString("fruits.orange"));  // => "Orange" or localized value
        System.out.println(bundle.getString("fruits.grape"));   // => "Grape" or localized value

        // Gets the list values
        System.out.println(bundle.getString("colors[0]"));      // => "Red" or localized value
        System.out.println(bundle.getString("colors[1]"));      // => "Orange" or localized value
        System.out.println(bundle.getString("colors[2]"));      // => "Purple" or localized value

        // Gets the list values as an array
        System.out.println(Arrays.toString(bundle.getStringArray("colors")));
        // => "[Red, Orange, Purple]" or localized values

        // Gets all the keys
        System.out.println(bundle.keySet());
        // => "[fruits.apple, fruits.orange, fruits.grape, colors, colors[0], colors[1], colors[2]]" (not sorted)

    }

}
