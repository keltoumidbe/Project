package com.Project.Project.util;

import java.util.ArrayList;
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
    private List<String> Details = new ArrayList<>();




    public FileComparator(String referenceFilePath, String resultFilePath) {
        this.referenceFilePath = referenceFilePath;
        this.resultFilePath = resultFilePath;

    }

    public void compareFields(List<Field> fieldControls) {
        try {
            Document refdoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(referenceFilePath));
            Document resdoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(resultFilePath));

            for (Field fieldControl : fieldControls) {
                String fieldNumber = fieldControl.getNumber();

                String resultValue = getFieldElementValue(resdoc, fieldNumber);
                if (resultValue == null) {
                    error++;

                    continue;
                }

                String referenceValue = getFieldElementValue(refdoc, fieldNumber);

                if (!referenceValue.equals(resultValue)) {

                    Details.add("Field " + fieldNumber + ": Expected=" + referenceValue + ", Actual=" + resultValue);

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
                String subfieldTag = subFieldControl.getTag();

                String referenceValue = getsubfieldvalue(referenceDoc, complexFieldNumber, subfieldTag);
                String resultValue = getsubfieldvalue(resultDoc, complexFieldNumber, subfieldTag);

                if (resultValue == null) {
                    error++;
                    continue;
                } else if (!referenceValue.equals(resultValue)) {
                    Details.add("SubField " + subfieldTag + ": Expected=" + referenceValue + ", Actual=" + resultValue);

                    error++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> getDetails() {
        return Details;
    }


    private String getsubfieldvalue(Document doc, String complexFieldNumber, String subFieldTag) {
        NodeList complexfieldList = doc.getElementsByTagName("complexFields");
        for (int i = 0; i < complexfieldList.getLength(); i++) {
            Element complexfieldEl = (Element) complexfieldList.item(i);
            String number = complexfieldEl.getAttribute("Number");
            if (number.equals(complexFieldNumber)) {
                NodeList subFieldList = complexfieldEl.getElementsByTagName("subFields");
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
    public int getErrorCount() {
        return error;
    }

}
