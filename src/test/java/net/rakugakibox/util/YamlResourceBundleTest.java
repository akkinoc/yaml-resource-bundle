package net.rakugakibox.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import static java.util.Collections.list;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;
import org.yaml.snakeyaml.error.YAMLException;

import lombok.Cleanup;

/**
 * Test of {@link YamlResourceBundle}.
 */
public class YamlResourceBundleTest {

    /**
     * Resource prefix. (This class name.)
     */
    private static final String RESOURCE_PREFIX = YamlResourceBundleTest.class.getName() + ".";

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(String)}.
     */
    @Test
    public void new_InstaniationForString() {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        assertThat(new YamlResourceBundle(string)).isNotNull();
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(String)}.
     */
    @Test
    public void new_StringIsRequired() {
        assertThatThrownBy(() -> new YamlResourceBundle((String) null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(String)}.
     */
    @Test
    public void new_InvalidFormatForString() {
        String string = new StringBuilder()
                .append(":")
                .toString();
        assertThatThrownBy(() -> new YamlResourceBundle(string)).isInstanceOf(YAMLException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(InputStream)}.
     *
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    @Test
    public void new_InstaniationForInputStream() throws IOException {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        @Cleanup InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        assertThat(new YamlResourceBundle(stream)).isNotNull();
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(InputStream)}.
     */
    @Test
    public void new_InputStreamIsRequired() {
        assertThatThrownBy(() -> new YamlResourceBundle((InputStream) null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(InputStream)}.
     *
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    @Test
    public void new_InvalidFormatForInputStream() throws IOException {
        String string = new StringBuilder()
                .append(":")
                .toString();
        @Cleanup InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        assertThatThrownBy(() -> new YamlResourceBundle(stream)).isInstanceOf(YAMLException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(Reader)}.
     *
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    @Test
    public void new_InstaniationForReader() throws IOException {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        @Cleanup Reader reader = new StringReader(string);
        assertThat(new YamlResourceBundle(reader)).isNotNull();
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(Reader)}.
     */
    @Test
    public void new_ReaderIsRequired() {
        assertThatThrownBy(() -> new YamlResourceBundle((Reader) null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#YamlResourceBundle(Reader)}.
     *
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    @Test
    public void new_InvalidFormatForReader() throws IOException {
        String string = new StringBuilder()
                .append(":")
                .toString();
        @Cleanup Reader reader = new StringReader(string);
        assertThatThrownBy(() -> new YamlResourceBundle(reader)).isInstanceOf(YAMLException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#handleGetObject(String)}.
     */
    @Test
    public void handleGetObject_GetString() {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleGetObject("a")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle#handleGetObject(String)}.
     */
    @Test
    public void handleGetObject_GetNestedString() {
        String string = new StringBuilder()
                .append("a:\n")
                .append("  b: x\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleGetObject("a.b")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle#handleGetObject(String)}.
     */
    @Test
    public void handleGetObject_OverrideIfKeyIsDuplicate() {
        String string = new StringBuilder()
                .append("a: x\n")
                .append("a: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleGetObject("a")).isEqualTo("y");
    }

    /**
     * Test of {@link YamlResourceBundle#handleGetObject(String)}.
     */
    @Test
    public void handleGetObject_OverrideIfNestedKeyIsDuplicate() {
        String string = new StringBuilder()
                .append("a.b: x\n")
                .append("a:\n")
                .append("  b: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleGetObject("a.b")).isEqualTo("y");
    }

    /**
     * Test of {@link YamlResourceBundle#handleGetObject(String)}.
     */
    @Test
    public void handleGetObject_KeyIsRequired() {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThatThrownBy(() -> resourceBundle.handleGetObject(null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle#handleKeySet()}.
     */
    @Test
    public void handleKeySet_GetKeySet() {
        String string = new StringBuilder()
                .append("a: x\n")
                .append("b: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleKeySet()).containsOnly("a", "b");
    }

    /**
     * Test of {@link YamlResourceBundle#handleKeySet()}.
     */
    @Test
    public void handleKeySet_GetNestedKeySet() {
        String string = new StringBuilder()
                .append("a:\n")
                .append("  b: x\n")
                .append("  c: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.handleKeySet()).containsOnly("a.b", "a.c");
    }

    /**
     * Test of {@link YamlResourceBundle#getKeys()}.
     */
    @Test
    public void getKeys_GetKeys() {
        String string = new StringBuilder()
                .append("a: x\n")
                .append("b: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(list(resourceBundle.getKeys())).containsOnly("a", "b");
    }

    /**
     * Test of {@link YamlResourceBundle#getKeys()}.
     */
    @Test
    public void getKeys_GetNestedKeys() {
        String string = new StringBuilder()
                .append("a:\n")
                .append("  b: x\n")
                .append("  c: y\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(list(resourceBundle.getKeys())).containsOnly("a.b", "a.c");
    }

    /**
     * Test of {@link YamlResourceBundle#getString(String)}.
     */
    @Test
    public void getString_GetString() {
        String string = new StringBuilder()
                .append("a: x\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.getString("a")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle#getString(String)}.
     */
    @Test
    public void getString_GetNestedString() {
        String string = new StringBuilder()
                .append("a:\n")
                .append("  b: x\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.getString("a.b")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle#getStringArray(String)}.
     */
    @Test
    public void getStringArray_GetStringArray() {
        String string = new StringBuilder()
                .append("a: \n")
                .append("  - x\n")
                .append("  - y\n")
                .append("  - z\n")
                .toString();
        YamlResourceBundle resourceBundle = new YamlResourceBundle(string);
        assertThat(resourceBundle.getStringArray("a")).containsExactly("x", "y", "z");
    }

    /**
     * Test of {@link YamlResourceBundle.Control#getFormats(String)}.
     */
    @Test
    public void control_getFormats_GetFormats() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThat(control.getFormats("a")).containsExactly("yaml", "yml");
    }

    /**
     * Test of {@link YamlResourceBundle.Control#getFormats(String)}.
     */
    @Test
    public void control_getFormats_BaseNameIsRequired() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() -> control.getFormats(null)).isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_GetBundleForYaml() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                RESOURCE_PREFIX + "GetBundleForYaml", control);
        assertThat(resourceBundle.getString("a")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_GetBundleYmlOnly() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                RESOURCE_PREFIX + "GetBundleYmlOnly", control);
        assertThat(resourceBundle.getString("a")).isEqualTo("x");
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_GetBundleWithLocale() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                RESOURCE_PREFIX + "GetBundleWithLocale", Locale.JAPAN, control);
        assertThat(resourceBundle.getString("a")).isEqualTo("x (ja_JP)");
        assertThat(resourceBundle.getString("b")).isEqualTo("y (ja)");
        assertThat(resourceBundle.getString("c")).isEqualTo("z");
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_GetInvalidFormatBundle() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() -> ResourceBundle.getBundle(RESOURCE_PREFIX + "GetInvalidFormatBundle", control))
                .isInstanceOf(MissingResourceException.class)
                .hasCauseInstanceOf(YAMLException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_BaseNameIsRequired() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() ->
                control.newBundle(null, Locale.getDefault(), "yaml", getClass().getClassLoader(), false))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_LocaleIsRequired() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() ->
                control.newBundle("a", null, "yaml", getClass().getClassLoader(), false))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_FormatIsRequired() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() ->
                control.newBundle("a", Locale.getDefault(), null, getClass().getClassLoader(), false))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     */
    @Test
    public void control_newBundle_LoaderIsRequired() {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThatThrownBy(() ->
                control.newBundle("a", Locale.getDefault(), "yaml", null, false))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Test of {@link YamlResourceBundle.Control#newBundle(String, Locale, String, ClassLoader, boolean)}.
     *
     * @throws IllegalAccessException If the class or constructor is not accessible.
     * @throws InstantiationException If the instantiation of a class fails for some other reason.
     * @throws IOException If an error occurred when reading resources using any I/O operations.
     */
    @Test
    public void control_newBundle_UnknownFormat() throws IllegalAccessException, InstantiationException, IOException {
        YamlResourceBundle.Control control = YamlResourceBundle.Control.INSTANCE;
        assertThat(control.newBundle("a", Locale.getDefault(), "xml", getClass().getClassLoader(), false)).isNull();
    }

}
