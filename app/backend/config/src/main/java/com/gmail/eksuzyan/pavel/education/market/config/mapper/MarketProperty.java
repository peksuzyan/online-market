package com.gmail.eksuzyan.pavel.education.market.config.mapper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MarketProperty {

    @XmlAttribute(name = "key")
    public String key;

    @XmlAttribute(name = "value")
    public String value;

    public MarketProperty() {
    }

    private MarketProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    static MarketProperty of(String key, String value) {
        return new MarketProperty(key, value);
    }
}
