package vn.com.shoppie.util.parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WiModelParserHandler extends DefaultHandler {
	private OnParseListener _parseProtocol;
	private String value="";
	public WiModelParserHandler() {
	}

	public void setProtocolListener(OnParseListener parserListener) {
		this._parseProtocol = parserListener;
	}

	/*
	 * ============================Default Handler for SAX
	 * parser===============================
	 */
	/*
	 * The major event-handling methods are: startDocument, endDocument,
	 * startElement, endElement.
	 */

	/*
	 * Defines what the application does when the parser encounters the start
	 * points of the document being parsed.
	 */
	public void startDocument() throws SAXException {

		if (_parseProtocol != null)
			_parseProtocol.startDocument();
	}

	/*
	 * Defines what the application does when the parser encounters the end
	 * points of the document being parsed.
	 */
	public void endDocument() throws SAXException {
		if (_parseProtocol != null)
			_parseProtocol.finishDocument();
	}

	/*
	 * When a start tag is encountered. Any attributes it defines are also
	 * passed in an Attributes list. Characters found within the element are
	 * passed as an array of characters.
	 */
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		/*
		 * Processes the element tags, including: the namespace universal
		 * resource identifier (URI), the local name and the qualified name of
		 * that element, any attributes defined in the start tag.
		 */
		if (_parseProtocol != null) {
			_parseProtocol.startElement(localName);
			_parseProtocol.parseAttributes(localName, atts);
		}
		value=new String();
	}

	/* When a end tag is encountered */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (_parseProtocol != null)
			_parseProtocol.endElement(localName);
		super.endElement(namespaceURI, localName, qName);
	}

	/* Get content of element */
	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		super.characters(arg0, arg1, arg2);
		StringBuilder builder = new StringBuilder();
		builder.append(new String(arg0, arg1, arg2));
		value=value+new String(arg0,arg1,arg2).trim();
//		builder.append("");
		if (_parseProtocol != null)
			_parseProtocol.charactor(value);
	}
}