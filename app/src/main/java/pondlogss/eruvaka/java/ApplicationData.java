package pondlogss.eruvaka.java;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class ApplicationData {
	static int mresponse=0;

	public static void setresponse(int resp) {
		// TODO Auto-generated method stub
		mresponse=resp;
	}

	public static int  getresponse() {
		// TODO Auto-generated method stub
		return mresponse;
	}
	static String mregenity=null;
	 

	public static void setregentity(String result) {
		// TODO Auto-generated method stub
		mregenity=result;
	}

	public static String getregentity() {
		// TODO Auto-generated method stub
		return mregenity;
	}
	private static String maddphoneno=null;
	public static void addphoneno(String phnum) {
		// TODO Auto-generated method stub
		maddphoneno=phnum;
	}

	public static String getphoneno() {
		// TODO Auto-generated method stub
		return maddphoneno;
	}
	private static String madduname=null;
	public static void adduname(String uname) {
		// TODO Auto-generated method stub
		madduname=uname;
	}

	public static String getuname() {
		// TODO Auto-generated method stub
		return madduname;
	}
	private static String memailid=null;
	public static void addequalmailid(String equalmailid) {
		// TODO Auto-generated method stub
		memailid=equalmailid;
		
	}

	public static String getequalimailid() {
		// TODO Auto-generated method stub
		return memailid;
	}
	private static String mequal=null;
	public static void addequalpwd(String equalpwd) {
		// TODO Auto-generated method stub
		mequal=equalpwd;
	}

	public static String getequalpwd() {
		// TODO Auto-generated method stub
		return mequal;
	}
	   private static String mstatus=null;
	 
	public static void setstatus(String resp) {
		// TODO Auto-generated method stub
		mstatus=resp;
	}

	public static String getstatus() {
		// TODO Auto-generated method stub
		return mstatus;
	}
	private static String mlocation;
	public static void setLocation(String location) {
		// TODO Auto-generated method stub
		mlocation=location;
	}

	public static String getLocationName() {
		// TODO Auto-generated method stub
		return mlocation;
	}
	private static String mschedules;
	public static void setSchedules(String schedules) {
		// TODO Auto-generated method stub
		mschedules=schedules;
	}

	public static String getSchedules() {
		// TODO Auto-generated method stub
		return mschedules;
	}
	private static String mpondid;
	public static void setpondid(String pondid) {
		// TODO Auto-generated method stub
		mpondid=pondid;
	}
	public static String getpondId() {
		// TODO Auto-generated method stub
		return mpondid;
	}
	private static String mpname;
	public static void setpondname(String pname) {
		// TODO Auto-generated method stub
		mpname=pname;
	}
	public static String getpondname() {
		// TODO Auto-generated method stub
		return mpname;
	}
	private static String mdoc;
	public static void setdoc(String doc) {
		// TODO Auto-generated method stub
		mdoc=doc;
	}
	public static String getdoc() {
		// TODO Auto-generated method stub
		return mdoc;
	}
	private static String mdensity;
	public static void setdensity(String density) {
		// TODO Auto-generated method stub
		mdensity=density;
	}
	public static String getplsstock() {
		// TODO Auto-generated method stub
		return mdensity;
	}

	private static String mtanksize;
	public static void settanksize(String tanksize) {
		// TODO Auto-generated method stub
		mtanksize=tanksize;
	}
	public static String getpondsize() {
		// TODO Auto-generated method stub
		return mtanksize;
	}
	private static String mschedules2;

	public static void setschedules2(String schedules) {
		// TODO Auto-generated method stub
		mschedules2=schedules;
	}
	private static String mtime1=null;
	public static void addtime1(String datetime1) {
		// TODO Auto-generated method stub
		mtime1=datetime1;
	}

	public static CharSequence gettime1() {
		// TODO Auto-generated method stub
		return mtime1;
	}
	private static String mshrimpname=null;
	public static void addshrimptype(String shrimpname) {
		// TODO Auto-generated method stub
		mshrimpname=shrimpname;
	}
	private static String msdate=null;
	public static void adddate(String date) {
		// TODO Auto-generated method stub
		msdate=date;
	}

	public static CharSequence getsname() {
		// TODO Auto-generated method stub
		return mshrimpname;
	}

	public static CharSequence getsdate() {
		// TODO Auto-generated method stub
		return msdate;
	}
	private static String mresult=null;
	public static void addregentity(String result) {
		// TODO Auto-generated method stub
		mresult=result;
	}

	public static String getregintiyt() {
		// TODO Auto-generated method stub
		return mresult;
	}
	private static String mfeildname=null;
	public static void setLocationName(String feildname) {
		// TODO Auto-generated method stub
		mfeildname=feildname;
	}

	public static Object getLocationIDname() {
		// TODO Auto-generated method stub
		return mfeildname;
	}
	private static String mfeedname=null;
	public static void setfeedname(String feedname) {
		// TODO Auto-generated method stub
		mfeedname=feedname;
	}

	public static String getfeedname() {
		// TODO Auto-generated method stub
		return mfeedname;
	}
	private static String mdocinput=null;

	public static void setdocinput(String dOC) {
		// TODO Auto-generated method stub
		mdocinput=dOC;
	}
	public static String getdocinput() {
		// TODO Auto-generated method stub
		return mdocinput;
	}
	private static String mwsainput=null;
	public static void setwsainput(String wSA) {
		// TODO Auto-generated method stub
		mwsainput=wSA;
	}

	public static String getwsainput() {
		// TODO Auto-generated method stub
		return mwsainput;
	}
	private static String mharvestdate=null;
	public static void setHarvestdate(String harvestdate) {
		// TODO Auto-generated method stub
		mharvestdate=harvestdate;
	}

	public static Object getHarvestdate() {
		// TODO Auto-generated method stub
		return mharvestdate;
	}
	private static String mlocationid=null;
	public static void addLocId(String locationId) {
		// TODO Auto-generated method stub
		mlocationid=locationId;
	}

	public static String getLocationId() {
		// TODO Auto-generated method stub
		return mlocationid;
	}
	private static String mharvest_schedule=null;
	public static void addharvest(String harvest_schedule) {
		// TODO Auto-generated method stub
		mharvest_schedule=harvest_schedule;
	}

	public static String getharvest() {
		// TODO Auto-generated method stub
		return mharvest_schedule;
	}
	private static String mtankid=null;
	public static void setTankid(String tankid) {
		// TODO Auto-generated method stub
		mtankid=tankid;
	}

	public static String getTankid() {
		// TODO Auto-generated method stub
		return mtankid;
	}
	private static String mareasum=null;
	public static void setareasum(String str) {
		// TODO Auto-generated method stub
		mareasum=str;
	}

	public static String getareasum() {
		return mareasum;
		// TODO Auto-generated method stub
		
	}
	private static String mweight=null;
	public static void setweight(String weight) {
		// TODO Auto-generated method stub
		mweight=weight;
	}
	public static String getweight() {
		// TODO Auto-generated method stub
		return mweight;
	}
	private static String mnumbers=null;
	public static void setnumbers(String numbers) {
		// TODO Auto-generated method stub
		mnumbers=numbers;
	}

	public static String getnumbers() {
		// TODO Auto-generated method stub
		return mnumbers;
	}
	private static String mabwpondid=null;

	public static void setabwpondid(String pondid) {
		// TODO Auto-generated method stub
		mabwpondid=pondid;
	}

	public static String getabwpondid() {
		// TODO Auto-generated method stub
		return mabwpondid;
	}
	private static String mlastabw=null;
	public static void setlastabw(String lastabw) {
		// TODO Auto-generated method stub
		mlastabw=lastabw;
	}

	public static String getlastabw() {
		// TODO Auto-generated method stub
		return mlastabw;
	}
	private static float msumweight=0;
	public static void setfloatweight(float sum) {
		// TODO Auto-generated method stub
		msumweight=sum;
	}

	public static float getfloatweight() {
		// TODO Auto-generated method stub
		return msumweight;
	}
	private static float msumnumbers=0;
	public static void setfloatnumber(float sumnumbers) {
		// TODO Auto-generated method stub
		msumnumbers=sumnumbers;
	}

	public static float getfloatnumbers() {
		// TODO Auto-generated method stub
		return msumnumbers;
	}
	private static String mabwdoc=null;
	public static void setabwdoc(String doc) {
		// TODO Auto-generated method stub
		mabwdoc=doc;
	}
	public static String getabwdoc() {
		// TODO Auto-generated method stub
		return mabwdoc;
	}
	private static String mabwhoc=null;
	public static void setabwhoc(String hoc) {
		// TODO Auto-generated method stub
		mabwhoc=hoc;
	}
	public static String getabwhoc() {
		// TODO Auto-generated method stub
		return mabwhoc;
	}
	private static String mabwdensity=null;
	public static void setabwdensity(String density) {
		// TODO Auto-generated method stub
		mabwdensity=density;
	}

	public static String getabwdensity() {
		// TODO Auto-generated method stub
		return mabwdensity;
	}
	private static float mtotalweight=0;
	public static void setsamplestotalweight(float totalweight) {
		// TODO Auto-generated method stub
		mtotalweight=totalweight;
	}

	public static float getsamplestotalweight() {
		// TODO Auto-generated method stub
		return mtotalweight;
	}
	private static String msamplescount=null;
	public static void setsamplescount(String str) {
		// TODO Auto-generated method stub
		msamplescount=str;
	}

	public static Object getsamplescount() {
		// TODO Auto-generated method stub
		return msamplescount;
	}
	private static String mabwtankid=null;
	public static void setabwtankname(String tankname) {
		// TODO Auto-generated method stub
		mabwtankid=tankname;
	}

	public static String getabwtankanme() {
		// TODO Auto-generated method stub
		return mabwtankid;
	}
	private static String mAbwId=null;
	public static void setAbwId(String parentText2) {
		// TODO Auto-generated method stub
		mAbwId=parentText2;
	}

	public static String getAbwId() {
		// TODO Auto-generated method stub
		return mAbwId;
	}
	private static String mAbwDateTime=null;
	public static void setAbwDateTime(String parentText) {
		// TODO Auto-generated method stub
		mAbwDateTime=parentText;
	}

	public static String getAbwDateTime() {
		// TODO Auto-generated method stub
		return mAbwDateTime;
	}
	private static String mfeildnames=null;
	public static void setfeildname(String feildname) {
		// TODO Auto-generated method stub
		mfeildnames=feildname;
	}

	public static String getfeildname() {
		// TODO Auto-generated method stub
		return mfeildnames;
	}
	private static String mcf1=null;
	public static void setcf1(String cf1) {
		// TODO Auto-generated method stub
		mcf1=cf1;
	}

	public static String getcf1() {
		// TODO Auto-generated method stub
		return mcf1;
	}
	private static String mcf2=null;
	public static void setcf2(String cf2) {
		// TODO Auto-generated method stub
		mcf2=cf2;
	}

	public static String getcf2() {
		// TODO Auto-generated method stub
		return mcf2;
	}
	private static String mcf3=null;
	public static void setcf3(String cf3) {
		// TODO Auto-generated method stub
		mcf3=cf3;
	}

	public static String getcf3() {
		// TODO Auto-generated method stub
		return mcf3;
	}
	private static String mcf4=null;
	public static void setcf4(String cf4) {
		// TODO Auto-generated method stub
		mcf4=cf4;
	}

	public static String getcf4() {
		// TODO Auto-generated method stub
		return mcf4;
	}
	private static String mcf5=null;
	public static void setcf5(String cf5) {
		// TODO Auto-generated method stub
		mcf5=cf5;
	}

	public static String getcf5() {
		// TODO Auto-generated method stub
		return mcf5;
	}
	private static String mcf6=null;
	public static void setcf6(String cf6) {
		// TODO Auto-generated method stub
		mcf6=cf6;
	}

	public static String getcf6() {
		// TODO Auto-generated method stub
		return mcf6;
	}
	private static String mcf7=null;
	public static void setcf7(String cf7) {
		// TODO Auto-generated method stub
		mcf7=cf7;
	}

	public static String getcf7() {
		// TODO Auto-generated method stub
		return mcf7;
	}
	private static String mcf8=null;
	public static void setcf8(String cf8) {
		// TODO Auto-generated method stub
		mcf8=cf8;
	}

	public static String getcf8() {
		// TODO Auto-generated method stub
		return mcf8;
	}
	private static String mcf9=null;
	public static void setcf9(String cf9) {
		// TODO Auto-generated method stub
		mcf9=cf9;
	}

	public static String getcf9() {
		// TODO Auto-generated method stub
		return mcf9;
	}
	private  static String mcf10=null;
	public static void setcf10(String cf10) {
		// TODO Auto-generated method stub
		mcf10=cf10;
	}

	public static String getcf10() {
		// TODO Auto-generated method stub
		return mcf10;
	}
	private static String mrsId=null;
	public static void setresid(String rsId) {
		// TODO Auto-generated method stub
		mrsId=rsId;
	}
	public static String getresid() {
		// TODO Auto-generated method stub
		return mrsId;
	}
	private static String mrestypename=null;
	public static void setrestypename(String rnameType) {
		// TODO Auto-generated method stub
		mrestypename=rnameType;
	}

	public static String getrestypename() {
		// TODO Auto-generated method stub
		return mrestypename;
	}
	 private static ArrayList<LatLng> mlatLang=null;
	public static void setLatLongArray(ArrayList<LatLng> latLang) {
		// TODO Auto-generated method stub
		mlatLang=latLang;
	}

	public static ArrayList<LatLng> getLatLongArray() {
		// TODO Auto-generated method stub
		return mlatLang;
	}
	private static List<Double> mlistLat = new ArrayList<Double>();
	public static void addlatarray(List<Double> listLat) {
		// TODO Auto-generated method stub
		mlistLat=listLat;
	}
	public static List<Double> getlatArray() {
		// TODO Auto-generated method stub
		return mlistLat;
	}

	private static  List<Double> mlistLong = new ArrayList<Double>();
	public static void addlongarray(List<Double> listLong) {
		// TODO Auto-generated method stub
		mlistLong=listLong;
	}

	public static List<Double> getlongtArray() {
		// TODO Auto-generated method stub
		return mlistLong;
	}
	private static int mlocid=0;
	public static void setsearchLocId(int locid) {
		// TODO Auto-generated method stub
		mlocid=locid;
	}

	public static int getsearchLocId() {
		// TODO Auto-generated method stub
		return mlocid;
	}
	private static String mlocation_lat_lang=null;
	public static void setLocLatLagstr(String location_Lat_Lang) {
		// TODO Auto-generated method stub
		mlocation_lat_lang=location_Lat_Lang;
	}

	public static String getloactionlatLag() {
		// TODO Auto-generated method stub
		return mlocation_lat_lang;
	}
	private static String mrNameType=null;
	public static void setrNameType(String rNameType) {
		// TODO Auto-generated method stub
		mrNameType=rNameType;
	}

	public static String getrNameType() {
		// TODO Auto-generated method stub
		return mrNameType;
	}
	private static String mqtystock=null;
	public static void setQtyStock(String quantitysStock) {
		// TODO Auto-generated method stub
		mqtystock=quantitysStock;
	}

	public static String getQtyStock() {
		// TODO Auto-generated method stub
		return mqtystock;
	}
	private static String mhttpresponse=null;
	public static void setHttpResponse(String string) {
		// TODO Auto-generated method stub
		mhttpresponse=string;
	}

	public static String gethttpresponse() {
		// TODO Auto-generated method stub
		return mhttpresponse;
	}
	private static String mvendorresId=null;
	public static void setvendorresId(String vendorid) {
		// TODO Auto-generated method stub
		mvendorresId=vendorid;
	}

	public static String getvendorId() {
		// TODO Auto-generated method stub
		return mvendorresId;
	}
	//dailog in feededitstock 
	private static String mdailogdresId=null;
	public static void setDailogResId(String rsId) {
		// TODO Auto-generated method stub
		mdailogdresId=rsId;
	}
	public static String getdailogrsrId() {
		// TODO Auto-generated method stub
		return mdailogdresId;
	}
	private static String mdialogrsrPurId=null;
	public static void setDailogResPurId(String rsrPurId) {
		// TODO Auto-generated method stub
		mdialogrsrPurId=rsrPurId;
	}
	public static String getdailogrsrpurId() {
		// TODO Auto-generated method stub
		return mdialogrsrPurId;
	}
	private static String mdialogrsrName=null;
	public static void setDailogResName(String rsrName) {
		// TODO Auto-generated method stub
		mdialogrsrName=rsrName;
	}
	public static String getdailogrsrName() {
		// TODO Auto-generated method stub
		return mdialogrsrName;
	}
	private static String mdialogrsrnbags=null; 
	public static void setdialognbags(String nbags) {
		// TODO Auto-generated method stub
		mdialogrsrnbags=nbags;
	}
	public static String getdailogNUnits() {
		// TODO Auto-generated method stub
		return mdialogrsrnbags;
	}

	private static String mdialogrsrbagw=null; 
	public static void setdialogbagwg(String bagw) {
		// TODO Auto-generated method stub
		mdialogrsrbagw=bagw;
	}
	public static String getdailogunitweight() {
		// TODO Auto-generated method stub
		return mdialogrsrbagw;
	}
	private static String mdialogtotalpurchase=null; 
	public static void setdialogttlpurchased(String totalpurchase) {
		// TODO Auto-generated method stub
		mdialogtotalpurchase=totalpurchase;
	}
	public static String getdailogpurchasedQty() {
		// TODO Auto-generated method stub
		return mdialogtotalpurchase;
	}
	private static String mdialogttlstock=null; 
	public static void setdialogtotalstock(String ttlstock) {
		// TODO Auto-generated method stub
		mdialogttlstock=ttlstock;
	}

	public static String getdailogrsrweight() {
		// TODO Auto-generated method stub
		return mdialogttlstock;
	}
	private static String mpurchaseddate=null;
	public static void setdailogrsdate(String purchasedDate) {
		// TODO Auto-generated method stub
		mpurchaseddate=purchasedDate;
	}

	public static String getdailogdate() {
		// TODO Auto-generated method stub
		return mpurchaseddate;
	}
	private static String mtankId=null;
	public static void setTankId(String tankId) {
		// TODO Auto-generated method stub
		mtankId=tankId;
	}

	public static String getTankId() {
		// TODO Auto-generated method stub
		return mtankId;
	}
	private static String malert=null;
	public static void addalertchecked(String checkedtrue) {
		// TODO Auto-generated method stub
		malert=checkedtrue;
	}
	public static String getalertcheck() {
		// TODO Auto-generated method stub
		return malert;
	}
	private static int mint=0;
	public static void addint(int j) {
		// TODO Auto-generated method stub
		mint=j;
	}

	public static int getint() {
		// TODO Auto-generated method stub
		return mint;
	}
	private static String musertype=null;
	public static void addUserType(String usertype) {
		// TODO Auto-generated method stub
		musertype=usertype;
	}
	public static String getUserType() {
		// TODO Auto-generated method stub
		return musertype;
	}
	private static String meditpermision=null;
	public static void addEditPermision(String editPermision) {
		// TODO Auto-generated method stub
		meditpermision=editPermision;
	}

	public static String getPermision() {
		// TODO Auto-generated method stub
		return meditpermision;
	}
	private static String mharvestdate2=null;
	public static void setHarvestdate2(String harvestdate) {
		// TODO Auto-generated method stub
		mharvestdate2=harvestdate;
	}

	public static String getharvestdate2() {
		// TODO Auto-generated method stub
		return mharvestdate2;
	}
	private static List<String> mlatarray=new ArrayList<String>();
	public static void addlatstrarray(List<String> latitudestr) {
		// TODO Auto-generated method stub
		mlatarray=latitudestr;
	}
	public static List<String> getlatstrarray() {
		// TODO Auto-generated method stub
		return mlatarray;
	}
	private static List<String> mlngarray=new ArrayList<String>();

	public static void addlagstrarray(List<String> longitudestr) {
		// TODO Auto-generated method stub
		mlngarray=longitudestr;
	}

	public static List<String> getlagtstrarray() {
		// TODO Auto-generated method stub
		return mlngarray;
	}
	private static double mareatotal=0;
	public static void setpondsize(Double areasum2) {
		// TODO Auto-generated method stub
		mareatotal=areasum2;
	}

	public static Double getareatotal() {
		// TODO Auto-generated method stub
		return mareatotal;
	}
	private static double mlakhs=0;
	public static void setplsStocked(Double plsStocked) {
		// TODO Auto-generated method stub
		mlakhs=plsStocked;
	}

	public static Double getplsstocked() {
		// TODO Auto-generated method stub
		return mlakhs;
	}
	private static String mloginresponse=null;
	public static void addLoginResponse(String result) {
		// TODO Auto-generated method stub
		mloginresponse=result;
	}

	public static String getLoginResponse() {
		// TODO Auto-generated method stub
		return mloginresponse;
	}
	private static String mlocationname=null;

	public static void setLocationNamed(String feildname) {
		// TODO Auto-generated method stub
		mlocationname=feildname;
	}

	public static String getLocationNamed() {
		// TODO Auto-generated method stub
		return mlocationname;
	}
	private static String mPondId=null;
	public static void setUpdatePondId(String pondId) {
		// TODO Auto-generated method stub
		mPondId=pondId;
	}
	public static String getUpdatepondId() {
		// TODO Auto-generated method stub
		return mPondId;
	}
	private static ArrayList<String> mschedulesarray=null;
	public static void setArraySchedues(ArrayList<String> al) {
		// TODO Auto-generated method stub
		mschedulesarray=al;
	}

	public static ArrayList<String> getFeedSchedules() {
		// TODO Auto-generated method stub
		return mschedulesarray;
	}
	private static String malertt=null;
	public static void setalert(String string) {
		// TODO Auto-generated method stub
		malertt=string;
	}

	public static String getalert() {
		// TODO Auto-generated method stub
		return malertt;
	}
	private static String mresName=null;
	public static void setresName(String resName) {
		// TODO Auto-generated method stub
		mresName=resName;
	}

	public static String getresName() {
		// TODO Auto-generated method stub
		return mresName;
	}
	private static String mQtyStock=null;
	public static void setQtyStock1(String qtyStock) {
		// TODO Auto-generated method stub
		mQtyStock=qtyStock;
	}

	public static String getQtyStock1() {
		// TODO Auto-generated method stub
		return mQtyStock;
	}
	private static String mpurdate=null;
	public static void setPurchasedDate(String purchasedDate) {
		// TODO Auto-generated method stub
		mpurdate=purchasedDate;
	}
	public static String getPurchasedDate() {
		// TODO Auto-generated method stub
		return mpurdate;
	}
	private static String mchildId=null;
	public static void setChidId(String childid) {
		// TODO Auto-generated method stub
		mchildId=childid;
	}

	public static String getchildId() {
		// TODO Auto-generated method stub
		return mchildId;
	}
}
