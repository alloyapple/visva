package vn.com.shoppie.util.parse;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WiModelParserException implements ErrorHandler{


	WiModelParserException() {
	}
	
	private String getParseExceptionInfo(SAXParseException spe) {
		String systemId = spe.getSystemId();

		if (systemId == null) {
			systemId = "null";
		}

		String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
				+ ": " + spe.getMessage();

		return info;
	}

	public void warning(SAXParseException spe) throws SAXException {
		
	}
	/* nonFatal error! */
	public void error(SAXParseException spe) throws SAXException {
		String message = "Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}
	/* When a fatal error occurs, the parser cannot continue. */
	public void fatalError(SAXParseException spe) throws SAXException {
		String message = "Fatal Error: " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}

}
