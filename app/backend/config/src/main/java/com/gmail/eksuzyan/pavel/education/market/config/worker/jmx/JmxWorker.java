package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.Marshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.subscriber.restartable.RestartableSubscriber;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.worker.Worker;
import com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.mbean.JmxProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Objects;

import static com.gmail.eksuzyan.pavel.education.market.config.util.Settings.STORAGE_ENCODING;
import static com.gmail.eksuzyan.pavel.education.market.config.worker.Worker.State.RUNNING;
import static com.gmail.eksuzyan.pavel.education.market.config.worker.Worker.State.STOPPED;

/**
 * Provides an interface to manage the configuration via JMX.
 */
public class JmxWorker extends RestartableSubscriber implements Worker {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JmxWorker.class);

    /**
     * MBean server.
     */
    private static final MBeanServer MBEAN_SERVER = ManagementFactory.getPlatformMBeanServer();

    /**
     * MBean object name.
     */
    private static ObjectName objectName;

    static {
        try {
            objectName = new ObjectName(
                    JmxWorker.class.getPackage() + ":type=" + JmxWorker.class.getSimpleName());
        } catch (MalformedObjectNameException e) {
            throw new AssertionError("Unable to initialize mbean object name. ", e);
        }
    }

    /**
     * Configuration.
     */
    private final Configuration configuration;

    /**
     * Settings.
     */
    private final Settings settings;

    /**
     * Properties marshaller.
     */
    private final Marshallizer marshallizer;

    /**
     * Jmx encoding.
     */
    private Charset charset;

    /**
     * Worker state.
     */
    private State state = STOPPED;

    /**
     * Sole constructor.
     *
     * @param configuration configuration
     * @param settings      util
     * @param marshallizer  marshaller
     * @throws NullPointerException if arg is null
     */
    public JmxWorker(Configuration configuration, Settings settings, Marshallizer marshallizer) {
        super(Collections.singletonList(STORAGE_ENCODING));

        this.configuration = Objects.requireNonNull(configuration);
        this.settings = Objects.requireNonNull(settings);
        this.marshallizer = Objects.requireNonNull(marshallizer);

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

        register();

        state = RUNNING;

        LOG.debug("Worker started. ");
    }

    /**
     * Registers object by specified mbean name.
     */
    private void register() {
        if (!MBEAN_SERVER.isRegistered(objectName))
            try {
                JmxProperties jmxObject =
                        new JmxProperties(configuration, marshallizer, charset);

                MBEAN_SERVER.registerMBean(jmxObject, objectName);

                LOG.debug("Mbean object registered. ");
            } catch (NotCompliantMBeanException
                    | InstanceAlreadyExistsException
                    | MBeanRegistrationException e) {
                LOG.warn("Unable to register mbean object. ", e);
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

        unregister();

        state = STOPPED;

        LOG.debug("Worker stopped. ");
    }

    /**
     * Unregisters object by specified mbean name.
     */
    private void unregister() {
        if (MBEAN_SERVER.isRegistered(objectName))
            try {
                MBEAN_SERVER.unregisterMBean(objectName);

                LOG.debug("Mbean object unregistered. ");
            } catch (InstanceNotFoundException | MBeanRegistrationException e) {
                LOG.warn("Unable to unregister mbean object. ", e);
            }
    }
}
