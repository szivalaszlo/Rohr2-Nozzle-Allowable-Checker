import java.util.ArrayList;

public class LoadCase {
	private String loadcaseName, nozzleName, nozzleDirection;
	private String [] coordinateAxes;
	private double Fx, Fy, Fz, Mx, My, Mz, resultantForceUtilization, resultantMomentUtilization;
	private double resultantForce, resultantMoment;
	private double[] forces, moments;
	private double [][] forcesMoments, axRadForces, axRadMoments, componentUtilization, axRadUtilization, axRadForcesMoments;
	private AxRadAllowable axRadAllowable;
	private ComponentAllowable componentAllowable;
	private ResultantAllowable resultantAllowable;
	private boolean hasAxRadAllowables, hasComponentAllowables, hasResultantAllowables;

	public LoadCase() {
		forces = new double[3];
		moments = new double[3];
		axRadForces = new double [3][2];
		axRadMoments = new double [3][2];
		coordinateAxes = new String [3]; 
		coordinateAxes [0] = "X";
		coordinateAxes [1] = "Y";
		coordinateAxes [2] = "Z";
		nozzleDirection = "";
		hasAxRadAllowables = false;
		hasComponentAllowables = false;
		hasResultantAllowables = false;
		componentUtilization = new double [2][3];
		axRadUtilization = new double [2][3];
		resultantForce = 0;
		resultantMoment = 0;
		resultantForceUtilization = 0;
		resultantMomentUtilization = 0;
		forcesMoments = new double [2][2];
		axRadForcesMoments = new double [2][2];
	}
	
	public void setLoadcaseName(String loadcaseName) {
		this.loadcaseName = loadcaseName;
	}
	
	public String getLoadcaseName() {
		return loadcaseName;
	}
	
	public void setNozzleName(String nozzleName) {
		this.nozzleName = nozzleName;
	}

	public void setNozzleDirection(String nozzleDirection) {
		this.nozzleDirection = nozzleDirection.toUpperCase();
	}
	
	public void setForceAndMoment(double [][] inputValues) {
		this.Fx = inputValues[0][0];
		this.Fy = inputValues[0][1];
		this.Fz = inputValues[0][2];
		forces[0] = Fx;
		forces[1] = Fy;
		forces[2] = Fz;
		this.Mx = inputValues[1][0];
		this.My = inputValues[1][1];
		this.Mz = inputValues[1][2];
		moments[0] = Mx;
		moments[1] = My;
		moments[2] = Mz;
		forcesMoments[0] = forces;
		forcesMoments[1] = moments;
	}
	
	public void calculateAllAxradAndResultantForcesMoments() {
		calculateAxRadForces();
		calculateAxRadMoments();
		calculateResultantForces();
	}
	
	private void calculateAxRadForces() {
		//[Fx][Frad]; [Fy][Frad]; [Fz][Frad]
		axRadForces[0][0] = Fx;
		axRadForces[0][1] = Math.sqrt(Fy*Fy+Fz*Fz);
		axRadForces[1][0] = Fy;
		axRadForces[1][1] = Math.sqrt(Fx*Fx+Fz*Fz);
		axRadForces[2][0] = Fz;
		axRadForces[2][1] = Math.sqrt(Fx*Fx+Fy*Fy);
	}
	
	private void calculateAxRadMoments() {
		//[Mx][Mrad]; [My][Mrad]; [Mz][Mrad]
		axRadMoments[0][0] = Mx;
		axRadMoments[0][1] = Math.sqrt(My*My+Mz*Mz);
		axRadMoments[1][0] = My;
		axRadMoments[1][1] = Math.sqrt(Mx*Mx+Mz*Mz);
		axRadMoments[2][0] = Mz;
		axRadMoments[2][1] = Math.sqrt(Mx*Mx+My*My);
	}
	
	private void calculateResultantForces() {
		resultantForce = Math.sqrt(Fx*Fx+Fy*Fy+Fz*Fz);
		//System.out.println(" resultant force: "+ resultantForce);
		resultantMoment = Math.sqrt(Mx*Mx+My*My+Mz*Mz);
		//System.out.println(" resultant moment: " + resultantMoment);
	}
	
	private double getRadialResultant(double [] values, String nozzleDirection) {
		ArrayList<Double> currentValues = new ArrayList<Double>();
		int sequentialNumberOfNozzleDirection = getAxisNumberOfNozzleDirection(nozzleDirection);
		//sequentialNumberOfNozzleDirection defined as: X=0; Y=1; Z=3
		for (int k=0; k<values.length; k++) {
			currentValues.add(values[k]);
		}
		if(sequentialNumberOfNozzleDirection == -1) {
			System.out.println("Nozzle direction defined in allowable input file does not mactch X, Y or Z!");
		}
		currentValues.remove(sequentialNumberOfNozzleDirection);
		double radialResultant = Math.sqrt(currentValues.get(0)*currentValues.get(0)+currentValues.get(1)*currentValues.get(1));
		return radialResultant;
	}
	
	private int getAxisNumberOfNozzleDirection(String nozzleDirection) {
		for(int k=0; k<coordinateAxes.length; k++) {
			if (coordinateAxes[k].contains(nozzleDirection)) {
				return k;
			}
		}
		return -1;
	}
	
	public void setAxradAllowables(AxRadAllowable axRadAllowable) {
		this.axRadAllowable = axRadAllowable;
		hasAxRadAllowables = true;
	}
	
	public void setComponentAllowables(ComponentAllowable componentAllowable) {
		this.componentAllowable = componentAllowable;
		hasComponentAllowables = true;
	}
	
	public void setResultantAllowables(ResultantAllowable resultantAllowable) {
		this.resultantAllowable = resultantAllowable;
		hasResultantAllowables = true;
	}
	
