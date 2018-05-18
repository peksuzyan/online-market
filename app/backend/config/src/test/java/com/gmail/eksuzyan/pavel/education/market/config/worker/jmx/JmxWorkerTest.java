package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummySettings;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class JmxWorkerTest {

    @Mock
    private Configuration configuration;

    @Mock
    private Settings settings;

    private JmxWorker worker;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullConfigurationArg() {
        doNothing().when(configuration).addDefaults(any());
        new JmxWorker(null, new DummySettings(configuration), new JaxbMarshallizer());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullSettingsArg() {
        new JmxWorker(configuration, null, new JaxbMarshallizer());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullMarshallizerArg() {
        doNothing().when(configuration).addDefaults(any());
        new JmxWorker(configuration, new DummySettings(configuration), null);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartExTwice() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.start();
        worker.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopExBeforeStart() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopExTwice() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.start();
        worker.stop();
        worker.stop();
    }

    @Test
    public void testStartAfterStopPos() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.start();
        worker.stop();
        worker.start();
    }

    @Test
    public void testRestartPos() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.start();
        worker.restart();
    }

    @Test(expected = IllegalStateException.class)
    public void testRestartExAfterStop() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.stop();
        worker.restart();
    }

    @Test(expected = IllegalStateException.class)
    public void testRestartExBeforeStart() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.restart();
    }

    @Test(expected = RuntimeException.class)
    public void testRestartExCharsetWrong() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker.start();

        when(settings.getStorageEncoding()).thenReturn("wrong_charset");

        worker.restart();
    }

    @Test
    public void testCreateAndStartTwoWorkers() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        JmxWorker worker1 = new JmxWorker(configuration, settings, new JaxbMarshallizer());
        JmxWorker worker2 = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker1.start();
        worker2.start();
    }

    @Test
    public void testCreateAndStopTwoWorkers() {
        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        JmxWorker worker1 = new JmxWorker(configuration, settings, new JaxbMarshallizer());
        JmxWorker worker2 = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        worker1.start();
        worker2.start();

        worker1.stop();
        worker2.stop();
    }

    @Test
    public void testSubscribedPos() {
        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        boolean result = worker.subscribed(Settings.STORAGE_ENCODING);

        assertTrue(result);
    }

    @Test
    public void testSubscribedNeg() {
        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        boolean result = worker.subscribed(Settings.STORAGE_NAME);

        assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testNotifySubscriberPos() {
        worker = new JmxWorker(configuration, settings, new JaxbMarshallizer());

        when(settings.getStorageEncoding()).thenReturn("UTF-8");

        worker.start();

        when(settings.getStorageEncoding()).thenReturn("wrong_encoding");

        worker.notifySubscriber();
    }
}
