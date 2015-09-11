package edu.pragmatic.homework.io.wordcounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WordCounter {

	public List<File> getTextFilesFromDir(String rootDirectory) {
		List<File> textFiles = new ArrayList<>();
		
		findFilesFromDirectory(Paths.get(rootDirectory).toFile(), textFiles);
		
		return textFiles;
	}
	
	private void findFilesFromDirectory(File currentFile, List<File> textFiles){
		if (currentFile.isDirectory()){
			for ( File file : currentFile.listFiles() )
				findFilesFromDirectory(file, textFiles);
		}
		
		if (currentFile.getName().endsWith(".txt")){
			textFiles.add(currentFile);
		}
	}

	public Map<String, Integer> countWordsForAFile(File targetFile) {
		ensureFileExists(targetFile);
		
		Map<String , Integer> wordCount = new HashMap<>();
		try (Scanner reader = new Scanner(targetFile)){
			while(reader.hasNext()){
				String word = reader.next();
				if ( isNonWhiteSpaceWord(word) ){
					String cleanWord = removePunctiation(word);
					ensureWordIsCounted(wordCount, cleanWord);
					incrementWordCounter(wordCount, cleanWord);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return wordCount;
	}
	
	public Map<String, Integer> countWords( List<File> textFiles){
		Map<String, Integer> mergedWordCount = new HashMap<>();
		for (File textFile : textFiles){
			Map<String, Integer> fileCountedWords = countWordsForAFile(textFile);
			mergedWordCount = mergeMaps(mergedWordCount, fileCountedWords);
		}
		
		return mergedWordCount;
	}
	
	private Map<String, Integer> mergeMaps(Map<String, Integer> left, Map<String, Integer> right){
		Map<String, Integer> mergedMap = new HashMap<>();
		mergedMap.putAll(left);
		for (String word : right.keySet()){
			if (mergedMap.containsKey(word)){
				int wordCount = mergedMap.get(word);
				int rightMapWordCount = right.get(word);
				mergedMap.put(word, wordCount + rightMapWordCount);
			}else {
				mergedMap.put(word, right.get(word));
			}
		}
		
		return mergedMap;
	}

	private boolean isNonWhiteSpaceWord(String word) {
		return !"\n".equals(word) && !"\t".equals(word) && !"\r".equals(word);
	}

	private void incrementWordCounter(Map<String, Integer> wordCount,String word) {
		int hits = wordCount.get(word);
		wordCount.put(word, ++hits);
	}

	private void ensureWordIsCounted(Map<String, Integer> wordCount, String word) {
		if (!wordCount.containsKey(word)){
			wordCount.put(word, 0);
		}
	}

	private void ensureFileExists(File targetFile) {
		if (targetFile == null || !targetFile.exists()){
			throw new IllegalArgumentException("File supplied must exist");
		}
	}
	
	private String removePunctiation(String word){
		return word.replaceAll("\\.", "")
				   .replaceAll("\"", "")
				   .replaceAll(",", "")
				   .replace(";", "")
				   .replaceAll("\\?", "") ;
	}

}
