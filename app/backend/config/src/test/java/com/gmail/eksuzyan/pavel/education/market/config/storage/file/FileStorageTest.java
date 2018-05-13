package com.gmail.eksuzyan.pavel.education.market.config.storage.file;

import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummyConfiguration;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummySettings;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.*;

import static org.junit.Assert.*;

public class FileStorageTest {

    private static final String DEFAULT_FILE_NAME = "config-test.xml";
    private static final Path DEFAULT_FILE_PATH = Paths.get(DEFAULT_FILE_NAME);
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final Settings SETTINGS = new DummySettings(new DummyConfiguration()) {
        @Override
        public String getStorageName() {
            return DEFAULT_FILE_NAME;
        }
    };

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
        FileStorage storage = new FileStorage(SETTINGS);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        String result;
        try (InputStream inputStream = storage.createInputStream()) {
            result = IOUtils.toString(inputStream, DEFAULT_CHARSET);
        }

        assertEquals(CONTENT, result);
    }

    @Test(expected = NoSuchFileException.class)
    public void testCreateInputStreamExNoSuchFile() throws IOException {
        FileStorage storage = new FileStorage(SETTINGS);

        storage.createInputStream();
    }

    @Test
    public void testCreateOutputStream() throws IOException {
        FileStorage storage = new FileStorage(SETTINGS);

        try (OutputStream outputStream = storage.createOutputStream()) {
            IOUtils.write(CONTENT, outputStream, DEFAULT_CHARSET);
        }

        String result = new String(Files.readAllBytes(DEFAULT_FILE_PATH));

        assertEquals(CONTENT, result);
    }

    @Test(expected = FileAlreadyExistsException.class)
    public void testCreateOutputStreamExFileAlreadyExists() throws IOException {
        FileStorage storage = new FileStorage(SETTINGS);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        storage.createOutputStream();
    }

    @Test
    public void testAvailablePos() throws IOException {
        FileStorage storage = new FileStorage(SETTINGS);

        Files.write(DEFAULT_FILE_PATH, CONTENT.getBytes(DEFAULT_CHARSET), StandardOpenOption.CREATE);

        assertTrue(storage.available());
    }

    @Test
    public void testAvailableNeg() throws IOException {
        FileStorage storage = new FileStorage(SETTINGS);

        Files.deleteIfExists(DEFAULT_FILE_PATH);

        assertFalse(storage.available());
    }


}
