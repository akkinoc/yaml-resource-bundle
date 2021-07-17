package dev.akkinoc.util

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.yaml.snakeyaml.error.YAMLException
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

/**
 * Tests [YamlResourceBundle].
 */
class YamlResourceBundleTest {

    @Test
    fun `constructor - Constructs an instance`() {
        val bundle = YamlResourceBundle()
        bundle.keySet().shouldBeEmpty()
    }

    @Test
    fun `constructor - Constructs an instance from a string`() {
        val docs = "key: value"
        val bundle = YamlResourceBundle(docs)
        bundle.getString("key").shouldBe("value")
    }

    @Test
    fun `constructor - Constructs an instance from an input stream`() {
        val docs = "key: value"
        val bundle = docs.byteInputStream().use { YamlResourceBundle(it) }
        bundle.getString("key").shouldBe("value")
    }

    @Test
    fun `constructor - Constructs an instance from a reader`() {
        val docs = "key: value"
        val bundle = docs.reader().use { YamlResourceBundle(it) }
        bundle.getString("key").shouldBe("value")
    }

    @Test
    fun `constructor - Constructs an instance from an empty documents`() {
        val docs = ""
        val bundle = YamlResourceBundle(docs)
        bundle.keySet().shouldBeEmpty()
    }

    @Test
    fun `constructor - Fails if the format is invalid`() {
        val docs = "["
        shouldThrow<YAMLException> { YamlResourceBundle(docs) }
    }

    @Test
    fun `constructor - Fails if the root is a scalar`() {
        val docs = "value"
        shouldThrow<IllegalArgumentException> { YamlResourceBundle(docs) }
    }

    @Test
    fun `constructor - Fails if the root is a list`() {
        val docs = "- value"
        shouldThrow<IllegalArgumentException> { YamlResourceBundle(docs) }
    }

    @Test
    fun `handleGetObject - Gets the scalar values`() {
        val docs = """
            boolean: true
            int: 1234
            double: 0.1234
            string: value
            empty: ""
            null: ~
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("boolean", "int", "double", "string", "empty")
        bundle.getObject("boolean").shouldBe(true)
        bundle.getObject("int").shouldBe(1234)
        bundle.getObject("double").shouldBe(0.1234)
        bundle.getString("string").shouldBe("value")
        bundle.getString("empty").shouldBeEmpty()
        shouldThrow<MissingResourceException> { bundle.getObject("null") }
        shouldThrow<MissingResourceException> { bundle.getObject("unknown") }
    }

    @Test
    fun `handleGetObject - Gets the map values`() {
        val docs = """
            map:
              a: value @map.a
              b: value @map.b
            empty: {}
            nulls:
              a: ~
              b: ~
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("map.a", "map.b")
        bundle.getString("map.a").shouldBe("value @map.a")
        bundle.getString("map.b").shouldBe("value @map.b")
        shouldThrow<MissingResourceException> { bundle.getObject("empty") }
        shouldThrow<MissingResourceException> { bundle.getObject("nulls.a") }
        shouldThrow<MissingResourceException> { bundle.getObject("nulls.b") }
    }

    @Test
    fun `handleGetObject - Gets the map values nested in the map`() {
        val docs = """
            nested:
              map:
                a: value @nested.map.a
                b: value @nested.map.b
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("nested.map.a", "nested.map.b")
        bundle.getString("nested.map.a").shouldBe("value @nested.map.a")
        bundle.getString("nested.map.b").shouldBe("value @nested.map.b")
    }

    @Test
    fun `handleGetObject - Gets the map values nested in the list`() {
        val docs = """
            nested:
              - a: value @nested[0].a
                b: value @nested[0].b
              - a: value @nested[1].a
                b: value @nested[1].b
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("nested[0].a", "nested[0].b", "nested[1].a", "nested[1].b")
        bundle.getString("nested[0].a").shouldBe("value @nested[0].a")
        bundle.getString("nested[0].b").shouldBe("value @nested[0].b")
        bundle.getString("nested[1].a").shouldBe("value @nested[1].a")
        bundle.getString("nested[1].b").shouldBe("value @nested[1].b")
    }

    @Test
    fun `handleGetObject - Gets the list values`() {
        val docs = """
            list:
              - value @list[0]
              - value @list[1]
            empty: []
            nulls:
              - ~
              - ~
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("list", "list[0]", "list[1]")
        bundle.getStringArray("list").shouldContainExactly("value @list[0]", "value @list[1]")
        bundle.getString("list[0]").shouldBe("value @list[0]")
        bundle.getString("list[1]").shouldBe("value @list[1]")
        shouldThrow<MissingResourceException> { bundle.getObject("empty") }
        shouldThrow<MissingResourceException> { bundle.getObject("nulls") }
        shouldThrow<MissingResourceException> { bundle.getObject("nulls[0]") }
        shouldThrow<MissingResourceException> { bundle.getObject("nulls[1]") }
    }

    @Test
    fun `handleGetObject - Gets the list values nested in the map`() {
        val docs = """
            nested:
              list:
                - value @nested.list[0]
                - value @nested.list[1]
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("nested.list", "nested.list[0]", "nested.list[1]")
        bundle.getStringArray("nested.list").shouldContainExactly("value @nested.list[0]", "value @nested.list[1]")
        bundle.getString("nested.list[0]").shouldBe("value @nested.list[0]")
        bundle.getString("nested.list[1]").shouldBe("value @nested.list[1]")
    }

