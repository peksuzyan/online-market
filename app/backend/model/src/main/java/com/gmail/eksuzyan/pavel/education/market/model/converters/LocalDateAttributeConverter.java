package com.gmail.eksuzyan.pavel.education.market.model.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate attribute) {
        return attribute != null ? Date.valueOf(attribute) : null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbData) {
        return dbData != null ? dbData.toLocalDate() : null;
    }
}
