package com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb;

import com.gmail.eksuzyan.pavel.education.market.config.marshaller.Marshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.entities.Property;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.entities.PropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Marshals and unmarshals configuration util forward and backward using JAXB context.
 */
public class JaxbMarshallizer implements Marshallizer {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JaxbMarshallizer.class);

    /**
     * JAXB context.
     */
    private static JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(Property.class, PropertySet.class);
        } catch (JAXBException e) {
            throw new AssertionError("Unable to initialize jaxb context. ", e);
        }
    }

    /**
     * Marshals a util object into an output stream.
     *
     * @param properties   util
     * @param outputStream outputStream
     * @throws NullPointerException     if util or outputStream is null
     * @throws IllegalArgumentException if util couldn't be marshalled into outputStream
     */
    @Override
    public void marshall(Properties properties, OutputStream outputStream) {
        Objects.requireNonNull(properties, "util must not be null");
        Objects.requireNonNull(outputStream, "outputStream must not be null");
        try {
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(cast(properties), outputStream);
        } catch (JAXBException e) {
            LOG.warn("Unable to marshall util into config storage. ", e);
            throw new IllegalArgumentException("Unable to unmarshall config storage into util. ", e);
        }
    }

    /**
     * Casts configuration util into JAXB-mapped util object.
     *
     * @param properties configuration util
     * @return JAXB-mapped util object
     */
    private static PropertySet cast(Properties properties) {
        return properties.entrySet().stream()
                .map(entry -> new Property((String) entry.getKey(), (String) entry.getValue()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), PropertySet::new));
    }

    /**
     * Unmarshals a util object from an input stream.
     *
     * @param inputStream inputStream
     * @return util
     * @throws NullPointerException     if inputStream is null
     * @throws IllegalArgumentException if util couldn't be unmarshalled from inputStream
     */
    @Override
    public Properties unmarshall(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream must not be null");
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return recast((PropertySet) unmarshaller.unmarshal(inputStream));
        } catch (JAXBException e) {
            LOG.warn("Unable to unmarshall config storage into util. ", e);
            throw new IllegalArgumentException("Unable to unmarshall config storage into util. ", e);
        }
    }

    /**
     * Recasts configuration util from JAXB-mapped util object.
     *
     * @param propertySet JAXB-mapped util object
     * @return configuration util
     */
    private static Properties recast(PropertySet propertySet) {
        Properties properties = new Properties();

        for (Property property : propertySet.getSet())
            properties.put(property.getKey(), property.getValue());

        return properties;
    }
}
