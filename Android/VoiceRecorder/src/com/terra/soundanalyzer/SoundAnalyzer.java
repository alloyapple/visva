package com.terra.soundanalyzer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import android.util.Log;

import com.ringdroid.soundfile.*;

/**
 * tactics: read a wav file, transform it into an array of integer
 * 
 */
public class SoundAnalyzer {
	public CheapWAV wav;
	int numOfFrame;
	int[] frameGains;
	double autoRatio;
	String fullpath;
	public double voiceLvl;
	
	final public static int NOISE = 1;
	final public static int VOICE_LENGTH = 9;
	Stack<Integer> testVoice;
	ArrayList<VoiceChunk> voices;
	
	public SoundAnalyzer(String fullpath, double voiceLvl){
		this.testVoice = new Stack<Integer>();
		this.voices = new ArrayList<VoiceChunk>();
		this.fullpath = fullpath;
		this.voiceLvl = voiceLvl;
	}
	
	/**
	 * - write frame gain to a file
	 * - initialize the array to store frame gain
	 */
	private void init(){
		wav = new CheapWAV();
		File inputFile = new File(this.fullpath);
		try {
			wav.ReadFile(inputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		numOfFrame = wav.getNumFrames();
		
		frameGains = new int[numOfFrame];
		frameGains = wav.getFrameGains();
		File outputFile = new File("output");	
		FileWriter fileWriter;
		String str;
		try {
			fileWriter = new FileWriter(outputFile,true);
			fileWriter.write("");
			for(int i=0;i<numOfFrame;i++){
				str = String.valueOf(frameGains[i])+"\n";
				fileWriter.append(str);
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * do something with the frameGains array to remove noise?
	 */
	private void removeNoise(){
	}
	
	/**
	 * create voices from signals
	 */
	private void createVoices(){
		int beginChunk = -1;
		System.out.println("*** begin ***");
		for(int i=0;i<numOfFrame;i++){
			if(frameGains[i] > SoundAnalyzer.NOISE && beginChunk == -1){
				beginChunk = i;
				testVoice.push(beginChunk);
				//System.out.println("beginChunk: "+i);
			}else if(frameGains[i] <= SoundAnalyzer.NOISE && beginChunk != -1){
				//System.out.println("end a chunk, length: "+testVoice.size());
				if(testVoice.size() > SoundAnalyzer.VOICE_LENGTH){
					// mark the nearest beginChunk
					VoiceChunk tmp = new VoiceChunk(beginChunk, i);
					// calculate the avg of this VoiceChunk
					int sum = 0;
					for(int j=beginChunk;j<=i;j++){
						sum += frameGains[j];
					}
					tmp.avg = ((double)sum)/((double)(i-beginChunk));
					System.out.println("start: "+beginChunk+";end: "+i+";length : "+(double)(i-beginChunk)*20/1000+" s; avg: "+tmp.avg);
					voices.add(tmp);
					// set it to -1
					beginChunk = -1;
					testVoice.removeAllElements();
					//System.out.println("size after remove: "+testVoice.size());
				}else{
					beginChunk = -1;
					testVoice.removeAllElements();
				}
			}else if(frameGains[i] > SoundAnalyzer.NOISE && beginChunk != -1){
				// suspecting a voice
				if(testVoice.size() < SoundAnalyzer.VOICE_LENGTH+1){
					testVoice.push(i);
				}	
				//System.out.println("in chunk ?");
				continue;
			}else if(frameGains[i] <= SoundAnalyzer.NOISE && beginChunk == -1){
				// going through a noise
				//System.out.println("noise");
				continue;
			}
		}
		System.out.println("*** end ***");
	}
	
	/**
	 * base on avg of each voice to separate voices into 2 group
	 */
	private void findTheshold(){
		/**
		 * using k-mean to find: autoRatio and cluster this group
		 */
		this.perform2Mean();
	}
	
	private void perform2Mean(){
		// loop through the list to find center points
		// minDistance === Error from all center points to its cluster's members
		double center1, center2, errorFromCenter, minDistance;
		int c1idx, c2idx, i;
		int finalc1idx = -1, finalc2idx = -1;
		int centerPoint[] = new int[numOfFrame];
		minDistance = Double.MAX_VALUE;
		for(c1idx=0;c1idx<this.voices.size()-1;c1idx++){
			center1 = this.voices.get(c1idx).avg; // point 1
			centerPoint[c1idx] = c1idx;
			for(c2idx=c1idx+1;c2idx<this.voices.size();c2idx++){
				center2 = this.voices.get(c2idx).avg; // point 2
				centerPoint[c2idx] = c2idx;
				errorFromCenter = 0;
				for(i=0;i<this.voices.size();i++){
					if(i == c1idx || i == c2idx){
						continue; // ignore the center points themselves
					}
					VoiceChunk v = this.voices.get(i);
					double distanceToC1 = distance(v.avg, center1);
					double distanceToC2 = distance(v.avg, center2);
					double tmpminDistance  = findMin(distanceToC1, distanceToC2);
					if(tmpminDistance == distanceToC1){
						centerPoint[i] = c1idx;
					}else if(tmpminDistance == distanceToC2){
						centerPoint[i] = c2idx;
					}
					errorFromCenter += tmpminDistance;
				}
				System.out.println("c1: "+c1idx+"; c2: "+c2idx+"; error: "+errorFromCenter);
				if(minDistance > errorFromCenter){
					minDistance = errorFromCenter;
					// assign and re-assign
					finalc1idx = c1idx;
					finalc2idx = c2idx;
					if(center1 > center2){
						this.autoRatio = center1/center2;
					}else{
						this.autoRatio = center2/center1;
					}
					for(i=0;i<this.voices.size();i++){
						VoiceChunk v = this.voices.get(i);
						v.centerPointIdx = centerPoint[i];
						if(v.centerPointIdx == finalc1idx && center2 > center1){
							v.needToIncrease = true;
						}else if(v.centerPointIdx == finalc2idx && center1 > center2){
							v.needToIncrease = true;
						}
					}
				}
			}
		}
		for(i=0;i<this.voices.size();i++){
			VoiceChunk v = this.voices.get(i);
			System.out.print(v.centerPointIdx+",");
		}
		System.out.println("\nfinalc1idx: "+finalc1idx);
		System.out.println("finalc2idx: "+finalc2idx);
	}
	
	private double findMin(double distanceToCenterPoint1, double distanceToCenterPoint2){
		if(distanceToCenterPoint1 >= distanceToCenterPoint2){
			return distanceToCenterPoint2;
		}else{
			return distanceToCenterPoint1;
		}
	}
	
	private double distance(double p1, double p2){
		// euclidean distance, squared
		return Math.abs(p1*p1 - p2*p2);
	}
	
	public void optimize(){
		Log.d("GHIAM","optimize 0");
		this.init();
		Log.d("GHIAM","optimize 1");
		this.removeNoise();
		Log.d("GHIAM","optimize 2");
		this.createVoices();
		Log.d("GHIAM","optimize 3");
		this.findTheshold();
		Log.d("GHIAM","optimize 4");
		Log.d("GHIAM","optimize "+this.fullpath);
		String fileName = this.fullpath+"-mod.wav";
		Log.d("GHIAM","optimize "+fileName);
		try {
			this.wav.WriteFileModified(new File(fileName), 0, this.numOfFrame-1, 
					this.voiceLvl, this.voices);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
