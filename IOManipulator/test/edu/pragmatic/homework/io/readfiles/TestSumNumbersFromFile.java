package edu.pragmatic.homework.io.readfiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.Test;

public class TestSumNumbersFromFile {
	
	private static final String FILE_WITH_NUMBERS = "test/testresources/numbers_and_chars.txt";
	private static final String FILE_WITH_SUM_OF_NUMBERS = "test/testresources/sumofnumbers.txt";

	@Test(expected=IllegalArgumentException.class)
	public void readNumbersFromFileWithNoFileSupplied() throws FileNotFoundException{
		SumNumbers sumNumbers = new SumNumbers();
		String relativeFilePath = "";
		sumNumbers.getNumbersFromFile(relativeFilePath);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void readNumbersFromFileWithWrongFilePath() throws FileNotFoundException{
		SumNumbers sumNumbers = new SumNumbers();
		sumNumbers.getNumbersFromFile("not/a/correct/path.txt");
	}
	
	@Test
	public void readNumbersFromFile() throws FileNotFoundException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		Assert.assertNotNull(numbers);
		Assert.assertFalse( numbers.isEmpty() );
	}

	@Test
	public void testSumOfNumbers() throws FileNotFoundException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		long result = sumNumbers.sum(numbers);
		Assert.assertTrue(result > 0);
	}
	
	
	@Test
	public void printSumOfNumbersToConsole() throws FileNotFoundException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		long result = sumNumbers.sum(numbers);
		sumNumbers.printToConsole(result);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void printToAnotherFileWithNullFile() throws IOException{
		SumNumbers sumNumbers = new SumNumbers();
		String destinationFileName = null;
		sumNumbers.printToFile(destinationFileName, null);
	}
	
	@Test
	public void printToAnotherFile() throws IOException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		long result = sumNumbers.sum(numbers);
		sumNumbers.printToFile(FILE_WITH_SUM_OF_NUMBERS, result);
		
		File output = Paths.get(FILE_WITH_SUM_OF_NUMBERS).toFile();
		Assert.assertTrue(output.exists());
		try (  Scanner sc = new Scanner(output) ) {
			long fileOutputedSum = sc.nextLong();
			Assert.assertEquals(result, fileOutputedSum);
		}
		
		// clean up
		Files.delete(Paths.get(FILE_WITH_SUM_OF_NUMBERS));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void printToAnotherFileWithNoSumProvided() throws IOException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		long result = sumNumbers.sum(numbers);
		sumNumbers.printToFile(FILE_WITH_SUM_OF_NUMBERS, null);
	}
	
	@Test
	public void printToEndOfFile() throws IOException{
		SumNumbers sumNumbers = new SumNumbers();
		List<Integer> numbers = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		long result = sumNumbers.sum(numbers);
		sumNumbers.printToEndOfFile(FILE_WITH_NUMBERS, result);
		List<Integer> allNumsWithSum = sumNumbers.getNumbersFromFile(FILE_WITH_NUMBERS);
		Integer printedSum = allNumsWithSum.get(allNumsWithSum.size() - 1);
		Assert.assertEquals(result, (long) printedSum);
		// clean up
	}
	
}
