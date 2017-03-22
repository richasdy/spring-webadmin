package com.richasdy.springwebadmin.account;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	public static boolean isValidDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static Date stringToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date output = null;

		try {
			output = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return output;
	}

}
