package com.gmail.eksuzyan.pavel.education.market.config.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@XmlRootElement(name = "Market")
public class PropertiesWrapper {

    @SuppressWarnings("WeakerAccess")
    @XmlElementWrapper(name = "Properties")
    @XmlElement(name = "property")
    public Set<PropertyWrapper> properties;

    @SuppressWarnings("unused")
    public PropertiesWrapper() {
    }

    private PropertiesWrapper(Set<PropertyWrapper> properties) {
        this.properties = properties;
    }

    public static PropertiesWrapper of(Properties properties) {
        return properties.entrySet().stream()
                .map(entry -> new PropertyWrapper((String) entry.getKey(), (String) entry.getValue()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), PropertiesWrapper::new));
    }

    public Properties toProperties() {
        Properties props = new Properties();

        for (PropertyWrapper prop : properties)
            props.setProperty(prop.key, prop.value);

        return props;
    }
}
