package com.gmail.eksuzyan.pavel.education.market.config.worker.file;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummySettings;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.file.mocks.MockStorage;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.worker.file.mocks.MockConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class FileWorkerTest {

    @Mock
    private Configuration configuration;

    @Mock
    private Settings settings;

    private FileWorker worker;
    private MockStorage storage;

    private static final int TEST_EXECUTION_DURATION = 1_000;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullConfigurationArg() {
        doNothing().when(configuration).addDefaults(any());
        new FileWorker(null, new DummySettings(configuration), new JaxbMarshallizer(), new MockStorage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullSettingsArg() {
        new FileWorker(configuration, null, new JaxbMarshallizer(), new MockStorage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullMarshallizerArg() {
        doNothing().when(configuration).addDefaults(any());
        new FileWorker(configuration, new DummySettings(configuration), null, new MockStorage());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullStorageArg() {
        doNothing().when(configuration).addDefaults(any());
        new FileWorker(configuration, new DummySettings(configuration), new JaxbMarshallizer(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartExReloadPeriodZero() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(0);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), new MockStorage());

        worker.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartExReloadPeriodNegative() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(-1);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), new MockStorage());

        worker.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartExReloadDelayNegative() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(-1);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), new MockStorage());

        worker.start();
    }

    @Test
    public void testUpdateStoragePos() throws InterruptedException {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);

        when(configuration.getProperties()).thenReturn(new Properties() {{
            put(KEY_1, VALUE_1);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }});

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();
        Thread.sleep(TEST_EXECUTION_DURATION);
        worker.stop();

        String result = storage.getConfig();

        assertTrue(CONFIG_RESULTS.contains(result));
    }

    @Test
    public void testUpdateRuntimePos() throws InterruptedException {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);

        MockConfiguration configuration = new MockConfiguration();

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        storage.setConfig(CONFIG_1);

        worker.start();
        Thread.sleep(TEST_EXECUTION_DURATION);
        worker.stop();

        Properties result = configuration.properties;

        assertEquals(PROPERTIES_RESULT, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartExTwice() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();
        worker.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopExBeforeStart() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopExTwice() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();
        worker.stop();
        worker.stop();
    }

    @Test
    public void testStartStopPos() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        FileWorker watchdog = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        watchdog.start();
        watchdog.stop();
    }

    @Test
    public void testRestartPos() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();
        worker.restart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestartExReloadDelayNegative() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();

        when(settings.getStorageReloadDelay()).thenReturn(-1);

        worker.restart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestartExReloadPeriodZero() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();

        when(settings.getStorageReloadPeriod()).thenReturn(0);

        worker.restart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestartExReloadPeriodNegative() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.start();

        when(settings.getStorageReloadPeriod()).thenReturn(-1);

        worker.restart();
    }

    @Test(expected = IllegalStateException.class)
    public void testRestartExAfterStop() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.stop();
        worker.restart();
    }

    @Test(expected = IllegalStateException.class)
    public void testRestartExBeforeStart() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(settings.getStorageReloadDelay()).thenReturn(0);

        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        worker.restart();
    }

    @Test
    public void testSubscribedPos1() {
        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        boolean result = worker.subscribed(Settings.STORAGE_ENCODING);

        assertTrue(result);
    }

    @Test
    public void testSubscribedPos2() {
        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        boolean result = worker.subscribed(Settings.STORAGE_RELOAD_DELAY);

        assertTrue(result);
    }

    @Test
    public void testSubscribedPos3() {
        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        boolean result = worker.subscribed(Settings.STORAGE_RELOAD_PERIOD);

        assertTrue(result);
    }

    @Test
    public void testSubscribedNeg() {
        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        boolean result = worker.subscribed(Settings.STORAGE_SAVE_ON_EXIT);

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotifySubscriberPos() {
        worker = new FileWorker(
                configuration, settings, new JaxbMarshallizer(), storage = new MockStorage());

        when(settings.getStorageEncoding()).thenReturn("UTF-8");
        when(settings.getStorageReloadDelay()).thenReturn(0);
        when(settings.getStorageReloadPeriod()).thenReturn(30);
        when(configuration.getProperties()).thenReturn(new Properties());
        doNothing().when(configuration).addProperties(any());

        worker.start();

        when(settings.getStorageReloadDelay()).thenReturn(-1);

        worker.notifySubscriber();
    }
}
