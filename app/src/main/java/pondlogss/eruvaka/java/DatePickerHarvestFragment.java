package pondlogss.eruvaka.java;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerHarvestFragment extends DialogFragment{
	 OnDateSetListener ondateSetHarvest;

	 public DatePickerHarvestFragment() {
	 }

	 public void setCallBack(OnDateSetListener ondate) {
		 ondateSetHarvest = ondate;
	 }

	 private int mStartYear, mStartMonth, mStartDay;
	 @Override
	 public void setArguments(Bundle args) {
	  super.setArguments(args);
	  mStartYear = args.getInt("year");
	  mStartMonth = args.getInt("month");
	  mStartDay = args.getInt("day");
	 }
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	  return new DatePickerDialog(getActivity(), ondateSetHarvest, mStartYear, mStartMonth, mStartDay);
	 }

}
