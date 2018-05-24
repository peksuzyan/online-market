package com.gmail.eksuzyan.pavel.education.market.config.worker.file;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.Marshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.Storage;
import com.gmail.eksuzyan.pavel.education.market.config.subscriber.restartable.RestartableSubscriber;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.worker.Worker;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gmail.eksuzyan.pavel.education.market.config.util.Settings.*;
import static com.gmail.eksuzyan.pavel.education.market.config.worker.Worker.State.RUNNING;
import static com.gmail.eksuzyan.pavel.education.market.config.worker.Worker.State.STOPPED;

/**
 * Provides background process for storage management.
 */
public class FileWorker extends RestartableSubscriber implements Worker {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FileWorker.class);

    /**
     * Thread factory.
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "file-watchdog-" + counter.incrementAndGet());
        }
    };

    /**
     * Configuration.
     */
    private final Configuration configuration;

    /**
     * Settings.
     */
    private final Settings settings;

    /**
     * Marshaller.
     */
    private final Marshallizer marshallizer;

    /**
     * Storage.
     */
    private final Storage storage;

    /**
     * Worker state.
     */
    private State state = STOPPED;

    /**
     * Charset.
     */
    private Charset charset;

    /**
     * Storage content.
     */
    private String content;

    /**
     * Background tasks executor.
     */
    private ScheduledExecutorService executor;

    /**
     * Single constructor.
     *
     * @param configuration configuration
     * @param settings      util
     * @param marshallizer  marshaller
     * @param storage       storage
     * @throws NullPointerException if any of args is null
     */
    public FileWorker(Configuration configuration, Settings settings, Marshallizer marshallizer, Storage storage) {
        super(Arrays.asList(STORAGE_ENCODING, STORAGE_RELOAD_DELAY, STORAGE_RELOAD_PERIOD));

        this.configuration = Objects.requireNonNull(configuration);
        this.settings = Objects.requireNonNull(settings);
        this.marshallizer = Objects.requireNonNull(marshallizer);
        this.storage = Objects.requireNonNull(storage);

        LOG.debug("Worker initialized. ");
    }

    /**
     * Restarts itself.
     *
     * @throws IllegalStateException if subscriber isn't ready to restart
     */
    @Override
    protected void restart() {
        stop();
        start();
    }

    /**
     * Starts worker's resources. Operation is thread-safe.
     *
     * @throws IllegalStateException if worker isn't ready
     */
    @Override
    public void start() {
        if (state == RUNNING)
            throw new IllegalStateException("Worker is already running. ");

        charset = Charset.forName(settings.getStorageEncoding());

        int period = settings.getStorageReloadPeriod();
        if (period <= 0)
            throw new IllegalArgumentException("Storage reload period isn't positive. ");

        int delay = settings.getStorageReloadDelay();
        if (delay < 0)
            throw new IllegalArgumentException("Storage initial reload delay is negative. ");

        if (delay == 0) {
            refresh();
            delay = period;
        }

        executor = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);

        executor.scheduleWithFixedDelay(this::refresh, delay, period, TimeUnit.SECONDS);

        state = RUNNING;

        LOG.debug("Worker started. ");
    }

    /**
     * Refreshes runtime properties or storage config based on the storage availability.
     */
    private void refresh() {
        if (storage.available())
            updateRuntime();
        else
            updateStorage();
    }

    /**
     * Updates runtime properties from the storage if it's been modified.
     */
    private void updateRuntime() {
        String newContent = getCurrentConfig();
        if (!Objects.equals(content, newContent)) {
            content = newContent;
            updateCurrentConfig();
        }
        LOG.debug("Storage was checked on being modified. ");
    }

    /**
     * Returns config properties from storage in string format.
     *
     * @return string representation
     */
    private String getCurrentConfig() {
        try (InputStream is = storage.createInputStream()) {
            return IOUtils.toString(is, charset);
        } catch (IOException e) {
            LOG.warn("Unable to check whether storage was modified or not. ", e);
            return content;
        }
    }

    /**
     * Updates runtime properties from the storage.
     */
    private void updateCurrentConfig() {
        try (InputStream is = storage.createInputStream()) {
            Properties actualProps = marshallizer.unmarshall(is);
            configuration.addProperties(actualProps);
            LOG.info("Properties were updated from the storage. ");
        } catch (IOException e) {
            LOG.warn("Unable to update runtime util from storage. ", e);
        }
    }

    /**
     * Updates config storage by runtime properties.
     */
    private void updateStorage() {
        Properties actualProps = configuration.getProperties();
        try (OutputStream os = storage.createOutputStream()) {
            marshallizer.marshall(actualProps, os);
            LOG.info("Properties were loaded into the storage. ");
        } catch (IOException e) {
            LOG.warn("Unable to update config util into the storage. ", e);
        }
    }

    /**
     * Stops worker's resources. Operation is thread-safe.
     *
     * @throws IllegalStateException if worker isn't running
     */
    @Override
    public void stop() {
        if (state == STOPPED)
            throw new IllegalStateException("Worker already stopped. ");

        final int waitingTime = 2; // waiting time in seconds

        executor.shutdown();
        try {
            if (!executor.awaitTermination(waitingTime, TimeUnit.SECONDS)) {
                List<Runnable> list = executor.shutdownNow();
                LOG.debug("Total {} tasks have been cancelled.", list.size());
                if (!executor.awaitTermination(waitingTime, TimeUnit.SECONDS)) {
                    LOG.info("Worker didn't terminate correctly. ");
                }
            }
        } catch (InterruptedException ex) {
            LOG.warn("Worker's been interrupted unexpectedly. ", ex);
        }

        if (settings.getStorageSaveOnExit())
            updateStorage();

        state = STOPPED;

        LOG.debug("Worker stopped. ");
    }
}
