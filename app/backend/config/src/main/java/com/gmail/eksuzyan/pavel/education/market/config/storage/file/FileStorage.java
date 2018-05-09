package com.gmail.eksuzyan.pavel.education.market.config.storage.file;

import com.gmail.eksuzyan.pavel.education.market.config.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Objects;

/**
 * Transfers data into and from configuration storage using FileSystem.
 */
public class FileStorage implements Storage {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileStorage.class);

    /**
     * Settings.
     */
    private final Settings settings;

    /**
     * Sole constructor.
     *
     * @param settings settings
     * @throws NullPointerException if arg is null
     */
    public FileStorage(Settings settings) {
        this.settings = Objects.requireNonNull(settings);

        LOG.info("Storage initialized. ");
    }

    /**
     * Creates input stream through which data is transferred from configuration storage.
     * <code>InputStream</code> has to be closed after it's no longer needed.
     *
     * @return inputStream
     * @throws NoSuchFileException if file doesn't exist
     * @throws IOException         if other I/O error occurred
     */
    @Override
    public InputStream createInputStream() throws IOException {
        Path path = Paths.get(settings.getStorageName());
        return Files.newInputStream(path, StandardOpenOption.READ);
    }

    /**
     * Creates output stream through which data is transferred into configuration storage.
     * <code>OutputStream</code> has to be closed after it's no longer needed.
     *
     * @return outputStream
     * @throws FileAlreadyExistsException if file already exists
     * @throws IOException                if other I/O error occurred
     */
    @Override
    public OutputStream createOutputStream() throws IOException {
        Path path = Paths.get(settings.getStorageName());
        return Files.newOutputStream(path, StandardOpenOption.CREATE_NEW);
    }

    /**
     * Checks whether storage is available or not.
     *
     * @return storage is available or not
     */
    @Override
    public boolean available() {
        Path path = Paths.get(settings.getStorageName());
        boolean ready = Files.isRegularFile(path);
        LOG.debug("Storage exists, is file, can be read: {} ", ready);
        return ready;
    }
}
