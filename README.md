# yaml-resource-bundle

[![maven central badge]][maven central]
[![javadoc badge]][javadoc]
[![release badge]][release]
[![build badge]][build]
[![codecov badge]][codecov]
[![license badge]][license]

[maven central]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.util/yaml-resource-bundle
[maven central badge]: https://maven-badges.herokuapp.com/maven-central/dev.akkinoc.util/yaml-resource-bundle/badge.svg
[javadoc]: https://javadoc.io/doc/dev.akkinoc.util/yaml-resource-bundle
[javadoc badge]: https://javadoc.io/badge2/dev.akkinoc.util/yaml-resource-bundle/javadoc.svg
[release]: https://github.com/akkinoc/yaml-resource-bundle/releases
[release badge]: https://img.shields.io/github/v/release/akkinoc/yaml-resource-bundle?color=brightgreen&sort=semver
[build]: https://github.com/akkinoc/yaml-resource-bundle/actions/workflows/build.yml
[build badge]: https://github.com/akkinoc/yaml-resource-bundle/actions/workflows/build.yml/badge.svg
[codecov]: https://codecov.io/gh/akkinoc/yaml-resource-bundle
[codecov badge]: https://codecov.io/gh/akkinoc/yaml-resource-bundle/branch/main/graph/badge.svg
[license]: LICENSE.txt
[license badge]: https://img.shields.io/github/license/akkinoc/yaml-resource-bundle?color=blue

[Java ResourceBundle] for YAML format.

[Java ResourceBundle]: https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html

## Features

* Accesses YAML-formatted resources via ResourceBundle.
* Supports locale-specific resources according to the ResourceBundle specification.
* Supports YAML values nested in a map or list.
* Supports YAML anchors and aliases indicated by `&` and `*`.
* Supports multiple YAML documents separated by `---`.

## Dependencies

Depends on:

* Java 8, 11 or 17
* Kotlin 1.5
* SnakeYAML 1.29

## Usage

### Adding the Dependency

The artifact is published on [Maven Central Repository][maven central].
If you are using Maven, add the following dependency.

```xml
<dependency>
    <groupId>dev.akkinoc.util</groupId>
    <artifactId>yaml-resource-bundle</artifactId>
    <version>${yaml-resource-bundle.version}</version>
</dependency>
```

### Creating Resource Files

Create a YAML-formatted resource file on the classpath.
Also, create locale-specific resource files as needed.

For example (resource.yml, resource_en.yml, resource_fr.yml, etc):

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

### Accessing the Resource

Access the resource via YamlResourceBundle.

For example in Java:

```java
import dev.akkinoc.util.YamlResourceBundle;
```

```java
// Gets the resource bundle
// YamlResourceBundle.Control is specified for ResourceBundle.Control
ResourceBundle bundle = ResourceBundle.getBundle(
        "resource", YamlResourceBundle.Control.INSTANCE);

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

// Gets the all keys
System.out.println(bundle.keySet());
// => "[fruits.apple, fruits.orange, fruits.grape,
//      colors, colors[0], colors[1], colors[2]]" (not sorted)
```

## API Reference

Please refer to the [Javadoc][javadoc].

## Release Notes

Please refer to the [Releases][release] page.

## License

Licensed under the [Apache License, Version 2.0][license].
