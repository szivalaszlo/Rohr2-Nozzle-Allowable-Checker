
public class AxRadAllowable {
	private double [][] axradAllowables;
	private String nozzleDirection;
	
	public AxRadAllowable() {
		nozzleDirection="";
		//[Fax][Frad]; [Max][Mrad]
		axradAllowables = new double [2][2];
	}
	
	public void setNozzleDirection(String direction) {
		nozzleDirection = direction;
	}
	
	public void setAxradAllowables(double [][] axradAllowables) {
		this.axradAllowables = axradAllowables;
	}
	
	public String getNozzleDirection() {
		return nozzleDirection;
	}
	
	public double [][] getAxradAllowables() {
		return axradAllowables;
	}
	
}
