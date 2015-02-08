package com.visva.soundanalyzer;

public class VoiceChunk {
	public int start;
	public int end;
	public double avg;
	public int centerPointIdx;
	public boolean needToIncrease = false;
	
	public VoiceChunk(int start, int end){
		this.start = start;
		this.end = end;
	}
}
