package vn.com.shoppie.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {

	public String getXmlFromUrl(String url) {
		String xml = null;

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			xml = xml.replaceAll("&#13;", "").replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");

		}catch(UnknownHostException e){
			log.e("UnknownHost", url);
		}catch (UnsupportedEncodingException e) {
			log.e("UnsupportedEncodingException", url);
		} catch (ClientProtocolException e) {
			log.e("ClientProtocolException", url);
		} catch (IOException e) {
			log.e("IOException", url);
		}
		// return XML
		return xml;
	}

	public String getXmlFromHttpResponse(HttpResponse httpResponse) {
		String xml = null;

		try {
			// defaultHttpClient
			HttpEntity httpEntity = httpResponse.getEntity();

			xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			xml = xml.replaceAll("&#13;", "").replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}

	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			// log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			// log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			// log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

}
