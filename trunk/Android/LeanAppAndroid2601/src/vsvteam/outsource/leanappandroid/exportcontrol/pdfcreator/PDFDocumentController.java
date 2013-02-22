package vsvteam.outsource.leanappandroid.exportcontrol.pdfcreator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.collection.PdfTargetDictionary;

public class PDFDocumentController {

	private Document document;
	private Context mContext;

	public PDFDocumentController(Context context) {
		this.mContext = context;
		// document = new Document();
	}

	public Document creatPDFDocument(String pFolder, String pFileName) {
		Document dcm = new Document();
		String file = pFolder + "/" + pFileName + ".pdf";
		try {
			PdfWriter.getInstance(dcm, new FileOutputStream(file));
			Log.e("pdf", "creat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dcm;
	}

	public void addText(Document document, String content) throws DocumentException {
		document.add(new Paragraph(content));
	}

	public PdfPTable addTable(String[] header) {
		PdfPTable table = new PdfPTable(header.length);
		for (int i = 0; i < header.length; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(header[i]));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
		}
		table.setHeaderRows(1);
		return table;
	}

	public void addContentTable(PdfPTable table, String[] content) {
		for (int i = 0; i < content.length; i++) {
			table.addCell(content[i]);
		}
	}

}
