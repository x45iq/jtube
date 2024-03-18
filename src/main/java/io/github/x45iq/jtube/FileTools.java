package io.github.x45iq.jtube;

import java.io.File;

class FileTools {
    private FileTools() {

    }

    static final String[] BAN_SYMBOLS = {"/", "<", ">", "*", "\"", ":", "?", "\\", "|"};

    static String censureFileName(String input) {
        assert input != null;
        String name = input;
        for (String symb : BAN_SYMBOLS) {
            while (name.contains(symb)) {
                name = name.replace(symb, "_");
            }
        }
        if (name.startsWith(".")) {
            name = name.replaceFirst(".", "_");
        }
        return name;
    }

    static File createExportFile(File folder, String name, String extension) {
        assert folder != null;
        assert name != null;
        assert extension != null;
        assert folder.isDirectory();
        File exportFile = new File(folder, String.format("%s.%s", censureFileName(name), extension));
        if (!exportFile.exists()) return exportFile;
        int n = 0;
        while (exportFile.exists())
            exportFile = new File(folder, String.format("%s(%s).%s", name, n++, extension));
        return exportFile;
    }

    static void deleteFile(File file) {
        assert file != null;
        assert !file.isDirectory();
        while (file.exists()) {
            file.delete();
        }
    }

    static String createRandomFileName() {
        return "jtube_" + System.currentTimeMillis();
    }
}
