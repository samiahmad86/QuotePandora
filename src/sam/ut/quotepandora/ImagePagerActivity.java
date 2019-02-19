/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package sam.ut.quotepandora;

import static sam.ut.quotepandora.Constants.IMAGES;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import sam.ut.quotepandora.Constants.Extra;
import sam.ut.quotepandora.MainActivity.LoadAllProducts;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;
	 byte[] buffer;
	ViewPager pager;
	Button set,fb,btn3,btn4,btn5;
	 ConnectionDetector cd;
	    Boolean isInternetPresent = false;
	    Context context;
	    private ProgressDialog pDialog; Bitmap image;
int abc;
String temp_id;
String click;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_image_pager);
		set=(Button)findViewById(R.id.button2);
		fb=(Button)findViewById(R.id.button1);
		btn3=(Button)findViewById(R.id.button3);
		btn4=(Button)findViewById(R.id.button4);
		btn5=(Button)findViewById(R.id.button5);
		
		abc=1024;
		click="";
		
		buffer= new byte[1];
		context=this;

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(pagerPosition);
		set.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
			    	 
			    	  String temp=Constants.IMAGES[pager.getCurrentItem()];
			    	  saveinSdcard(temp);
			    	 
			}
		});
		
		fb.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				/* Intent intent = new Intent(ImagePagerActivity.this, ShareActivity.class);
					intent.putExtra("url", Constants.IMAGES[pager.getCurrentItem()]);
					startActivity(intent);*/
				click="fb";
				temp_id=Constants.IMAGES[pager.getCurrentItem()];
				new RetrieveFeedTask().execute();
				

			}
		});
		btn3.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				/* Intent intent = new Intent(ImagePagerActivity.this, ShareActivity.class);
					intent.putExtra("url", Constants.IMAGES[pager.getCurrentItem()]);
					startActivity(intent);*/
				click="wa";
				temp_id=Constants.IMAGES[pager.getCurrentItem()];
				new RetrieveFeedTask().execute();
				

			}
		});
		btn4.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				/* Intent intent = new Intent(ImagePagerActivity.this, ShareActivity.class);
					intent.putExtra("url", Constants.IMAGES[pager.getCurrentItem()]);
					startActivity(intent);*/
				click="twi";
				temp_id=Constants.IMAGES[pager.getCurrentItem()];
				new RetrieveFeedTask().execute();
				

			}
		});
		btn5.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View arg0) {
				
				/* Intent intent = new Intent(ImagePagerActivity.this, ShareActivity.class);
					intent.putExtra("url", Constants.IMAGES[pager.getCurrentItem()]);
					startActivity(intent);*/
				click="nothing else matters";
				temp_id=Constants.IMAGES[pager.getCurrentItem()];
				new RetrieveFeedTask().execute();
				

			}
		});
		
		
		
		
		
		
		
		
	}
	
	public void saveinSdcard(String temp)
	{
		cd = new ConnectionDetector(getApplicationContext());
	      isInternetPresent = cd.isConnectingToInternet();
		
		if (isInternetPresent) {
	    	  
			
		        	
		        
		    
		   // String root = Environment.getExternalStorageDirectory().toString();
		   // File myDir = new File(root + "/quotes");
			

            try
            {
            	DownloadManager mgr = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(temp);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("Demo")
                        .setDescription("Downloading in your gallery!")
                        .setDestinationInExternalPublicDir("/quotes", System.currentTimeMillis()+".jpg");
                

                mgr.enqueue(request);

	        	Toast.makeText(getApplicationContext(), " Downloaded in your gallery!",
		    			   Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
            	 Toast.makeText(getApplicationContext(), "Downloading inComplete", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        
	    	    
		
	        
	        
	      }
	      else
	      {
	    	  Toast.makeText(getApplicationContext(), "Connect to the internet first asshole", 
	    			   Toast.LENGTH_LONG).show();
	      }
	    	
		
		
	}

	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			imageLoader.displayImage(images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
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
	    	 
	    	 
	    	  pDialog.dismiss();
	    	  Bitmap icon = image;
	    	Intent share1 = new Intent(Intent.ACTION_SEND);
	    		share1.setType("image/jpeg");
	    		

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

	    		
	    		
	    		 List<Intent> targetedShareIntents = new ArrayList<Intent>();
	    		    Intent share = new Intent(android.content.Intent.ACTION_SEND);
	    		    share.setType("image/jpeg");
	    		    boolean flag=false;
	    		    String chk="something that can never be achieved";
	    		    if(click=="fb")
	    		    {
	    		    	chk="facebook";
	    		    }
	    		    if(click=="wa")
	    		    {
	    		    	chk="what";
	    		    }
	    		    if(click=="twi")
	    		    {
	    		    	chk="twitter";
	    		    }	
	    		    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
	    		    if (!resInfo.isEmpty()){
	    		        for (ResolveInfo info : resInfo) {
	    		            Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
	    		            targetedShare.setType("image/jpeg"); // put here your mime type

	    		            if( (info.activityInfo.packageName.toLowerCase().startsWith(chk) || 
	    		                    info.activityInfo.name.toLowerCase().startsWith(chk))&&flag==false) {
	    		                targetedShare.putExtra(Intent.EXTRA_STREAM,uri);
	    		             
	    		                targetedShare.setPackage(info.activityInfo.packageName);
	    		                targetedShareIntents.add(targetedShare);
	    		                flag=true;
	    		                break;
	    		            }
	    		        }

	    		       // Intent chooserIntent = Intent.createChooser(targetedShareIntents.get(0), "Select app to share");
	    		      //  chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
	    		       if(flag==true)
	    		       { startActivity(targetedShareIntents.get(0));}
	    		       else
	    		       {
	    		    	   share1.putExtra(Intent.EXTRA_STREAM, uri);
	    		    		startActivity(Intent.createChooser(share1, "Share Image"));
	    		    	   
	    		       }
	    	
	      
	    	
	    		    }
	      }
	     
	}
}