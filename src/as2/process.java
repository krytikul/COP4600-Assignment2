//Assignment 2 
//COP 4600
//Lucas Gillespie & Michael Colluciello


package as2;

public class process {
	String name;
	int pid; 
	int arrival; 
	int burst;
	int bTemp;
	public int getbTemp() {
		return bTemp;
	}
	public void setbTemp(int bTemp) {
		this.bTemp = bTemp;
	}
	int wait;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public int getWait() {
		return wait;
	}
	public void setWait(int wait) {
		this.wait = wait;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getArrival() {
		return arrival;
	}
	public void setArrival(int arrival) {
		this.arrival = arrival;
	}
	public int getBurst() {
		return burst;
	}
	public void setBurst(int burst) {
		this.burst = burst;
	}
}
