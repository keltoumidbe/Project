package com.Project.Project.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@JacksonXmlRootElement(localName = "ComplexField")
public class ComplexField extends Field {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SubField> subFields = new ArrayList<>();

    public List<SubField> getSubFieldControls() {
        return subFields;
    }

}
