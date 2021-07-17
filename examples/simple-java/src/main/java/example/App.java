package example;

import dev.akkinoc.util.YamlResourceBundle;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import java.util.ResourceBundle;

public class App {

    public static void main(String... args) {

        // Gets the resource bundle
        // "YamlResourceBundle.Control" is specified for "ResourceBundle.Control"
        ResourceBundle bundle = ResourceBundle.getBundle("resource", YamlResourceBundle.Control.INSTANCE);

        // Gets the map values
        out.println(bundle.getString("fruits.apple"));   // => "Apple" or a localized value
        out.println(bundle.getString("fruits.orange"));  // => "Orange" or a localized value
        out.println(bundle.getString("fruits.grape"));   // => "Grape" or a localized value

        // Gets the list values
        out.println(bundle.getString("colors[0]"));      // => "Red" or a localized value
        out.println(bundle.getString("colors[1]"));      // => "Orange" or a localized value
        out.println(bundle.getString("colors[2]"));      // => "Purple" or a localized value

        // Gets the list values as an array
        out.println(asList(bundle.getStringArray("colors")));
        // => "[Red, Orange, Purple]" or localized values

        // Gets all the keys
        out.println(bundle.keySet());
        // => "[fruits.apple, fruits.orange, fruits.grape, colors, colors[0], colors[1], colors[2]]" (not sorted)

    }

}
