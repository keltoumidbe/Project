package com.Project.Project;

import com.Project.Project.config.ControleFile;
import com.Project.Project.model.ComplexField;
import com.Project.Project.model.Field;
import com.Project.Project.model.SubField;
import com.Project.Project.util.FileComparator;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class ProjectApplication {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Erreur:Veuillez fournir les répertoires de référence et de résultat");
			System.exit(1);
		}

		String referenceDirectory = args[0];
		String resultDirectory = args[1];


		ApplicationContext context = new ClassPathXmlApplicationContext("controleFile.xml");
		ControleFile controleFile = (ControleFile) context.getBean("controleFile");


		List<File> referenceFiles = getFilesInDirectory(referenceDirectory);
		List<File> resultFiles = getFilesInDirectory(resultDirectory);

		List<FilePair> filePairs = associateFiles(referenceFiles, resultFiles);

		Workbook workbook = new XSSFWorkbook();
		CellStyle blackBorder = workbook.createCellStyle();
		blackBorder.setAlignment(HorizontalAlignment.CENTER);
		blackBorder.setVerticalAlignment(VerticalAlignment.CENTER);
		blackBorder.setBorderBottom(BorderStyle.THIN);
		blackBorder.setBorderTop(BorderStyle.THIN);
		blackBorder.setBorderLeft(BorderStyle.THIN);
		blackBorder.setBorderRight(BorderStyle.THIN);

		CellStyle leftAligned = workbook.createCellStyle();
		leftAligned.setAlignment(HorizontalAlignment.LEFT);
		leftAligned.setBorderBottom(BorderStyle.THIN);
		leftAligned.setBorderTop(BorderStyle.THIN);
		leftAligned.setBorderLeft(BorderStyle.THIN);
		leftAligned.setBorderRight(BorderStyle.THIN);

		CellStyle red = workbook.createCellStyle();
		Font redFont = workbook.createFont();
		redFont.setColor(IndexedColors.RED.getIndex());
		red.setFont(redFont);
		red.setAlignment(HorizontalAlignment.LEFT);
		red.setVerticalAlignment(VerticalAlignment.CENTER);
		red.setBorderBottom(BorderStyle.THIN);
		red.setBorderTop(BorderStyle.THIN);
		red.setBorderLeft(BorderStyle.THIN);
		red.setBorderRight(BorderStyle.THIN);

		CellStyle redFontStyle2 = workbook.createCellStyle();
		Font redFont2 = workbook.createFont();
		redFont2.setColor(IndexedColors.RED.getIndex());
		redFontStyle2.setFont(redFont2);

		Sheet sheet = workbook.createSheet("Index");

		Row headerRow = sheet.createRow(0);
		Cell headerCell1 = headerRow.createCell(0);
		headerCell1.setCellValue("Test Id");
		headerCell1.setCellStyle(blackBorder);

		Cell headerCell2 = headerRow.createCell(1);
		headerCell2.setCellValue("Nb Errors");
		headerCell2.setCellStyle(blackBorder);

		int rowNum = 1;
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 4000);


		for (FilePair filePair : filePairs) {
			FileComparator fileComparator = new FileComparator(filePair.getReferenceFile().getPath(), filePair.getResultFile().getPath());
			fileComparator.compareFields(controleFile.getFieldControls());

			for (ComplexField complexFieldControl : controleFile.getComplexFieldControls()) {
				fileComparator.compareSubFields(complexFieldControl.getSubFieldControls(), complexFieldControl.getNumber());
			}

			List<String> Details = fileComparator.getDetails();

			System.out.println();
			int errorCount = fileComparator.getErrorCount();

			Row dataRow = sheet.createRow(rowNum++);
			String fileName = filePair.getReferenceFile().getName().replace("_ref.xml", "");
			Cell fileNameCell = dataRow.createCell(0);
			fileNameCell.setCellValue(fileName);
			fileNameCell.setCellStyle(blackBorder);

			Cell errorCountCell = dataRow.createCell(1);
			errorCountCell.setCellValue(errorCount);

			Sheet newSheet = workbook.createSheet(fileName);


			int summaryTableStartRow = 0;
			int referenceTableStartRow = summaryTableStartRow + (errorCount > 0 ? Details.size() + 2 : 1);
			referenceTableStartRow += 2;
			Row head = newSheet.createRow(referenceTableStartRow - 2);


			Cell ReferenceCell = head.createCell(1);
			ReferenceCell.setCellValue("Reference");
			ReferenceCell.setCellStyle(blackBorder);


			Cell ResultatCell = head.createCell(11);
			ResultatCell.setCellValue("Result");
			ResultatCell.setCellStyle(blackBorder);





			Row tableRef = newSheet.createRow(referenceTableStartRow - 1);

			Cell FieldcellRef = tableRef.createCell(0);
			FieldcellRef.setCellValue("Field");
			FieldcellRef.setCellStyle(blackBorder);

			Cell LengthcellRef = tableRef.createCell(1);
			LengthcellRef.setCellValue("Length");
			LengthcellRef.setCellStyle(blackBorder);

			Cell DatacellRef = tableRef.createCell(2);
			DatacellRef.setCellValue("Data");
			DatacellRef.setCellStyle(blackBorder);



			newSheet.createRow(referenceTableStartRow);

			Cell FieldcellRes = tableRef.createCell(10);
			FieldcellRes.setCellValue("Field");
			FieldcellRes.setCellStyle(blackBorder);

			Cell LengthcellRes = tableRef.createCell(11);
			LengthcellRes.setCellValue("Length");
			LengthcellRes.setCellStyle(blackBorder);

			Cell DatacellRes = tableRef.createCell(12);
			DatacellRes.setCellValue("Data");
			DatacellRes.setCellStyle(blackBorder);

			newSheet.setColumnWidth(0, 6000);
			newSheet.setColumnWidth(1, 6000);
			newSheet.setColumnWidth(2, 8500);
			newSheet.setColumnWidth(10, 6000);
			newSheet.setColumnWidth(11, 6000);
			newSheet.setColumnWidth(12, 8500);

			CreationHelper creationHelper = workbook.getCreationHelper();
			Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.DOCUMENT);
			hyperlink.setAddress("'" + fileName + "'!A1");
			fileNameCell.setHyperlink(hyperlink);

			CellStyle cellstyle;

			if (errorCount > 0) {
				cellstyle = red;
				Row summaryRow = newSheet.createRow(summaryTableStartRow);
				Cell summaryCell = summaryRow.createCell(0);
				summaryCell.setCellValue("Summary Table");
				summaryCell.setCellStyle(blackBorder);
				int rowIndex = summaryTableStartRow + 1;


				for (String discrepancy : Details) {
					Row discrepancyRow = newSheet.createRow(rowIndex++);
					Cell discrepancyCell = discrepancyRow.createCell(0);
					discrepancyCell.setCellValue(discrepancy);
					discrepancyCell.setCellStyle(redFontStyle2);
				}

			} else {
				cellstyle = leftAligned;
			}

			errorCountCell.setCellStyle(cellstyle);

			int referenceIndex = referenceTableStartRow;
			int resultIndex = referenceTableStartRow;

			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

				Document referenceDoc = dBuilder.parse(filePair.getReferenceFile());

				NodeList fieldList = referenceDoc.getElementsByTagName("field");

				for (int i = 0; i < fieldList.getLength(); i++) {
					Element fieldElement = (Element) fieldList.item(i);
					String fieldNumber = fieldElement.getAttribute("Number");
					String fieldValue = fieldElement.getAttribute("Value");

					Row rowDataRowRef = newSheet.createRow(referenceIndex);
					Cell numberCellRef = rowDataRowRef.createCell(0);
					numberCellRef.setCellValue("Field " + fieldNumber);
					numberCellRef.setCellStyle(leftAligned);

					Cell lengthCellRef = rowDataRowRef.createCell(1);
					lengthCellRef.setCellValue(fieldValue.length());
					lengthCellRef.setCellStyle(leftAligned);

					Cell valueCellRef = rowDataRowRef.createCell(2);
					valueCellRef.setCellValue(fieldValue);
					valueCellRef.setCellStyle(leftAligned);

					referenceIndex++;
				}

				Document resultDoc = dBuilder.parse(filePair.getResultFile());

				NodeList resultFieldList = resultDoc.getElementsByTagName("field");

				for (int i = 0; i < resultFieldList.getLength(); i++) {
					Element fieldElement = (Element) resultFieldList.item(i);
					String fieldNumber = fieldElement.getAttribute("Number");
					String fieldValue = fieldElement.getAttribute("Value");
					String referenceValue = getReferenceValue(referenceDoc, fieldNumber);

					Row DatarowRes = newSheet.getRow(resultIndex);
					if (DatarowRes == null) {
						DatarowRes = newSheet.createRow(resultIndex);
					}

					Cell numberCellResult = DatarowRes.createCell(10);
					numberCellResult.setCellValue("Field " + fieldNumber);
					numberCellResult.setCellStyle(leftAligned);

					Cell valueCellResult = DatarowRes.createCell(12);
					valueCellResult.setCellValue(fieldValue);

					if (ApplyRedFont(fieldNumber, referenceValue, fieldValue)) {
						valueCellResult.setCellStyle(redFontStyle2);
					} else {
						valueCellResult.setCellStyle(leftAligned);
					}
					Cell lengthCellRes = DatarowRes.createCell(11);
					lengthCellRes.setCellValue(fieldValue.length());
					lengthCellRes.setCellStyle(leftAligned);

					resultIndex++;
				}
				int rowIndexRef = referenceIndex;
				NodeList complexFieldsListRef = referenceDoc.getElementsByTagName("complexFields");
				for (int i = 0; i < complexFieldsListRef.getLength(); i++) {
					Element complexFieldsElement = (Element) complexFieldsListRef.item(i);
					String complexNumber = complexFieldsElement.getAttribute("Number");

					NodeList subFieldList = complexFieldsElement.getElementsByTagName("subFields");
					for (int j = 0; j < subFieldList.getLength(); j++) {
						Element subFieldElement = (Element) subFieldList.item(j);
						String subFieldTag = subFieldElement.getAttribute("Tag");
						String subFieldValue = subFieldElement.getAttribute("Value");

						Row subFieldRowRef = newSheet.getRow(rowIndexRef);
						if (subFieldRowRef == null) {
							subFieldRowRef = newSheet.createRow(rowIndexRef);
						}
						int subFieldValueLength = subFieldValue.length();

						Cell subFieldNumref = subFieldRowRef.createCell(0);
						subFieldNumref.setCellValue("Complex " + complexNumber + ", SubField " + subFieldTag);
						subFieldNumref.setCellStyle(leftAligned);
						Cell subFieldLengthref = subFieldRowRef.createCell(1);
						subFieldLengthref.setCellValue(subFieldValueLength);
						subFieldLengthref.setCellStyle(leftAligned);
						Cell subFieldValueref = subFieldRowRef.createCell(2);
						subFieldValueref.setCellValue(subFieldValue);
						subFieldValueref.setCellStyle(leftAligned);

						rowIndexRef++;
					}
				}

				int rowIndexResult = resultIndex;
				NodeList complexFieldsListRes = resultDoc.getElementsByTagName("complexFields");
				for (int i = 0; i < complexFieldsListRes.getLength(); i++) {
					Element complexFieldsElement = (Element) complexFieldsListRes.item(i);
					String complexNumber = complexFieldsElement.getAttribute("Number");

					NodeList subFieldList = complexFieldsElement.getElementsByTagName("subFields");
					for (int j = 0; j < subFieldList.getLength(); j++) {
						Element subFieldElement = (Element) subFieldList.item(j);
						String subFieldTag = subFieldElement.getAttribute("Tag");
						String subFieldValue = subFieldElement.getAttribute("Value");

						Row subFieldRowResult = newSheet.getRow(rowIndexResult);
						if (subFieldRowResult == null) {
							subFieldRowResult = newSheet.createRow(rowIndexResult);
						}
						int subFieldValueLength2 = subFieldValue.length();

						Cell subFieldNumberres = subFieldRowResult.createCell(10);
						subFieldNumberres.setCellValue("Complex " + complexNumber + ", SubField " + subFieldTag);
						subFieldNumberres.setCellStyle(leftAligned);

						Cell subFieldLengthref = subFieldRowResult.createCell(11);
						subFieldLengthref.setCellValue(subFieldValueLength2);
						subFieldLengthref.setCellStyle(leftAligned);

						Cell subFieldValueres = subFieldRowResult.createCell(12);
						subFieldValueres.setCellValue(subFieldValue);

						String referenceSubFieldValue = getReferenceSubFieldValue(referenceDoc, complexNumber, subFieldTag);

						if (ApplyRedFontSubField(complexNumber, subFieldTag, referenceSubFieldValue, subFieldValue)) {
							subFieldValueres.setCellStyle(redFontStyle2);
						} else {
							subFieldValueres.setCellStyle(leftAligned);
						}

						rowIndexResult++;
					}
				}


			} catch (ParserConfigurationException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (SAXException e) {
				throw new RuntimeException(e);
			}


		}
		try {
			FileOutputStream outputStream = new FileOutputStream("Report.xls");
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static String getReferenceValue(Document doc, String fieldNumber) {
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
	private static boolean ApplyRedFont(String fieldNumber, String referenceValue, String resultValue) {
		if (FieldNumberinspecifiedList(fieldNumber)) {
			return !Objects.equals(referenceValue, resultValue);
		}
		return false;
	}
	private static boolean FieldNumberinspecifiedList(String fieldNumber) {

		for (Field fieldControl : ControleFile.getFieldControls()) {
			if (fieldControl.getNumber().equals(fieldNumber)) {
				return true;
			}
		}
		return false;
	}
	private static boolean SubFieldinspecifiedList(String complexFieldNumber, String subFieldTag) {
		for (ComplexField complexField : ControleFile.getComplexFieldControls()) {
			if (complexField.getNumber().equals(complexFieldNumber)) {
				for (SubField subField : complexField.getSubFieldControls()) {
					if (subField.getTag().equals(subFieldTag)) {
						return true;
					}
				}
			}
		}
		return false;
	}



	private static boolean ApplyRedFontSubField(String complexFieldNumber, String subFieldTag,
														 String referenceValue, String resultValue) {
		if (SubFieldinspecifiedList(complexFieldNumber, subFieldTag)) {
			return !Objects.equals(referenceValue, resultValue);
		}
		return false;
	}
	private static String getReferenceSubFieldValue(Document referenceDoc, String complexNumber, String subFieldTag) {
		NodeList complexFieldsListRef = referenceDoc.getElementsByTagName("complexFields");
		for (int i = 0; i < complexFieldsListRef.getLength(); i++) {
			Element complexFieldsElement = (Element) complexFieldsListRef.item(i);
			String refComplexNumber = complexFieldsElement.getAttribute("Number");
			if (refComplexNumber.equals(complexNumber)) {
				NodeList subFieldList = complexFieldsElement.getElementsByTagName("subFields");
				for (int j = 0; j < subFieldList.getLength(); j++) {
					Element subFieldElement = (Element) subFieldList.item(j);
					String refSubFieldTag = subFieldElement.getAttribute("Tag");
					if (refSubFieldTag.equals(subFieldTag)) {
						return subFieldElement.getAttribute("Value");
					}
				}
			}
		}
		return null;
	}



	private static List<File> getFilesInDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		List<File> fileList = new ArrayList<>();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					fileList.add(file);
				}
			}
		}
		return fileList;
	}

	private static List<FilePair> associateFiles(List<File> referenceFiles, List<File> resultFiles) {
		List<FilePair> filePairs = new ArrayList<>();

		Comparator<String> testNameComparator = new Comparator<String>() {
			@Override
			public int compare(String testName1, String testName2) {
				int testNumber1 = Integer.parseInt(testName1.replaceAll("\\D", ""));
				int testNumber2 = Integer.parseInt(testName2.replaceAll("\\D", ""));

				return Integer.compare(testNumber1, testNumber2);
			}
		};

		referenceFiles.sort(Comparator.comparing(file -> file.getName(), testNameComparator));
		resultFiles.sort(Comparator.comparing(file -> file.getName(), testNameComparator));

		for (File resultFile : resultFiles) {
			String resFileName = resultFile.getName();
			int resultId = Integer.parseInt(resFileName.replaceAll("\\D", ""));

			for (File refFile : referenceFiles) {
				String refFileName = refFile.getName();
				int refId = Integer.parseInt(refFileName.replaceAll("\\D", ""));

				if (refId == resultId) {
					filePairs.add(new FilePair(refFile, resultFile));
					break;
				}
			}
		}

		return filePairs;
	}

}