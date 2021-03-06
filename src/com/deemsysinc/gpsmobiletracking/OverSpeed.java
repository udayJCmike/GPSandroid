package com.deemsysinc.gpsmobiletracking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import android.widget.SpinnerAdapter;

import android.widget.TextView;

public class OverSpeed extends Activity implements AnimationListener {

	/** Called when the activity is first created. */

	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	public ProgressDialog pDialog;
	JsonParser jsonParser = new JsonParser();
	JSONObject jobject;
	Context context = this;
	String status;
	JSONArray number = null;
	JSONArray mobile = null;
	TextView overspeed, drivername, reg_number;
	Button fromdate, todate, submit, signout;
	static final int DATE_PICKER_ID = 1111;
	static final int DATE_PICKER_ID1 = 1112;
	String succ;
	private static final String TAG_SRES = "serviceresponse";
	TextView welcomeusername, welcome, devicestatus, ownername;
	private static final String TAG_Count_BT_DATES = "overspeed_count";
	static final String TAG_Count = "overspeed_count";
	private int year;
	private int month;
	private int day;
	String fromstring, tostring;
	String checkdate;
	String countbtdates, count;
	LinearLayout linear;
	Button btn, clobtn;
	Animation animSlideUp, animSlideDown;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overspeed);
		ActionBar actions = getActionBar();
		getActionBar().setBackgroundDrawable(
				new BitmapDrawable(BitmapFactory.decodeResource(getResources(),
						R.drawable.actionbarbg)));

		actions.setIcon(R.drawable.overspeedicon);
		actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actions.setDisplayShowTitleEnabled(false);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
		linear = (LinearLayout) findViewById(R.id.linearover);
		overspeed = (TextView) findViewById(R.id.overspeed);
		drivername = (TextView) findViewById(R.id.drivername);
		reg_number = (TextView) findViewById(R.id.veg_reg_no);
		ownername = (TextView) findViewById(R.id.ownername);
		devicestatus = (TextView) findViewById(R.id.device_status);
		reg_number.setText(LiveTrack.vehicle_reg_no);
		drivername.setText(LiveTrack.driver_name);
		ownername.setText(Config.username);

		if (LiveTrack.devicestatus.equalsIgnoreCase("0")) {
			devicestatus.setText("Switched Off");
		} else if (LiveTrack.devicestatus.equalsIgnoreCase("1")) {
			devicestatus.setText("Active");
		} else if (LiveTrack.devicestatus.equalsIgnoreCase("2")) {
			devicestatus.setText("No GPS signal");
		} else if (LiveTrack.devicestatus.equalsIgnoreCase("3")) {
			devicestatus.setText("Sleep mode");
		} else {
			devicestatus.setText("Switched Off");
		}
		fromdate = (Button) findViewById(R.id.fromdate);
		todate = (Button) findViewById(R.id.todate);
		submit = (Button) findViewById(R.id.submit);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		fromdate.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID);
			}
		});
		animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_up);
		animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_down);

		animSlideUp.setAnimationListener(this);
		animSlideDown.setAnimationListener(this);

		submit.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				isInternetPresent = cd.isConnectingToInternet();
				if (!fromdate.getText().toString()
						.equalsIgnoreCase("From date")) {
					if (!todate.getText().toString()
							.equalsIgnoreCase("To date")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						try {
							Date date1 = sdf.parse(fromdate.getText()
									.toString());
							Date date2 = sdf.parse(todate.getText().toString());

							if (date1.compareTo(date2) > 0) {
								AlertDialog alertDialog = new AlertDialog.Builder(
										OverSpeed.this).create();

								alertDialog.setTitle("INFO!");

								alertDialog
										.setMessage("To date must greater than from date.");

								alertDialog.setIcon(R.drawable.delete);

								alertDialog.setButton("OK",
										new DialogInterface.OnClickListener() {

											public void onClick(
													final DialogInterface dialog,
													final int which) {

											}
										});

								alertDialog.show();
							} else if (date1.compareTo(date2) < 0) {
								linear.setVisibility(View.GONE);
								linear.startAnimation(animSlideUp);
								if (isInternetPresent) {
									new CompareAsync().execute();
								} else {
									AlertDialog alertDialog = new AlertDialog.Builder(
											OverSpeed.this).create();

									alertDialog.setTitle("INFO!");

									alertDialog
											.setMessage("No network connection.");

									alertDialog.setIcon(R.drawable.delete);

									alertDialog
											.setButton(
													"OK",
													new DialogInterface.OnClickListener() {

														public void onClick(
																final DialogInterface dialog,
																final int which) {

														}
													});

									alertDialog.show();
								}
							} else if (date1.compareTo(date2) == 0) {
								linear.setVisibility(View.GONE);
								linear.startAnimation(animSlideUp);
								if (isInternetPresent) {
									new CompareAsync().execute();
								} else {
									AlertDialog alertDialog = new AlertDialog.Builder(
											OverSpeed.this).create();

									alertDialog.setTitle("INFO!");

									alertDialog
											.setMessage("No network connection.");

									alertDialog.setIcon(R.drawable.delete);

									alertDialog
											.setButton(
													"OK",
													new DialogInterface.OnClickListener() {

														public void onClick(
																final DialogInterface dialog,
																final int which) {

														}
													});

									alertDialog.show();
								}
							} else {
								System.out.println("How to get here?");
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						AlertDialog alertDialog = new AlertDialog.Builder(
								OverSpeed.this).create();

						alertDialog.setTitle("INFO!");

						alertDialog.setMessage("Select to date.");

						alertDialog.setIcon(R.drawable.delete);

						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {

									public void onClick(
											final DialogInterface dialog,
											final int which) {

									}
								});

						alertDialog.show();
					}
				} else {
					AlertDialog alertDialog = new AlertDialog.Builder(
							OverSpeed.this).create();

					alertDialog.setTitle("INFO!");

					alertDialog.setMessage("Select from date.");

					alertDialog.setIcon(R.drawable.delete);

					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(
										final DialogInterface dialog,
										final int which) {

								}
							});

					alertDialog.show();

				}
			}
		});
		todate.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_PICKER_ID1);
			}
		});
		if (Config.role.equalsIgnoreCase("ROLE_FCLIENT")||Config.role.equalsIgnoreCase("ROLE_PCLIENT")) {
			SpinnerAdapter adapter1 = ArrayAdapter.createFromResource(
					getActionBar().getThemedContext(),
					R.array.nav_drawer_items3_withoutalert,
					android.R.layout.simple_spinner_dropdown_item);

			OnNavigationListener callback = new OnNavigationListener() {

				@Override
				public boolean onNavigationItemSelected(int itemPosition,
						long id) {

					Intent myIntent;
					if (itemPosition != 0) {
						if (itemPosition == 0) { // Activity#1 Selected
							LiveTrack.timer.cancel();
							LiveTrack.doAsynchronousTask.cancel();
							myIntent = new Intent(OverSpeed.this,
									OverSpeed.class);
							OverSpeed.this.startActivity(myIntent);
						} else if (itemPosition == 1) { // Activity#2 Selected
							LiveTrack.timer.cancel();
							LiveTrack.doAsynchronousTask.cancel();
							myIntent = new Intent(OverSpeed.this,
									LiveTrack.class);
							myIntent.putExtra("vehicleregnum",
									LiveTrack.vehicle_reg_no);
							myIntent.putExtra("drivername",
									LiveTrack.driver_name);
							myIntent.putExtra("routenum", LiveTrack.routeno);
							myIntent.putExtra("devicestatus",
									LiveTrack.devicestatus);
							OverSpeed.this.startActivity(myIntent);
							overridePendingTransition(R.anim.slide_in,
									R.anim.slide_out);
						} else if (itemPosition == 2) { // Activity#3 Selected
							LiveTrack.timer.cancel();
							LiveTrack.doAsynchronousTask.cancel();
							myIntent = new Intent(OverSpeed.this,
									HistoryTrack.class);
							OverSpeed.this.startActivity(myIntent);
							overridePendingTransition(R.anim.slide_in,
									R.anim.slide_out);
						} else if (itemPosition == 3) { // Activity#3 Selected
							LiveTrack.timer.cancel();
							LiveTrack.doAsynchronousTask.cancel();
							myIntent = new Intent(OverSpeed.this,
									TheftAlarm.class);
							OverSpeed.this.startActivity(myIntent);
							overridePendingTransition(R.anim.slide_in,
									R.anim.slide_out);
						} else if (itemPosition == 4) { // Activity#3 Selected
							LiveTrack.timer.cancel();
							LiveTrack.doAsynchronousTask.cancel();
							myIntent = new Intent(OverSpeed.this,
									DashboardActivity.class);
							Config.flag = "alreadyloggedin";
							myIntent.putExtra("isalreadylogged", Config.flag);
							OverSpeed.this.startActivity(myIntent);
							overridePendingTransition(R.anim.slide_in,
									R.anim.slide_out);
						}

					} else {

					}
					return true;

				}

			};

			actions.setListNavigationCallbacks(adapter1, callback);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_PICKER_ID:

			return new DatePickerDialog(this, pickerListener, year, month, day);

		case DATE_PICKER_ID1:

			return new DatePickerDialog(this, pickerListener1, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			// int noOfTimesCalled = 0;
			if (view.isShown()) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;

				String date1 = year + "-" + checkDigit(month + 1) + "-"
						+ checkDigit(day);
				fromstring = date1.toString();
				System.out.println("from string::" + fromstring);
				fromdate.setText(fromstring);

			}
		}
	};
	private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			// int noOfTimesCalled = 0;
			if (view.isShown()) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;

				String date1 = year + "-" + checkDigit(month + 1) + "-"
						+ checkDigit(day);
				tostring = date1.toString();
				todate.setText(tostring);

			}
		}
	};

	public String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}

	class CompareAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("vechicle_reg_no",
					LiveTrack.vehicle_reg_no));
			params1.add(new BasicNameValuePair("org_id", Config.org_id));
			params1.add(new BasicNameValuePair("date1", fromstring));
			params1.add(new BasicNameValuePair("date2", tostring));
			JsonParser jLogin = new JsonParser();

			JSONObject json = jLogin.makeHttpRequest(Config.ServerUrl
					+ "OverSpeed.php?service=overspeeddate", "POST", params1);
			try {
				if (json != null) {
					try {
						if (json != null) {
							System.out.println("json value::" + json);

							JSONObject jUser = json.getJSONObject(TAG_SRES);
							countbtdates = jUser.getString(TAG_Count_BT_DATES);

						}

					}

					catch (Exception e) {
						e.printStackTrace();

					}
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			try {
				overspeed.setText(countbtdates);
			} catch (Exception e) {

			}

		}
	}

	class GetAsync extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("vechicle_reg_no",
					LiveTrack.vehicle_reg_no));
			params1.add(new BasicNameValuePair("org_id", Config.org_id));

			JsonParser jLogin = new JsonParser();

			JSONObject json = jLogin.makeHttpRequest(Config.ServerUrl
					+ "OverSpeed.php?service=overspeed", "POST", params1);
			try {
				if (json != null) {
					try {
						if (json != null) {
							System.out.println("json value::" + json);

							JSONObject jUser = json.getJSONObject(TAG_SRES);
							countbtdates = jUser.getString(TAG_Count_BT_DATES);

						}

					}

					catch (JSONException e) {
						e.printStackTrace();

					}

				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			super.onPostExecute(file_url);
			try {
				overspeed.setText(countbtdates);
			} catch (Exception e) {

			}

		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.ovrspeed:

			linear.setVisibility(View.VISIBLE);
			linear.startAnimation(animSlideDown);

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.overspeed, menu);
		return true;
	}
}