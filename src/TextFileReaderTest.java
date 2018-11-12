import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class TextFileReaderTest {
	TextFileReader testReaderNozzleOut = new TextFileReader();
	TextFileReader testReaderAllowablesIni = new TextFileReader();
	String filePathNozzleOut = "c:/nl/nozzle.out";
	String filePathAllowableIni = "c:/nl/allowables.ini";
	
	
	

	//@Test
	public void testGetDataFromFile() {
		

	}
	
	//@Test
		public void testOpenFile2() throws FileNotFoundException {
			try {
				System.out.println(testReaderNozzleOut.openFile(filePathNozzleOut));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String [][] dataMatrix = testReaderNozzleOut.getDataMatrixFromFile();
			System.out.println("datamatrix len : "+ dataMatrix.length);
			for(int k=0; k<dataMatrix.length; k++) {
				//System.out.println("line size: " + dataMatrix[k].length);
				if(dataMatrix[k].length>0) {
					for (int i=0; i<dataMatrix[k].length; i++) {
						System.out.print(dataMatrix[k][i].toString() + " ");
					}
				}
				
				System.out.println("");
			}
			
		}
		
	

	
	@Test
		public void nozzleFinder() throws FileNotFoundException {
			//System.out.println(testReaderNozzleOut.openFile(filePathNozzleOut));
			//System.out.println(testReaderAllowablesIni.openFile(filePathAllowableIni));
			String [][] dataMatrixFromResultsFile = testReaderNozzleOut.getDataMatrixFromFile();
			String [][] dataMatrixFromAllowableFile = testReaderAllowablesIni.getDataMatrixFromFile();
			NozzleFinder nozzleFinder = new NozzleFinder(dataMatrixFromResultsFile);
			ArrayList<Nozzle> nozzles = nozzleFinder.getNozzles();
			for(int k=0; k<nozzles.size(); k++) {
				System.out.println(nozzles.get(k).getName());
				System.out.println(nozzles.get(k));
			}
			
			
			LoadcaseFinder loadcaseFinder = new LoadcaseFinder(dataMatrixFromResultsFile, nozzles);
			//loadcaseFinder.addLoadcasesToNozzles();
			loadcaseFinder.printLoadcases();
			
			ResultOutput resultWriter = new ResultOutput(nozzles);
			
			/*
			for(int k=0; k<dataMatrixFromAllowableFile.length; k++) {
				//System.out.println("line size: " + dataMatrix[k].length);
				if(dataMatrixFromAllowableFile[k].length>0) {
					for (int i=0; i<dataMatrixFromAllowableFile[k].length; i++) {
						System.out.print(dataMatrixFromAllowableFile[k][i].toString() + " ");
					}
				}
				
				System.out.println("");
			}
			*/
			
		}
}
