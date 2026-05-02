package dev.akkinoc.util

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Locale

/**
 * Tests [YamlResourceBundle.Control].
 */
class YamlResourceBundleControlTest {

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
            false,
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
            false,
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
            true,
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
                false,
            )
        }
    }

}
