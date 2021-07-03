package dev.akkinoc.util

import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.io.Reader
import java.util.Collections.enumeration
import java.util.Enumeration
import java.util.Locale
import java.util.ResourceBundle

/**
 * [ResourceBundle] for YAML format.
 *
 * @property entries The resource bundle entries.
 */
class YamlResourceBundle private constructor(private val entries: Map<String, Any>) : ResourceBundle() {

    constructor() : this(emptyMap())

    /**
     * @param docs A string containing the YAML documents.
     */
    constructor(docs: String) : this(parseDocs(docs))

    /**
     * @param docs An input stream to read the YAML documents.
     */
    constructor(docs: InputStream) : this(parseDocs(docs))

    /**
     * @param docs A reader to read the YAML documents.
     */
    constructor(docs: Reader) : this(parseDocs(docs))

    override fun getKeys(): Enumeration<String> = enumeration(keySet())

    override fun handleKeySet(): Set<String> = entries.keys

    override fun handleGetObject(key: String): Any? = entries[key]

    private companion object {

        /**
         * Parses the YAML documents.
         *
         * @param docs A string containing the YAML documents.
         * @return The resource bundle entries.
         */
        private fun parseDocs(docs: String): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

        /**
         * Parses the YAML documents.
         *
         * @param docs An input stream to read the YAML documents.
         * @return The resource bundle entries.
         */
        private fun parseDocs(docs: InputStream): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

        /**
         * Parses the YAML documents.
         *
         * @param docs A reader to read the YAML documents.
         * @return The resource bundle entries.
         */
        private fun parseDocs(docs: Reader): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

        /**
         * Parses the YAML documents.
         *
         * @param read A function to read the YAML documents.
         * @return The resource bundle entries.
         */
        private fun parseDocs(read: () -> Iterable<Any>): Map<String, Any> {
            val docs = read()
            val entries = docs.asSequence().flatMap { parseDoc(it) }.toMap()
            return entries.asSequence().mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
        }

        /**
         * Parses the YAML document.
         *
         * @param doc The YAML document.
         * @return The resource bundle entries.
         */
        private fun parseDoc(doc: Any): Sequence<Pair<String, Any?>> {
            require(doc is Map<*, *>) { "The root of the YAML document must be a map." }
            return doc.asSequence().flatMap { parseNode("${it.key}", it.value) }
        }

        /**
         * Parses the YAML document node.
         * If the node value is [Map] or [List], process it recursively.
         *
         * @param key The key of the node in the YAML document.
         * @param value The value of the node in the YAML document.
         * @return The resource bundle entries.
         */
        private fun parseNode(key: String, value: Any?): Sequence<Pair<String, Any?>> = when (value) {
            is Map<*, *> -> {
                value.asSequence().flatMap { parseNode("$key.${it.key}", it.value) }
            }
            is List<*> -> {
                val strings = value.run {
                    if (isEmpty()) return@run emptySequence()
                    val items = map { it as? String ?: return@run emptySequence() }
                    sequenceOf(key to items.toTypedArray())
                }
                strings + value.asSequence().withIndex().flatMap { parseNode("$key[${it.index}]", it.value) }
            }
            else -> sequenceOf(key to value)
        }

    }

    /**
     * [ResourceBundle.Control] for YAML format.
     */
    object Control : ResourceBundle.Control() {

        override fun getFormats(baseName: String): List<String> = listOf("yaml", "yml")

        override fun newBundle(
                baseName: String,
                locale: Locale,
                format: String,
                loader: ClassLoader,
                reload: Boolean
        ): ResourceBundle? {
            require(format in getFormats(baseName)) { "Unknown format: $format" }
            val bundleName = toBundleName(baseName, locale)
            val resourceName = toResourceName(bundleName, format)
            val resource = loader.getResource(resourceName) ?: return null
            val connection = resource.openConnection()
            if (reload) connection.useCaches = false
            return connection.getInputStream().use { YamlResourceBundle(it) }
        }

    }

}
