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

public class XlsDataOfResultant extends XlsData {
	private Map<Integer, String> resultantTextList;
	private Nozzle nozzle;
	private double [] resultantAllowable;
	private ArrayList<LoadCase> loadcases;
	private HashMap<String, CellStyle> cellStyles;
	private int [] valueColumnNumbers, utilisationColumnNumbers;
	private int startRow;
	
	public XlsDataOfResultant(Sheet sheet) {
		super(sheet);
		
		valueColumnNumbers = new int [] {3, 7};
		utilisationColumnNumbers = new int [] {4, 8};
		
		resultantTextList = new HashMap<Integer, String>();
		resultantTextList.put(2, "Fres [kN]");
		resultantTextList.put(5, "%");
		resultantTextList.put(6, "Mres [kNm]");
		resultantTextList.put(9, "%");
	}
	
	public void setDataAndFormat() {
		this.startRow = super.getStartRow();
		this.cellStyles = super.getCellStyles();
		setFixedText(resultantTextList);
		writeNozzleName();
		writeAllowableLine();
		writeLoadcaseLines();
		super.formatArea();
	}
	
	public void setNozzle(Nozzle nozzle) {
		this.nozzle = nozzle;
		this.resultantAllowable = nozzle.getResultantAllowables().getResultantAllowables();
		this.loadcases = nozzle.getLoadcases();
	}
	
	private void writeNozzleName() {
		String nozzleName = nozzle.getName();
		Row firstRowOfNozzle = sheet.getRow(startRow); 
		Cell cell = firstRowOfNozzle.getCell(0);
		cell.setCellStyle(cellStyles.get("styleP11BoldBlack"));
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
		cell.setCellValue(nozzleName);
	}
	
	private void writeAllowableLine() {
		Row allowableRow = sheet.getRow(startRow); 
		Cell nameCell = allowableRow.getCell(1);
		nameCell.setCellValue("ALLOWABLE");
		CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
		writeData(resultantAllowable, valueColumnNumbers, startRow);
	}
	
	private void writeLoadcaseLines() {
		int rowCounterLoadcase = startRow + 1;
		for(int k=0; k<loadcases.size(); k++) {
			setOKStatus();
			Row currentLoadcaseRow = sheet.getRow(rowCounterLoadcase);
			Cell nameCell = currentLoadcaseRow.getCell(1);
			LoadCase currentLoadcase = loadcases.get(k);
			String currentLoadcaseName = loadcases.get(k).getLoadcaseName();
			nameCell.setCellValue(currentLoadcaseName);
			CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
			double []currentResultantForcesMoments = currentLoadcase.getResultantForcesMoments();
			double [] utilisation = currentLoadcase.getResultantUtilization();
			writeData(currentResultantForcesMoments, valueColumnNumbers, rowCounterLoadcase);
			writeData(utilisation, utilisationColumnNumbers, rowCounterLoadcase);
			if(utilisationOver100(utilisation)) {
				setNotOKStatus();
			}
			rowCounterLoadcase++;
		}
	}
	
	private void writeData(double [] inputValues, int[] columnNumbers, int rowNumber) {
		Row currentRow = sheet.getRow(rowNumber);
		int coulmnPositionCounter = 0;
		for (int k=0; k<2; k++) {
				double currentValue = inputValues[k];
				int columnNumber = columnNumbers[coulmnPositionCounter];
				Cell cell = currentRow.getCell(columnNumber);
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				cell.setCellValue(currentValue);
				coulmnPositionCounter++;
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
	}
	
	private boolean utilisationOver100(double [] utilisation) {
		for (int k=0; k<2; k++) {
			double currentValue = utilisation[k];
			if(currentValue >= 100) {
				return true;
			}
		}
		return false;
	}
}