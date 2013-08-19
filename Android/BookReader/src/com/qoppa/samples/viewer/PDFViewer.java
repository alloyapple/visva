package com.qoppa.samples.viewer;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qoppa.android.pdf.IPassword;
import com.qoppa.android.pdf.source.FilePDFSource;
import com.qoppa.android.pdf.source.InputStreamPDFSource;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;
import com.samsung.svmc.pdfreader.R;

public class PDFViewer implements IPassword
{
	private LinearLayout llPagePane;
	private QScrollView svScroll;

	private float m_CurrentScale;
	private PDFDocument m_Document;
	private float m_FitToWidth;
	private LRUCache m_PageContentsCache;
	private Vector<PDFPageView> m_PageViews;
	private Activity m_ParentActivity;
	private TouchHandlerView m_TouchHandler;

	public PDFViewer(Activity parentActivity)
	{
		m_ParentActivity = parentActivity;

		StandardFontTF.mAssetMgr = m_ParentActivity.getAssets();

		llPagePane = (LinearLayout) parentActivity.findViewById(R.id.pagepane);
		svScroll = (QScrollView) parentActivity.findViewById(R.id.scrollview);
		svScroll.setPDFViewer(this);

		m_PageContentsCache = new LRUCache(20);
		m_PageViews = new Vector<PDFPageView>();
		m_TouchHandler = new TouchHandlerView(this);
	}
	
	public void onConfigurationChanged(Configuration newConfig)
	{
		if (getDocument() != null)
		{
			final int prevScroll = svScroll.getScrollY();
			final float prevScale = m_CurrentScale;

			calculateFitToWidth();
			setScale(m_FitToWidth, null);

			svScroll.post(new Runnable() {
				@Override
				public void run()
				{
					// Scroll to the same place respectively when we saved the
					// instance state
					svScroll.scrollTo(0, (int) (prevScroll * m_CurrentScale / prevScale));
					redrawVisible();
				}
			});
		}
	}
	
	public void cachePage(int pageIndex, PageContents pageContents)
	{
		m_PageContentsCache.put(pageIndex, pageContents);
	}
	
	public void clearDoc()
	{
		if (m_Document != null)
		{
			m_Document = null;

			llPagePane.removeAllViews();
			m_PageViews.clear();
			m_PageContentsCache.clear();

			updateTitle("");

			svScroll.scrollTo(0, 0);

			redrawVisible();
		}
	}
	
	public PageContents getCachcedPage(int pageIndex)
	{
		return m_PageContentsCache.get(pageIndex);
	}

	public float getCurrentScale()
	{
		return m_CurrentScale;
	}
	
	public PDFDocument getDocument()
	{
		return m_Document;
	}
	
	public float getFitToWidth()
	{
		return m_FitToWidth;
	}
	
	public float getMaximumScale()
	{
		return 64;
	}

	public float getMinimumScale()
	{
		return getFitToWidth() * .5f;
	}
	
	public int getScreenHeight()
	{
		Display display = m_ParentActivity.getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}
	
	public int getScreenWidth()
	{
		Display display = m_ParentActivity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}
	
	public QScrollView getScrollView()
	{
		return svScroll;
	}
	
	public TouchHandlerView getTouchHandler()
	{
		return m_TouchHandler;
	}
	
	public void redrawVisible()
	{
		svScroll.resetBuffers();
		svScroll.postInvalidate();
	}

	public void setScale(float scale, PointF center)
	{
		m_CurrentScale = scale;

		// force redraw of buffers
		svScroll.resetBuffers();

		for (int i = 0; i < llPagePane.getChildCount(); i++)
		{
			View view = llPagePane.getChildAt(i);
			if (view instanceof PDFPageView)
			{
				((PDFPageView) view).zoomChanged(m_CurrentScale);
			}
			else
			{
				((PageViewDivider) view).zoomChanged(m_CurrentScale);
			}
		}

		svScroll.validate();

		if (center != null)
		{
			svScroll.setShouldClamp(false);
			svScroll.scrollTo((int) center.x, (int) center.y);
			svScroll.setShouldClamp(true);
		}
	}

	//handles a double-tap zoom.  If at fitToWidth, this will zoom to double
	//fitToWidth; if not at fitToWidth, this zooms to fitToWidth
	public void zoom(PointF point)
	{
		if (m_CurrentScale == m_FitToWidth)
		{
			zoom(point, 2f / m_CurrentScale);
		}
		else
		{
			zoom(point, m_FitToWidth / m_CurrentScale);
		}
	}

