package com.Project.Project;

import com.Project.Project.config.ControleFile;
import com.Project.Project.model.ComplexField;
import com.Project.Project.model.SubField;
import com.Project.Project.util.FileComparator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("controleFile.xml");

		ControleFile controleFile = (ControleFile) context.getBean("controleFile");

		String referenceDirectory = "src/main/resources/ref/";
		String resultDirectory = "src/main/resources/res/";

		List<File> referenceFiles = getFilesInDirectory(referenceDirectory);
		List<File> resultFiles = getFilesInDirectory(resultDirectory);

		List<FilePair> filePairs = associateFiles(referenceFiles, resultFiles);

		if (filePairs.isEmpty()) {
			System.out.println("Aucune paire de fichiers de référence et de résultat correspondante trouvée.");
			return;
		}

		for (FilePair filePair : filePairs) {
			FileComparator fileComparator = new FileComparator(filePair.getReferenceFile().getPath(), filePair.getResultFile().getPath());

			System.out.println("**********************Comparaison du fichier de référence : " + filePair.getReferenceFile().getName() + " avec le fichier de résultat : " + filePair.getResultFile().getName()+"**********************");

			fileComparator.compareFields(controleFile.getFieldControls());

			for (ComplexField complexFieldControl : controleFile.getComplexFieldControls()) {
				fileComparator.compareSubFields(complexFieldControl.getSubFieldControls(), complexFieldControl.getNumber());
			}

			fileComparator.printErrorCounts(); // Afficher les totaux d'erreurs pour cette paire de fichiers
			System.out.println(); // Ajouter une ligne vide pour séparer les résultats de chaque paire de fichiers
		}
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
		for (File referenceFile : referenceFiles) {
			String referenceFileName = referenceFile.getName();
			String referenceName = referenceFileName.substring(0, referenceFileName.indexOf("_ref"));

			for (File resultFile : resultFiles) {
				String resultFileName = resultFile.getName();
				String resultName = resultFileName.substring(0, resultFileName.indexOf("_res"));

				if (referenceName.equals(resultName)) {
					filePairs.add(new FilePair(referenceFile, resultFile));
				}
			}
		}
		return filePairs;
	}

	private static class FilePair {
		private final File referenceFile;
		private final File resultFile;

		public FilePair(File referenceFile, File resultFile) {
			this.referenceFile = referenceFile;
			this.resultFile = resultFile;
		}

		public File getReferenceFile() {
			return referenceFile;
		}

		public File getResultFile() {
			return resultFile;
		}
	}
}
