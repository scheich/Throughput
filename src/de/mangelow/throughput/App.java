package de.mangelow.throughput;

public class App {

	int uid;
	public int getUID() {
		return uid;
	}
	public void setUID(int uid) {
		this.uid = uid;
	}
	
	String processname = "";
	public String getProcessname() {
		return processname;
	}
	public void setProcessname(String processname) {
		this.processname = processname;
	}
	
	String packagename = "";
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	
	long last_rx;
	public long getLastRx() {
		return last_rx;
	}
	public void setLastRx(long last_rx) {
		this.last_rx = last_rx;
	}
	
	long last_tx;
	public long getLastTx() {
		return last_tx;
	}
	public void setLastTx(long last_tx) {
		this.last_tx = last_tx;
	}
	
	String label = "";
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
