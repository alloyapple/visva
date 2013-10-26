package vn.com.shoppie.util.parse;

import org.xml.sax.Attributes;

public interface OnParseListener{
	// start Parse XML Document
	public void startDocument();
	// finish Parse XML Document
	public void finishDocument();
	// read start tag in XML Document
	public void startElement(String element);
	// read end tag in XML Document
	public void endElement(String element);
	// read content of tag in XML Document. trim()
	public void charactor(String content);
	// parse Object to WiObject
	public void parseAttributes(String elementName,Attributes atts);
}
