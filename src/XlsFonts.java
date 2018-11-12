import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class XlsFonts {
	HSSFWorkbook workbook;
	Sheet sheet;
	Font fontP11BoldBlack, fontP11BoldRed, fontP11Black, fontP11Red;
	Font fontP14BoldBlack, fontP18BoldBlack, fontP22BoldBlack, fontP22BoldRed;
	CellStyle styleP11BoldBlack, styleP11BoldRed, styleP11Number3DecimalsBlack, styleP11Number3DecimalsRed;
	CellStyle styleP11Black, styleP11Red, styleP11Number1DecimalBlack, styleP11Number1DecimalRed;
	CellStyle styleP14BoldBlack, styleP18BoldBlack, styleP22BoldBlack, styleP22BoldRed, styleHorizontalAndVerticalCenter;
	Set<Font> fonts;
	HashMap<String, CellStyle> cellStyles;
	
	public XlsFonts() {
	
	}
	
	public void createFontandCellStyles(HSSFWorkbook workbook) {
		this.workbook = workbook;
		fonts = new HashSet<Font>();
		cellStyles = new HashMap<String, CellStyle>();
		createFontStyles();
		createCellStyles();
	}
	
	private void createFontStyles() {
		fontP11Black = workbook.createFont();
		fontP11Black.setFontName("Arial");
	    fontP11Black.setBold(false);
	    fontP11Black.setFontHeightInPoints((short) 11);
	    fontP11Black.setColor(IndexedColors.BLACK.getIndex());
	    fonts.add(fontP11Black);
	    
	    fontP11Red = workbook.createFont();
	    fontP11Red.setFontName("Arial");
	    fontP11Red.setBold(false);
	    fontP11Red.setFontHeightInPoints((short) 11);
	    fontP11Red.setColor(IndexedColors.RED.getIndex());
	    fonts.add(fontP11Red);
		
		fontP11BoldBlack = workbook.createFont();
		fontP11BoldBlack.setFontName("Arial");
	    fontP11BoldBlack.setBold(true);
	    fontP11BoldBlack.setFontHeightInPoints((short) 11);
	    fontP11BoldBlack.setColor(IndexedColors.BLACK.getIndex());
	    fonts.add(fontP11BoldBlack);
	    
	    fontP11BoldRed = workbook.createFont();
	    fontP11BoldRed.setFontName("Arial");
	    fontP11BoldRed.setBold(true);
	    fontP11BoldRed.setFontHeightInPoints((short) 11);
	    fontP11BoldRed.setColor(IndexedColors.RED.getIndex());
	    fonts.add(fontP11BoldRed);
	    
	    fontP14BoldBlack = workbook.createFont();
	    fontP14BoldBlack.setFontName("Arial");
	    fontP14BoldBlack.setBold(true);
	    fontP14BoldBlack.setFontHeightInPoints((short) 14);
	    fontP14BoldBlack.setColor(IndexedColors.BLACK.getIndex());
	    fonts.add(fontP14BoldBlack);
	    
	    fontP18BoldBlack = workbook.createFont();
	    fontP18BoldBlack.setFontName("Arial");
	    fontP18BoldBlack.setBold(true);
	    fontP18BoldBlack.setFontHeightInPoints((short) 18);
	    fontP18BoldBlack.setColor(IndexedColors.BLACK.getIndex());
	    fonts.add(fontP18BoldBlack);
	    
	    fontP22BoldBlack = workbook.createFont();
	    fontP22BoldBlack.setFontName("Arial");
	    fontP22BoldBlack.setBold(true);
	    fontP22BoldBlack.setFontHeightInPoints((short) 22);
	    fontP22BoldBlack.setColor(IndexedColors.BLACK.getIndex());
	    fonts.add(fontP22BoldBlack);
	    
	    fontP22BoldRed = workbook.createFont();
	    fontP22BoldRed.setFontName("Arial");
	    fontP22BoldRed.setBold(true);
	    fontP22BoldRed.setFontHeightInPoints((short) 22);
	    fontP22BoldRed.setColor(IndexedColors.RED.getIndex());
	    fonts.add(fontP22BoldBlack);
	}
	
	private void createCellStyles() {
		styleP11Black = workbook.createCellStyle();
	    styleP11Black.setFont(fontP11Black);
	    cellStyles.put("styleP11Black", styleP11Black);
	    
	    styleP11Red = workbook.createCellStyle();
	    styleP11Red.setFont(fontP11Red);
	    cellStyles.put("styleP11Red", styleP11Red);
		
		styleP11BoldBlack = workbook.createCellStyle();
	    styleP11BoldBlack.setFont(fontP11BoldBlack);
	    cellStyles.put("styleP11BoldBlack", styleP11BoldBlack);
	    
	    styleP11BoldRed = workbook.createCellStyle();
	    styleP11BoldRed.setFont(fontP11BoldRed);
	    cellStyles.put("styleP11BoldRed", styleP11BoldRed);
	    
	    styleP14BoldBlack = workbook.createCellStyle();
	    styleP14BoldBlack.setFont(fontP14BoldBlack);
	    cellStyles.put("styleP14BoldBlack", styleP14BoldBlack);
	    
	    styleP18BoldBlack = workbook.createCellStyle();
	    styleP18BoldBlack.setFont(fontP18BoldBlack);
	    cellStyles.put("styleP18BoldBlack", styleP18BoldBlack);
	    
	    styleP22BoldBlack = workbook.createCellStyle();
	    styleP22BoldBlack.setFont(fontP22BoldBlack);
	    cellStyles.put("styleP22BoldBlack", styleP22BoldBlack);
	    
	    styleP22BoldRed = workbook.createCellStyle();
	    styleP22BoldRed.setFont(fontP22BoldRed);
	    cellStyles.put("styleP22BoldRed", styleP11BoldRed);
	    
	    styleP11Number1DecimalBlack = workbook.createCellStyle();
	    styleP11Number1DecimalBlack.setFont(fontP11Black);
	    styleP11Number1DecimalBlack.setDataFormat(workbook.createDataFormat().getFormat("0.0"));
	    cellStyles.put("styleP11Number1DecimalBlack", styleP11Number1DecimalBlack);
	    
	    styleP11Number1DecimalRed = workbook.createCellStyle();
	    styleP11Number1DecimalRed.setFont(fontP11BoldRed);
	    styleP11Number1DecimalRed.setDataFormat(workbook.createDataFormat().getFormat("0.0"));
	    cellStyles.put("styleP11Number1DecimalRed", styleP11Number1DecimalRed);
	    
	    styleP11Number3DecimalsBlack = workbook.createCellStyle();
	    styleP11Number3DecimalsBlack.setFont(fontP11Black);
	    styleP11Number3DecimalsBlack.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	    cellStyles.put("styleP11Number3DecimalsBlack", styleP11Number3DecimalsBlack);
	    
	    styleP11Number3DecimalsRed = workbook.createCellStyle();
	    styleP11Number3DecimalsRed.setFont(fontP11BoldRed);
	    styleP11Number3DecimalsRed.setDataFormat(workbook.createDataFormat().getFormat("0.000")); 
	    cellStyles.put("styleP11Number3DecimalsRed", styleP11Number3DecimalsRed);
	    
	    styleHorizontalAndVerticalCenter = workbook.createCellStyle();
	    styleHorizontalAndVerticalCenter.setAlignment(HorizontalAlignment.CENTER);
	    styleHorizontalAndVerticalCenter.setVerticalAlignment(VerticalAlignment.CENTER);
	    cellStyles.put("styleHorizontalAndVerticalCenter", styleHorizontalAndVerticalCenter);
	}
	
	public HashMap<String, CellStyle> getCellStyles(){
		return cellStyles;
	}
    
	
	
	
	
}