    @Test
    fun `handleGetObject - Gets the list values nested in the list`() {
        val docs = """
            nested:
              - - value @nested[0][0]
                - value @nested[0][1]
              - - value @nested[1][0]
                - value @nested[1][1]
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder(
                        "nested[0]", "nested[0][0]", "nested[0][1]",
                        "nested[1]", "nested[1][0]", "nested[1][1]"
                )
        bundle.getStringArray("nested[0]").shouldContainExactly("value @nested[0][0]", "value @nested[0][1]")
        bundle.getString("nested[0][0]").shouldBe("value @nested[0][0]")
        bundle.getString("nested[0][1]").shouldBe("value @nested[0][1]")
        bundle.getStringArray("nested[1]").shouldContainExactly("value @nested[1][0]", "value @nested[1][1]")
        bundle.getString("nested[1][0]").shouldBe("value @nested[1][0]")
        bundle.getString("nested[1][1]").shouldBe("value @nested[1][1]")
    }

    @Test
    fun `handleGetObject - Gets the alias values`() {
        val docs = """
            scalar: &SCALAR value @scalar
            alias-scalar: *SCALAR
            map: &MAP
              a: value @map.a
              b: value @map.b
            alias-map: *MAP
            list: &LIST
              - value @list[0]
              - value @list[1]
            alias-list: *LIST
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder(
                        "scalar", "alias-scalar",
                        "map.a", "map.b", "alias-map.a", "alias-map.b",
                        "list", "list[0]", "list[1]", "alias-list", "alias-list[0]", "alias-list[1]"
                )
        bundle.getString("scalar").shouldBe("value @scalar")
        bundle.getString("alias-scalar").shouldBe("value @scalar")
        bundle.getString("map.a").shouldBe("value @map.a")
        bundle.getString("map.b").shouldBe("value @map.b")
        bundle.getString("alias-map.a").shouldBe("value @map.a")
        bundle.getString("alias-map.b").shouldBe("value @map.b")
        bundle.getStringArray("list").shouldContainExactly("value @list[0]", "value @list[1]")
        bundle.getString("list[0]").shouldBe("value @list[0]")
        bundle.getString("list[1]").shouldBe("value @list[1]")
        bundle.getStringArray("alias-list").shouldContainExactly("value @list[0]", "value @list[1]")
        bundle.getString("alias-list[0]").shouldBe("value @list[0]")
        bundle.getString("alias-list[1]").shouldBe("value @list[1]")
    }

    @Test
    fun `handleGetObject - Gets the values in multiple documents`() {
        val docs = """
            a: value @a
            ---
            b: value @b
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("a", "b")
        bundle.getString("a").shouldBe("value @a")
        bundle.getString("b").shouldBe("value @b")
    }

    @Test
    fun `handleGetObject - Prefers the last value if there is a key conflict`() {
        val docs = """
            scalar: value @scalar (1)
            scalar: value @scalar (2)
            map.a: value @map.a (1)
            map:
              a: value @map.a (2)
              b: value @map.b (1)
            map.b: value @map.b (2)
            list[0]: value @list[0] (1)
            list:
              - value @list[0] (2)
              - value @list[1] (1)
            list[1]: value @list[1] (2)
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("scalar", "map.a", "map.b", "list", "list[0]", "list[1]")
        bundle.getString("scalar").shouldBe("value @scalar (2)")
        bundle.getString("map.a").shouldBe("value @map.a (2)")
        bundle.getString("map.b").shouldBe("value @map.b (2)")
        bundle.getStringArray("list").shouldContainExactly("value @list[0] (2)", "value @list[1] (1)")
        bundle.getString("list[0]").shouldBe("value @list[0] (2)")
        bundle.getString("list[1]").shouldBe("value @list[1] (2)")
    }

    @Test
    fun `handleGetObject - Prefers the last value if there is a key conflict across multiple documents`() {
        val docs = """
            scalar: value @scalar (1)
            map:
              b: value @map.b (1)
            map.a: value @map.a (1)
            list:
              - ~
              - value @list[1] (1)
            list[0]: value @list[0] (1)
            ---
            scalar: value @scalar (2)
            map:
              a: value @map.a (2)
            map.b: value @map.b (2)
            list:
              - value @list[0] (2)
            list[1]: value @list[1] (2)
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldContainExactlyInAnyOrder("scalar", "map.a", "map.b", "list", "list[0]", "list[1]")
        bundle.getString("scalar").shouldBe("value @scalar (2)")
        bundle.getString("map.a").shouldBe("value @map.a (2)")
        bundle.getString("map.b").shouldBe("value @map.b (2)")
        bundle.getStringArray("list").shouldContainExactly("value @list[0] (2)")
        bundle.getString("list[0]").shouldBe("value @list[0] (2)")
        bundle.getString("list[1]").shouldBe("value @list[1] (2)")
    }

    @Test
    fun `handleGetObject - Overwrite the existing key with a null value`() {
        val docs = """
            scalar: value @scalar (1)
            scalar: ~
            map.a: value @map.a (1)
            map:
              a: ~
              b: value @map.b (1)
            map.b: ~
            list[0]: value @list[0] (1)
            list:
              - ~
              - value @list[1] (1)
            list[1]: ~
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldBeEmpty()
        shouldThrow<MissingResourceException> { bundle.getObject("scalar") }
        shouldThrow<MissingResourceException> { bundle.getObject("map.a") }
        shouldThrow<MissingResourceException> { bundle.getObject("map.b") }
        shouldThrow<MissingResourceException> { bundle.getObject("list[0]") }
        shouldThrow<MissingResourceException> { bundle.getObject("list[1]") }
    }

