package pondlogss.eruvaka.classes;

import java.util.List;

public class FeedChild {
  private  String mdaytotal;
  private  String mnetconsumed;
  private  String mschqty;
  private  String mschct;
  private  String mschcf;
  private  List<String> mlist;
  private String mdays;
  private  String mfeedname;
  private String mfeedate;
	public void setdaytotal(String dayTotal) {
		// TODO Auto-generated method stub
		mdaytotal=dayTotal;
	}
	public void setnetconsumed(String netConsumed) {
		// TODO Auto-generated method stub
		mnetconsumed=netConsumed;
	}
	public String getdaytotal() {
		// TODO Auto-generated method stub
		return mdaytotal;
	}
	public String getnetconsumed() {
		// TODO Auto-generated method stub
		return mnetconsumed;
	}
	public void setsch_qty(String sch_qty) {
		// TODO Auto-generated method stub
		mschqty=sch_qty;
	}
	public String getsch_qty() {
		// TODO Auto-generated method stub
		return mschqty;
	}
	public void setsch_ct(String sch_ct) {
		// TODO Auto-generated method stub
		mschct=sch_ct;
	}
	public String getsch_ct() {
		// TODO Auto-generated method stub
		return mschct;
	}
	public void setsch_cf(String sch_cf) {
		// TODO Auto-generated method stub
		mschcf=sch_cf;
	}
	public String getsch_cf() {
		// TODO Auto-generated method stub
		return mschcf;
	}
	public void add(List<String> childdata3) {
		// TODO Auto-generated method stub
		mlist=childdata3;
	}
	public List<String> getarray() {
		// TODO Auto-generated method stub
		return mlist;
	}
	public void setdays(String string) {
		// TODO Auto-generated method stub
		mdays=string;
	}
	public String getdays() {
		// TODO Auto-generated method stub
		return mdays;
	}
	public void setfeedname(String feedname) {
		// TODO Auto-generated method stub
		mfeedname=feedname;
	}
	public String getfeedname() {
		// TODO Auto-generated method stub
		return mfeedname;
	}
	public void setfeeddate(String feeddate) {
		// TODO Auto-generated method stub
		mfeedate=feeddate;
	}
	public String getdate() {
		// TODO Auto-generated method stub
		return mfeedate;
	}
	
	

}
