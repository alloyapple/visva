package com.visva.android.flashlight.common;

import java.util.ArrayList;

public class SaveVisibleLightSource implements Key {

	public static ArrayList<Integer> _lstVisibleLightSourcesId = new ArrayList<Integer>();

	public static void setLstVisibleLightSourcesId(ArrayList<Integer> lst) {
		_lstVisibleLightSourcesId.clear();
		_lstVisibleLightSourcesId.addAll(lst);
	}

	public static void insertVisibleLightSource(int lightSourceId) {
		if (_lstVisibleLightSourcesId.isEmpty()) {
			_lstVisibleLightSourcesId.add(lightSourceId);
		} else {
			boolean isExist = false;
			for (int i = 0; i < _lstVisibleLightSourcesId.size(); i++) {
				if (lightSourceId == _lstVisibleLightSourcesId.get(i)) {
					isExist = true;
					break;
				}
			}

			if (!isExist) {
				_lstVisibleLightSourcesId.add(Integer.valueOf(lightSourceId));
			}
		}
	}

	public static void removeVisibleLightSource(int lightSourceId) {
		_lstVisibleLightSourcesId.remove(Integer.valueOf(lightSourceId));
	}

	public static void updateVisibleLightSource(boolean isCheck, int lightSourceId) {
		if (isCheck) {
			insertVisibleLightSource(lightSourceId);
		} else {
			removeVisibleLightSource(lightSourceId);
		}
	}

	public static String getStringVisibleLightSource() {
		String __visibleLightSource = "";
		int __count = 0;
		if (!_lstVisibleLightSourcesId.isEmpty()) {
			for (int i = 0; i < _lstVisibleLightSourcesId.size() - 1; i++) {
				for (int j = i + 1; j < _lstVisibleLightSourcesId.size(); j++) {
					if (_lstVisibleLightSourcesId.get(i) > _lstVisibleLightSourcesId.get(j)) {
						int tmp = _lstVisibleLightSourcesId.get(i);
						_lstVisibleLightSourcesId.set(i, _lstVisibleLightSourcesId.get(j));
						_lstVisibleLightSourcesId.set(j, tmp);
					}
				}
			}
			for (Integer i : _lstVisibleLightSourcesId) {
				if (__count < _lstVisibleLightSourcesId.size() - 1) {
					__visibleLightSource += i + ",";
				} else {
					__visibleLightSource += i;
				}
				__count++;
			}
		}
		return __visibleLightSource;
	}

	public static ArrayList<Integer> getListVisibleSources(String visibleLightSource) {
		ArrayList<Integer> _lstVisibleLightSourcesId = new ArrayList<Integer>();
		if (visibleLightSource.trim().equals("")) {
			_lstVisibleLightSourcesId.add(SETTING);
		} else {
			String[] __arr = visibleLightSource.split("[,]");

			if (__arr.length > 0) {
				for (int i = 0; i < __arr.length; i++) {
					try {
						_lstVisibleLightSourcesId.add(Integer.parseInt(__arr[i]));
					} catch (Exception e) {
					}
				}
			}
		}

		for (int i = 0; i < _lstVisibleLightSourcesId.size() - 1; i++) {
			for (int j = i + 1; j < _lstVisibleLightSourcesId.size(); j++) {
				if (_lstVisibleLightSourcesId.get(i) > _lstVisibleLightSourcesId.get(j)) {
					int tmp = _lstVisibleLightSourcesId.get(i);
					_lstVisibleLightSourcesId.set(i, _lstVisibleLightSourcesId.get(j));
					_lstVisibleLightSourcesId.set(j, tmp);
				}
			}
		}

		return _lstVisibleLightSourcesId;
	}

}
