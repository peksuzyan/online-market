package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummyConfiguration;
import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummySettings;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        new JmxWorker(null, new DummySettings(), new JaxbMarshallizer());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullSettingsArg() {
        new JmxWorker(new DummyConfiguration(), null, new JaxbMarshallizer());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullMarshallizerArg() {
        new JmxWorker(new DummyConfiguration(), new DummySettings(), null);
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


}
