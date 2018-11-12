import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderExtent;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.PropertyTemplate;

public class ResultOutput {
	private HSSFWorkbook workbook;
	private Sheet sheet;
	private ArrayList<Nozzle> nozzles;
	private NozzleGroup resultantNozzlesGroup, axRadNozzlesGroup, componentNozzlesGroup;
	private ArrayList<NozzleGroup> allNozzleGroups;
	private int numberOfAllRows, numberOfTotalColumns, firstRowOfCurrentNozzle;
	private HashMap<String, CellStyle> cellStyles;
	
	public ResultOutput(ArrayList<Nozzle> nozzles) {
		this.nozzles = nozzles;
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Results");
		resultantNozzlesGroup = new NozzleGroup();
		axRadNozzlesGroup = new NozzleGroup();
		componentNozzlesGroup = new NozzleGroup();
		allNozzleGroups = new ArrayList<NozzleGroup>();
		numberOfTotalColumns = 28; //as per layout definition
		firstRowOfCurrentNozzle = 3; //0,1 and 2 rows are reserved for header text
	}
	
	public void writeAndFormatExcelFile(String filePath) throws FileNotFoundException, IOException {
		createCellStyles();
		createAllEmtpyCells();
		groupNozzlesByAllowableType();
		writeHeaderInformation();
		writeResults();
		setAutoColumnWidths();
		setFixedColumnWidths();
		hideColumnsWhichAreNotUsed();
		setZoom();
		saveWorkbookUnder(filePath);
	}
	
	private void createCellStyles() {
		XlsFonts xlsFonts= new XlsFonts();
		xlsFonts.createFontandCellStyles(workbook);
		cellStyles = xlsFonts.getCellStyles();
	}
	
	private void createAllEmtpyCells() {
		numberOfAllRows = calculateNumberOfAllRows();
		for(int k=0; k<numberOfAllRows; k++) {
			Row row = sheet.createRow(k);
			for(int j=0; j<numberOfTotalColumns; j++) {
				Cell cell = row.createCell(j);
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
				cell.setCellValue("");
			}
		}
	}
	
	private void groupNozzlesByAllowableType() {
		for(Nozzle currentNozzle: nozzles) {
			if(currentNozzle.hasResultantAllowables()) {
				resultantNozzlesGroup.addNozzle(currentNozzle);
				resultantNozzlesGroup.setNozzleGroupToResultant();
			}
			if(currentNozzle.hasAxRadAllowables()) {
				axRadNozzlesGroup.addNozzle(currentNozzle);
				axRadNozzlesGroup.setNozzleGroupToAxRad();
			}
			if(currentNozzle.hasComponentAllowables()) {
				componentNozzlesGroup.addNozzle(currentNozzle);
				componentNozzlesGroup.setNozzleGroupToComponent();
			}
		}
		allNozzleGroups.add(resultantNozzlesGroup);
		allNozzleGroups.add(axRadNozzlesGroup);
		allNozzleGroups.add(componentNozzlesGroup);
	}
	
	private void writeHeaderInformation() {
		String headerText = "Forces and moments at terminal points";
		//Enough to get the first nozzle's commissioning string. All nozzle has the same value.
		String commissioningString = nozzles.get(0).getCommissioningString();
		writeTextToCell(0, 0, headerText);
		writeTextToCell(1, 0, commissioningString);
		writeTextToCell(2, 0, "Nozzle");
		writeTextToCell(2, 1, "Loadcase");
		writeTextToCell(2, 2, "Results: forces [kN], moments [kNm], utilizations [%]");
		writeTextToCell(2, 26, "Acceptable");
		writeTextToCell(2, 27, "Remarks");
		setHeaderMergedRegions();
		setBordersAroundHeaderRegion();
	}
	
	private void writeTextToCell(int rowNo, int cellNo, String text) {
		Row row = sheet.getRow(rowNo);
		Cell cell = row.getCell(cellNo);
		cell.setCellStyle(cellStyles.get("styleP14BoldBlack"));
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		cell.setCellValue(text);
	}
	
	private void setHeaderMergedRegions() {
		//CellRangeAddress param list: firstrow, lastrow, firstcol, lastcol
		CellRangeAddress headerTopRow = new CellRangeAddress(0, 0, 0, 27); 
		sheet.addMergedRegion(headerTopRow);
		CellRangeAddress commissioningRow = new CellRangeAddress(1, 1, 0, 27); 
		sheet.addMergedRegion(commissioningRow);
		CellRangeAddress columnTitleRow = new CellRangeAddress(2, 2, 2, 25); 
		sheet.addMergedRegion(columnTitleRow);
	}
	
