package com.Project.Project;

import com.Project.Project.config.ControleFile;
import com.Project.Project.model.ComplexField;
import com.Project.Project.model.SubField;
import com.Project.Project.util.FileComparator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		// Charger le contexte Spring à partir du fichier controleFile.xml
		ApplicationContext context = new ClassPathXmlApplicationContext("controleFile.xml");

		// Obtenir le bean "controleFile" du contexte
		ControleFile controleFile = (ControleFile) context.getBean("controleFile");

		// Comparer les fichiers en utilisant FileComparator
		FileComparator fileComparator = new FileComparator("src/main/resources/ref/reference.xml", "src/main/resources/res/resultat.xml");

		// Comparer les champs spécifiés dans controleFile
		fileComparator.compareFields(controleFile.getFieldControls());

		// Comparer les sous-champs spécifiés dans controleFile pour les complexFields
		for (ComplexField complexFieldControl : controleFile.getComplexFieldControls()) {
			fileComparator.compareSubFields(complexFieldControl.getSubFieldControls(), complexFieldControl.getNumber());
		}
	}
}
