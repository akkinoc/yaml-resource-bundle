package net.rakugakibox.util;

import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * Example of {@link YamlResourceBundle}.
 */
public class YamlResourceBundleExample {

    /**
     * Example of {@link YamlResourceBundle}.
     */
    @Test
    public void example() {

        // Specify the "YamlResourceBundle.Control.INSTANCE" to "ResourceBundle.Control".
        ResourceBundle r = ResourceBundle.getBundle(
                "net.rakugakibox.util.YamlResourceBundleExample.example",
                YamlResourceBundle.Control.INSTANCE
        );

        // Support the "ResourceBundle#getString(String)" for yaml string.
        assertThat(r.getString("fruits.apple")).isEqualTo("Apple");
        assertThat(r.getString("fruits.orange")).isEqualTo("Orange");
        assertThat(r.getString("fruits.grape")).isEqualTo("Grape");

        // Support the "ResourceBundle#getString(String)" for yaml array.
        assertThat(r.getString("colors[0]")).isEqualTo("Red");
        assertThat(r.getString("colors[1]")).isEqualTo("Orange");
        assertThat(r.getString("colors[2]")).isEqualTo("Purple");

        // Support the "ResourceBundle#getStringArray(String)".
        assertThat(r.getStringArray("colors")).containsExactly("Red", "Orange", "Purple");

        // Support the "ResourceBundle#keySet()".
        assertThat(r.keySet()).containsOnly(
                "fruits.apple", "fruits.orange", "fruits.grape",
                "colors[0]", "colors[1]", "colors[2]", "colors"
        );

    }

}
