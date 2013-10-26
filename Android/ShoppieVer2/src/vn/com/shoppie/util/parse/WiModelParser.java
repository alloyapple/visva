package vn.com.shoppie.util.parse;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class WiModelParser extends DefaultHandler {
	String xml="";
	WiModelManager mng;
	
	public WiModelParser(){
		
	}
	public WiModelParser(String xml,WiModelManager mng){
		this.xml=xml;
		this.mng=mng;
	}
	public void parse(){
			try {
				parseXML();
			} catch (ParserConfigurationException e) {
				Log.e("ERR: WiModelParser line 33", e.getMessage());
			} catch (SAXException e) {
				Log.e("ERR: WiModelParser line 35", e.getMessage()+xml);
			} catch (IOException e) {
				Log.e("ERR: WiModelParser line 37", e.getMessage());
			}
	}

	private void parseXML() throws ParserConfigurationException, SAXException, IOException {
		// the SAXParserFactory class that creates the parser instance used.
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);

		/*
		 * Throws a ParserConfigurationException if it cannot produce a parser
		 * that matches the specified configuration of options.
		 */
		SAXParser saxParser = spf.newSAXParser();

		/*
		 * The SAXParser class, which is what the factory returns for parsing.
		 */
		XMLReader xmlReader = saxParser.getXMLReader();

		/* Set listener for parse SAX */
		WiModelParserHandler _handler=new WiModelParserHandler();
		_handler.setProtocolListener(new WiModelProtocol(mng));	// set callback
		
		xmlReader.setContentHandler(_handler);
		
		/* StartParse */
		xmlReader.parse(new InputSource(new StringReader(xml)));
		
		/* It is safer to implement some error handling. */
		xmlReader.setErrorHandler(new WiModelParserException());
		
	}

	public static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}

}