	private void zoom(PointF point, float delta)
	{
		float newScale = m_CurrentScale * delta;

		if (getDocument() == null)
		{
			// nothing to zoom into
		}
		else if (delta == 1)
		{
			// no need to zoom
		}
		else
		{
			// we are zooming in, check maximum zoom
			if (delta > 1)
			{
				// limit our zoom to the maximum if needed
				if (newScale > getMaximumScale())
				{
					newScale = getMaximumScale();
				}
			}

			// we are zooming out, check minimum zoom
			if (delta < 1)
			{
				// limit our zoom to the minimum if needed
				if (newScale < getMinimumScale())
				{
					newScale = getMinimumScale();
				}
			}

			if (newScale != m_CurrentScale)
			{
				// we can zoom
				if (point == null)
				{
					final float centerX = svScroll.getScrollX() + getScreenWidth() / 2;
					final float centerY = svScroll.getScrollY() + getScreenHeight() / 2;

					float newcenterX = centerX * delta;
					float newcenterY = centerY * delta;

					point = new PointF(newcenterX - getScreenWidth() / 2, newcenterY - getScreenHeight() / 2);
				}
				else
				{
					final float centerX = svScroll.getScrollX() + point.x;
					final float centerY = svScroll.getScrollY() + point.y;

					float newcenterX = centerX * delta;
					float newcenterY = centerY * delta;

					point = new PointF(newcenterX - getScreenWidth() / 2, newcenterY - getScreenHeight() / 2);

				}

				setScale(newScale, point);
			}
		}
	}
	
	public void setupDocument(PDFDocument doc)
	{
		m_Document = doc;

		calculateFitToWidth();
		m_CurrentScale = m_FitToWidth;

		updateTitle(doc.getPDFSource().getName());

		for (int i = 0; i < m_Document.getPageCount(); i++)
		{
			PDFPageView view = new PDFPageView(m_ParentActivity, m_Document.getPage(i), this, m_CurrentScale);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(llPagePane.getLayoutParams());
			params.setMargins(0, 0, 0, 0);

			llPagePane.addView(view, params);
			m_PageViews.add(view);
			view.setOnTouchListener(m_TouchHandler);

			// add a divider, as long we're not on the last page
			if (i < m_Document.getPageCount() - 1)
			{
				llPagePane.addView(new PageViewDivider(m_ParentActivity, m_CurrentScale, view.getPage().getDisplayWidth()));
			}

			// register this view with the touchhandler
			// TODO:
			// view.setOnTouchListener(m_View.m_TouchHandler);
		}
	}

