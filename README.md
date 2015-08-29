YamlResourceBundle [![Circle CI](https://circleci.com/gh/akihyro/yaml-resource-bundle.png?circle-token=432a2297d85d3ce3fc7a3610605976851b227fee)](https://circleci.com/gh/akihyro/yaml-resource-bundle)
=======================================================================================================================================================================================================

Java ResourceBundle for YAML format.

Usage
-----

### YAML file (example.yaml):

```yaml
fruits:
  apple: Apple
  orange: Orange
  grape: Grape
colors:
  - Red
  - Orange
  - Purple
```

### Java code:

```java
import java.util.ResourceBundle;
import net.rakugakibox.util.YamlResourceBundle;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class YamlResourceBundleExample {

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
```
