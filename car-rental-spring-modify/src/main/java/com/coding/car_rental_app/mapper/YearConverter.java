package com.coding.car_rental_app.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Year;
//@Converter(autoApply = true) //applied to all fields
public class YearConverter  {
    //implements AttributeConverter<Year, Integer>
    /*
    @Override  //from year-> integer to be stored in db
    public Integer convertToDatabaseColumn(Year year) {
        return year != null ? year.getValue() : null;
    }

    @Override  //from integer-> year to be retrieved from db
    public Year convertToEntityAttribute(Integer dbData) {
        return dbData != null ? Year.of(dbData) : null;
    }*/
}
