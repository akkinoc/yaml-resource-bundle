package net.rakugakibox.util;

import java.util.ResourceBundle;

/**
 * Example of {@link YamlResourceBundle}.
 */
public class YamlResourceBundleExample {

    /**
     * Example entry point of {@link YamlResourceBundle}.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {

        // Specify the "YamlResourceBundle.Control.INSTANCE" to "ResourceBundle.Control".
        ResourceBundle r = ResourceBundle.getBundle(
                "net.rakugakibox.util.YamlResourceBundleExample.example",
                YamlResourceBundle.Control.INSTANCE
        );

        // Support the "ResourceBundle#getString(String)" for yaml string.
        System.out.println(r.getString("fruits.apple"));    // => "Apple"
        System.out.println(r.getString("fruits.orange"));   // => "Orange"
        System.out.println(r.getString("fruits.grape"));    // => "Grape"

        // Support the "ResourceBundle#getString(String)" for yaml array.
        System.out.println(r.getString("colors[0]"));       // => "Red"
        System.out.println(r.getString("colors[1]"));       // => "Orange"
        System.out.println(r.getString("colors[2]"));       // => "Purple"

        // Support the "ResourceBundle#getStringArray(String)".
        for (String s : r.getStringArray("colors")) {
            System.out.println(s);
        }       // => "Red", "Orange", "Purple"

        // Support the "ResourceBundle#keySet()".
        for (String s: r.keySet()) {
            System.out.println(s);
        }       // => "fruits.apple", "fruits.orange", "fruits.grape",
                //    "colors[0]", "colors[1]", "colors[2]", "colors" (not sorted)

    }

}
