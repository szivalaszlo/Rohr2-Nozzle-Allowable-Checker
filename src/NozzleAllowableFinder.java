import java.util.ArrayList;

public class NozzleAllowableFinder {
	private Nozzle nozzle;
	private String [][] dataMatrixFromAllowablesFile; 
	private String [] coordinateAxes;
	private String nozzleDirection;
	private ArrayList<Integer> nozzleRows;
	private boolean nozzleHasAllowableDefined;
	private int numberOfAllowablesDefined;
	
	public NozzleAllowableFinder(String [][] dataMatrixFromAllowablesFile) {
		this.dataMatrixFromAllowablesFile = dataMatrixFromAllowablesFile;
		coordinateAxes = new String[3];
		coordinateAxes[0] = "X";
		coordinateAxes[1] = "Y";
		coordinateAxes[2] = "Z";
		nozzleDirection = "";
		nozzleRows = new ArrayList<Integer>();
		nozzleHasAllowableDefined = false;
		numberOfAllowablesDefined = 0;
	}
	
	public void addAllowablesToNozzles(ArrayList<Nozzle> nozzles) throws NoAllowablesDefinedException {
		for(Nozzle currentNozzle: nozzles) {
			addAllowableTo(currentNozzle);
			if(nozzleHasAllowableDefined()) {
				numberOfAllowablesDefined++;
			}
		}
		if(numberOfAllowablesDefined == 0) {
			throw new NoAllowablesDefinedException("Error");
		}
	}
	
	private void addAllowableTo(Nozzle nozzle) {
		this.nozzle = nozzle;
		nozzleRows = findNozzleRowsForInputNozzle();
		for(int k=0; k<nozzleRows.size(); k++) {
			int currentRowNumber = nozzleRows.get(k);
			String [] currentLine = dataMatrixFromAllowablesFile[currentRowNumber];
			if(inputLineContainsAllData(currentLine)) {
				if(isNozzleDirectionDefined(currentLine)) {
					AxRadAllowable axRadAllowable = new AxRadAllowable();
					axRadAllowable.setAxradAllowables(readAllowablesForDirectionNozzle(currentLine));
					axRadAllowable.setNozzleDirection(currentLine[1].toUpperCase());
					nozzle.setAxradAllowables(axRadAllowable);
					nozzleHasAllowableDefined = true;
				}
				if(isLineLengthOKForComponentNozzle(currentLine)) {
					ComponentAllowable componentAllowable = new ComponentAllowable();
					componentAllowable.setComponentAllowables(readAllowablesForComponentNozzle(currentLine));
					nozzle.setComponentAllowables(componentAllowable);
					nozzleHasAllowableDefined = true;
				}
				if(isLineLengthOKForResultantNozzle(currentLine)) {
					ResultantAllowable resultantAllowable = new ResultantAllowable();
					resultantAllowable.setResultantAllowables(readAllowablesForResultantNozzle(currentLine));
					nozzle.setResultantAllowables(resultantAllowable);
					nozzleHasAllowableDefined = true;
				}
			}
		}
	}
	
	public boolean nozzleHasAllowableDefined() {
		return nozzleHasAllowableDefined;
	}
	
	private ArrayList<Integer> findNozzleRowsForInputNozzle(){
		ArrayList<Integer> nozzleRows = new ArrayList<Integer>();
		for (int k=0; k<dataMatrixFromAllowablesFile.length; k++ ) {
			String [] currentLine = dataMatrixFromAllowablesFile[k];
			if(isCurrentLineInputForNozzle(currentLine)) {
				nozzleRows.add(k);
			}
		}
		return nozzleRows;
	}
	
	private boolean isCurrentLineInputForNozzle(String [] currentLine) {
		String currentNozzleName = nozzle.getName().toUpperCase();
		String nozzleNameFromInputFile = currentLine [0].toUpperCase();
		if(nozzleNameFromInputFile.equals(currentNozzleName)) {
			return true;
		}
		return false;
	}
	