	/*
	 * This implements com.qoppa.android.pdf.IPassword. This interface must be
	 * implemented in order to open encrypted documents; the library will call
	 * this method to retrieve the password(s) when an attempt to load an
	 * encrypted document is made.
	 */
	@Override
	public synchronized String[] getPasswords()
	{
		/*
		 * We would like to present a user a dialog to enter their password, and
		 * return input from this method. Unfortunately Android does not provide
		 * a way to show a dialog and wait for user input before proceeding. So,
		 * if we simply show a dialog the application the method will finish and
		 * there will be no way to return the user input. So, this method will
		 * call wait() after displaying the dialog and notify() after the input
		 * completes, in order to pause the application while the user enters
		 * their password. This seems like a bit of a hack, but works fine.
		 */

		m_ParentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(m_ParentActivity);

				// Set an EditText view to get user input
				final EditText input = new EditText(m_ParentActivity);
				input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				input.setTransformationMethod(new PasswordTransformationMethod());
				alert.setView(input);
				alert.setTitle("Encrypted Document");
				alert.setMessage("Please enter password: ");
				alert.setIcon(null);
				alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog)
					{
						setPassword("");
					}
				});

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						Editable value = input.getText();
						if (value != null && value.toString() != null && !value.toString().equals(""))
						{
							setPassword(value.toString());
						}
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{
						setPassword("");
					}
				});

				alert.show();
			}
		});

		try
		{
			wait();
		}
		catch (InterruptedException e)
		{
			handleException(m_ParentActivity, e);
		}

		return new String[] { password };
	}

	private void calculateFitToWidth()
	{
		float pageWidth = m_Document.getPage(0).getDisplayWidth();

		int pageCount = m_Document.getPageCount();

		if (pageCount > 1)
		{
			for (int i = 1; i < pageCount; i++)
			{
				pageWidth = Math.max(pageWidth, m_Document.getPage(i).getDisplayWidth());
			}
		}

		// need to fit the entire page width in the screen (including the
		// margins)
		float scaleX = (getScreenWidth()) / pageWidth;
		m_FitToWidth = scaleX;
	}
	
	private String password = "";

	private synchronized void setPassword(String pass)
	{
		password = pass;
		notify();
	}
	
	private void updateTitle(String docName)
	{
		if (docName == null || docName.equals(""))
		{
			m_ParentActivity.setTitle(m_ParentActivity.getString(R.string.app_name));
		}
		else
		{
			m_ParentActivity.setTitle(m_ParentActivity.getString(R.string.app_name) + " - " + docName);
		}
	}
	
	public void loadDocument(InputStream stream)
	{
		LoadDocument loadTask = new LoadDocument();
		loadTask.execute(new Object[] { stream });
	}

	public void loadDocument(String path)
	{
		LoadDocument loadTask = new LoadDocument();
		loadTask.execute(new Object[] { path });
	}
	
	/*
	 * in order to show (and wait on) a progress dialog while we load a
	 * document, we do the loading in an asynchronous task
	 */
	private class LoadDocument extends AsyncTask<Object, Void, Void>
	{
		private Throwable m_Exception;
		protected ProgressDialog m_Dialog;
		private PDFDocument m_LoadedDoc;

		public LoadDocument()
		{
			super();
		}

		@Override
		protected void onPreExecute()
		{
			m_Dialog = new ProgressDialog(m_ParentActivity);
			m_Dialog.setMessage("Loading Document...");
			m_Dialog.setCancelable(false);
			m_Dialog.show();

			clearDoc();
		}

		@Override
		protected Void doInBackground(Object... path)
		{
			try
			{
				// release the current document
				if (path[0] instanceof String)
				{
					m_LoadedDoc = new PDFDocument(new FilePDFSource((String) path[0]), PDFViewer.this);
				}
				else if (path[0] instanceof InputStream)
				{
					m_LoadedDoc = new PDFDocument(new InputStreamPDFSource((InputStream) path[0]), PDFViewer.this);
				}
			}
			catch (Throwable t)
			{
				Log.e("error", Log.getStackTraceString(t));
				m_Exception = t;
			}

			return null;
		}

		protected void onPostExecute(Void unused)
		{
			m_Dialog.dismiss();

			try
			{
				if (m_Exception != null || m_LoadedDoc == null)
				{
					if (m_Exception == null)
					{
						// load failed but exception wasn't thrown. this
						// probably shouldn't happen,
						// just give some generic error message
						showMessage(m_ParentActivity, "Error loading document.");
					}
					else if (m_Exception instanceof OutOfMemoryError)
					{
						// show a friendlier error message in this case
						handleException(m_ParentActivity, m_Exception, "Ran out of memory loading document.");
					}
					else
					{
						handleException(m_ParentActivity, m_Exception);
					}
				}
				else
				{
					setupDocument(m_LoadedDoc);
				}
			}
			catch (Throwable t)
			{
				handleException(m_ParentActivity, m_Exception);
			}
		}
	}
	
	class LRUCache extends LinkedHashMap<Integer, PageContents>
	{
		int m_MaxEntries;

		public LRUCache(int maxEntries)
		{
			super(maxEntries, 0.75f, true);
			m_MaxEntries = maxEntries;
		}

		protected boolean removeEldestEntry(Map.Entry eldest)
		{
			return size() >= m_MaxEntries;
		}
	}

	public static void showMessage(final Activity activity, final String message)
	{
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	public static void handleException(Activity activity, Throwable throwable)
	{
		String message = throwable.getMessage();
		if (message == null || message.equals(""))
		{
			message = throwable.toString();
		}

		handleException(activity, throwable, message);
	}

	public static void handleException(final Activity activity, final Throwable throwable, final String message)
	{
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				Log.e("error", Log.getStackTraceString(throwable));

				String message = throwable.getMessage();
				if (message != null && !message.equals(""))
				{
					Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				else
				{
					Toast toast = Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		});
	}
}
