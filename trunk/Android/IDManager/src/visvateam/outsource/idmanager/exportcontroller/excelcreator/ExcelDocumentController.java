package visvateam.outsource.idmanager.exportcontroller.excelcreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class ExcelDocumentController {
	private Context mContext;
	public static final String PATH_FOLDER = "/LeanApp/";
	public static final String PATH_FOLDER_DOCUMENT = Environment.getExternalStorageDirectory()
			.getPath() + "/LeanApp/Documents/";

	public ExcelDocumentController(Context pContext) {
		// TODO Auto-generated constructor stub
		mContext = pContext;
	}

	public File saveExcelFile(String fileName) {
		// check if available and not read only
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			Log.w("FileUtils", "Storage not available or read only");
			return null;
		}
		boolean success = false;
		// New Workbook
		Workbook wb = new HSSFWorkbook();
		Cell c = null;
		// Cell style for header row
		CellStyle cs = wb.createCellStyle();
		cs.setFillForegroundColor(HSSFColor.LIME.index);
		cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet("myOrder");
		// Generate column headings
		Row row = sheet1.createRow(0);
		c = row.createCell(0);
		c.setCellValue("Item Number");
		c.setCellStyle(cs);
		c = row.createCell(1);
		c.setCellValue("Quantity");
		c.setCellStyle(cs);
		c = row.createCell(2);
		c.setCellValue("Price");
		c.setCellStyle(cs);
		sheet1.setColumnWidth(0, (15 * 500));
		sheet1.setColumnWidth(1, (15 * 500));
		sheet1.setColumnWidth(2, (15 * 500));

		// Create a path where we will place our List of objects on external
		// storage
//		File file = new File(Environment.getExternalStorageDirectory().getPath() + PATH_FOLDER,
//				fileName);
		String filePath = PATH_FOLDER_DOCUMENT + fileName;
		File file = new File(filePath);
		FileOutputStream os = null;

		try {
			
			os = new FileOutputStream(filePath);
			wb.write(os);
			Log.w("FileUtils", "Writing file" + file);
			Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG).show();
			success = true;
		} catch (IOException e) {
			Log.w("FileUtils", "Error writing " + file, e);
		} catch (Exception e) {
			Log.w("FileUtils", "Failed to save file", e);
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception ex) {
			}
		}

		return file;
	}

	public void readExcelFile(String filename) {

		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			Log.w("FileUtils", "Storage not available or read only");
			return;
		}
		try {
			// Creating Input Stream
			File file = new File(Environment.getExternalStorageDirectory().getPath() + PATH_FOLDER,
					filename);
			FileInputStream myInput = new FileInputStream(file);
			// Create a POIFSFileSystem object
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
			// Create a workbook using the File System
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
			// Get the first sheet from workbook
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);
			/** We now need something to iterate through the cells. **/
			Iterator<Row> rowIter = mySheet.rowIterator();
			while (rowIter.hasNext()) {
				HSSFRow myRow = (HSSFRow) rowIter.next();
				Iterator<Cell> cellIter = myRow.cellIterator();
				while (cellIter.hasNext()) {
					HSSFCell myCell = (HSSFCell) cellIter.next();
					Log.w("FileUtils", "Cell Value: " + myCell.toString());
					Toast.makeText(mContext, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	public static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	public static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}
}
