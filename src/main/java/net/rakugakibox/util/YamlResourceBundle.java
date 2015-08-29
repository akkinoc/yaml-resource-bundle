package net.rakugakibox.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.AbstractMap.SimpleImmutableEntry;
import static java.util.Arrays.asList;
import static java.util.Collections.enumeration;
import static java.util.Collections.unmodifiableList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import static java.util.stream.Collectors.toMap;
import java.util.stream.Stream;

import org.yaml.snakeyaml.Yaml;

import lombok.Cleanup;
import lombok.NonNull;

/**
 * {@link ResourceBundle} for YAML format.
 */
public class YamlResourceBundle extends ResourceBundle {

    /**
     * Entries.
     */
    private final Map<String, Object> entries;

    /**
     * Constructor.
     *
     * @param string YAML data.
     */
    public YamlResourceBundle(@NonNull String string) {
        entries = flattenYamlTree(new Yaml().loadAs(string, Map.class));
    }

    /**
     * Constructor.
     *
     * @param stream YAML data as input stream.
     */
    public YamlResourceBundle(@NonNull InputStream stream) {
        entries = flattenYamlTree(new Yaml().loadAs(stream, Map.class));
    }

    /**
     * Constructor.
     *
     * @param reader YAML data as input character stream.
     */
    public YamlResourceBundle(@NonNull Reader reader) {
        entries = flattenYamlTree(new Yaml().loadAs(reader, Map.class));
    }

    /**
     * Flatten yaml tree structure.
     *
     * @param map {@link Map} of yaml tree.
     * @return {@link Map} of entries.
     */
    private static Map<String, Object> flattenYamlTree(Map<?, ?> map) {
        return map.entrySet().stream()
                .flatMap(YamlResourceBundle::flattenYamlTree)
                .collect(toMap(
                        e -> e.getKey(),
                        e -> e.getValue(),
                        (oldValue, newValue) -> newValue
                ));
    }

    /**
     * Flatten yaml tree structure.
     *
     * @param entry {@link Entry} of yaml tree.
     * @return {@link Stream} of entries.
     */
    private static Stream<Entry<String, Object>> flattenYamlTree(Entry<?, ?> entry) {
        String key = entry.getKey().toString();
        Object value = entry.getValue();
        if (value instanceof Map) {
            Map<?, ?> valueAsMap = (Map<?, ?>) value;
            return valueAsMap.entrySet().stream()
                    .flatMap(YamlResourceBundle::flattenYamlTree)
                    .map(e -> new SimpleImmutableEntry<>(key + "." + e.getKey(), e.getValue()));
        } else if (value instanceof List) {
            List<?> valueAsList = (List<?>) value;
            value = valueAsList.stream().toArray(String[]::new);
        }
        return Stream.of(new SimpleImmutableEntry<>(key, value));
    }

    /** {@inheritDoc} */
    @Override
    protected Set<String> handleKeySet() {
        return entries.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getKeys() {
        return enumeration(keySet());
    }

    /** {@inheritDoc} */
    @Override
    protected Object handleGetObject(@NonNull String key) {
        return entries.get(key);
    }

    /**
     * {@link ResourceBundle.Control} for YAML format.
     */
    public static class Control extends ResourceBundle.Control {

        /**
         * Singleton instance.
         */
        public static final Control INSTANCE = new Control();

        /**
         * Constructor.
         */
        protected Control() {
        }

        /** {@inheritDoc} */
        @Override
        public List<String> getFormats(@NonNull String baseName) {
            return unmodifiableList(asList("yaml", "yml"));
        }

        /** {@inheritDoc} */
        @Override
        public ResourceBundle newBundle(@NonNull String baseName,
                @NonNull Locale locale, @NonNull String format, @NonNull ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            if (!getFormats(baseName).contains(format)) {
                return null;
            }
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, format);
            @Cleanup InputStream stream = loader.getResourceAsStream(resourceName);
            return new YamlResourceBundle(stream);
        }

    }

}