    @Test
    fun `handleGetObject - Overwrite the existing key with a null value across multiple documents`() {
        val docs = """
            scalar: value @scalar (1)
            map:
              b: value @map.b (1)
            map.a: value @map.a (1)
            list:
              - ~
              - value @list[1] (1)
            list[0]: value @list[0] (1)
            ---
            scalar: ~
            map:
              a: ~
            map.b: ~
            list:
              - ~
            list[1]: ~
        """.trimIndent()
        val bundle = YamlResourceBundle(docs)
        bundle.keySet()
                .shouldContainExactlyInAnyOrder(bundle.keys.toList())
                .shouldBeEmpty()
        shouldThrow<MissingResourceException> { bundle.getObject("scalar") }
        shouldThrow<MissingResourceException> { bundle.getObject("map.a") }
        shouldThrow<MissingResourceException> { bundle.getObject("map.b") }
        shouldThrow<MissingResourceException> { bundle.getObject("list[0]") }
        shouldThrow<MissingResourceException> { bundle.getObject("list[1]") }
    }

    @Test
    fun `getBundle - Gets the resource bundle in yaml format`() {
        val bundle = ResourceBundle.getBundle(
                "${this::class.qualifiedName}.yaml",
                YamlResourceBundle.Control
        )
        bundle.getString("yaml").shouldBe("value @yaml")
    }

    @Test
    fun `getBundle - Gets the resource bundle in yml format`() {
        val bundle = ResourceBundle.getBundle(
                "${this::class.qualifiedName}.yml",
                YamlResourceBundle.Control
        )
        bundle.getString("yml").shouldBe("value @yml")
    }

    @Test
    fun `getBundle - Prefers the yaml format if yaml and yml formats exist`() {
        val bundle = ResourceBundle.getBundle(
                "${this::class.qualifiedName}.yaml+yml",
                YamlResourceBundle.Control
        )
        bundle.getString("yaml").shouldBe("value @yaml")
        shouldThrow<MissingResourceException> { bundle.getString("yml") }
    }

    @Test
    fun `getBundle - Gets the localized resource bundle`() {
        val bundle = ResourceBundle.getBundle(
                "${this::class.qualifiedName}.localized",
                Locale.JAPAN,
                YamlResourceBundle.Control
        )
        bundle.getString("a").shouldBe("value @a (ja_JP)")
        bundle.getString("b").shouldBe("value @b (ja)")
        bundle.getString("c").shouldBe("value @c")
    }

    /**
     * Tests [YamlResourceBundle.Control].
     */
    @Nested
    inner class ControlTest {

        @Test
        fun `getFormats - Gets the formats`() {
            val control = YamlResourceBundle.Control
            control.getFormats("resource").shouldContainExactly("yaml", "yml")
        }

        @Test
        fun `newBundle - Constructs a resource bundle`() {
            val control = YamlResourceBundle.Control
            val bundle = control.newBundle(
                    "${this::class.qualifiedName}.resource",
                    Locale.ROOT,
                    "yml",
                    javaClass.classLoader,
                    false
            )
            bundle.shouldNotBeNull()
            bundle.getString("a").shouldBe("value @a")
        }

        @Test
        fun `newBundle - Returns null if the resource is not found`() {
            val control = YamlResourceBundle.Control
            val bundle = control.newBundle(
                    "${this::class.qualifiedName}.resource",
                    Locale.JAPAN,
                    "yml",
                    javaClass.classLoader,
                    false
            )
            bundle.shouldBeNull()
        }

        @Test
        fun `newBundle - Reloads a resource bundle`() {
            val control = YamlResourceBundle.Control
            val bundle = control.newBundle(
                    "${this::class.qualifiedName}.resource",
                    Locale.ROOT,
                    "yml",
                    javaClass.classLoader,
                    true
            )
            bundle.shouldNotBeNull()
            bundle.getString("a").shouldBe("value @a")
        }

        @Test
        fun `newBundle - Fails if the format is unknown`() {
            val control = YamlResourceBundle.Control
            shouldThrow<IllegalArgumentException> {
                control.newBundle(
                        "${this::class.qualifiedName}.resource",
                        Locale.getDefault(),
                        "unknown",
                        javaClass.classLoader,
                        false
                )
            }
        }

    }

}
