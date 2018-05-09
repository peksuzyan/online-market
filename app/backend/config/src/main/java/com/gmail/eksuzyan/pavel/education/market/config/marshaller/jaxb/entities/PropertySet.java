package com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "Market")
public class PropertySet {

    private Set<Property> set;

    @SuppressWarnings("unused")
    public PropertySet() {
    }

    public PropertySet(Set<Property> set) {
        this.set = set;
    }

    @XmlElementWrapper(name = "Properties")
    @XmlElement(name = "property")
    public Set<Property> getSet() {
        return set;
    }

    public void setSet(Set<Property> set) {
        this.set = set;
    }
}
