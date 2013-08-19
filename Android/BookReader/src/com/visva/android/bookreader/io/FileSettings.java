package com.visva.android.bookreader.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

public class FileSettings extends Activity {
	private String filePath;
	private String folderPath;
	
	private final String RECENT_FILES_PATH = Environment.getExternalStorageDirectory() + "/PDFReader/RecentFiles.txt";
	
	private static final int MAX_LIGHT_SENSOR_VALUE = 36000;
	private static final String[] DATA_DEFAULT_SETTING = { "ADVANCE", "0 20 20",
			"20 99 50", "100 249 80", "250 599 100", "600 1499 130",
			"1500 4499 160", "5000 8499 190", "8500 14999 230", "15000 " + MAX_LIGHT_SENSOR_VALUE + " 255" };

	public FileSettings() {};
	
	public FileSettings(String folderPath, String filePath) {
		this.folderPath = folderPath;
		this.filePath = filePath;
	}

	// Create a file Setting if not exist
	public void createFileSettingIfNotExist() {
		File folderPDFReader = new File(folderPath);
		if (!folderPDFReader.exists()) {
			folderPDFReader.mkdir();
		}
				
		File fileSetting = new File(filePath);

		if (!fileSetting.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(fileSetting);
				PrintWriter pw = new PrintWriter(fos);
				

				for (int i = 0; i < DATA_DEFAULT_SETTING.length; i++) {
					pw.println(DATA_DEFAULT_SETTING[i]);
				}
				pw.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Read file PDFReaderSetting.txt and fill out the fields of Auto Advance Setting
/*	public void readFileSetting(RadioButton manualRadioButoon, RadioButton deviceRadioButton, 
			RadioButton advanceRadioButton, EditText[] lowerMode,  EditText[] upperMode, EditText[] levelMode) {
		
		String brightnessMode;

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			String line = "";
			int mode = 0;
			String[] tmp;

			//read first line that contain brightness mode
			brightnessMode = bufferReader.readLine();
			if (brightnessMode.equals("AUTO")) {
				deviceRadioButton.setChecked(true);
			}
			else if (brightnessMode.equals("ADVANCE")) {
				advanceRadioButton.setChecked(true);
			}
			else {
				manualRadioButoon.setChecked(true);
			}
			
			Log.d("Brightness", "" + brightnessMode);
			
			//fill out the EditTexts of Advance Mode
			while ((line = bufferReader.readLine()) != null) {
				tmp = line.split(" ");

				lowerMode[mode].setText(tmp[0]);
				upperMode[mode].setText(tmp[1]);
				levelMode[mode].setText(tmp[2]);

				mode++;
				
				Log.d("Brightness", "" + tmp[0] + "," + tmp[1] + "," + tmp[2]);
			}
			
			bufferReader.close();
			fr.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}*/
	
	public void readFileSetting(RadioButton manualRadioButoon, RadioButton deviceRadioButton, RadioButton advanceRadioButton) {
		String brightnessMode;

		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			// read first line that contain brightness mode
			brightnessMode = bufferReader.readLine();
			if (brightnessMode.equals("AUTO")) {
				deviceRadioButton.setChecked(true);
			}
			else if (brightnessMode.equals("ADVANCE")) {
				advanceRadioButton.setChecked(true);
			}
			else {
				manualRadioButoon.setChecked(true);
			}
			
			bufferReader.close();
			fr.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void readFileSetting(int position, EditText mEdtLower, EditText mEdtUper, EditText mEdtLevel) {
		String aLineConfig = "";
		
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			// ignore the first line - brightness mode
			bufferReader.readLine();
			
			for (int i=0; i<position; i++) {
				aLineConfig = bufferReader.readLine();
			}
			
			String[] tmp = aLineConfig.split(" ");
			mEdtLower.setText("" + tmp[0]);
			mEdtUper.setText("" + tmp[1]);
			mEdtLevel.setText("" + tmp[2]);
			
			bufferReader.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	//Write new Configuration to PDFReaderSetting File
	/*public void writeNewConfigToFileSetting(String mode, EditText[] lowerMode, EditText[] upperMode, EditText[] levelMode) {
		String tmp;
		
		try {
			FileOutputStream fos = new FileOutputStream(filePath, false);
			PrintWriter pr = new PrintWriter(fos);
			
			//Write brightness mode to first line
			pr.println(mode);
			
			for (int i=0; i<DATA_DEFAULT_SETTING.length-1; i++) {
				tmp = lowerMode[i].getText() + " " + upperMode[i].getText() + " " + levelMode[i].getText();
				pr.println(tmp);
			}
			
			pr.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public void writeNewConfigToFileSettings(String aLine, int position) {
		List<String> tmp = new ArrayList<String>();
		String line = "";
		
		try {
			// read current configuration
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			while((line = bufferReader.readLine()) != null) {
				tmp.add(line);
			}
			
			bufferReader.close();
			fr.close();
			
			// re-write a line at position
			tmp.remove(position);
			tmp.add(position, aLine);
			
			// re-write new configuration
			FileOutputStream fos = new FileOutputStream(filePath, false);
			PrintWriter pr = new PrintWriter(fos);
			
			for (int i=0; i<tmp.size(); i++) {
				pr.println(tmp.get(i));
			}
			
			pr.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void writeNewConfigToFileSettings(float scale, boolean isPositive) {
		List<String> tmp = new ArrayList<String>();
		String line = "";
		
		try {
			// read current configuration
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			while((line = bufferReader.readLine()) != null) {
				tmp.add(line);
			}
			
			bufferReader.close();
			fr.close();
			
			for (int i=1; i< tmp.size(); i++) {
				String[] tmpSplit = tmp.get(i).split(" ");
				int brightnessLevelCorrespondingToModei = Integer.parseInt(tmpSplit[2]);
				
				int brightnessLevelCorrespondingToModeiWithNewScale;
				if (isPositive) 
					brightnessLevelCorrespondingToModeiWithNewScale = brightnessLevelCorrespondingToModei + Math.round((float) brightnessLevelCorrespondingToModei * scale);
				else
					brightnessLevelCorrespondingToModeiWithNewScale = brightnessLevelCorrespondingToModei - Math.round((float) brightnessLevelCorrespondingToModei * scale);
				
				if (brightnessLevelCorrespondingToModeiWithNewScale < 20)
					brightnessLevelCorrespondingToModeiWithNewScale = 20;
				else if (brightnessLevelCorrespondingToModeiWithNewScale > 255)
					brightnessLevelCorrespondingToModeiWithNewScale = 255;
				
				String newLine = tmpSplit[0] + " " + tmpSplit[1] + " " + brightnessLevelCorrespondingToModeiWithNewScale;
				
				// re-write a line at position i
				tmp.remove(i);
				tmp.add(i, newLine);
			}

			
			// re-write new configuration
			FileOutputStream fos = new FileOutputStream(filePath, false);
			PrintWriter pr = new PrintWriter(fos);
			
			for (int i=0; i<tmp.size(); i++) {
				pr.println(tmp.get(i));
			}
			
			pr.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	//Get the brightness mode
	public String getBrightnessMode() {
		String mode = "";
		
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			mode = bufferReader.readLine();
			
			bufferReader.close();
			fr.close();
			
			return mode;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}
	
	//reWrite file setting when battery less than 30%
	public void reWriteFileSetting() {
		ArrayList<String> lines = new ArrayList<String>();
		String aLine;
		
		try {
			// read file current settings and store to ArrayList
			FileReader fr = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			while ((aLine = bufferReader.readLine()) != null) {
				if (aLine.equals("AUTO") || aLine.equals("ADVANCE"))
					aLine = "MANUAL";
				
				lines.add(aLine);
			}
			bufferReader.close();
			fr.close();
			
			
			// write new configuration to file setting
			FileOutputStream fos = new FileOutputStream(filePath, false);
			PrintWriter pr = new PrintWriter(fos);
			
			for (int i=0; i<lines.size(); i++) {
				pr.println(lines.get(i));
			}
			
			pr.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// write to RecentFiles
	public void writeToRecentFiles(File aFile) {
		File fileSetting = new File(RECENT_FILES_PATH);

		if (!fileSetting.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(fileSetting);
				PrintWriter pw = new PrintWriter(fos);
				pw.println("RECENT");

				pw.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boolean isExistInRecentFiles = false;
		
		List<File> listRecentFiles = readRecentFiles();
		if (listRecentFiles != null) {
			for (int i=0; i<listRecentFiles.size(); i++) {
				if (listRecentFiles.get(i).getAbsolutePath().equals(aFile.getAbsolutePath())) {
					isExistInRecentFiles = true;
					break;
				}
			}
		}
		
		if (isExistInRecentFiles == false) {
			try {
				FileOutputStream fos = new FileOutputStream(RECENT_FILES_PATH, true);
				PrintWriter pr = new PrintWriter(fos);
				pr.println(aFile.getAbsolutePath());
					
				pr.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// read RecentFiles
	public List<File> readRecentFiles() {
		List<File> listRecentFiles = new ArrayList<File>();
		
		try {
			FileReader fr = new FileReader(RECENT_FILES_PATH);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			String line = "";
			
			// Ignore first line
			line = bufferReader.readLine();
			
			while ((line = bufferReader.readLine()) != null) {
				File mFile = new File(line);
				listRecentFiles.add(mFile);
			}
			
			bufferReader.close();
			fr.close();
			return listRecentFiles;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}
	
	// delete a recent file in RecentFiles Setting
	public void deleteARecentFile(String aPathRecentFile) {
		List<String> tmp = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(RECENT_FILES_PATH);
			BufferedReader bufferReader = new BufferedReader(fr);
			
			String line = "";
			while ((line = bufferReader.readLine()) != null) {
				tmp.add(line);
			}
			
			bufferReader.close();
			fr.close();
			
			for (int i=0; i<tmp.size(); i++) {
				if (tmp.get(i).equals(aPathRecentFile)) {
					tmp.remove(i);
					break;
				}
			}
			
			// re-write to file
			File fileSetting = new File(RECENT_FILES_PATH);

			if (fileSetting.exists()) {
				try {
					FileOutputStream fos = new FileOutputStream(fileSetting);
					PrintWriter pw = new PrintWriter(fos);

					for (int i=0; i<tmp.size(); i++) {
						pw.println(tmp.get(i));
					}

					pw.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
