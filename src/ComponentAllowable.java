
public class ComponentAllowable {
	private double [][] componentAllowables;
	
	public ComponentAllowable() {
		//[Fx][Fy][Fz]; [Mx][My][Mz]
		componentAllowables = new double [2][3];
	}
	
	public void setComponentAllowables(double [][] componentAllowables) {
		this.componentAllowables = componentAllowables;
	}
	
	public double [][] getComponentAllowables() {
		return componentAllowables;
	}
}
