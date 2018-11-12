import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
	static String filePathNozzleOut;
	static String filePathAllowableIni;
	static String filePathOutputXls;
	static TextFileReader nozzleOutFileReader, allowablesIniFileReader;
	static ArrayList<Nozzle> nozzles;
	static ArrayList<LoadCase> loadcases;
	static String [][] dataMatrixFromNozzleOutFile, dataMatrixFromAllowablesIniFile;
	static long startTimeFirstHalf, stopTimeFirstHalf, elapsedTimeFirstHalf;
	static long startTimeSecondHalf, stopTimeSecondHalf, elapsedTimeSecondHalf;
	static String [] arguments;

	public static void main(String[] args) throws FileNotFoundException {
		arguments = args;
		checkArguments();
		startTimeFirstHalf = System.currentTimeMillis();
		filePathNozzleOut = args[0];
		filePathAllowableIni = args[1];
		filePathOutputXls =  args[2];
		System.out.println("\n" + "*** Calculation starts ***" + "\n");
		openInputFiles();
		readInputFiles();
		findNozzles();
		findAllowables();
		findLoadcases();
		printFoundNozzlesToScreen();
		printFoundLoadcasesToScreen();
		stopTimeFirstHalf = System.currentTimeMillis();
	    elapsedTimeFirstHalf = (stopTimeFirstHalf - startTimeFirstHalf);
		writeCalculationResultsToFile();		
	    System.out.println("\n" + "Run time: " + (elapsedTimeFirstHalf + elapsedTimeSecondHalf) + " msec");
	}
	
	

	private static void checkArguments() {
		//System.out.println("args len: " + arguments.length);
		if(arguments[0].toUpperCase().equals("H")) {
			System.out.println("*** HELP ***");
			System.out.println("\n" + "How to start the program ");
			System.out.println("\n" + "The program needs 3 arguments to start:");
			System.out.println("  1. nozzle.out Rohr2 output file's path");
			System.out.println("  2. allowables.ini initialisation file's path");
			System.out.println("  3. results.xls output file's path");
			System.out.println("\n" + "Example usage: ");
			System.out.println("   java -jar C:\\Rohr2Results.jar argument1 argument2 argument3");
			System.out.println("   java -jar C:\\Rohr2Results.jar c:\\nozzle.out c:\\allowables.ini c:\\results.xls" + "\n");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		}
		if(arguments.length != 3) {
			System.out.println("\n" + "*** ERROR: Incorrect number of arguments ***");
			System.out.println("\n" + "How to start the program ");
			System.out.println("\n" + "The program needs 3 arguments to start:");
			System.out.println("  1. nozzle.out Rohr2 output file's path");
			System.out.println("  2. allowables.ini initialisation file's path");
			System.out.println("  3. results.xls output file's path");
			System.out.println("\n" + "Example usage: ");
			System.out.println("   java -jar C:\\Rohr2Results.jar argument1 argument2 argument3");
			System.out.println("   java -jar C:\\Rohr2Results.jar c:\\nozzle.out c:\\allowables.ini c:\\results.xls" + "\n");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		}
	}

	private static void openInputFiles() {
		nozzleOutFileReader = new TextFileReader();
		allowablesIniFileReader = new TextFileReader();
		try {
			nozzleOutFileReader.openFile(filePathNozzleOut);
			System.out.println("File opened: " + filePathNozzleOut);
			} catch (FileNotFoundException e) {
				System.err.format("ERROR: Exception occurred trying to read '%s'.", filePathNozzleOut);
				System.out.println("*** THE PROGRAM WILL EXIT! ***");
				System.exit(0);
			} catch (IOException e) {
				System.err.format("ERROR: Exception occurred trying to read '%s'.", filePathNozzleOut);
				System.out.println("*** THE PROGRAM WILL EXIT! ***");
				System.exit(0);
			}
			
		try {
			allowablesIniFileReader.openFile(filePathAllowableIni);
			System.out.println("File opened: " + filePathAllowableIni);
			} catch (FileNotFoundException e) {
				System.err.format("ERROR: Exception occurred trying to read '%s'.", filePathAllowableIni);
				System.out.println("");
				System.out.println("*** THE PROGRAM WILL EXIT! ***");
				System.exit(0);
			} catch (IOException e) {
				System.err.format("ERROR: Exception occurred trying to read '%s'.", filePathAllowableIni);
				System.out.println("");
				System.out.println("*** THE PROGRAM WILL EXIT! ***");
				System.exit(0);
			}
	}

	private static void readInputFiles() {
		dataMatrixFromNozzleOutFile = nozzleOutFileReader.getDataMatrixFromFile();
		System.out.println("File read: " + filePathNozzleOut);
		dataMatrixFromAllowablesIniFile = allowablesIniFileReader.getDataMatrixFromFile();
		System.out.println("File read: " + filePathAllowableIni);
	}
	
	private static void findNozzles() {
		try {
			NozzleFinder nozzleFinder = new NozzleFinder(dataMatrixFromNozzleOutFile);
			nozzles = nozzleFinder.getNozzles();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("\n" + "*** ERROR: No nozzle found in " + filePathNozzleOut +  " ***");
			System.out.println("*** The current nozzle search algorithm is looking for string 'P o i n t'. ***");
			System.out.println("*** When found, the adjacent string is considered as nozzle name. Check the input file. ***");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		} 
	}
	
	private static void findAllowables() {
		NozzleAllowableFinder currentNozzleAllowable = new NozzleAllowableFinder(dataMatrixFromAllowablesIniFile);
		try {
			currentNozzleAllowable.addAllowablesToNozzles(nozzles);
		} catch (NoAllowablesDefinedException e) {
			System.out.println("\n" + "*** ERROR: No well defined allowables found in " + filePathAllowableIni +  " ***");
			System.out.println("*** Check this file for instructions. ***");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		} 
	}
	
	private static void findLoadcases() {
		LoadcaseFinder loadcaseFinder = new LoadcaseFinder(dataMatrixFromNozzleOutFile, nozzles);
		try {
			loadcaseFinder.addLoadcasesToNozzles();
		} catch (NoElementsInListException e) {
			System.out.println("\n" + "*** ERROR: No loadcase found in " + filePathNozzleOut +  " ***");
			System.out.println("*** The current loadcase search algorithm is looking for two consequent lines with 6 numbers in each line. ***");
			System.out.println("*** Check the input file. ***");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		}
	}
		
	private static void printFoundNozzlesToScreen() {
		System.out.println("\n" + "Following nozzles found in input file:");
		for(int k=0; k< nozzles.size(); k++) {
			System.out.println("  " + (k+1) + ". "+ nozzles.get(k).getName());
		}
	}
	
	private static void printFoundLoadcasesToScreen() {
		System.out.println("\n" + "Following loadcases found in input file:");
		loadcases = nozzles.get(0).getLoadcases(); //get loadcases for first found nozzle only. By definition all nozzles have the same loadcases.
		for(int k=0; k<loadcases.size(); k++) {
			System.out.println("  " + (k+1) + ". "+ loadcases.get(k).getLoadcaseName());
		}
	}
	
	private static void writeCalculationResultsToFile() {
		ResultOutput resultWriter = new ResultOutput(nozzles);
		checkIfOutputFileExists();
		startTimeSecondHalf = System.currentTimeMillis();
		try {
			resultWriter.writeAndFormatExcelFile(filePathOutputXls);
			System.out.println("\n" + "Results file written: " + filePathOutputXls);
			System.out.println("\n" + "*** Calculation finished ***");
			stopTimeSecondHalf = System.currentTimeMillis();
			elapsedTimeSecondHalf = (stopTimeSecondHalf - startTimeSecondHalf);			
		} catch (FileNotFoundException e) {
			System.out.println("\n" + "*** ERROR: Output "+ filePathOutputXls + " file is open! Close it! ***");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("\n" + "*** ERROR: Output "+ filePathOutputXls + " file is open! Close it! ***");
			System.out.println("*** THE PROGRAM WILL EXIT! ***");
			System.exit(0);
		}
	}

	private static void checkIfOutputFileExists() {
		File file = new File(filePathOutputXls); 
	    if (file.exists()){
	      System.out.println("\n" + "*** WARNING: Output "+ filePathOutputXls + " file already exists! ***");
	      Scanner scanner = new Scanner(System.in);
	      System.out.print("Do you want to overwrite it? [Y/N]: ");
	      String fileOverwriteUserDecision = scanner.next();
	      fileOverwriteUserDecision = fileOverwriteUserDecision.toUpperCase();
	      fileOverwriteUserDecision.replaceAll("\\s+","");
	      
	      if(fileOverwriteUserDecision.equals("N")) {
	    	  System.out.println("\n" + "*** Restart the application with another output file path! ***");
	    	  System.out.println("*** THE PROGRAM WILL EXIT! ***");
	    	  System.exit(0);
	      }
	      else if(fileOverwriteUserDecision.equals("Y")) {
	    	  System.out.println("\n" + filePathOutputXls + " file will be overwritten! ***");
	      }
	      else {
	    	  System.out.println("\n" + "*** Please choose next time from 'Y' or 'N'! ***");
	    	  System.out.println("*** THE PROGRAM WILL EXIT! ***");
	    	  System.exit(0);
	      }
	    }
	}
}
