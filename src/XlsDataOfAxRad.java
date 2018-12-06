import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellUtil;

public class XlsDataOfAxRad extends XlsData {
	private Map<Integer, String> axRadTextList;
	private Nozzle nozzle;
	private double [][] axRadAllowable;
	private ArrayList<LoadCase> loadcases;
	private HashMap<String, CellStyle> cellStyles;
	private int [] valueColumnNumbers, utilisationColumnNumbers;
	private int startRow;
	private boolean isOK;
	
	public XlsDataOfAxRad(Sheet sheet) {
		super(sheet);
		
		valueColumnNumbers = new int [] {3, 7, 11, 15};
		utilisationColumnNumbers = new int [] {4, 8, 12, 16};
		
		axRadTextList = new HashMap<Integer, String>();
		axRadTextList.put(2, "Fax [kN]");
		axRadTextList.put(5, "%");
		axRadTextList.put(6, "Frad [kN]");
		axRadTextList.put(9, "%");
		axRadTextList.put(10, "Max [kNm]");
		axRadTextList.put(13, "%");
		axRadTextList.put(14, "Mrad [kNm]");
		axRadTextList.put(17, "%");
		isOK = true;
	}
	
	public void setDataAndFormat() {
		this.startRow = super.getStartRow();
		this.cellStyles = super.getCellStyles();
		setFixedText(axRadTextList);
		writeNozzleName();
		writeAllowableLine();
		writeLoadcaseLines();
		formatArea();
	}

	public void setNozzle(Nozzle nozzle) {
		this.nozzle = nozzle;
		this.axRadAllowable = nozzle.getAxradAllowables().getAxradAllowables();
		this.loadcases = nozzle.getLoadcases();
	}
	
	private void writeNozzleName() {
		String nozzleName = nozzle.getName();
		Row firstRowOfNozzle = sheet.getRow(startRow); 
		Cell cell = firstRowOfNozzle.getCell(0);
		String neededCellStyle = "styleP11BoldBlack";
		cell.setCellStyle(cellStyles.get(neededCellStyle));
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
		cell.setCellValue(nozzleName);
	}
	
	private void writeAllowableLine() {
		Row allowableRow = sheet.getRow(startRow); 
		Cell nameCell = allowableRow.getCell(1);
		nameCell.setCellValue("ALLOWABLE");
		CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
		writeData(axRadAllowable, valueColumnNumbers, startRow);
	}
	
	private void writeData(double [][] inputValues, int[] columnNumbers, int rowNumber) {
		Row currentRow = sheet.getRow(rowNumber);
		int coulmnPositionCounter = 0;
		for (int k=0; k<2; k++) {
			for (int j=0; j<2; j++) {
				double currentValue = inputValues[k][j];
				int columnNumber = columnNumbers[coulmnPositionCounter];
				Cell cell = currentRow.getCell(columnNumber);
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				cell.setCellValue(currentValue);
				coulmnPositionCounter++;
			}
		}
	}
	
	private void writeLoadcaseLines() {
		int rowCounterLoadcase = startRow + 1;
		for(int k=0; k<loadcases.size(); k++) {
			if(isOK) {
				setOKStatus();
			}
			Row currentLoadcaseRow = sheet.getRow(rowCounterLoadcase);
			Cell nameCell = currentLoadcaseRow.getCell(1);
			LoadCase currentLoadcase = loadcases.get(k);
			String currentLoadcaseName = loadcases.get(k).getLoadcaseName();
			nameCell.setCellValue(currentLoadcaseName);
			CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
			double [][] currentAxRadForcesMoments = currentLoadcase.getAxRadForceMoments();
			double [][] utilisation = currentLoadcase.getAxRadUtilization();
			writeData(currentAxRadForcesMoments, valueColumnNumbers, rowCounterLoadcase);
			writeData(utilisation, utilisationColumnNumbers, rowCounterLoadcase);
			if(utilisationOver100(utilisation)) {
				setNotOKStatus();
			}
			rowCounterLoadcase++;
		}
	}
	
	private void setOKStatus() {
		Row okRow = sheet.getRow(startRow); 
		Cell nameCell = okRow.getCell(26);
		nameCell.setCellStyle(cellStyles.get("styleP11BoldBlack"));
		CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(nameCell, VerticalAlignment.CENTER);
		nameCell.setCellValue("OK");
	}
	
	private void setNotOKStatus() {
		Row okRow = sheet.getRow(startRow); 
		Cell nameCell = okRow.getCell(26);
		nameCell.setCellStyle(cellStyles.get("styleP11BoldBlack"));
		CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(nameCell, VerticalAlignment.CENTER);
		nameCell.setCellValue("Not OK");
		isOK = false;
	}
	
	private boolean utilisationOver100(double [][] utilisation) {
		for (int k=0; k<2; k++) {
			for (int j=0; j<2; j++) {
				double currentValue = utilisation[k][j];
				if(currentValue >= 100) {
					return true;
				}
			}
		}
		return false;
	}
}







