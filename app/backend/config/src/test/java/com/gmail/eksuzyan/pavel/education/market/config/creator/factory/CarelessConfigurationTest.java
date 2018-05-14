package com.gmail.eksuzyan.pavel.education.market.config.creator.factory;

import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CarelessConfigurationTest {

    @Mock
    private Subscriber subscriber1;

    @Mock
    private Subscriber subscriber2;

    private static final String KEY_1 = "com.gmail.eksuzyan.pavel.market.test.param.1";
    private static final String KEY_2 = "com.gmail.eksuzyan.pavel.market.test.param.2";
    private static final String KEY_3 = "com.gmail.eksuzyan.pavel.market.test.param.3";

    private static final String VALUE_1 = "one";
    private static final String VALUE_2 = "two";
    private static final String VALUE_3 = "three";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        System.clearProperty(KEY_1);
        System.clearProperty(KEY_2);
        System.clearProperty(KEY_3);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExDefaultPropsNull() {
        new CarelessConfiguration(null);
    }

    @Test
    public void testConstructorDefaultPropsPos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties() {{
            put(KEY_1, VALUE_1);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testConstructorDefaultPropsNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties() {{
            put(KEY_1, VALUE_1);
        }});

        Object result = configuration.getProperty(KEY_2, null);
        assertNotEquals(VALUE_1, result);
    }

    @Test
    public void testAddDefaultsPos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_1);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testAddDefaultsNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_1);
        }});

        Object result = configuration.getProperty(KEY_2, null);
        assertNotEquals(VALUE_1, result);
    }

    @Test
    public void testSubscribePos() throws InterruptedException, ExecutionException {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Boolean> task = () -> configuration.subscribe(subscriber1);

        Future<Boolean> future1 = executor.submit(task);
        Future<Boolean> future2 = executor.submit(task);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        assertTrue(result1 || result2);
    }

    @Test
    public void testSubscribeNeg() throws InterruptedException, ExecutionException {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Boolean> task = () -> configuration.subscribe(subscriber1);

        Future<Boolean> future1 = executor.submit(task);
        Future<Boolean> future2 = executor.submit(task);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        assertFalse(result1 && result2);
    }

    @Test
    public void testUnsubscribePos() throws InterruptedException, ExecutionException {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        assert configuration.subscribe(subscriber1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Boolean> task = () -> configuration.unsubscribe(subscriber1);

        Future<Boolean> future1 = executor.submit(task);
        Future<Boolean> future2 = executor.submit(task);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        assertTrue(result1 || result2);
    }

    @Test
    public void testUnsubscribeNeg() throws InterruptedException, ExecutionException {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        assert configuration.subscribe(subscriber1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Boolean> task = () -> configuration.unsubscribe(subscriber1);

        Future<Boolean> future1 = executor.submit(task);
        Future<Boolean> future2 = executor.submit(task);

        boolean result1 = future1.get();
        boolean result2 = future2.get();

        assertFalse(result1 && result2);
    }

    @Test
    public void testGetPropertyRuntimePos1() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);

        Object result = configuration.getProperty(KEY_1, null);

        assertEquals(VALUE_1, result);
    }

    @Test
    public void testGetPropertyRuntimePos2() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testGetPropertyRuntimePos3() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testGetPropertyDefaultNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertNotEquals(VALUE_2, result);
    }

    @Test
    public void testGetPropertyDefaultPos1() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_2, result);
    }

    @Test
    public void testGetPropertyDefaultPos2() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_2, result);
    }

    @Test
    public void testGetPropertySystemNeg1() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertNotEquals(VALUE_3, result);
    }

    @Test
    public void testGetPropertySystemNeg2() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertNotEquals(VALUE_3, result);
    }

    @Test
    public void testGetPropertySystemPos() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_3, result);
    }

    @Test
    public void testGetPropertyDefaultValuePos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        Object result = configuration.getProperty(KEY_1, "smth");
        assertEquals("smth", result);
    }

    @Test
    public void testAddPropertyPos1() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testAddPropertyPos2() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});
        configuration.addProperty(KEY_1, VALUE_1);

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testAddPropertyPos3() {
        System.setProperty(KEY_1, VALUE_3);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});
        configuration.addProperty(KEY_1, VALUE_1);

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testAddPropertyThenNotifySubscriberPos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        when(subscriber1.subscribed(KEY_1)).thenReturn(true);

        configuration.subscribe(subscriber1);

        configuration.addProperty(KEY_1, VALUE_1);

        verify(subscriber1, times(1)).notifySubscriber();
    }

    @Test
    public void testAddPropertyThenNotifySubscriberNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        when(subscriber1.subscribed(KEY_1)).thenReturn(true);

        configuration.subscribe(subscriber1);

        configuration.addProperty(KEY_2, VALUE_2);

        verify(subscriber1, never()).notifySubscriber();
    }

    @Test
    public void testAddPropertyThenNotifySubscribersPos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        when(subscriber1.subscribed(KEY_1)).thenReturn(true);
        when(subscriber2.subscribed(KEY_2)).thenReturn(true);

        configuration.subscribe(subscriber1);
        configuration.subscribe(subscriber2);

        configuration.addProperty(KEY_1, VALUE_1);

        verify(subscriber1, times(1)).notifySubscriber();
    }

    @Test
    public void testAddPropertyThenNotifySubscribersNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        when(subscriber1.subscribed(KEY_1)).thenReturn(true);
        when(subscriber2.subscribed(KEY_2)).thenReturn(true);

        configuration.subscribe(subscriber1);
        configuration.subscribe(subscriber2);

        configuration.addProperty(KEY_2, VALUE_2);

        verify(subscriber1, never()).notifySubscriber();
    }

    @Test
    public void testClearNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        configuration.clear();

        Object result = configuration.getProperty(KEY_1, null);
        assertNotEquals(VALUE_1, result);
    }

    @Test
    public void testClearPos() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        configuration.clear();

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_2, result);
    }

    @Test
    public void testAddPropertiesPos1() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperties(new Properties() {{
            put(KEY_1, VALUE_1);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }});

        Object result = configuration.getProperty(KEY_1, null);
        assertEquals(VALUE_1, result);
    }

    @Test
    public void testAddPropertiesPos2() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperties(new Properties() {{
            put(KEY_1, VALUE_1);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }});

        Object result = configuration.getProperty(KEY_2, null);
        assertEquals(VALUE_2, result);
    }

    @Test
    public void testAddPropertiesPos3() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperties(new Properties() {{
            put(KEY_1, VALUE_1);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }});

        Object result = configuration.getProperty(KEY_3, null);
        assertEquals(VALUE_3, result);
    }

    @Test
    public void testAddPropertiesNeg() {
        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperties(new Properties() {{
            put(KEY_1, VALUE_1);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }});

        configuration.addProperties(new Properties() {{
            put(KEY_1, VALUE_2);
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_2);
        }});

        Object result = configuration.getProperty(KEY_1, null);

        assertNotEquals(VALUE_1, result);
    }

    @Test
    public void testGetPropertiesPos1() {
        Properties expected = new Properties() {{
            put(KEY_2, VALUE_2);
        }};

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_2, VALUE_2);

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesNeg1() {
        Properties expected = new Properties();

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_2);

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesPos2() {
        Properties expected = new Properties() {{
            put(KEY_2, VALUE_2);
        }};

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_2, VALUE_2);
        }});

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesNeg2() {
        Properties expected = new Properties();

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addDefaults(new Properties() {{
            put(KEY_1, VALUE_2);
        }});

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesPos3() {
        Properties expected = new Properties() {{
            put(KEY_2, VALUE_2);
            put(KEY_3, VALUE_3);
        }};

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_3, VALUE_3);
        configuration.addDefaults(new Properties() {{
            put(KEY_2, VALUE_2);
        }});

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesNeg3() {
        Properties expected = new Properties();

        System.setProperty(KEY_1, VALUE_1);
        System.setProperty(KEY_2, VALUE_2);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_1, VALUE_1);
        configuration.addDefaults(new Properties() {{
            put(KEY_2, VALUE_2);
        }});

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesPos4() {
        Properties expected = new Properties() {{
            put(KEY_2, VALUE_2);
        }};

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        configuration.addProperty(KEY_2, VALUE_2);

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPropertiesNeg4() {
        Properties expected = new Properties();

        System.setProperty(KEY_1, VALUE_1);

        CarelessConfiguration configuration = new CarelessConfiguration(new Properties());

        Properties result = configuration.getProperties();
        assertEquals(expected, result);
    }

}
