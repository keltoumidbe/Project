package com.Project.Project;

import java.io.File;

public class FilePair {
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
