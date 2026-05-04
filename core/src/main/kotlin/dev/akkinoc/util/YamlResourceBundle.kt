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
    constructor(docs: String) : this(Parser.parseDocs(docs))

    /**
     * @param docs An input stream to read the YAML documents.
     */
    constructor(docs: InputStream) : this(Parser.parseDocs(docs))

    /**
     * @param docs A reader to read the YAML documents.
     */
    constructor(docs: Reader) : this(Parser.parseDocs(docs))

    override fun getKeys(): Enumeration<String> = enumeration(keySet())

    override fun handleKeySet(): Set<String> = entries.keys

    override fun handleGetObject(key: String): Any? = entries[key]

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
            reload: Boolean,
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

    /**
     * The YAML documents parser.
     */
    private object Parser {

        /**
         * Parses the YAML documents.
         *
         * @param docs A string containing the YAML documents.
         * @return The resource bundle entries.
         */
        fun parseDocs(docs: String): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

        /**
         * Parses the YAML documents.
         *
         * @param docs An input stream to read the YAML documents.
         * @return The resource bundle entries.
         */
        fun parseDocs(docs: InputStream): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

        /**
         * Parses the YAML documents.
         *
         * @param docs A reader to read the YAML documents.
         * @return The resource bundle entries.
         */
        fun parseDocs(docs: Reader): Map<String, Any> = parseDocs { Yaml().loadAll(docs) }

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
            return doc.asSequence().flatMap { (itemKey, itemValue) ->
                parseNode(key = "$itemKey", value = itemValue, ancestors = emptyList())
            }
        }

        /**
         * Parses the YAML document node.
         *
         * @param key The key of the node in the YAML document.
         * @param value The value of the node in the YAML document.
         * @param ancestors A list of node values from root to parent.
         * @return The resource bundle entries.
         */
        private fun parseNode(key: String, value: Any?, ancestors: List<Any>): Sequence<Pair<String, Any?>> {
            return when (value) {
                is Map<*, *> -> parseMapNode(key = key, value = value, ancestors = ancestors)
                is List<*> -> parseListNode(key = key, value = value, ancestors = ancestors)
                else -> sequenceOf(key to value)
            }
        }

        /**
         * Parses the YAML document map node.
         *
         * @param key The key of the node in the YAML document.
         * @param value The value of the node in the YAML document.
         * @param ancestors A list of node values from root to parent.
         * @return The resource bundle entries.
         */
        private fun parseMapNode(key: String, value: Map<*, *>, ancestors: List<Any>): Sequence<Pair<String, Any?>> {
            if (ancestors.any { it === value }) return emptySequence()
            val path = ancestors + value as Any
            return value.asSequence().flatMap { (itemKey, itemValue) ->
                parseNode(key = "$key.$itemKey", value = itemValue, ancestors = path)
            }
        }

        /**
         * Parses the YAML document list node.
         *
         * @param key The key of the node in the YAML document.
         * @param value The value of the node in the YAML document.
         * @param ancestors A list of node values from root to parent.
         * @return The resource bundle entries.
         */
        private fun parseListNode(key: String, value: List<*>, ancestors: List<Any>): Sequence<Pair<String, Any?>> {
            if (ancestors.any { it === value }) return emptySequence()
            val path = ancestors + value as Any
            val strings = value.run {
                if (isEmpty()) return@run emptySequence()
                val items = map { it as? String ?: return@run emptySequence() }
                sequenceOf(key to items.toTypedArray())
            }
            val items = value.asSequence().flatMapIndexed { itemIndex, itemValue ->
                parseNode(key = "$key[$itemIndex]", value = itemValue, ancestors = path)
            }
            return strings + items
        }

    }

}
