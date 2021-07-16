# yaml-resource-bundle

[![maven central][maven central badge]][maven central]
[![javadoc][javadoc badge]][javadoc]
[![build][build badge]][build]
[![codecov][codecov badge]][codecov]
[![license][license badge]][license]

[maven central badge]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.util/yaml-resource-bundle/badge.svg
[maven central]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.util/yaml-resource-bundle
[javadoc badge]: https://javadoc.io/badge2/dev.akkinoc.util/yaml-resource-bundle/javadoc.svg
[javadoc]: https://javadoc.io/doc/dev.akkinoc.util/yaml-resource-bundle
[build badge]: https://github.com/akkinoc/yaml-resource-bundle/actions/workflows/build.yml/badge.svg
[build]: https://github.com/akkinoc/yaml-resource-bundle/actions/workflows/build.yml
[codecov badge]: https://codecov.io/gh/akkinoc/yaml-resource-bundle/branch/main/graph/badge.svg
[codecov]: https://codecov.io/gh/akkinoc/yaml-resource-bundle
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue
[license]: LICENSE.txt

[Java ResourceBundle] for YAML format.  

[Java ResourceBundle]: https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html

## Features

* Accesses YAML-formatted resources via ResourceBundle.
* Supports locale-specific resources according to the ResourceBundle specification.
* Supports YAML values nested in maps or lists.
* Supports multiple YAML documents separated by "---".

## Dependency versions

Supports the following dependency versions.  
Other versions might also work, but we have not tested it.  

* Java 8, 11, 15
* Kotlin 1.5
* SnakeYAML 1.29

## Usage

### Adding the dependency

The artifact is published on Maven Central Repository.  
If you are using Maven, add the following dependency.  

```xml
<dependency>
    <groupId>dev.akkinoc.util</groupId>
    <artifactId>yaml-resource-bundle</artifactId>
    <version>${yaml-resource-bundle.version}</version>
</dependency>
```

### Creating a resource file

Create a resource file in YAML format on the classpath.  
Also, create locale-specific resource files as needed.  

For example:  

```yaml
# resource.yml, resource_en.yml, resource_fr.yml, etc
fruits:
  apple: Apple
  orange: Orange
  grape: Grape
colors:
  - Red
  - Orange
  - Purple
```

### Accessing the resource

Access the resource via YamlResourceBundle.  

For example in Java:  

```java
import dev.akkinoc.util.YamlResourceBundle;
import static java.lang.System.out;
import static java.util.Arrays.asList;
import java.util.ResourceBundle;

public class Main {

    public static void main(String... args) {

        // Gets the resource bundle
        // "YamlResourceBundle.Control" is specified for "ResourceBundle.Control"
        ResourceBundle bundle = ResourceBundle.getBundle(
                "resource", YamlResourceBundle.Control.INSTANCE);

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
        // => "[fruits.apple, fruits.orange, fruits.grape,
        //      colors, colors[0], colors[1], colors[2]]" (not sorted)

    }

}
```

## Release notes

Please refer to the "[Releases]" page.  

[Releases]: https://github.com/akkinoc/yaml-resource-bundle/releases

## Contributing

[Bug reports] and [pull requests] are welcome :)  

[Bug reports]: https://github.com/akkinoc/yaml-resource-bundle/issues
[pull requests]: https://github.com/akkinoc/yaml-resource-bundle/pulls

## Building and testing

To build and test, run:  

```console
$ git clone git@github.com:akkinoc/yaml-resource-bundle.git
$ cd yaml-resource-bundle
$ mvn clean install
```

## License

Licensed under the [Apache License, Version 2.0].  

[Apache License, Version 2.0]: LICENSE.txt
