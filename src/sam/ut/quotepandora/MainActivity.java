package sam.ut.quotepandora;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import static sam.ut.quotepandora.Constants.IMAGES;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sam.ut.quotepandora.Constants.Extra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;




public class MainActivity extends BaseActivity {
	//static int ff=1;
	
	Button button1;
	Button button2,button3,button4;
	 private ProgressDialog pDialog;
	 JSONParser jParser = new JSONParser();
	  JSONArray products = null;
	 Context context;
	 int message;
	 ConnectionDetector cd;
	    Boolean isInternetPresent = false;
	 private static String url_all_products ;
	 private static final String TAG_SUCCESS = "success";
	 private static final String TAG_MESSAGE = "message";
	 private static final String TAG_PRODUCT = "product";
		private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		 context=this;
		 File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
			if (!testImageOnSdCard.exists()) {
				copyTestImageToSdCard(testImageOnSdCard);
			}
			/*try {
		        PackageInfo info = getPackageManager().getPackageInfo(
		                "sam.ut.quotepandora", 
		                PackageManager.GET_SIGNATURES);
		        for (Signature signature : info.signatures) {
		            MessageDigest md = MessageDigest.getInstance("SHA");
		            md.update(signature.toByteArray());
		            Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		            }
		    } catch (NameNotFoundException e) {

		    } catch (NoSuchAlgorithmException e) {

		    }*/
			
			
			
			
			
		button1=(Button)findViewById(R.id.btn1);
		button2=(Button)findViewById(R.id.btn2);
		button3=(Button)findViewById(R.id.btn3);
		button4=(Button)findViewById(R.id.btn4);
		button1.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
 
				 cd = new ConnectionDetector(getApplicationContext());
			      isInternetPresent = cd.isConnectingToInternet();
			      if (isInternetPresent) {
			    	  
			    	  Constants.IMAGES=null;
			    	 
			    	  Constants.ad="http://brainsfromthebasement.uphero.com/cat01/";
			    	  url_all_products = "http://brainsfromthebasement.uphero.com/test1.php";
			        new LoadAllProducts().execute();
			        
			      }
			      else
			      {
			    	  new AlertDialog.Builder(context)
					    .setTitle("No internet")
					    .setMessage("Connect to the internet")
				        .setCancelable(true)
					    .show();
			      }
				
				
			}
 
		});
		button2.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {

				 cd = new ConnectionDetector(getApplicationContext());
			      isInternetPresent = cd.isConnectingToInternet();
			      if (isInternetPresent) {
			    	  
			    	  Constants.IMAGES=null;
			    	  Constants.ad="http://brainsfromthebasement.uphero.com/cat02/";
			    	  url_all_products = "http://brainsfromthebasement.uphero.com/test2.php";
			        new LoadAllProducts().execute();
			        
			        
			      }
			      else
			      {
			    	  new AlertDialog.Builder(context)
					    .setTitle("No internet")
					    .setMessage("Connect to the internet")
				        .show();
			      }
				
				
			}

		});
		button3.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {

				 cd = new ConnectionDetector(getApplicationContext());
			      isInternetPresent = cd.isConnectingToInternet();
			      if (isInternetPresent) {
			    	  
			    	  Constants.IMAGES=null;
			    	  Constants.ad="http://brainsfromthebasement.uphero.com/cat03/";
			    	  url_all_products = "http://brainsfromthebasement.uphero.com/test3.php";
			        new LoadAllProducts().execute();
			        
			        
			      }
			      else
			      {
			    	  new AlertDialog.Builder(context)
					    .setTitle("No internet")
					    .setMessage("Connect to the internet")
				        .show();
			      }
				
				
			}

		});
		button4.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {

				 cd = new ConnectionDetector(getApplicationContext());
			      isInternetPresent = cd.isConnectingToInternet();
			      if (isInternetPresent) {
			    	  
			    	  Constants.IMAGES=null;
			    	  Constants.ad="http://brainsfromthebasement.uphero.com/cat04/";
			    	  url_all_products = "http://brainsfromthebasement.uphero.com/test4.php";
			        new LoadAllProducts().execute();
			        
			        
			      }
			      else
			      {
			    	  new AlertDialog.Builder(context)
					    .setTitle("No internet")
					    .setMessage("Connect to the internet")
				        .show();
			      }
				
				
			}

		});
	
	}
	class LoadAllProducts extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("",""));
            
          
          
          //  Log.e("do in background",uid);
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "POST", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
           
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                message=json.getInt(TAG_MESSAGE);
                String ah,name;
                 if (success == 1) {
                 	
                	 
                	 Log.e("No of files",message+"");
                	 new Constants(message);
                	
                	 
                 // products found
                  
  	                      
                    
                    }	
                        
                            else {
                        	   
                          	 Log.e("response frm server","okay");
                    
                 
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
 
          
            
           
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
			intent.putExtra(Extra.IMAGES, IMAGES);
			startActivity(intent);
 
        }
 
    }
	

	private void copyTestImageToSdCard(final File testImageOnSdCard) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try {
						while ((read = is.read(buffer)) != -1) {
							fos.write(buffer, 0, read);
						}
					} finally {
						fos.flush();
						fos.close();
						is.close();
					}
				} catch (IOException e) {
					Log.w("dfd","Can't copy test image onto SD card");
				}
			}
		}).start();
	}
}


