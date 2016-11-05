package pondlogss.eruvaka.classes;

public class GroupStock {
	
private  String mId;
private String  mRsrName;
	 
	public void setgruopId(String string) {
		// TODO Auto-generated method stub
		mId=string;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return mId;
	}

	public void add(String rsrName) {
		// TODO Auto-generated method stub
		mRsrName=rsrName;
	}

	public String getRsrName() {
		// TODO Auto-generated method stub
		return mRsrName;
	}

}
