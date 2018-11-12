import java.util.ArrayList;

public class NozzleGroup {
	private ArrayList<Nozzle> nozzles;
	private boolean isResultantNozzleGroup, isAxRadNozzleGroup, isComponentNozzleGroup;
	
	
	public NozzleGroup() {
		nozzles = new ArrayList<Nozzle>();
		isResultantNozzleGroup = false;
		isAxRadNozzleGroup = false;
		isComponentNozzleGroup = false;
	}
	
	public void addNozzle(Nozzle nozzle) {
		nozzles.add(nozzle);
	}
	
	public ArrayList<Nozzle> getNozzles(){
		return nozzles;
	}
	
	public boolean isResultantNozzleGroup() {
		return isResultantNozzleGroup;
	}
	
	public boolean isAxRadNozzleGroup() {
		return isAxRadNozzleGroup;
	}
	
	public boolean isComponentNozzleGroup() {
		return isComponentNozzleGroup;
	}
	
	public void setNozzleGroupToResultant() {
		isResultantNozzleGroup = true;
	}
	
	public void setNozzleGroupToAxRad() {
		isAxRadNozzleGroup = true;
	}
	
	public void setNozzleGroupToComponent() {
		isComponentNozzleGroup = true;
	}
	
	public int getSize() {
		return nozzles.size();
	}
	
	public boolean contains(Nozzle nozzle) {
		return nozzles.contains(nozzle);
	}
}
