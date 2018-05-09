package com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public class JaxbMarshallizerTest {

    private static final String KEY_1 = "com.gmail.eksuzyan.pavel.market.test.param.1";
    private static final String KEY_2 = "com.gmail.eksuzyan.pavel.market.test.param.2";
    private static final String KEY_3 = "com.gmail.eksuzyan.pavel.market.test.param.3";

    private static final String VALUE_1 = "one";
    private static final String VALUE_2 = "two";
    private static final String VALUE_3 = "three";

    private static final Properties PROPERTIES_RESULT = new Properties() {{
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

    @Test(expected = NullPointerException.class)
    public void testMarshallingExWrongProps() {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.marshall(null, os);
    }

    @Test(expected = NullPointerException.class)
    public void testMarshallingExWrongOs() {

        Properties props = new Properties();
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.marshall(props, null);
    }

    @Test
    public void testMarshallingPos() {
        Properties props = new Properties();

        props.put(KEY_1, VALUE_1);
        props.put(KEY_2, VALUE_2);
        props.put(KEY_3, VALUE_3);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.marshall(props, os);

        String result = new String(os.toByteArray());

        assertTrue(CONFIG_RESULTS.contains(result));
    }

    @Test
    public void testMarshallingNeg() {
        Properties props = new Properties();

        props.put(KEY_1, VALUE_1);
        props.put(KEY_2, VALUE_2);
        props.put(KEY_3, VALUE_2); // wrong associated value

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.marshall(props, os);

        String result = new String(os.toByteArray());

        assertFalse(CONFIG_RESULTS.contains(result));
    }

    @Test(expected = NullPointerException.class)
    public void testUnmarshallingExWrongIs() {

        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.unmarshall(null);
    }

    @Test
    public void testUnmarshallingPos() {

        ByteArrayInputStream is = new ByteArrayInputStream(CONFIG_1.getBytes());
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        Properties result = marshallizer.unmarshall(is);

        assertEquals(PROPERTIES_RESULT, result);
    }

    @Test
    public void testUnmarshallingNeg() {

        String config = CONFIG_1.replace(VALUE_3, VALUE_2); // wrong associated value

        ByteArrayInputStream is = new ByteArrayInputStream(config.getBytes());
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        Properties result = marshallizer.unmarshall(is);

        assertNotEquals(PROPERTIES_RESULT, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmarshallingEx() {

        String config = "any wrong config content";

        ByteArrayInputStream is = new ByteArrayInputStream(config.getBytes());
        JaxbMarshallizer marshallizer = new JaxbMarshallizer();

        marshallizer.unmarshall(is);
    }

}
