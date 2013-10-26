package vn.com.shoppie.util.parse;

import java.util.ArrayList;

import vn.com.shoppie.database.sobject.ShoppieObject;

public class WiModelManager {
	private ArrayList<ShoppieObject> _data;

	/* Constructor */
	public WiModelManager() {
		_data = new ArrayList<ShoppieObject>();
	}

	public void clear() {
		_data = new ArrayList<ShoppieObject>();
	}

	public void addShoppieObject(ShoppieObject object) {
		_data.add(object);
	}

	public ArrayList<ShoppieObject> getResultShoppieObject() {
		return _data;
	}

	/* Parse Manager */
	public void parse(String xml) {
		clear();
		WiModelParser parser = new WiModelParser(xml, this);
		parser.parse();
	}
}
