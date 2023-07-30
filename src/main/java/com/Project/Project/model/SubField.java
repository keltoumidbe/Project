package com.Project.Project.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JacksonXmlRootElement(localName = "SubField")
public class SubField {
    @JacksonXmlProperty( isAttribute = true,localName = "Tag")
    private String tag;
    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    private String value;
}
