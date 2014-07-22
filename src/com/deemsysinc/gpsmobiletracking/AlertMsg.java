package com.deemsysinc.gpsmobiletracking;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class AlertMsg  extends Activity {

	/** Called when the activity is first created. */
	
	
	ConnectionDetector cd;
	Boolean isInternetPresent = false;
	public ProgressDialog pDialog;
	EditText msgtxt;
	String sendmsg;
	JsonParser jsonParser = new JsonParser();
	JSONObject jobject;
	Context context=this;
	String msg;
    JSONArray number = null;
    JSONArray mobile = null;
    Button home,signout;
    TextView welcomeusername;
	public static ArrayList<String> mobilenumber= new ArrayList<String>();
	

	private static String url = "http://192.168.1.158:8888/gpsandroid/service/message.php?service=select"; 
	
    private static final String TAG_VEHICLE_ARRAY = "mobilenumber";

	
	private static final String TAG_SRES= "serviceresponse";
	
	
	private static final String TAG_Parent_mobile1= "parent_mobile1";
	

	
	private static String parent_mobile1;
	
	String route;
	String orgid;
	int a;
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.alertmsg);
	      
	      route= TrackingActivity.routeno;
	      orgid=LoginActivity.orgid;
	      RelativeLayout layout = (RelativeLayout) findViewById(R.id.alertlayout);
	      layout.setOnTouchListener(new OnTouchListener()
	        {
	            @Override
	            public boolean onTouch(View view, MotionEvent ev)
	            {
	                hideKeyboard(view);
	                return false;
	            }

				
	        });
	      cd = new ConnectionDetector(getApplicationContext());
	      Button btnsend,btnclr;
	      final EditText msgtxt;
	      btnsend =(Button)findViewById(R.id.button1);
	      btnclr =(Button)findViewById(R.id.button2);
	      msgtxt=(EditText)findViewById(R.id.e6);
	      home=(Button)findViewById(R.id.hmebutton);
	      signout=(Button)findViewById(R.id.logingout);
	  	welcomeusername=(TextView)findViewById(R.id.username);
		welcomeusername.setText(LoginActivity.usernamepassed+"!");
			new SendMessage().execute();
			  signout.setOnClickListener(new View.OnClickListener() {
					
		            
		        	public void onClick(View v) { 
		        		LoginActivity.usernamepassed="";
		       VehichleArrayAdapter.data.clear();
		       DashboardActivity.vehicleall.clear();
		       HistoryTrack.vehiclehistory1.clear();
		       LoginActivity.usernamepassed="";
		        		Intent intentSignUP=new Intent(getApplicationContext(),LoginActivity.class);
	  			startActivity(intentSignUP);
		        	}
			 });
			 home.setOnClickListener(new View.OnClickListener() {
					
		            
		        	public void onClick(View v) {
		        		
		       VehichleArrayAdapter.data.clear();
		       DashboardActivity.vehicleall.clear();
		     
		       HistoryTrack.vehiclehistory1.clear();
		      
		        		Intent intentSignUP=new Intent(getApplicationContext(),DashboardActivity.class);
				startActivity(intentSignUP);
		        	}
			 });
	      
	      
	      btnclr.setOnClickListener(new OnClickListener() {
	            
	            @Override
	            public void onClick(View v) {
	                
	                msgtxt.setText("");
	               
	            }
	        });
	        
	      btnsend.setOnClickListener(new OnClickListener() {
	            
	         
				@SuppressWarnings("deprecation")
				@Override
	            public void onClick(View v) {
					
	
					
				        
	            	isInternetPresent = cd.isConnectingToInternet();
	            	System.out.println("is internet present:::"+isInternetPresent);
	            		        		
	       
	            		        		 
	            		        		 if(isInternetPresent)
	            		        			{
	            		        			 	
	            		        
	            		        			
	            		        			 	
	            		        			 	  msg = msgtxt.getText().toString();
	            		        			 	  
	            		        			 	 a=1;
	  						                    if(msgtxt.length()>0)
	  						                    {
	  						       
	  					                       
	  					                            if (isValidName(msg))
	  					                            {
	  					                            	try 
	  					                            	{
	  					                            		running();
	  												/*JSONObject	c = jobject.getJSONObject(TAG_SRES);
	  													
	  			         					    	
	  			         						    	mobile = c.getJSONArray(TAG_VEHICLE_ARRAY);
	  			         						    	Log.i("tagconvertstrxx", "["+mobile+"]");
	  			         						    	
	  			         						    	for(int i=0;i<mobile.length();i++)
	  			         						    	{
	  			         					    		
	  			         					    		JSONObject c1 = mobile.getJSONObject(i);
	  			         					    		JSONObject c2 = c1.getJSONObject(TAG_SRES);
	  			         					    	 
	  			         					    	    parent_mobile1 = c2.getString(TAG_Parent_mobile1);
	  			         					    	   System.out.println("mobile number list"+parent_mobile1);
	  			         					    	   
	  			         					    
	  			         					    	  
	  			         						    	}*/
	  			            		        			}
	  			         						    	
	  													catch (Exception e)
	  													{
	  														// TODO Auto-generated catch block
	  																e.printStackTrace();
	  													}
	  					                              
	  					                            }
	  					                            else{
	  					                                
	  					                         a=0;
	  					                   	AlertDialog alertDialog = new AlertDialog.Builder(
													AlertMsg.this).create();

											// Setting Dialog Title
											alertDialog.setTitle("INFO!");

											// Setting Dialog Message
											alertDialog.setMessage("Please enter valid message.");

											// Setting Icon to Dialog
											alertDialog.setIcon(R.drawable.delete);
											

											// Setting OK Button
											alertDialog.setButton("OK",	new DialogInterface.OnClickListener() {

														public void onClick(final DialogInterface dialog,
																final int which) {
															// Write your code here to execute after dialog
															// closed
															
														}
													});

											// Showing Alert Message
											alertDialog.show();
	  					                  
	  					                            }
	  					                          
	  						                    }else
	  						                    {
	  						                  	AlertDialog alertDialog = new AlertDialog.Builder(
														AlertMsg.this).create();

												// Setting Dialog Title
												alertDialog.setTitle("INFO!");

												// Setting Dialog Message
												alertDialog.setMessage("Message should not be empty." );

												// Setting Icon to Dialog
												alertDialog.setIcon(R.drawable.delete);
												

												// Setting OK Button
												alertDialog.setButton("OK",	new DialogInterface.OnClickListener() {

															public void onClick(final DialogInterface dialog,
																	final int which) {
																// Write your code here to execute after dialog
																// closed
																
															}
														});

												// Showing Alert Message
												alertDialog.show();
	  						                    
	  						                    }
	  						           
											
	            		        			}
	            		        			 
	            		        				
	            		        			
	            		        		else
	            		        		{
	            		        			AlertDialog alertDialog = new AlertDialog.Builder(
													AlertMsg.this).create();

											// Setting Dialog Title
											alertDialog.setTitle("INFO!");

											// Setting Dialog Message
											alertDialog.setMessage("No network connection.");

											// Setting Icon to Dialog
											alertDialog.setIcon(R.drawable.delete);
											

											// Setting OK Button
											alertDialog.setButton("OK",	new DialogInterface.OnClickListener() {

														public void onClick(final DialogInterface dialog,
																final int which) {
															// Write your code here to execute after dialog
															// closed
															
														}
													});

											// Showing Alert Message
											alertDialog.show();
	            		        		
	            		        		}
	            		        		}

				public void running() {
					// TODO Auto-generated method stub
					new messaging().execute();
				}

				private boolean isValidName(String messagetext2) {
					// TODO Auto-generated method stub
					
					
					 String other = "^[a-zA-Z0-9@_.,-/\n ]*$";
		                
		                Pattern pattern = Pattern.compile(other);
		                Matcher matcher = pattern.matcher(messagetext2);
		                return matcher.matches();
				}

	      });
	            
}
	      	 
	  
	    class messaging extends AsyncTask<String, String, String> {
	    	  
					
				
					@Override
					protected String doInBackground(String... args) {
						// TODO Auto-generated method stub
						
						int i;
						
						    try
						    {
						    	System.out.println("json value::"+jobject);
					    		JSONObject c = jobject.getJSONObject(TAG_SRES);
					    	
						    	Log.i("tagconvertstr", "["+c+"]");
						    	mobile = c.getJSONArray(TAG_VEHICLE_ARRAY);
						    	Log.i("tagconvertstr1", "["+mobile+"]");
							    	
							    	for(i=0;i<mobilenumber.size();i++)
							    	{
						    		System.out.println("forloop1");
						    		JSONObject c1 = mobile.getJSONObject(i);
						    		JSONObject c2 = c1.getJSONObject(TAG_SRES);
						    	 
						    	   // parent_mobile1 = c2.getString(TAG_Parent_mobile1);
						    		parent_mobile1=mobilenumber.get(i);
						    	    
						        System.out.println("mobile number list"+parent_mobile1);
						    	   
						   
						        
						        String username ="info@holycrossengineeringcollege.com";
							 	String password ="tSE4A7qY";
							 	
							 	String message =msg+"\n";
							 	String resultString = message.replaceAll(" ","%20");
							 	resultString=resultString.replaceAll("\n", "%20");
							 	String trinstring=resultString.trim();
							 	System.out.println("string afet trim"+trinstring);
							 	System.out.println("msg value after append"+resultString);
							 	String number =parent_mobile1;
							 	
					    	   
						    	
							 
						 	
						 	String url= "http://api.cutesms.in/sms.aspx?a=submit&un="+username+"&pw="+password+"&to="+number+"&msg="+resultString+"";
							System.out.println("url:"+url);
							System.out.println("url length"+url.length());
						 	DefaultHttpClient mClient= new DefaultHttpClient();
							 HttpGet get = new HttpGet(url);

						        
							 try {
						        	System.out.println("enter1");
						          	
						          		System.out.println("enter2");
						          		HttpResponse res = mClient.execute(get);
						          		System.out.println("enter3");
						          		System.out.println("responset"+res);
						         } 
						          	catch (Exception e) 
						          	{
						           
						        }
							
						    	System.out.println("i value"+i);
							   }
							    
							
						    }
						    catch (JSONException e) 
						    {
						        e.printStackTrace();
						    }
						    

							return null;
						}
					@Override
	    			protected void onPostExecute(String file_url) {
					
	    				 super.onPostExecute(file_url);
	    				pDialog.dismiss();
	    			
	 
							}
	    					 
					
	    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	      class SendMessage extends AsyncTask<String, String, String> {
	    	  
				@Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(AlertMsg.this);
		            pDialog.setMessage("Please wait...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(false);
		            pDialog.show();

		        }

				@Override
				protected String doInBackground(String... args) {
					// TODO Auto-generated method stub
					
					
					List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			        
			        params1.add(new BasicNameValuePair("org_id", LoginActivity.orgid));

			        params1.add(new BasicNameValuePair("routeno", route));
			        System.out.println("parameters"+params1);
			        
			        
			        
			        jobject = ((JsonParser) jsonParser).makeHttpRequest(url, "POST", params1);
					
					    Log.i("tagconvertstr", "["+jobject+"]");
					    
				
					    try
					    {
					    	if(jobject != null){
					    		
					    		System.out.println("json value::"+jobject);
					    		JSONObject c = jobject.getJSONObject(TAG_SRES);
					    		
						    	Log.i("tagconvertstr", "["+c+"]");
						    	number = c.getJSONArray(TAG_VEHICLE_ARRAY);
						    	Log.i("tagconvertstr1", "["+number+"]");
						    	
						    	for(int i=0;i<number.length();i++)
						    	{
					    		System.out.println("forloop1");
					    		JSONObject c1 = number.getJSONObject(i);
					    		JSONObject c2 = c1.getJSONObject(TAG_SRES);
					    	 
					    	    parent_mobile1 = c2.getString(TAG_Parent_mobile1);
					    	    
					        System.out.println("mobile number list"+parent_mobile1);
					    	   
					    	  
					      mobilenumber.add(parent_mobile1);
					    	    
					    
					    	
					 	}
						 
						    	
						    	}
					    	
					    	}catch (JSONException e) {
					        e.printStackTrace();
					    }
					    pDialog.dismiss();
						return null;
					}
	      
				
				@SuppressWarnings("deprecation")
				@Override
    			protected void onPostExecute(String file_url) {
				

    				 super.onPostExecute(file_url);
    				pDialog.dismiss();
    			if(mobilenumber.size()==0)
    			{
    				AlertDialog alertDialog = new AlertDialog.Builder(
							AlertMsg.this).create();

					// Setting Dialog Title
					alertDialog.setTitle("INFO!");

					// Setting Dialog Message
					alertDialog.setMessage("No mobile numbers available." );

					// Setting Icon to Dialog
					alertDialog.setIcon(R.drawable.delete);
					

					// Setting OK Button
					alertDialog.setButton("OK",	new DialogInterface.OnClickListener() {

								public void onClick(final DialogInterface dialog,
										final int which) {
									// Write your code here to execute after dialog
									// closed
									
								}
							});

					// Showing Alert Message
					alertDialog.show();
    			/*	 AlertDialog.Builder builder= new AlertDialog.Builder(AlertMsg.this,R.style.MyTheme );
	    		        
	    	            builder.setMessage("No mobile numbers available." )
	    	                .setTitle( "INFO!" )
	    	                .setIcon( R.drawable.pink_pin )
	    	                .setCancelable( false )
	    	             
	    	                .setPositiveButton( "OK", new DialogInterface.OnClickListener()
	    	                    {
	    	                        public void onClick( DialogInterface dialog, int which )
	    	                           {
	    	                        	
	    	                                dialog.dismiss();
	    	                           }
	    	                        } 
	    	                    );
	    	            Dialog dialog = null;
	    	            builder.setInverseBackgroundForced(true);
	    	            
	    	            dialog = builder.create();
	    	            dialog.getWindow().setLayout(600, 400); 
	    	            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    				dialog.show();
	    				*/
    			}
 
						}
    					 
    					 
    				 }
    				 
	      @Override
	      public void onBackPressed() {
	      }

	      protected void hideKeyboard(View view)
	 	 {
	 	     InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 	     in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	 	 }
				}
    			
