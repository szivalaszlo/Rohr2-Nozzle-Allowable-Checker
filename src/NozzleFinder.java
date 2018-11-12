import java.util.ArrayList;

public class NozzleFinder {
	private String [][] dataMatrixFromResultsFile; 
	private ArrayList<Nozzle> nozzles;
	private String commissionString;
	
	public NozzleFinder(String [][] inputDataMatrix) {
		dataMatrixFromResultsFile = inputDataMatrix;
		nozzles = new ArrayList<Nozzle>();
	}
	
	public ArrayList<Nozzle> getNozzles() throws ArrayIndexOutOfBoundsException {
		findCommisionAndDate();
		findNozzles();
		addRowNoOfNextNozzleToNozzles();
		return nozzles;
	}
	
	private void findCommisionAndDate() {
		commissionString = "";
		int commissioningRow = 0;
		for (int k=0; k<dataMatrixFromResultsFile.length; k++) {
			int positionOfInputsStringinLine = positionOfStringInLineWithSpace(dataMatrixFromResultsFile[k], "INPUTS");
			if (positionOfInputsStringinLine>0) {
				commissioningRow = k+1; // we need the line after "I N P U T S"
			}
		}
		String [] commissioningLine = dataMatrixFromResultsFile[commissioningRow];
		for(int k=0; k<commissioningLine.length; k++) {
			commissionString += commissioningLine[k] + "   ";
		}
	}
		
	private void findNozzles() throws ArrayIndexOutOfBoundsException {
		for (int k=0; k<dataMatrixFromResultsFile.length; k++) {
			int positionOfPointStringinLine = positionOfStringInLineWithSpace(dataMatrixFromResultsFile[k], "Point");
			if (positionOfPointStringinLine>0) {
				//add nozzle name to coord data. Nozzle name is +5 next to start of P o i n t string.
				int rowNo = k;
				int colNo = positionOfPointStringinLine+5;
				String nozzleName = dataMatrixFromResultsFile[rowNo][colNo];
				Nozzle currentNozzle = new Nozzle();
				currentNozzle.setRowOfNozzle(rowNo);
				currentNozzle.setColOfNozzle(colNo);
				currentNozzle.setName(nozzleName);
				currentNozzle.setCommissioningString(commissionString);
				nozzles.add(currentNozzle);
			}
		}
		
	} 
		
	private int positionOfStringInLineWithSpace(String [] line, String searchedStringInclSpaces) {
		//!comment - if searchedStringInclSpaces = "Point" , the alg looks for each char in different array location
		//!comment - in text file the text is "P o i n t"
		for(int k=0; k<line.length-searchedStringInclSpaces.length(); k++) {
			String concatStrLenSearched = "";
			for (int i=0; i<searchedStringInclSpaces.length(); i++) {
				concatStrLenSearched = concatStrLenSearched + line[k+i];
			}
			if (searchedStringInclSpaces.equals(concatStrLenSearched)) {
				return k;
			}
		}
		return -1;
	}
	
	private void addRowNoOfNextNozzleToNozzles() throws ArrayIndexOutOfBoundsException{
		for (int k=0; k<nozzles.size()-1; k++) {
			Nozzle currentNozzle = nozzles.get(k);
			Nozzle nextNozzle = nozzles.get(k+1);
			currentNozzle.setNextNozzleRowNo(nextNozzle.getRowNo());
		}
		Nozzle lastNozzle = nozzles.get(nozzles.size()-1);
		lastNozzle.setNextNozzleRowNo(dataMatrixFromResultsFile.length);
	}
}
