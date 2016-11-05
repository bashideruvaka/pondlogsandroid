package pondlogss.eruvaka.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE="pondlogs.db28";
	public static final int VERSION=1;
	public static final String TABLE="abwsamples";
	public static final String Weight="Weight";
	public static final String Numbers="Numbers";
	public static final String ID="ID";
	
	public static final String TABLE1="feedentry";
	public static final String PID="PID";
	public static final String S1="S1";
	public static final String S2="S2";
	public static final String S3="S3";
	public static final String S4="S4";
	public static final String S5="S5";
	public static final String S6="S6";
	public static final String S7="S7";
	public static final String S8="S8";
	public static final String S9="S9";
	public static final String S10="S10";
	
	public static final String F1="F1";
	public static final String F2="F2";
	public static final String F3="F3";
	public static final String F4="F4";
	public static final String F5="F5";
	public static final String F6="F6";
	public static final String F7="F7";
	public static final String F8="F8";
	public static final String F9="F9";
	public static final String F10="F10";
	public static final String SP1="SP1";
	public static final String SP2="SP2";
	public static final String SP3="SP3";
	public static final String SP4="SP4";
	public static final String SP5="SP5";
	public static final String SP6="SP6";
	public static final String SP7="SP7";
	public static final String SP8="SP8";
	public static final String SP9="SP9";
	public static final String SP10="SP10";
	
	public static final String RESID="RESID";
	public static final String RESNAMETYPE="RESNAMETYPE";
	
	public static final String TABLE2="feedstocksave";
	public static final String GroupId="GroupId";
	public static final String Nbags="Nbags";
	public static final String BagWeight="BagWeight";
	public static final String TPurchased="TPurchased";
	public static final String PStock="PStock";
	public static final String TStock="TStock";
	public static final String RsrName="RsrName";
	public static final String ResId="ResId";
	public static final String VendorId="VendorId";
	public static final String VendorName="VendorName";
	
	public static final String TABLE3="ponddeatils";
	public static final String  pid="pid";
	public static final String  pondname="pondname";
	public static final String  doc="doc";
	public static final String  hoc="hoc";
	public static final String  density="density";
	public static final String  tanksize="tanksize";
	public static final String  areaunits="areaunits";
	public static final String  schedules="schedules";
	public static final String  sch1="sch1";
	public static final String  sch2="sch2";
	public static final String  sch3="sch3";
	public static final String  sch4="sch4";
	public static final String  sch5="sch5";
	public static final String  sch6="sch6";
	public static final String  sch7="sch7";
	public static final String  sch8="sch8";
	public static final String  sch9="sch9";
	public static final String  sch10="sch10";
	
	
	
	public DBHelper(Context context) {
		super(context, DATABASE, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table pondlogs (ID integer,FEILEDID text,FFEILDNAME text)");
		//db.execSQL("create table feeddemo (ID integer,QTY text,CF text)");
		
		
		db.execSQL("create table permisionstable (ID integer,UserType text,AddLocation text,EditLocation text,DeleteLocation text," +
				"AddTank text,DeleteTank text,AddStock text," +
				"EditStock text,DeleteStock text,AddResources text," +
				"EditResources text,DeleteResource text,FAM text,LabTest text,Harvest text,EditTank text)");
		
		db.execSQL("create table harvest (ID integer,TANKID text,TANKNAME text,HARVEST text)");
		db.execSQL("create table abwsamples (ID integer,Weight text,Numbers text)");
		db.execSQL("create table tankdata (ID integer,TankId text,TankName text)");
		db.execSQL("create table abwdata (ABWID integer,ABW text,WGA text,SWG text)");
		db.execSQL("create table HarvestData (ID integer,HarvestId integer,HarvestWeight text,HarvestSize text,HOC text,TankId integer,TankName text)");
		db.execSQL("CREATE TABLE feedentry (PID text ," +
				"S1 TEXT, S2 TEXT, S3 TEXT, S4 TEXT, S5 TEXT, " +
				"S6 TEXT, S7 TEXT, S8 TEXT, S9 TEXT, S10 TEXT," +
				"F1 text,F2 text,F3 text,F4 text,F5 text," +
				"F6 text,F7 text,F8 text ,F9 text ,F10 text," +
				"SP1 text, SP2 text,SP3 text,SP4 text,SP5 text," +
				"SP6 text,SP7 text,SP8 text, SP9 text, SP10 text,RESID text,RESNAMETYPE text )");
		db.execSQL("CREATE TABLE ponddeatils (pid text, pondname TEXT, doc TEXT, hoc TEXT, density TEXT, tanksize TEXT, areaunits TEXT, schedules TEXT, sch1 TEXT, sch2 TEXT, sch3 TEXT, sch4 TEXT, sch5 TEXT, sch6 TEXT, sch7 TEXT, sch8 TEXT, sch9 TEXT, sch10 TEXT)");
		//feed resources data
		db.execSQL("create table resourcedata (RESID integer,RESNAME text,RESTYPE text,TotalP text,QStock text,LDate etxt)");
		db.execSQL("create table allfeedresource (ID integer,RESID text,RESNAME text)");
		db.execSQL("create table vendordata (ID integer,VENDORID text,VENDORNAME text)");
		db.execSQL("create table feedstocksave (ID integer,GroupId text,Nbags text,BagWeight text,TPurchased text,PStock text,TStock text,RsrName text,ResId text,VendorId text,VendorName text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}