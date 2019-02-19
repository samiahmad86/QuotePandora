package sam.ut.quotepandora;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import android.net.Uri;
import android.os.Handler;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.facebook.widget.WebDialog;
public class ShareActivity extends Activity {
	private UiLifecycleHelper uiHelper;
	public Button postImageBtn;
	private LoginButton loginBtn;

	
 
	private TextView userName;
	String temp_id;
	Bitmap image;
	 private ProgressDialog pDialog;
	  Context context;
	  ImageView mImg;
	  ConnectionDetector cd;
	    Boolean isInternetPresent = false;
	    EditText ed;
 
	 
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
 
	private static String message = "Sample status posted from android app";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 Bundle bundle = getIntent().getExtras();
	    	temp_id = bundle.getString("url");
		
	uiHelper = new UiLifecycleHelper(this, statusCallback);
	uiHelper.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);
	context=this;
	//userName = (TextView) findViewById(R.id.user_name);
	mImg = (ImageView) findViewById(R.id.img);
	loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
	ed=(EditText)findViewById(R.id.ed);
	loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
		@Override
		public void onUserInfoFetched(GraphUser user) {
			if (user != null) {
				//userName.setText("Hello, " + user.getName());
				 cd = new ConnectionDetector(getApplicationContext());
			      isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
			    	  
						
						new RetrieveFeedTask().execute();
						
			      }
			      else
			      {
			    	  new AlertDialog.Builder(context)
					    .setTitle("No internet")
					    .setMessage("Connect to the internet")
				        .show();
			      }
			} else {
				//userName.setText("");
			}
		}
	});

	postImageBtn = (Button) findViewById(R.id.post_image);
	postImageBtn.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View view) {
			try {
				postImage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});

	

	buttonsEnabled(false);
}

private Session.StatusCallback statusCallback = new Session.StatusCallback() {
	@Override
	public void call(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			buttonsEnabled(true);
			Log.d("FacebookSampleActivity", "Facebook session opened");
		} else if (state.isClosed()) {
			buttonsEnabled(false);
			Log.d("FacebookSampleActivity", "Facebook session closed");
		}
	}
};

public void buttonsEnabled(boolean isEnabled) {
	postImageBtn.setEnabled(isEnabled);
	ed.setEnabled(isEnabled);
	
}
private FacebookDialog.PhotoShareDialogBuilder createShareDialogBuilderForPhoto(Bitmap... photos) {
    return new FacebookDialog.PhotoShareDialogBuilder(this)
            .addPhotos(null);
}


public void postImage() throws IOException {
	/*if (checkPermissions()) {
		
		 cd = new ConnectionDetector(getApplicationContext());
	      isInternetPresent = cd.isConnectingToInternet();
	      if (isInternetPresent) {
	    	  
			
			//	new RetrieveFeedTask1().execute();
	    	  List<Bitmap> images = new ArrayList<Bitmap>();
	    	    images.add(image);
	    	    
	    	    boolean canPresentShareDialogWithPhotos = FacebookDialog.canPresentShareDialog(this,
	                    FacebookDialog.ShareDialogFeature.PHOTOS);
	    	    
	    	    
	    	    if (canPresentShareDialogWithPhotos) {
	                FacebookDialog shareDialog = createShareDialogBuilderForPhoto(image).build();
	                uiHelper.trackPendingDialogCall(shareDialog.present());
	            }  
	       
		
		
    }
	}
	 else {
		requestPermissions();
	 }*/
	Bitmap icon = image;
	Intent share = new Intent(Intent.ACTION_SEND);
	share.setType("image/jpeg");
	

	ContentValues values = new ContentValues();
	values.put(Images.Media.TITLE, "title");
	values.put(Images.Media.MIME_TYPE, "image/jpeg");
	Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
	        values);


	OutputStream outstream;
	try {
	    outstream = getContentResolver().openOutputStream(uri);
	    icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
	    outstream.close();
	} catch (Exception e) {
	    System.err.println(e.toString());
	}

	share.putExtra(Intent.EXTRA_STREAM, uri);
	startActivity(Intent.createChooser(share, "Share Image"));
}



