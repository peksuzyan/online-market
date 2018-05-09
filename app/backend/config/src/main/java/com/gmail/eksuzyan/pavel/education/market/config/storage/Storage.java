package com.gmail.eksuzyan.pavel.education.market.config.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Transfers data into and from configuration storage.
 */
public interface Storage {

    /**
     * Creates output stream through which data is transferred into configuration storage.
     * <code>OutputStream</code> has to be closed after it's no longer needed.
     *
     * @return outputStream
     * @throws IOException if <code>OutputStream</code> couldn't be created
     */
    OutputStream createOutputStream() throws IOException;

    /**
     * Creates input stream through which data is transferred from configuration storage.
     * <code>InputStream</code> has to be closed after it's no longer needed.
     *
     * @return inputStream
     * @throws IOException if <code>InputStream</code> couldn't be created
     */
    InputStream createInputStream() throws IOException;

    /**
     * Checks whether storage is available or not.
     *
     * @return storage is available or not
     */
    boolean available();

}