	private boolean inputLineContainsAllData(String [] inputLine) {
		if(isNozzleDirectionDefined(inputLine)) {
			if(isLineLengthOKForDirectionalNozzle(inputLine) && areLastNmebersOfLineParsableNumbers(inputLine, 4)) {
				return true;
			}
		}
		if(isLineLengthOKForComponentNozzle(inputLine) && areLastNmebersOfLineParsableNumbers(inputLine, 6)) {
			return true;
		}
		
		if(isLineLengthOKForResultantNozzle(inputLine) &&  areLastNmebersOfLineParsableNumbers(inputLine, 2)) {
			return true;
		}
		System.out.println("\n" + "*** WARNING ***");
		System.out.println("   Incomplete input for nozzle: "+nozzle.getName() );
		System.out.println("   " + nozzle.getName() + " will not be considered in results file. See ini file for instructions.");
		return false;
	}
	
	private boolean isNozzleDirectionDefined(String [] inputLine) {
		if(inputLine.length<2)return false;
		String nozzleDirection = inputLine[1];
		int foundAxisFlag = 0;
		for(int k=0; k<coordinateAxes.length; k++) {
			if (coordinateAxes[k].contains(nozzleDirection.toUpperCase())) {
				foundAxisFlag = 1;
			}
		}
		if(foundAxisFlag == 1) {
			return true;
		}
		else return false;
	}
	
	private boolean isLineLengthOKForDirectionalNozzle(String [] inputLine) {
		if (inputLine.length != 6) return false;
		else return true;
	}
	
	private boolean isLineLengthOKForComponentNozzle(String [] inputLine) {
		if (inputLine.length != 7) return false;
		else return true;
	}
	
	private boolean isLineLengthOKForResultantNozzle(String [] inputLine) {
		if (inputLine.length != 3) return false;
		else return true;
	}
	
	private boolean areLastNmebersOfLineParsableNumbers(String [] inputLine, int n) {
		if(inputLine.length<n) {
			System.out.println("Nozzle line is shorter, than number of numbers expected!");
			return false;
		}
		else {
			int lastIndexOfLine = inputLine.length-1;
			for(int k=lastIndexOfLine; k>(lastIndexOfLine-n); k--) {
				if(!canBeParsedToDouble(inputLine[k])) return false;
			}
		}
		return true;
	}

	private double [][]  readAllowablesForDirectionNozzle(String [] inputLine) {
		double [][] allowables = new double [2][2];
		//Fax, Frad
		allowables [0][0] = Double.parseDouble(inputLine [2]);
		allowables [0][1] = Double.parseDouble(inputLine [3]);
		//Max, Mrad
		allowables [1][0] = Double.parseDouble(inputLine [4]);
		allowables [1][1] = Double.parseDouble(inputLine [5]);
		return allowables;
	}
	
	private double []  readAllowablesForResultantNozzle(String [] inputLine) {
		double []allowables = new double [2];
		//Fresultant, Mresultant
		allowables [0] = Double.parseDouble(inputLine [1]);
		allowables [1] = Double.parseDouble(inputLine [2]);
		return allowables;
	}
	
	private double [][]  readAllowablesForComponentNozzle(String [] inputLine) {
		double [][] allowables = new double [2][3];
		//Fx, Fy, Fz
		allowables [0][0] = Double.parseDouble(inputLine [1]);
		allowables [0][1] = Double.parseDouble(inputLine [2]);
		allowables [0][2] = Double.parseDouble(inputLine [3]);
		//Mx, My, Mz
		allowables [1][0] = Double.parseDouble(inputLine [4]);
		allowables [1][1] = Double.parseDouble(inputLine [5]);
		allowables [1][2] = Double.parseDouble(inputLine [6]);
		return allowables;
	}
	
	private boolean canBeParsedToDouble(String inputString) {
		//first try to parse the input as double number
		//when fails simply store as string
		try {
			double dataNumber = Double.parseDouble(inputString);
			return true;
		}
		catch (NullPointerException e){
			String dataString = inputString;
			return false;
		}
		catch (NumberFormatException e){
			String dataString = inputString;
			return false;
		}
	}
}
