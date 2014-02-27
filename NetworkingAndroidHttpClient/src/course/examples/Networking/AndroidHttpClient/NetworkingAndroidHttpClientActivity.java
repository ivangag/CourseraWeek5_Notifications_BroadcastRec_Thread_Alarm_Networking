package course.examples.Networking.AndroidHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetworkingAndroidHttpClientActivity extends Activity {
	private TextView mTextView = null;
    private  TextView mTextLastError = null;
    DialogFragment alertDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);



		mTextView = (TextView) findViewById(R.id.textView1);
        mTextLastError = (TextView)findViewById(R.id.textViewLastError);

		final Button loadButton = (Button) findViewById(R.id.button1);
		loadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                new HttpPostMyRESTExampleTask().execute();
				//new HttpGetMyRESTExampleTask().execute();
				//new HttpPostTask().execute();
			}
		});
	}
    public static class ProgressDialogFragment extends DialogFragment {

        public static ProgressDialogFragment newInstance() {
            return new ProgressDialogFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final ProgressDialog dialog = new ProgressDialog(this.getActivity());
            dialog.setMessage("Retrieving data...");
            dialog.setIndeterminate(true);
            return dialog;
        }
    }
    void showDialogFragment(int dialogID) {
        switch (dialogID) {
            /*
            case ALERTTAG:
                mDialog = AlertDialogFragment.newInstance();
                mDialog.show(getFragmentManager(), "Alert");
                break;
            case PROGRESSTAG:
                mDialog = ProgressDialogFragment.newInstance();
                mDialog.show(getFragmentManager(), "Shutdown");
                break;
                */

        }
        alertDialog = ProgressDialogFragment.newInstance();
        alertDialog.show(getFragmentManager(), "Data Fetching");
    }

     void shutdownDialogFragment() {
        alertDialog.dismiss();
    }

    private class HttpPostTask extends AsyncTask<Void,Void,List<String>>
	{
		private static final String TAG = "HttpPostTask";

		private static final String URL = "http://iob.actiaitalia.com/RDSService/service.asmx";
		private String PostHeaderRequest = 
				
				"POST /RDSService/service.asmx HTTP/1.1\r\n" +
				"Host: iob.actiaitalia.com\r\n" +
				"Content-Type: text/xml; charset=utf-8\r\n" +
				"Content-Length: %d\r\n" +
				"SOAPAction: \"http://tempuri.org/CheckDownload\"\r\n\r\n";

		private String PostBodyRequest =
				//"SOAPAction: \"http://tempuri.org/CheckDownload\"\r\n\r\n" +
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
				"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n" +
				  "<soap12:Body>\r\n" +
				    "<CheckDownload xmlns=\"http://tempuri.org/\">\r\n" +
				      "<UserId>WDB9300361L616244</UserId>\r\n" +
				      "<password>356398040160473</password>\r\n" +
				      "<apdudat>\r\n" +
				        "<ExtensionData />\r\n" +
				        "<_apduId>string</_apduId>\r\n" +
				        "<apduId>30</apduId>\r\n" +
				        "<_apduData>string</_apduData>\r\n" +
				        //"<apduData>WDB9300361L616244;0000000000LOG0;;.Basa                               .Emil-Constantin                    ;</apduData>\r\n" +
				        "<apduData>V0RCOTMwMDM2MUw2MTYyNDQ7MDAwMDAwMDAwMExPRzA7Oy5CYXNhICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC5FbWlsLUNvbnN0YW50aW4gICAgICAgICAgICAgICAgICAgIDs=</apduData>\r\n" +				        
				      "</apdudat>\r\n" +
				   "</CheckDownload>\r\n" +
				  "</soap12:Body>\r\n" +
				"</soap12:Envelope>\r\n\r\n";
				

		AndroidHttpClient mClient = AndroidHttpClient.newInstance(null);
		
		@Override
		protected List<String> doInBackground(Void... params) {
			PostHeaderRequest = String.format(PostHeaderRequest, PostBodyRequest.length() + 2);
			// TODO Auto-generated method stub
			HttpPost request = new HttpPost(URL);	
			//prepare and add both header and body request			
			StringEntity soapRequest = null;
			try {
				soapRequest = new StringEntity(PostBodyRequest ,HTTP.UTF_8);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			soapRequest.setContentType("text/xml");  
			request.setHeader("Content-Type","application/soap+xml;charset=utf-8");
			request.setEntity(soapRequest);  
			
			//ResponseHandler<String> responseHandler = new BasicResponseHandler();
           XMLRDSResponseHandler responseHandler = new XMLRDSResponseHandler();
			
			try {
				return mClient.execute(request, responseHandler);
			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<String> result) {

			if (null != mClient)
				mClient.close();

			//mTextView.setText(result);

		}
		
	}

	private class HttpGetTask extends AsyncTask<Void, Void, String> {

		private static final String TAG = "HttpGetTask";

		// Get your own user name at http://www.geonames.org/login
		private static final String USER_NAME = "aporter";

		private static final String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
				+ USER_NAME;

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected String doInBackground(Void... params) {

			HttpGet request = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			try {

				return mClient.execute(request, responseHandler);

			} catch (ClientProtocolException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if (null != mClient)
				mClient.close();

			mTextView.setText(result);

		}
	}

    private class HttpGetMyRESTExampleTask extends AsyncTask<Void, Void, String> {

        String mLastError = "";
        private static final String TAG = "HttpGetTask";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";

        private static final String URL = "http://192.168.0.7:8080/RESTWCFTest/RESTService.svc/simpledata/?tag=3";

        AndroidHttpClient mClient = AndroidHttpClient.newInstance(null);

        @Override
        protected String doInBackground(Void... params) {

            HttpGet request = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {

                return mClient.execute(request, responseHandler);

            } catch (ClientProtocolException exception) {
                mLastError = exception.getLocalizedMessage();
                exception.printStackTrace();
            } catch (IOException exception) {
                mLastError = exception.getLocalizedMessage();
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (null != mClient)
                mClient.close();

            mTextView.setText(result);
            mTextLastError.setText(mLastError);
        }
    }

    private class HttpPostMyRESTExampleTask extends AsyncTask<Void, Void, String> {

        String mLastError = "";
        private static final String TAG = "HttpPostMyRESTExampleTask";

        private static final String URL = "http://192.168.0.7:8080/RESTWCFTest/RESTService.svc/complexdata/";

        AndroidHttpClient mClient = AndroidHttpClient.newInstance(null);

        @Override
        protected void onPreExecute()
        {
            showDialogFragment(0);
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpPost httpost = new HttpPost(URL);
            SimpleDataType simpleDataType = new SimpleDataType();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            //simpleDataType.DataTime = new
            String json = new GsonBuilder().create().toJson(simpleDataType,SimpleDataType.class);

            //sets the post request as the resulting string
            //httpost.setEntity(se);
            try {
                httpost.setEntity(new StringEntity(json));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {

                return mClient.execute(httpost, responseHandler);

            } catch (ClientProtocolException exception) {
                mLastError = exception.getLocalizedMessage();
                exception.printStackTrace();
            } catch (IOException exception) {
                mLastError = exception.getLocalizedMessage();
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            shutdownDialogFragment();
            if (null != mClient)
                mClient.close();

            mTextView.setText(result);
            mTextLastError.setText(mLastError);
        }
    }


    class SimpleDataType
    {
        public String DataName = "";
        public String DataType = "";
        public Date DataTime ;

    }

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //using the earlier example your first entry would get email
        //and the inner while would get the value which would be 'foo@bar.com'
        //{ fan: { email : 'foo@bar.com' } }

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            //creates a key for Map
            String key = (String)pairs.getKey();

            //Create a new map
            Map m = (Map)pairs.getValue();

            //object for storing Json
            JSONObject data = new JSONObject();

            //gets the value
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext())
            {
                Map.Entry pairs2 = (Map.Entry)iter2.next();
                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
            }

            //puts email and 'foo@bar.com'  together in map
            holder.put(key, data);
        }
        return holder;
    }

}