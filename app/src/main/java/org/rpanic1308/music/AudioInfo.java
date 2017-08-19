package org.rpanic1308.music;

import java.io.InputStream;
import java.io.Serializable;

public class AudioInfo implements Serializable{
	
	String title, singer;
	InputStream stream;
	AudioType type;
	
	public AudioInfo() {
		super();
	}

	public AudioInfo(InputStream stream, String title, String singer, AudioType type) {
		super();
		this.stream = stream;
		this.title = title;
		this.singer = singer;
		this.type = type;
	}
	
	public AudioInfo(InputStream stream,AudioType type) {
		super();
		this.stream = stream;
		this.type = type;
	}
	
	public AudioInfo(AudioType type) {
		super();
		this.type = type;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}
	
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	public AudioType getType() {
		return type;
	}

	public void setType(AudioType type) {
		this.type = type;
	}

	public enum AudioType{
		TRACKINFO, TRACKINFO_ADDTOQUEUE, TRACKINFO_ALARM, END_ALARM, PAUSE, RESUME, START, CLEARQUEUE, SKIP_TRACK, IS_INTERRUPTED, IS_RUNNING,
		SET_LINE_GAIN, ADD_LINE_GAIN, GET_LINE_GAIN
	}

	@Override
	public String toString() {
		return "AudioInfo [title=" + title + ", singer=" + singer + ", stream=" + stream + ", type=" + type + "]";
	}

	private static final long serialVersionUID = 1888722389771596662L;
}
