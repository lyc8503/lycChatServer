package server;


public class Mydata{
	String data[];
	ReadData readData;
	public Mydata() {
		
	}
	
	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public ReadData getReadData() {
		return readData;
	}

	public void setReadData(ReadData readData) {
		this.readData = readData;
	}

	public Mydata(String data[],ReadData readData) {
		this.data=data;
		this.readData=readData;
	}
}
