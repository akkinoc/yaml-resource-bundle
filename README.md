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

public class YamlResourceBundleExample {

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
```

License
-------

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
