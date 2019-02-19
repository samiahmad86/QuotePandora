package sam.ut.quotepandora;

import android.util.Log;

public class Constants {

	public static  String IMAGES[];
	public static String ad;
		// Heavy images
		


public Constants(int count) {
	IMAGES=new String[count];
	for(int i=0;i<count;i++)
	{
		IMAGES[i]=ad+i+".jpg";
		Log.e("check",IMAGES[i]);
	}
}

public static class Config {
	public static final boolean DEVELOPER_MODE = false;
}

public static class Extra {
	public static final String IMAGES = "sam.ut.quotepandora.IMAGES";
	public static final String IMAGE_POSITION = "sam.ut.quotepandora.IMAGE_POSITION";
}
}
