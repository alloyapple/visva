package vsvteam.outsource.leanappandroid.exportcontrol;

//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFCanvas;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFFontStandard;
import com.qoppa.android.pdfProcess.PDFFontStandard.PDFFontFamily;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;

public class ExportPDF {
	private final static int PAGE_WIDTH = 612;
	private final static int PAGE_HEIGHT = 792;
	private Context mContext;

	public ExportPDF(Context pContext) {
		mContext = pContext;
	}

	public PDFDocument creatPDFDocument() {
		StandardFontTF.mAssetMgr = mContext.getAssets();
		// create a new document and append a page
		PDFDocument pdf = null;
		try {
			pdf = new PDFDocument();
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pdf;
	}

	@SuppressWarnings("deprecation")
	public void savePDFFile(PDFDocument pdf, String pFilePath) {
		try {
			pdf.saveDocument(pFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void creatPDFFile(String pFolder, String pFileName) {
		savePDFFile(creatPDFDocument(), "/sdcard/" + pFolder + "/" + pFileName
				+ ".pdf");
	}

	public void addContentToPDF(PDFDocument pdf, ContentPDF pContent) {
		try {
			StandardFontTF.mAssetMgr = mContext.getAssets();
			PDFPage page = pdf.appendNewPage(PAGE_WIDTH, PAGE_HEIGHT);
			// the PDFCanvas object is used to draw to the page
			PDFCanvas canvas = page.createCanvas();
			// add some text
			switch (pContent.getType()) {
			case ContentPDF.TYPE_TEXT: {
				PDFFontStandard font = new PDFFontStandard(
						PDFFontFamily.HELVETICA, PDFFontStandard.Style.NORMAL,
						22);
				canvas.drawText(pContent.getTextContent(), Color.BLACK, 20, 20,
						font);
			}
				break;
			case ContentPDF.TYPE_IMAGE: {
				// PDFPaint paint = new PDFPaint();
				// paint.setStyle(PDFPaint.Style.STROKE);
				// paint.setStrokeWidth(1);
				// paint.setStrokeColor(Color.RED);
				// canvas.drawRect(40, 100, 120, 240, paint);
				Matrix matrix = new Matrix();
				matrix.preTranslate(300, 300);
				matrix.preScale(2, 2);
				canvas.drawBitmap(pContent.getImageContent(), matrix, null);
			}
				break;
			case ContentPDF.TYPE_BOTH: {
				PDFFontStandard font = new PDFFontStandard(
						PDFFontFamily.HELVETICA, PDFFontStandard.Style.NORMAL,
						22);
				canvas.drawText(pContent.getTextContent(), Color.BLACK, 20, 20,
						font);
				Matrix matrix = new Matrix();
				matrix.preTranslate(300, 300);
				matrix.preScale(2, 2);
				canvas.drawBitmap(pContent.getImageContent(), matrix, null);
			}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e("error", e.toString());
		}
	}

	public Bitmap savePDFPageToBitmap(PDFDocument pdf, int pPage) {
		Bitmap bm = null;
		try {

			StandardFontTF.mAssetMgr = mContext.getAssets();
			// Load a document and get the first page
			PDFPage page = pdf.getPage(pPage);
			// Create a bitmap and canvas to draw the page into
			int width = (int) Math.ceil(page.getDisplayWidth());
			int height = (int) Math.ceil(page.getDisplayHeight());
			bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			// Create canvas to draw into the bitmap
			Canvas c = new Canvas(bm);
			// paint the page into the canvas
			page.paintPage(c);
			// Save the bitmap
			// OutputStream outStream = new
			// FileOutputStream("/sdcard/output.jpg");
			// bm.compress(CompressFormat.JPEG, 80, outStream);
			// outStream.close();
		} catch (Exception e) {
			Log.e("error", Log.getStackTraceString(e));
		}
		return bm;
	}

	public void viewPDFDocument() {
	}

	class ContentPDF {
		public final static int TYPE_TEXT = 0;
		public final static int TYPE_IMAGE = 1;
		public final static int TYPE_BOTH = 2;
		private String mTextContent = "";
		private Bitmap mImageContent = null;
		private int mType;

		public ContentPDF(String pTextContent) {
			mTextContent = pTextContent;
			mType = TYPE_TEXT;
		}

		public ContentPDF(String pTextContent, Bitmap pImageContent) {
			mTextContent = pTextContent;
			mImageContent = pImageContent;
			mType = TYPE_BOTH;
		}

		public ContentPDF(Bitmap pImageContent) {
			mImageContent = pImageContent;
			mType = TYPE_IMAGE;
		}

		public void setTextContent(String mTextContent) {
			this.mTextContent = mTextContent;
		}

		public String getTextContent() {
			return mTextContent;
		}

		public void setImageContent(Bitmap mImageContent) {
			this.mImageContent = mImageContent;
		}

		public Bitmap getImageContent() {
			return mImageContent;
		}

		public void setType(int mType) {
			this.mType = mType;
		}

		public int getType() {
			return mType;
		}
	}
}
