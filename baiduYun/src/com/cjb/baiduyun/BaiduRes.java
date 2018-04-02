package com.cjb.baiduyun;

public class BaiduRes {
	private String id;
	private String num;
	private String yurl;
	private String burl;
	private String filename;
	private String sxtime;
	private String filetime;
	private boolean getUrlSuccess=false;
	private boolean getMsgSuccess=false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYurl() {
		return yurl;
	}
	public void setYurl(String yurl) {
		this.yurl = yurl;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getSxtime() {
		return sxtime;
	}
	public void setSxtime(String sxtime) {
		this.sxtime = sxtime;
	}
	public String getFiletime() {
		return filetime;
	}
	public void setFiletime(String filetime) {
		this.filetime = filetime;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public boolean isGetUrlSuccess() {
		return getUrlSuccess;
	}
	public void setGetUrlSuccess(boolean getUrlSuccess) {
		this.getUrlSuccess = getUrlSuccess;
	}
	public boolean isGetMsgSuccess() {
		return getMsgSuccess;
	}
	public void setGetMsgSuccess(boolean getMsgSuccess) {
		this.getMsgSuccess = getMsgSuccess;
	}
	public String getBurl() {
		return burl;
	}
	public void setBurl(String burl) {
		this.burl = burl;
	}
	

}
