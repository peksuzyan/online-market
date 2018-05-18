package com.gmail.eksuzyan.pavel.education.market.config.storage.file;

import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FileStorageTest {

    private static final String DEFAULT_FILE_NAME = "config-test.xml";
    private static final Path DEFAULT_FILE_PATH = Paths.get(DEFAULT_FILE_NAME);
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Mock
    private Settings settings;

    private static final String CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<Market>\n" +
                    "    <Properties>\n" +
                    "        <property key=\"com.gmail.eksuzyan.pavel.market.test.param.1\" value=\"one\"/>\n" +
                    "        <property key=\"com.gmail.eksuzyan.pavel.market.test.param.2\" value=\"two\"/>\n" +
                    "        <property key=\"com.gmail.eksuzyan.pavel.market.test.param.3\" value=\"three\"/>\n" +
                    "    </Properties>\n" +
                    "</Market>\n";

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Files.deleteIfExists(DEFAULT_FILE_PATH);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(DEFAULT_FILE_PATH);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorExNullArg() {
        new FileStorage(null);
    }

    @Test
    public void testCreateInputStream() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        String result;
        try (InputStream inputStream = storage.createInputStream()) {
            result = IOUtils.toString(inputStream, DEFAULT_CHARSET);
        }

        assertEquals(CONTENT, result);
    }

    @Test(expected = NoSuchFileException.class)
    public void testCreateInputStreamExNoSuchFile() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        storage.createInputStream();
    }

    @Test
    public void testCreateOutputStream() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        try (OutputStream outputStream = storage.createOutputStream()) {
            IOUtils.write(CONTENT, outputStream, DEFAULT_CHARSET);
        }

        String result = new String(Files.readAllBytes(DEFAULT_FILE_PATH));

        assertEquals(CONTENT, result);
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateOutputStreamExFileAlreadyExists() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        storage.createOutputStream();
    }

    @Test
    public void testAvailablePos() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        assertTrue(storage.available());
    }

    @Test
    public void testAvailableNeg() throws IOException {
        when(settings.getStorageName()).thenReturn(DEFAULT_FILE_NAME);
        FileStorage storage = new FileStorage(settings);

        Files.deleteIfExists(DEFAULT_FILE_PATH);

        assertFalse(storage.available());
    }


}
