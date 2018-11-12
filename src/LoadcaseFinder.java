import java.util.ArrayList;

public class LoadcaseFinder {
	private ArrayList<Nozzle> nozzles;
	private String [][] dataMatrix;
	private ArrayList<Integer> loadCaseRows;
	
	public LoadcaseFinder(String [][] dataMatrix, ArrayList<Nozzle> nozzles) {
		this.nozzles = nozzles;
		this.dataMatrix = dataMatrix;
		loadCaseRows = new ArrayList<Integer>();
	}
	
	public void printLoadcases() {
		for (int k=0; k<nozzles.size(); k++) {
			Nozzle currentNozzle = nozzles.get(k);
			ArrayList<LoadCase> loadCasesOfCurrNozzle = currentNozzle.getLoadcases();
			for (int i=0; i< loadCasesOfCurrNozzle.size(); i++) {
				System.out.println(loadCasesOfCurrNozzle.get(i).toString());
			}
		}
	}
		
	public void addLoadcasesToNozzles() throws NoElementsInListException {
		for(int k=0; k<nozzles.size(); k++) {
			loadCaseRows = findLoadcaseRowNos(nozzles.get(k));
			Nozzle currentNozzle = nozzles.get(k);
			for(int i=0; i<loadCaseRows.size(); i++) {
				LoadCase currentLoadcase = createLoadcase(loadCaseRows.get(i));
				addAllowablesToLoadcaseFromNozzle(currentLoadcase, currentNozzle);
				currentLoadcase.setNozzleName(currentNozzle.getName());
				currentLoadcase.calculateAllAxradAndResultantForcesMoments();
				currentLoadcase.calculateAllUtilization();
				currentNozzle.addLoadcase(currentLoadcase);
			}
		}
		if(loadCaseRows.size() == 0) {
			throw new NoElementsInListException("No loadcases found at all!");
		}
	}
	
	private ArrayList<Integer> findLoadcaseRowNos(Nozzle inputNozzle) {
		ArrayList<Integer> loadcaseRows = new ArrayList<Integer>();
		int currentNozzleRow = inputNozzle.getRowNo();
		int nextNozzleRow = inputNozzle.getNextNozzleRowNo();
		for (int k = currentNozzleRow; k<nextNozzleRow; k++) {
			if(areTwoConsquetiveLinesLoadcaseValues(k)) {
				loadcaseRows.add(k);
			}
		}
		return loadcaseRows;
	}
	
	private boolean areTwoConsquetiveLinesLoadcaseValues(int firstRowNumber) {
		if(areLastSixValuesOfLineDoubles(firstRowNumber) && areLastSixValuesOfLineDoubles(firstRowNumber+1)) {
			return true;
		}
		else return false;
	}
	
	private boolean areLastSixValuesOfLineDoubles(int rowNumber) {
		String [] inputLine = dataMatrix[rowNumber];
		if(inputLine.length >=6) {
			for(int k = inputLine.length-1; k>=inputLine.length-6; k--) {
				//System.out.println("!!!!!!" +inputLine[k]);
				if(!canBeParsedToDouble(inputLine[k])) {
					return false;
				}
			}
			return true;
		}
		else return false;
	}
	
	private LoadCase createLoadcase(int loadcaseRow) {
		LoadCase loadCase = new LoadCase();
		double [][] forcesAndMoments= new double[2][3];
		String [] forcesRow = dataMatrix[loadcaseRow];
		String [] momentsRow = dataMatrix[loadcaseRow+1];
		forcesAndMoments[0] = readLastThreeValuesOfLine(forcesRow);
		forcesAndMoments[1] = readLastThreeValuesOfLine(momentsRow);
		loadCase.setLoadcaseName(readLoadcaseName(forcesRow));
		loadCase.setForceAndMoment(forcesAndMoments);
		return loadCase;
	}
	
	public double[] readLastThreeValuesOfLine (String [] inputLine) {
		double [] lastThreeValues = new double [3];
		int arrayCounter = 0;
		for (int k= inputLine.length-3; k< inputLine.length; k++) {
			if(canBeParsedToDouble(inputLine[k])) {
				lastThreeValues[arrayCounter] = Double.parseDouble(inputLine[k]);
			}
			else System.out.println("Tried to add double value to Loadcase, but string could not be parsed to double");
			arrayCounter++;
		}
		return lastThreeValues;
	}
	
	private boolean canBeParsedToDouble(String inputString) {
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
	
	private String readLoadcaseName (String [] inputLine) {
		String loadcaseName = "";
		for(int k=1; k<inputLine.length-6; k++) {
			loadcaseName += inputLine[k] + " ";
		}
		return loadcaseName;
	}
	
	private void addAllowablesToLoadcaseFromNozzle(LoadCase loadCase, Nozzle nozzle) {
		if(nozzle.hasAxRadAllowables()) {
			loadCase.setAxradAllowables(nozzle.getAxradAllowables());
		}
		if(nozzle.hasComponentAllowables()) {
			loadCase.setComponentAllowables(nozzle.getComponentAllowables());
		}
		if(nozzle.hasResultantAllowables()) {
			loadCase.setResultantAllowables(nozzle.getResultantAllowables());
		}
	}	
}
