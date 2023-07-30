package com.Project.Project;

import com.Project.Project.model.Field;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MicroserviceTest {

    public static void main(String[] args) {
        testMicroservice1();
        // Ajoutez d'autres appels de méthodes de test pour les autres fichiers de testCases et de reference
    }

    @Test
    public static void testMicroservice1() {
        // Lire le fichier de controle
        ControlFile controlFile = readControlFile("src/main/resources/controleFile.xml");

        // Lire le fichier de reference
        BaseMessage referenceMessage = readReferenceFile("src/main/resources/ref/reference.xml");

        // Lire le fichier de test
        BaseMessage testMessage = readTestFile("src/main/resources/testCases/testcase1.xml");

        // Vérifier les champs spécifiés dans le controlFile
        boolean result = compareFields(testMessage, referenceMessage, controlFile);

        // Afficher le résultat du test
        if (result) {
            System.out.println("Test Case: testcase1.xml - OK");
        } else {
            System.out.println("Test Case: testcase1.xml - NOK");
        }
    }

    // Ajouter d'autres méthodes de test pour les autres fichiers de testCases et de reference

    private static ControlFile readControlFile(String fileName) {
        try {
            File file = new File(fileName);
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(file, ControlFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BaseMessage readReferenceFile(String fileName) {
        try {
            File file = new File(fileName);
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(file, BaseMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BaseMessage readTestFile(String fileName) {
        try {
            File file = new File(fileName);
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(file, BaseMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean compareFields(BaseMessage testMessage, BaseMessage referenceMessage, ControlFile controlFile) {
        // Comparer les fields spécifiés dans le controlFile
        for (Field field : controlFile.getFields()) {
            String number = field.getNumber();
            String expectedValue = field.getValue();
            String actualValue = getField(testMessage, number);
            String expectedValueReference = getField(referenceMessage, number);

            if (!expectedValue.equals(actualValue)) {
                System.out.println("- Field " + number + ": Expected value: " + expectedValueReference + ", Actual value: " + actualValue);
                return false;
            }
        }

        // Comparer les complexFields spécifiés dans le controlFile
        for (ComplexField complexField : controlFile.getComplexFields()) {
            String number = complexField.getNumber();
            List<SubField> subFields = complexField.getSubFields();
            List<SubField> expectedSubFields = getComplexField(testMessage, number).getSubFields();
            List<SubField> expectedSubFieldsReference = getComplexField(referenceMessage, number).getSubFields();

            if (!compareSubFields(subFields, expectedSubFields, expectedSubFieldsReference)) {
                return false;
            }
        }

        return true;
    }

    private static boolean compareSubFields(List<SubField> subFields, List<SubField> expectedSubFields, List<SubField> expectedSubFieldsReference) {
        // Vérifier que le nombre de sous-champs est le même dans les fichiers de test et de référence
        if (subFields.size() != expectedSubFields.size() || subFields.size() != expectedSubFieldsReference.size()) {
            System.out.println("- Number of subfields mismatch.");
            return false;
        }

        // Comparer les valeurs des sous-champs pour chaque complexField
        for (int i = 0; i < subFields.size(); i++) {
            SubField subField = subFields.get(i);
            SubField expectedSubField = expectedSubFields.get(i);
            SubField expectedSubFieldReference = expectedSubFieldsReference.get(i);

            // Vérifier que le numéro de sous-champ est le même dans les fichiers de test et de référence
            if (!subField.getTag().equals(expectedSubField.getTag()) || !subField.getTag().equals(expectedSubFieldReference.getTag())) {
                System.out.println("- Subfield number mismatch.");
                return false;
            }

            // Comparer les valeurs des sous-champs dans les fichiers de test et de référence
            String actualValue = subField.getValue();
            String expectedValue = expectedSubField.getValue();
            String expectedValueReference = expectedSubFieldReference.getValue();

            if (!expectedValue.equals(actualValue) || !expectedValue.equals(expectedValueReference)) {
                System.out.println("- SubField " + subField.getTag() + ": Expected value: " + expectedValueReference + ", Actual value: " + actualValue);
                return false;
            }
        }

        return true;
    }



    private static String getField(BaseMessage message, String number) {
        for (Field field : message.getField()) {
            if (field.getNumber().equals(number)) {
                return field.getValue();
            }
        }
        return null;
    }

    private static ComplexField getComplexField(BaseMessage message, String number) {
        for (ComplexField complexField : message.getComplexFields()) {
            if (complexField.getNumber().equals(number)) {
                return complexField;
            }
        }
        return null;
    }
}
