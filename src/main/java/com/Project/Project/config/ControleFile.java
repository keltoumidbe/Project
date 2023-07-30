package com.Project.Project.config;

import com.Project.Project.model.ComplexField;
import com.Project.Project.model.Field;

import java.util.List;

public class ControleFile {
    private List<Field> fieldControls;
    private List<ComplexField> complexFieldControls;

    public ControleFile() {
    }

    public List<ComplexField> getComplexFieldControls() {
        return complexFieldControls;
    }

    public void setComplexFieldControls(List<ComplexField> complexFieldControls) {
        this.complexFieldControls = complexFieldControls;
    }

    public List<Field> getFieldControls() {
        return fieldControls;
    }

    public void setFieldControls(List<Field> fieldControls) {
        this.fieldControls = fieldControls;
    }
}
