package com.gmail.eksuzyan.pavel.education.market.config.mapper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@XmlRootElement(name = "Market")
public class MarketProperties {

    @XmlElementWrapper(name = "Properties")
    @XmlElement(name = "property")
    public Set<MarketProperty> properties;

    public MarketProperties() {
    }

    private MarketProperties(Set<MarketProperty> properties) {
        this.properties = properties;
    }

    public static MarketProperties of(Map<String, String> properties) {
        return properties.entrySet().stream()
                .map(entry -> MarketProperty.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), MarketProperties::new));
    }

    public Map<String, String> toMap() {
        return properties.stream()
                .collect(Collectors.toMap(entry -> entry.key, entry -> entry.value));
    }
}
