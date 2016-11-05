package pondlogss.eruvaka.classes;

public class GroupYeild {
  private String tankname;
  private String mhoc;
	public void addtankname(String tankName) {
		// TODO Auto-generated method stub
		tankname=tankName;
	}
	public void addhoc(String hoc) {
		// TODO Auto-generated method stub
		mhoc=hoc;
	}
	public String getTankname() {
		// TODO Auto-generated method stub
		return tankname;
	}
	public String gethoc() {
		// TODO Auto-generated method stub
		return mhoc;
	}
	private String mharvId;
	public void addharvestId(String harvId) {
		// TODO Auto-generated method stub
		mharvId=harvId;
	}
	public String getharvestId() {
		// TODO Auto-generated method stub
		return mharvId;
	}
	private static String mformat;
	public void setdate(String format) {
		// TODO Auto-generated method stub
		mformat=format;
	}
	public static CharSequence getdate() {
		// TODO Auto-generated method stub
		return mformat;
	}

}
