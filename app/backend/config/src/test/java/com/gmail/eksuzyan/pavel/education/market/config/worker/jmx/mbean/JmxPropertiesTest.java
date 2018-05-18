package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.mbean;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JmxPropertiesTest {

    @Mock
    private Configuration configuration;

    private JmxProperties jmxProperties;

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final String KEY_1 = "com.gmail.eksuzyan.pavel.market.test.param.1";
    private static final String KEY_2 = "com.gmail.eksuzyan.pavel.market.test.param.2";
    private static final String KEY_3 = "com.gmail.eksuzyan.pavel.market.test.param.3";

    private static final String VALUE_1 = "one";
    private static final String VALUE_2 = "two";
    private static final String VALUE_3 = "three";

    private static final Properties PROPERTIES = new Properties() {{
        put(KEY_1, VALUE_1);
        put(KEY_2, VALUE_2);
        put(KEY_3, VALUE_3);
    }};

    private static final String PROPERTY_PATTERN = "<property key=\"%s\" value=\"%s\"/>";

    private static final String PROPERTY_1 = String.format(PROPERTY_PATTERN, KEY_1, VALUE_1);
    private static final String PROPERTY_2 = String.format(PROPERTY_PATTERN, KEY_2, VALUE_2);
    private static final String PROPERTY_3 = String.format(PROPERTY_PATTERN, KEY_3, VALUE_3);

    private static final String CONFIG_PATTERN =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<Market>\n" +
                    "    <Properties>\n" +
                    "        %s\n" +
                    "        %s\n" +
                    "        %s\n" +
                    "    </Properties>\n" +
                    "</Market>\n";

    private static final String CONFIG_1 = String.format(CONFIG_PATTERN, PROPERTY_1, PROPERTY_2, PROPERTY_3);
    private static final String CONFIG_2 = String.format(CONFIG_PATTERN, PROPERTY_2, PROPERTY_3, PROPERTY_1);
    private static final String CONFIG_3 = String.format(CONFIG_PATTERN, PROPERTY_3, PROPERTY_1, PROPERTY_2);
    private static final String CONFIG_4 = String.format(CONFIG_PATTERN, PROPERTY_1, PROPERTY_3, PROPERTY_2);
    private static final String CONFIG_5 = String.format(CONFIG_PATTERN, PROPERTY_2, PROPERTY_1, PROPERTY_3);
    private static final String CONFIG_6 = String.format(CONFIG_PATTERN, PROPERTY_3, PROPERTY_2, PROPERTY_1);

    private static final Set<String> CONFIG_RESULTS = Stream
            .of(CONFIG_1, CONFIG_2, CONFIG_3, CONFIG_4, CONFIG_5, CONFIG_6)
            .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullConfigurationArg() {
        new JmxProperties(null, new JaxbMarshallizer(), DEFAULT_CHARSET);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullMarshallizerArg() {
        new JmxProperties(configuration, null, DEFAULT_CHARSET);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullCharsetArg() {
        new JmxProperties(configuration, new JaxbMarshallizer(), null);
    }

    @Test
    public void testGetPropertyPos() {
        when(configuration.getProperty(KEY_1, "")).thenReturn(VALUE_1);

        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        String result = jmxProperties.getProperty(KEY_1);

        assertEquals(VALUE_1, result);
    }

    @Test
    public void testGetPropertyNeg() {
        when(configuration.getProperty(KEY_1, "")).thenReturn("");

        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        String result = jmxProperties.getProperty(KEY_1);

        assertNotEquals(VALUE_1, result);
    }

    @Test
    public void testAddPropertyPos() {
        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        jmxProperties.addProperty(KEY_1, VALUE_1);

        verify(configuration, times(1)).addProperty(KEY_1, VALUE_1);
    }

    @Test
    public void testAddPropertyNeg() {
        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        jmxProperties.addProperty(KEY_2, VALUE_2);

        verify(configuration, never()).addProperty(KEY_1, VALUE_1);
    }

    @Test
    public void testGetPropertiesPos() {
        when(configuration.getProperties()).thenReturn(PROPERTIES);

        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        String result = jmxProperties.getProperties();

        assertTrue(CONFIG_RESULTS.contains(result));
    }

    @Test
    public void testGetPropertiesNeg() {
        Properties properties = new Properties(PROPERTIES);
        properties.put(KEY_1, VALUE_3);

        when(configuration.getProperties()).thenReturn(properties);

        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        String result = jmxProperties.getProperties();

        assertFalse(CONFIG_RESULTS.contains(result));
    }

    @Test
    public void testAddPropertiesPos() {
        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        jmxProperties.addProperties(CONFIG_1);

        verify(configuration, times(1)).addProperties(PROPERTIES);
    }

    @Test
    public void testAddPropertiesNeg() {
        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        jmxProperties.addProperties(CONFIG_1.replace("three", "four"));

        verify(configuration, never()).addProperties(PROPERTIES);
    }

    @Test
    public void testClearPos() {
        jmxProperties = new JmxProperties(configuration, new JaxbMarshallizer(), DEFAULT_CHARSET);

        jmxProperties.clear();

        verify(configuration, times(1)).clear();
    }

}
