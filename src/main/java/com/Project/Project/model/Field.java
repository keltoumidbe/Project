/*
 * Copyright (c) 2022. HPS Solution.
 * Author: ismail.chakour@hps-worldwide.com (Ismail CHAKOUR)
 */

package com.Project.Project.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

@Data

@ToString
@JacksonXmlRootElement(localName = "Field")
public class Field implements Comparable<Field> {
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    private String number;
    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    private String value;

    public Field() {
    }

    @Override
    public int compareTo(Field field) {
        return Integer.parseInt(this.getNumber()) - Integer.parseInt(field.getNumber());
    }
}
