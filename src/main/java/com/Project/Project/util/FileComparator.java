package com.Project.Project.util;

import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;

import com.Project.Project.model.Field;
import com.Project.Project.model.SubField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;

public class FileComparator {
    private String referenceFilePath;
    private String resultFilePath;
    private int error;



    public FileComparator(String referenceFilePath, String resultFilePath) {
        this.referenceFilePath = referenceFilePath;
        this.resultFilePath = resultFilePath;

    }

    public void compareFields(List<Field> fieldControls) {
        try {
            Document referenceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(referenceFilePath));
            Document resultDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(resultFilePath));

            for (Field fieldControl : fieldControls) {
                String fieldNumber = fieldControl.getNumber();

                String resultValue = getFieldElementValue(resultDoc, fieldNumber);
                if (resultValue == null) {
                    System.out.println("Field " + fieldNumber + " : Le champ n'existe pas dans le fichier de résultat.\n");
                    error++;

                    continue;
                }

                String referenceValue = getFieldElementValue(referenceDoc, fieldNumber);

                if (referenceValue.equals(resultValue)) {
                    System.out.println("Field "+ fieldNumber + ":");
                    System.out.println("Expected : " + referenceValue);
                    System.out.println("Actual   : " + resultValue);
                    System.out.println("Les valeurs sont identiques.\n");
                } else {
                    System.out.println("Field " + fieldNumber + ":");
                    System.out.println("Expected : " + referenceValue);
                    System.out.println("Actual   : " + resultValue);
                    System.out.println("Les valeurs sont differentes .\n");
                    error++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFieldElementValue(Document doc, String fieldNumber) {
        NodeList fieldList = doc.getElementsByTagName("field");
        for (int i = 0; i < fieldList.getLength(); i++) {
            Element fieldElement = (Element) fieldList.item(i);
            String number = fieldElement.getAttribute("Number");
            if (number.equals(fieldNumber)) {
                return fieldElement.getAttribute("Value");
            }
        }
        return null;
    }

    public void compareSubFields(List<SubField> subFieldControls, String complexFieldNumber) {
        try {
            Document referenceDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(referenceFilePath));
            Document resultDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(resultFilePath));

            for (SubField subFieldControl : subFieldControls) {
                String subFieldTag = subFieldControl.getTag();

                String referenceValue = getSubFieldElementValue(referenceDoc, complexFieldNumber, subFieldTag);
                String resultValue = getSubFieldElementValue(resultDoc, complexFieldNumber, subFieldTag);

                if (resultValue == null) {
                    System.out.println("Sous champ " + subFieldTag + " : n'existe pas dans le fichier de résultat.\n");
                    error++;
                    continue;
                } else if (referenceValue != null && referenceValue.equals(resultValue)) {
                    System.out.println("SubField " + subFieldTag + ":");
                    System.out.println("Expected : " + referenceValue);
                    System.out.println("Actual   : " + resultValue);
                    System.out.println("Les valeurs des sous champs sont identiques.\n");
                } else {
                    System.out.println("SubField " + subFieldTag + ":");
                    System.out.println("Expected : " + referenceValue);
                    System.out.println("Actual   : " + resultValue);
                    System.out.println("Les valeurs des sous champs sont differentes .\n");
                    error++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getSubFieldElementValue(Document doc, String complexFieldNumber, String subFieldTag) {
        NodeList complexFieldList = doc.getElementsByTagName("complexFields");
        for (int i = 0; i < complexFieldList.getLength(); i++) {
            Element complexFieldElement = (Element) complexFieldList.item(i);
            String number = complexFieldElement.getAttribute("Number");
            if (number.equals(complexFieldNumber)) {
                NodeList subFieldList = complexFieldElement.getElementsByTagName("subFields");
                for (int j = 0; j < subFieldList.getLength(); j++) {
                    Element subFieldElement = (Element) subFieldList.item(j);
                    String tag = subFieldElement.getAttribute("Tag");
                    if (tag.equals(subFieldTag)) {
                        return subFieldElement.getAttribute("Value");
                    }
                }
            }
        }
        return null;
    }
    public void printErrorCounts() {
        System.out.println("Le nombre total d'erreurs : " + error);
    }
}
