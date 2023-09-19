package com.Project.Project.config;

import com.Project.Project.model.ComplexField;
import com.Project.Project.model.Field;

import java.util.List;

public class ControleFile {
    private static List<Field> fieldControls;
    private static List<ComplexField> complexFieldControls;

    public ControleFile() {
    }

    public static List<ComplexField> getComplexFieldControls() {
        return complexFieldControls;
    }



    public static List<Field> getFieldControls() {
        return fieldControls;
    }

    public static void setComplexFieldControls(List<ComplexField> complexFieldControls) {
        ControleFile.complexFieldControls = complexFieldControls;
    }

    public static void setFieldControls(List<Field> fieldControls) {
        ControleFile.fieldControls = fieldControls;
    }
}