	private void setBordersAroundHeaderRegion() {
		PropertyTemplate pt = new PropertyTemplate();
		//medium outside borders and thin inside borders
		//CellRangeAddress param list: firstrow, lastrow, firstcol, lastcol
		//System.out.println("setting borders");
		CellRangeAddress totalNozzleField = new CellRangeAddress(0, 2, 0, 27); 
		pt.drawBorders(totalNozzleField, BorderStyle.MEDIUM, BorderExtent.OUTSIDE);
		pt.drawBorders(totalNozzleField, BorderStyle.THIN, BorderExtent.INSIDE);
		pt.applyBorders(sheet);
	}
	
	private void writeResults() {
		for(NozzleGroup currentNozzleGroup: allNozzleGroups) {
			ArrayList<Nozzle> nozzleList = currentNozzleGroup.getNozzles();
			for(Nozzle currentNozzle: nozzleList) {
					XlsData cellDataWithFormat = null;
					int rowNumberOfCurrentNozzle = currentNozzle.getLoadcases().size();
					if(currentNozzleGroup.isResultantNozzleGroup()) {
						cellDataWithFormat = new XlsDataOfResultant(sheet);
						cellDataWithFormat.addCellStyles(cellStyles);
						cellDataWithFormat.setNozzle(currentNozzle);
					}
					if(currentNozzleGroup.isAxRadNozzleGroup()) {
						cellDataWithFormat = new XlsDataOfAxRad(sheet);
						cellDataWithFormat.addCellStyles(cellStyles);
						cellDataWithFormat.setNozzle(currentNozzle);
					}
					if (currentNozzleGroup.isComponentNozzleGroup()) {
						cellDataWithFormat = new XlsDataOfComponent(sheet);
						cellDataWithFormat.addCellStyles(cellStyles);
						cellDataWithFormat.setNozzle(currentNozzle);
					}			
					cellDataWithFormat.setStartRow(firstRowOfCurrentNozzle);
					cellDataWithFormat.setNumberOfRows(rowNumberOfCurrentNozzle);
					cellDataWithFormat.setDataAndFormat();
					firstRowOfCurrentNozzle += rowNumberOfCurrentNozzle+1;
			} 
		}
	}
	
	private int calculateNumberOfAllRows() {
		int allRows = 0;
		int rowsForHeader = 3;
		allRows += rowsForHeader;
		for(int k=0; k<nozzles.size(); k++) {
			if(nozzles.get(k).hasResultantAllowables()) {
				int rowForAllowableValues = 1;
				allRows += nozzles.get(k).getLoadcases().size() + rowForAllowableValues;
			}
			if(nozzles.get(k).hasAxRadAllowables()) {
				int rowForAllowableValues = 1;
				allRows += nozzles.get(k).getLoadcases().size() + rowForAllowableValues;
			}
			if(nozzles.get(k).hasComponentAllowables()) {
				int rowForAllowableValues = 1;
				allRows += nozzles.get(k).getLoadcases().size() + rowForAllowableValues;
			}
		}
		return allRows;
	}
	
	private void setAutoColumnWidths() {
		int [] autoSizeColumnNumbers = new int [] {0, 1, 26, 27};
		
		for(int k=0; k<autoSizeColumnNumbers.length; k++) {
			sheet.autoSizeColumn(autoSizeColumnNumbers[k]);
		}
	}
	
	private void setFixedColumnWidths() {
		int [] forceMomentComumnNumbers = new int [] {2, 6, 10, 14, 18, 22};
		int [] valueColumnNumbers = new int [] {3, 4, 7, 8, 11, 12, 15, 16, 19, 20, 23, 24};
		int [] percentage = new int [] {5, 9, 13, 17, 21, 25};
		setColumnExactWidth(forceMomentComumnNumbers, 3300);
		setColumnExactWidth(valueColumnNumbers, 2400);
		setColumnExactWidth(percentage, 1000);
	}
	
	private void setColumnExactWidth(int [] columnNumbers, int width) {
		for(int k=0; k<columnNumbers.length; k++) {
			int currentColumn = columnNumbers[k];
			sheet.setColumnWidth(currentColumn, width);
		}
	}
	
	private void hideColumnsWhichAreNotUsed(){
		int [] componentResultsExtraColumnNumbers = new int [] {18, 19, 20, 21, 22, 23, 24, 25};
		int [] axradResultsExtraColumnNumbers = new int [] {10, 11, 12, 13, 14, 15, 16, 17};
		if(componentNozzlesGroup.getSize()==0) {
			hideColumn(componentResultsExtraColumnNumbers);
		}
		if(axRadNozzlesGroup.getSize()==0) {
			hideColumn(axradResultsExtraColumnNumbers);
		}
	}
	
	private void hideColumn(int [] columnNumbers) {
		for(int k=0; k<columnNumbers.length; k++) {
			int currentColumn = columnNumbers[k];
			sheet.setColumnHidden(currentColumn, true);
		}
	}
	
	private void setZoom() {
		sheet.setZoom(75);
	}
		
	public void saveWorkbookUnder(String filePath) throws FileNotFoundException, IOException {
			try (OutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}
	}
}
