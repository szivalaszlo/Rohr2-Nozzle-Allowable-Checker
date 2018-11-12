import java.util.ArrayList;

public class Nozzle {
	private ArrayList<LoadCase> loadcases;
	private String name, commissionString;
	private int rowOfNozzle, colOfNozzle, rowOfNextNozzle;
	private AxRadAllowable axRadAllowable;
	private ComponentAllowable componentAllowable;
	private ResultantAllowable resultantAllowable;
	private boolean hasAxRadAllowables, hasComponentAllowables, hasResultantAllowables;

	public Nozzle() {
		loadcases = new ArrayList<LoadCase>();
		hasAxRadAllowables = false;
		hasComponentAllowables = false;
		hasResultantAllowables = false;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRowOfNozzle(int rowOfNozzle) {
		this.rowOfNozzle = rowOfNozzle;
	}
	
	public int getRowNo() {
		return rowOfNozzle;
	}
	
	public void setColOfNozzle (int colOfNozzle) {
		this.colOfNozzle = colOfNozzle;
	}
	
	public int getColNo() {
		return colOfNozzle;
	}
	
	public void setNextNozzleRowNo(int row) {
		rowOfNextNozzle = row;
	}
	
	public int getNextNozzleRowNo() {
		return rowOfNextNozzle;
	}
	
	public void setCommissioningString(String commissionString) {
		this.commissionString = commissionString;
	}
	
	public String getCommissioningString() {
		return commissionString;
	}
	
	public void setAxradAllowables(AxRadAllowable axRadAllowable) {
		this.axRadAllowable = axRadAllowable;
		hasAxRadAllowables = true;
	}
	
	public AxRadAllowable getAxradAllowables() {
		return axRadAllowable;
	}
	
	public boolean hasAxRadAllowables() {
		return hasAxRadAllowables;
	}
	
	public void setComponentAllowables(ComponentAllowable componentAllowable) {
		this.componentAllowable = componentAllowable;
		hasComponentAllowables = true;
	}
	
	public boolean hasComponentAllowables() {
		return hasComponentAllowables;
	}
	
	public ComponentAllowable getComponentAllowables() {
		return componentAllowable;
	}
	
	public void setResultantAllowables(ResultantAllowable resultantAllowable) {
		this.resultantAllowable = resultantAllowable;
		hasResultantAllowables = true;
	}
	
	public ResultantAllowable getResultantAllowables() {
		return resultantAllowable;
	}
	
	public boolean hasResultantAllowables() {
		return hasResultantAllowables;
	}

	public String getNozzleDirection() {
		return axRadAllowable.getNozzleDirection();
	}
	
	public void addLoadcase(LoadCase inputLoadcase) {
		loadcases.add(inputLoadcase);
	}
	
	public ArrayList<LoadCase> getLoadcases(){
		return loadcases;
	}
	
	public String toString() {
		String nozzleDetails = "Nozzle: "+ name+ " has resultant allowables: "+ hasResultantAllowables + " has axrad allowables: "+ hasAxRadAllowables+ " has component allowables: "+ hasComponentAllowables + "\n";
		String resultantDetails = "";
		String axRadDetails = "";
		String componentDetails = "";
		if(hasResultantAllowables) {
			resultantDetails =" resultant force: \t"+ resultantAllowable.getResultantAllowables()[0] + "\n" +
							  " resultant moment: \t"+ resultantAllowable.getResultantAllowables()[1] + "\n";		
		}
		
		if(hasAxRadAllowables) {
			axRadDetails =" axrad force: \t"+ axRadAllowable.getAxradAllowables()[0][0] + "\t" + axRadAllowable.getAxradAllowables()[0][1]+ "\n" +
						  " axrad moment: \t"+ axRadAllowable.getAxradAllowables()[1][0] + "\t" + axRadAllowable.getAxradAllowables()[1][1]+ "\n";		
		}
		
		if(hasComponentAllowables) {
			componentDetails = " component force: \t"+ componentAllowable.getComponentAllowables()[0][0] + "\t" + componentAllowable.getComponentAllowables()[0][1]+"\t" + componentAllowable.getComponentAllowables()[0][2] + "\n" +
							   " component moment: \t"+ componentAllowable.getComponentAllowables()[1][0] + "\t" + componentAllowable.getComponentAllowables()[1][1]+"\t" + componentAllowable.getComponentAllowables()[1][2] + "\n" ;
					
		}
		return (nozzleDetails +resultantDetails + axRadDetails + componentDetails);
	}
	
}
