package tcss450.uw.edu.team15project450.server;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Log;

/**
 * Upload will upload a file to a server path after writing it to bytes.
 *
 * Created by Gabe on 5/25/2016.
 */
public class Upload implements Runnable{

    private final static String UPLOAD_FILE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm15/uploadFile.php?";
    private static final String TAG = "Upload";
    private static final String TOUR = "tour";
    private static final String PLACE = "place";

    URL connectURL;
    String mUserID;
    String mType;
    String mTour;
    String mPlace;
    FileInputStream fileInputStream = null;

    public Upload(String userid, String type, String tour, String place){
        try{
            mUserID = userid;
            mType= type;
            mTour = tour;
            if (mType.equals(PLACE)) {
                mPlace = place;
            }
            Log.d(TAG, "Type: " + mType);
            Log.d(TAG, "Place: " + mPlace);
            connectURL = new URL(buildUploadURL());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public String Send(FileInputStream fStream){
        fileInputStream = fStream;
        return Sending();
    }

    private String Sending(){
        String response = "";

        String fileName;
        if (mType.equals(TOUR)) {
            fileName = "tour_main_audio.3gp";
        } else {
            fileName = "place_audio.3gp";
        }
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try
        {
            Log.d(TAG,"Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpURLConnection urlConnection = (HttpURLConnection)connectURL.openConnection();
            urlConnection.setDoInput(true); // Allow Inputs
            urlConnection.setDoOutput(true); // Allow Outputs
            urlConnection.setUseCaches(false); // Don't use a cached copy.
            urlConnection.setRequestMethod("POST"); // Use a post method.
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            urlConnection.setRequestProperty("uploaded_file", fileName);

            DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"userid\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(mUserID);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"type\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(mType);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"tour\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(mTour);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            if (mType.equals(PLACE)) {
                dos.writeBytes("Content-Disposition: form-data; name=\"place\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(mPlace);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
            }

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.d(TAG, "Headers written.");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1 * 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            Log.d(TAG,"File Sent, Response: " + String.valueOf(urlConnection.getResponseCode()));

            InputStream is = urlConnection.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }
            response = b.toString();
            Log.i(TAG,"Response: " + response);
            dos.close();
        } catch (MalformedURLException mue) {
            Log.e(TAG, "URL error: " + mue.getMessage(), mue);
            response =  mue.getMessage();
        } catch (IOException ioe) {
            Log.e(TAG, "IO error: " + ioe.getMessage(), ioe);
            response =  ioe.getMessage();
        }
        return response;
    }

    @Override
    public void run() {
    }

    /**
     * Builds the url with variables to pass to the php files
     * on the server.
     *
     * @return
     */
    private String buildUploadURL() {

        StringBuilder sb = new StringBuilder(UPLOAD_FILE_URL);

        try {
            sb.append("tour=");
            sb.append(URLEncoder.encode(mTour, "UTF-8"));

            sb.append("&userid=");
            sb.append(URLEncoder.encode(mUserID, "UTF-8"));

            sb.append("&type=");
            sb.append(URLEncoder.encode(mType, "UTF-8"));

            if (mType == PLACE) {
                sb.append("&place=");
                sb.append(URLEncoder.encode(mPlace, "UTF-8"));
            }

            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Log.e(TAG, "Something wrong with the url.");
        }
        return sb.toString();
    }
}