	public void calculateAllUtilization() {
		if(hasAxRadAllowables) {
			calculateAxRadUtilization();
		}
		if(hasComponentAllowables) {
			caculateComponentUtilization();
		}
		if(hasResultantAllowables) {
			calculateResultantUtilization();
		}
	}
	
	private void calculateResultantUtilization() {
		double resultantForceAllowable = resultantAllowable.getResultantAllowables()[0];
		double resultantMomentAllowable = resultantAllowable.getResultantAllowables()[1];
		resultantForceUtilization = Math.abs(resultantForce / resultantForceAllowable * 100);
		resultantMomentUtilization = Math.abs(resultantMoment / resultantMomentAllowable * 100);
	}
	
	public double [] getResultantUtilization() {
		double [] resultantUtil = new double [2];
		resultantUtil[0] = resultantForceUtilization;
		resultantUtil[1] = resultantMomentUtilization;
		return resultantUtil;
	}
	
	private void caculateComponentUtilization() {
		double [][] componentAllowables = componentAllowable.getComponentAllowables();
		for (int k=0; k<2; k++) {
			for (int j=0; j<3; j++) {
				componentUtilization[k][j]= Math.abs(forcesMoments[k][j] / componentAllowables[k][j] * 100);
			}
		}
	}
	
	public double[][] getComponentUtilization(){
		return componentUtilization;
	}
	
	private void calculateAxRadUtilization() {
		double [][] axRadAllowables = axRadAllowable.getAxradAllowables();
		String nozzleDirection = axRadAllowable.getNozzleDirection();
		int axisNumber = getAxisNumberOfNozzleDirection(nozzleDirection);
		axRadForcesMoments[0] = axRadForces[axisNumber];
		axRadForcesMoments[1] = axRadMoments[axisNumber];
		for(int k=0; k<2; k++) {
			for (int j=0; j<2; j++) {
				axRadUtilization[k][j] =  Math.abs(axRadForcesMoments[k][j] / axRadAllowables[k][j] * 100);
			}
		}	
	}
	
	public double[][] getAxRadUtilization(){
		return axRadUtilization;
	}
	
	public double [][] getForceMoments(){
		return forcesMoments;
	}
	
	public double [][] getAxRadForceMoments(){
		return axRadForcesMoments;
	}
	
	public double [] getResultantForcesMoments() {
		double resultantForcesMoments [] = new double [2];
		resultantForcesMoments [0] = resultantForce;
		resultantForcesMoments [1] = resultantMoment;
		return resultantForcesMoments;
	}
	
	public String printAllowables() {
		String nozzleDetails = "Nozzle: "+ nozzleName + " has resultant allowables: "+ hasResultantAllowables + " has axrad allowables: "+ hasAxRadAllowables+ " has component allowables: "+ hasComponentAllowables + "\n";
		String resultantDetails = "";
		String axRadDetails = "";
		String componentDetails = "";
		String resultantUtilDetails = "";
		String axRadUtilDetails = "";
		String componentUtilDetails = "";
		if(hasResultantAllowables) {
			resultantDetails =" resultant force: \t"+ resultantAllowable.getResultantAllowables()[0] + "\n" +
							  " resultant moment: \t"+ resultantAllowable.getResultantAllowables()[1] + "\n";		
		
			resultantUtilDetails = " resultant force utilization: \t"+ resultantForceUtilization + "\n" +
					          " resultant moment utilization: \t"+ resultantMomentUtilization + "\n";
		}
		
		if(hasAxRadAllowables) {
			axRadDetails =" axrad force: \t"+ axRadAllowable.getAxradAllowables()[0][0] + "\t" + axRadAllowable.getAxradAllowables()[0][1]+ "\n" +
						  " axrad moment: \t"+ axRadAllowable.getAxradAllowables()[1][0] + "\t" + axRadAllowable.getAxradAllowables()[1][1]+ "\n";		
		
		
			axRadUtilDetails = " axrad force utilization: \t"+ axRadUtilization[0][0] + "\t" + axRadUtilization[0][1]+ "\n" +
					           " axrad moment utilization: \t"+ axRadUtilization[1][0] + "\t" + axRadUtilization[1][1]+ "\n";		
		}
		
		if(hasComponentAllowables) {
			componentDetails = " component force: \t"+ componentAllowable.getComponentAllowables()[0][0] + "\t" + componentAllowable.getComponentAllowables()[0][1]+"\t" + componentAllowable.getComponentAllowables()[0][2] + "\n" +
							   " component moment: \t"+ componentAllowable.getComponentAllowables()[1][0] + "\t" + componentAllowable.getComponentAllowables()[1][1]+"\t" + componentAllowable.getComponentAllowables()[1][2] + "\n" ;
			
			componentUtilDetails = " component force utilization: \t"+ componentUtilization[0][0] + "\t" + componentUtilization[0][1]+"\t" + componentUtilization[0][2] + "\n" +
					               " component moment utilization: \t"+ componentUtilization[1][0] + "\t" + componentUtilization[1][1]+"\t" + componentUtilization[1][2] + "\n" ;
		}
		return (nozzleDetails  +resultantDetails +resultantUtilDetails + axRadDetails +axRadUtilDetails + componentDetails+ componentUtilDetails);
	}
	
	public String toString() {
		return ("Nozzle: "+ nozzleName +  "\t loadcase: " + loadcaseName + "\n" +
				"F-Mx \t \t" + "F-My \t \t" + "F-Mz +\n" +
				Fx + "\t \t" + Fy + "\t \t" + Fz + "\n" +
				Mx + "\t \t" + My + "\t \t" + Mz + "\n" + printAllowables());
	}	
}
