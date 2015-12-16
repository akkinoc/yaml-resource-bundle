YamlResourceBundle
==================

Java ResourceBundle for YAML format.

Status
------

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.util/yaml-resource-bundle/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.rakugakibox.util/yaml-resource-bundle)
[![Circle CI](https://circleci.com/gh/akihyro/yaml-resource-bundle.svg?style=shield)](https://circleci.com/gh/akihyro/yaml-resource-bundle)

Usage
-----

### Maven dependency:

```xml
<dependency>
    <groupId>net.rakugakibox.util</groupId>
    <artifactId>yaml-resource-bundle</artifactId>
    <version>1.1</version>
</dependency>
```

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
