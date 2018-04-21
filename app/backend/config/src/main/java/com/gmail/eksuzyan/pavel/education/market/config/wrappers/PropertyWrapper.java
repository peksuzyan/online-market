package com.gmail.eksuzyan.pavel.education.market.config.wrappers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PropertyWrapper {

    @SuppressWarnings("WeakerAccess")
    @XmlAttribute(name = "key")
    public String key;

    @SuppressWarnings("WeakerAccess")
    @XmlAttribute(name = "value")
    public String value;

    @SuppressWarnings("unused")
    public PropertyWrapper() {
    }

    PropertyWrapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