public boolean checkPermissions() {
	Session s = Session.getActiveSession();
	if (s != null) {
		return s.getPermissions().contains("publish_actions");
	} else
		return false;
}

public void requestPermissions() {
	Session s = Session.getActiveSession();
	if (s != null)
		s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
				this, PERMISSIONS));
}

@Override
public void onResume() {
	super.onResume();
	uiHelper.onResume();
	buttonsEnabled(Session.getActiveSession().isOpened());
}

@Override
public void onPause() {
	super.onPause();
	uiHelper.onPause();
}

@Override
public void onDestroy() {
	super.onDestroy();
	uiHelper.onDestroy();
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	uiHelper.onActivityResult(requestCode, resultCode, data);
}

@Override
public void onSaveInstanceState(Bundle savedState) {
	super.onSaveInstanceState(savedState);
	uiHelper.onSaveInstanceState(savedState);
}
public void timerDelayRemoveDialog(long time, final Dialog d){
    new Handler().postDelayed(new Runnable() {
        public void run() {                
            d.dismiss();         
        }
    }, time); 
}
class RetrieveFeedTask extends AsyncTask<String, Boolean, String> {

    private Exception exception;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
       // timerDelayRemoveDialog(10000,pDialog); 
    }

      protected String doInBackground(String... a) {
    	
 		
 		try
 		{
 		URL url = new URL(temp_id);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setDoInput(true);
         connection.connect();
         InputStream input = connection.getInputStream();
          image = BitmapFactory.decodeStream(input);
        
         
          
        
          return null;
          
         
 		
 	
 		}
 		catch (IOException e) {
 	        // Log exception
 		return null;
 			
 	    }
 		
    }
     

      protected void onPostExecute(String file_url) {
    	 
    	  mImg.setImageBitmap(image);  
    	  pDialog.dismiss();
    	
      
    	
          
      }
     
}
class RetrieveFeedTask1 extends AsyncTask<String, Boolean, String> {

    private Exception exception;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Uploading on Facebook");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
       //timerDelayRemoveDialog(10000,pDialog); 
    }

      protected String doInBackground(String... a) {
    	
 		
 		try
 		{
 			
         
          
        
          return null;
          
         
 		
 	
 		}
 		catch (Exception e) {
 	        // Log exception
 		return null;
 			
 	    }
 		
    }
     

      protected void onPostExecute(String file_url) {
    	 
    	  Bundle param = new Bundle();
    	  try {
             
              ByteArrayOutputStream stream = new ByteArrayOutputStream();
              image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
              byte[] byteArrayData = stream.toByteArray();
              param.putByteArray("picture", byteArrayData);
          } catch (Exception ioe) {
              // The image that was send through is now not there?
              Assert.assertTrue(false);
          }
    	  param.putString("message", ed.getText().toString());
    	 
    	


// Fallback. For example, publish the post using the Feed Dialog

    	  
    /*
    	  Request uploadRequest = new Request(Session.getActiveSession(),"me/photos",param,HttpMethod.POST,new Request.Callback() {
	 					@Override
	 					public void onCompleted(Response response) {
	 						 mImg.setImageBitmap(image);
		 						Toast.makeText(ShareActivity.this,
		 								"Photo uploaded successfully",
		 								Toast.LENGTH_LONG).show();
		 						pDialog.dismiss();
		 						new AlertDialog.Builder(context)
		 					    .setTitle("Successful")
		 					    .setMessage("Posted on Facebook")
		 				        .show();
	 					}
	 				},null);
    	  RequestAsyncTask asyncTask = new RequestAsyncTask(uploadRequest);
          asyncTask.execute();*/
        
	 
	 		
         
        
         

      }
     
}
}