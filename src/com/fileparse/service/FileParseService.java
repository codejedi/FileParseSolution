package com.fileparse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

public class FileParseService {

    //private String fileName = "src/resources/tempest.txt";

    public void readContentsFromFile() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.showOpenDialog(null);
        File keyfile = fileChooser.getSelectedFile();

        Map<String, Integer> wordCountMap = new HashMap<>();
        try (Stream<String> fileStream = Files.lines(Paths.get(keyfile.toURI()))) {
            fileStream.forEach(lineOfFile -> {
                tokenizeStringtoWords(lineOfFile, wordCountMap);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Integer> sortedMap = wordCountMap.entrySet().stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed())).limit(10).collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));

        sortedMap.forEach((word, count) -> {
            System.out.println(word.concat(" ").concat("(" + count + ")"));
        });
    }

    private void tokenizeStringtoWords(String eachLineOfDoc, Map<String, Integer> wordCountMap) {
        List<String> tokenizedWords = Collections.list(new StringTokenizer(eachLineOfDoc, " ")).stream()
                .map(String::valueOf).map(this::cleanUpWords).filter(Objects::nonNull).collect(Collectors.toList());
        tokenizedWords.forEach(word -> {
            performWordCountOperation(word, wordCountMap);
        });
    }

    private void performWordCountOperation(String word, Map<String, Integer> wordCountMap) {
        wordCountMap.computeIfAbsent(word, (key) -> 1);
        wordCountMap.computeIfPresent(word, (key, value) -> ++value);
    }

    private String cleanUpWords(String wordToCleanUp) {
        if (null != wordToCleanUp && !"".equals(wordToCleanUp) && !" ".equals(wordToCleanUp)) {
            wordToCleanUp.trim();
            wordToCleanUp = wordToCleanUp.replaceAll("[^a-zA-Z0-9'\\-]", "");
            return wordToCleanUp.toLowerCase();
        }
        return null;
    }
}
