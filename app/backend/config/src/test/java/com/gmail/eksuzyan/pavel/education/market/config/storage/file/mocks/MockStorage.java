package com.gmail.eksuzyan.pavel.education.market.config.storage.file.mocks;

import com.gmail.eksuzyan.pavel.education.market.config.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class MockStorage implements Storage {

    private String config;

    private OutputStream outputStream;

    @Override
    public OutputStream createOutputStream() throws IOException {
        if (config != null)
            throw new IOException("Config mock already exists. ");

        return outputStream = new OutputStream() {
            private final byte[] content = new byte[10_000];
            private int index = 0;

            @Override
            public void write(int b) {
                content[index++] = (byte) b;
            }

            @Override
            public String toString() {
                return new String(Arrays.copyOf(content, index));
            }
        };
    }

    @Override
    public InputStream createInputStream() throws IOException {
        if (config == null)
            throw new IOException("Config mock doesn't exist yet. ");

        return new InputStream() {
            private final byte[] content = config.getBytes();
            private int index = 0;

            @Override
            public int read() {
                return index < content.length ? content[index++] : -1;
            }
        };
    }

    @Override
    public boolean available() {
        return config != null;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return outputStream.toString();
    }
}
