import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class TextFileReader {
		private BufferedReader fileReader;
		private ArrayList<String[]>  dataReadFromFile;
		private String [][] dataMatrix; //1. row; 2. column
		private String filePath;
		
	public TextFileReader() {
		dataReadFromFile = new ArrayList<String[]>();
	}
	
	public boolean openFile(String filePath) throws IOException, FileNotFoundException  {
		this.filePath = filePath;
		try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
			String fileLine;
			while ((fileLine = fileReader.readLine()) != null) {
				//read in words split by whitespace
				dataReadFromFile.add(fileLine.split("\\s+"));
			}
			return true;
		} 		
	}
	
	public String [][] getDataMatrixFromFile(){
		fillDataStringMatrix();
		return dataMatrix;
	}
	
	private void fillDataStringMatrix() {
		int lastRowNumber = dataReadFromFile.size();
		dataMatrix = new String [lastRowNumber][];
		for(int k=0; k<lastRowNumber; k++) {
			int currentColumnLength = dataReadFromFile.get(k).length;
			if (currentColumnLength > 0) {
				dataMatrix [k] = new String [currentColumnLength];
				for (int i=0; i<currentColumnLength; i++) {
					dataMatrix [k][i] = dataReadFromFile.get(k)[i];
				}
			}
			else {
				//add a space to lines which were completely empty. Needed to fill the matrix properly.
				dataMatrix [k] = new String [1];
				dataMatrix [k][0] = " ";
			}
		}
	}
}
	

