package com.example.jsoncompare;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class JsonFileHelper {

    // Return a list of all JSON files in a folder
    public static List<String> getJsonFilesFromFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return Collections.emptyList();
        }
        return Arrays.stream(folder.listFiles((dir, name) -> name.endsWith(".json")))
                .map(File::getAbsolutePath)
                .sorted()
                .collect(Collectors.toList());
        }

    // Return a map grouped by scenario folder (subfolders)
    public static Map<String, List<String>> getJsonFilesGroupedByScenario(String parentFolder) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();
        File folder = new File(parentFolder);
        if (!folder.exists() || !folder.isDirectory()) {
            return grouped;
        }
        for (File sub : folder.listFiles(File::isDirectory)) {
            List<String> files = Arrays.stream(sub.listFiles((dir, name) -> name.endsWith(".json")))
                    .map(File::getAbsolutePath)
                    .sorted()
                    .collect(Collectors.toList());
            if (!files.isEmpty()) {
                grouped.put(sub.getName(), files);
            }
        }
        return grouped;
    }
}
