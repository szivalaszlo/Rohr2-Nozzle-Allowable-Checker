import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.PropertyTemplate;

public abstract class XlsData {
	private int startRow;
	private int numberOfRows;
	private int lastRow;
	protected Sheet sheet;
	private Map<Integer, String> fixedTextList;
	private HashMap<String, CellStyle> cellStyles;
	private HSSFWorkbook workbook;
	
	
	public XlsData(Sheet sheet) {
		this.sheet = sheet;
		this.workbook = (HSSFWorkbook) sheet.getWorkbook();
		fixedTextList = new HashMap<Integer, String>();
	}
	
	abstract void setDataAndFormat();
	
	abstract void setNozzle(Nozzle nozzle);
	
	public void formatArea() {
		setFont();
		setBordersAroundNozzleRegion();
		setMergedRegions();
		writeFixedText();
	}
	
	public void addCellStyles(HashMap<String, CellStyle> cellStyles) {
		this.cellStyles = cellStyles;
	}
	
	public HashMap<String, CellStyle> getCellStyles(){
		return cellStyles;
	}
	
	private void setFont() {
		int [] fixedTextColumnNumbers = new int [] {1, 2, 6, 10, 14, 18, 22};
		int [] percentageSymbolColumnNumbers = new int [] {5, 9, 13, 17, 21, 25};
		int [] valuesColumnNumbers = new int [] {3, 7, 11, 15, 19, 23};
		int [] utilisationColumnNumbers = new int [] {4, 8, 12, 16, 20, 24};
		for(int k= startRow; k<=lastRow; k++) {
			formatTextColumnOfRow(k, fixedTextColumnNumbers, "styleP11BoldBlack");
			formatTextColumnOfRow(k, percentageSymbolColumnNumbers, "styleP11Black");
			formatTextColumnOfRow(k, valuesColumnNumbers, "styleP11Number3DecimalsBlack");
			formatTextColumnOfRow(k, utilisationColumnNumbers, "styleP11Number1DecimalBlack");
			formatAccordingToUtilisationRow(k);
		}
	}
	
	private void formatTextColumnOfRow(int rowNo, int [] columnNumbers, String cellStyleName) {
		Row row = sheet.getRow(rowNo);
		for (int k=0; k<columnNumbers.length; k++) {
			int currentColumn = columnNumbers[k];
			Cell cell = row.getCell(currentColumn);
			cell.setCellStyle(cellStyles.get(cellStyleName));
			CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		}
	}
	
	private void formatAccordingToUtilisationRow(int rowNo) {
		Row row = sheet.getRow(rowNo);
		int [] utilisationColumnNumbers = new int [] {4, 8, 12, 16, 20, 24};
		for (int k=0; k<utilisationColumnNumbers.length; k++) {
			int currentColumn = utilisationColumnNumbers[k];
			Cell cell = row.getCell(currentColumn);
			if (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue()> 100.0) {
				cell.setCellStyle(cellStyles.get("styleP11Number1DecimalRed"));
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				setLoadcaseTextRedOfRow(rowNo);	
			 }
		}
	}
	
	private void setLoadcaseTextRedOfRow(int rowNo) {
		Row row = sheet.getRow(rowNo);
		Cell cell = row.getCell(1);
		cell.setCellStyle(cellStyles.get("styleP11BoldRed"));
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
	}
	
	private void setBordersAroundNozzleRegion() {
		PropertyTemplate pt = new PropertyTemplate();
		//medium outside borders and thin inside borders
		//CellRangeAddress param list: firstrow, lastrow, firstcol, lastcol
		//System.out.println("setting borders");
		CellRangeAddress totalNozzleField = new CellRangeAddress(startRow, lastRow, 0, 27); 
		pt.drawBorders(totalNozzleField, BorderStyle.MEDIUM, BorderExtent.OUTSIDE);
		pt.drawBorders(totalNozzleField, BorderStyle.THIN, BorderExtent.INSIDE);
		pt.applyBorders(sheet);
	}
	
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
		lastRow = startRow + numberOfRows;
	}	
	
	private void setMergedRegions() {
		//CellRangeAddress param list: firstrow, lastrow, firstcol, lastcol
		CellRangeAddress nozzleNameField = new CellRangeAddress(startRow, lastRow, 0, 0); 
		sheet.addMergedRegion(nozzleNameField);
		CellRangeAddress acceptableField = new CellRangeAddress(startRow, lastRow, 26, 26); 
		sheet.addMergedRegion(acceptableField);
		CellRangeAddress remarksField = new CellRangeAddress(startRow, lastRow, 27, 27); 
		sheet.addMergedRegion(remarksField);
	}
	
	public void setFixedText(Map<Integer, String> fixedTextList) {
		this.fixedTextList = fixedTextList;
	}
	
	private void writeFixedText() {
		for(int k=startRow; k<=lastRow; k++) {
			Row currentRow = sheet.getRow(k); 
			for(int currentColumn : fixedTextList.keySet()) {
				String currentFixedText = fixedTextList.get(currentColumn);
				Cell cell = currentRow.getCell(currentColumn);
				//scell.setCellStyle(cellStyles.get("styleP11Black"));
				cell.setCellValue(currentFixedText);
			}
		}
	}

	public int getStartRow() {
		return startRow;
	}

	public Sheet getSheet() {
		return sheet;
	}
}
