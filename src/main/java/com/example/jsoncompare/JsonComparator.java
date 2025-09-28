package com.example.jsoncompare;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;


public class JsonComparator {

    private static final ObjectMapper mapper = new ObjectMapper();

    // 🔹 Compare two JSON files
    public static String compareTwoJsonFiles(String filePath1, String filePath2) throws Exception {
        JsonNode json1 = mapper.readTree(new File(filePath1));
        JsonNode json2 = mapper.readTree(new File(filePath2));

        Map<String, Object> differences = new LinkedHashMap<>();
        findDifferences(json1, json2, differences, "");

        if (differences.isEmpty()) {
            return "No differences";
        }
        return differences.toString();
    }

    // 🔹 Compare all files in a flat list (old method)
    public static List<String> compareFiles(List<String> files) throws Exception {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < files.size() - 1; i++) {
            String file1 = files.get(i);
            String file2 = files.get(i + 1);

            String diff = compareTwoJsonFiles(file1, file2);
            results.add("Comparison between " + new File(file1).getName() +
                    " and " + new File(file2).getName() + ": " );

            results.add(diff);
        }
        return results;
    }

    // 🔹 NEW: Compare all scenarios
    public static Map<String, List<String>> compareAllScenarios(Map<String, List<String>> groupedFiles) throws Exception {
        Map<String, List<String>> results = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> entry : groupedFiles.entrySet()) {
            String scenario = entry.getKey();
            List<String> files = entry.getValue();

            if (files.size() > 1) {
                results.put(scenario, compareFiles(files)); // reuse existing method
            } else {
                results.put(scenario, Collections.singletonList("Only one file, no comparison"));
            }
        }
        return results;
    }

    // 🔹 Recursive difference finder
    private static void findDifferences(JsonNode json1, JsonNode json2,
                                        Map<String, Object> differences, String path) {
        Iterator<String> fieldNames = json1.fieldNames();

        // Check for removed fields
        while (fieldNames.hasNext()) {
            String field = fieldNames.next();
            String currentPath = path.isEmpty() ? field : path + "." + field;

            if (!json2.has(field)) {
                differences.put(currentPath, "Removed");
            } else if (json1.get(field).isObject() && json2.get(field).isObject()) {
                findDifferences(json1.get(field), json2.get(field), differences, currentPath);
            }
            // Ignore value changes; we only care about structure
        }

        // Check for added fields
        Iterator<String> fieldNames2 = json2.fieldNames();
        while (fieldNames2.hasNext()) {
            String field = fieldNames2.next();
            String currentPath = path.isEmpty() ? field : path + "." + field;

            if (!json1.has(field)) {
                differences.put(currentPath, "Added");
            }
        }
    }


    // This is optional helper method to find additions/removals
// 🔹 Recursive helper to find added/removed keys
private static void findAdditionsRemovals(JsonNode oldJson, JsonNode newJson,
                                          String path, Set<String> added, Set<String> removed) {
    // Keys in old JSON
    Iterator<String> oldFields = oldJson.fieldNames();
    while (oldFields.hasNext()) {
        String field = oldFields.next();
        String currentPath = path.isEmpty() ? field : path + "." + field;

        if (!newJson.has(field)) {
            removed.add(currentPath);
        } else {
            JsonNode oldChild = oldJson.get(field);
            JsonNode newChild = newJson.get(field);

            if (oldChild.isObject() && newChild.isObject()) {
                findAdditionsRemovals(oldChild, newChild, currentPath, added, removed);
            }
        }
    }

    // Keys in new JSON
    Iterator<String> newFields = newJson.fieldNames();
    while (newFields.hasNext()) {
        String field = newFields.next();
        String currentPath = path.isEmpty() ? field : path + "." + field;

        if (!oldJson.has(field)) {
            added.add(currentPath);
        }
    }
}

    // 🔹 Summarize additions/removals between two JSON files
    // 🔹 Summarize changes in a flat list of JSONs

    public static String summarizeAdditionsRemovals(List<String> files) throws Exception {
        List<String> comparisons = new ArrayList<>();
        for (int i = 0; i < files.size() - 1; i++) {
            String file1 = files.get(i);
            String file2 = files.get(i + 1);
            String diff = compareTwoJsonFiles(file1, file2);
            if (!diff.equals("No differences")) {
//                comparisons.add("Comparison between " + new File(file1).getName() +
//                        " and " + new File(file2).getName() + System.lineSeparator() + diff);
                comparisons.add("Comparison between " + new File(file1).getName() +
                        " and " + new File(file2).getName() + ":"+diff );


            }
        }

        if (comparisons.isEmpty()) {
            return "No differences found across files";
        }

        return String.join(System.lineSeparator(), comparisons);

    }






    public static Map<String, String> summarizeAdditionsRemovalsByScenario(Object input) {
        Map<String, String> results = new LinkedHashMap<>();
        if (!(input instanceof Map)) {
            results.put("Error", "Input is not a Map<String,List<String>>");
            return results;
        }
        Map<String, List<String>> groupedFiles = (Map<String, List<String>>) input;

        for (Map.Entry<String, List<String>> entry : groupedFiles.entrySet()) {
            String scenario = entry.getKey();
            List<String> files = entry.getValue();
            if (files == null || files.size() <= 1) {
                results.put(scenario, "Only one file, no comparison");
            } else {
                try {
                    results.put(scenario, summarizeAdditionsRemovals(files));
                } catch (Exception e) {
                    results.put(scenario, "Error: " + e.getMessage());
                }
            }
        }
        return results;
    }


}
