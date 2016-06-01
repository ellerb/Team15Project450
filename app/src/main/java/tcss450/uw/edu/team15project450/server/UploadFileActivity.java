package tcss450.uw.edu.team15project450.server;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import tcss450.uw.edu.team15project450.R;

public class UploadFileActivity extends Activity {

    private static final String TAG = "UploadFileActivity";
    private final static String UPLOAD_FILE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm15/uploadFile.php?";
    private static final String TOUR = "tour";
    private static final String PLACE = "place";

    //TextView messageText;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String mFilePathToUpload;
    String mServerFilePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setContentView(R.layout.activity_upload_file);

        //messageText  = (TextView)findViewById(R.id.messageText);

        Bundle bundle = getIntent().getExtras();
        if (bundle.isEmpty()) Log.i(TAG, "Bundle is empty");
        String type = bundle.getString("type");
        String tour = bundle.getString("tour");
        String place = bundle.getString("place");
        String createdBy = bundle.getString("userid");
        mFilePathToUpload = bundle.getString("filepath");

        //messageText.setText("Uploading file path :-" + mFilePathToUpload + "'");
        //Log.i(TAG, mFilePathToUpload);
        Log.i(TAG, bundle.toString());

        mServerFilePath = getString(R.string.base_server_url) + "uploads" + "users";

        switch (type) {
            case TOUR:
                mServerFilePath += "/" + createdBy + "/" + tour + "/touraudio.3gp";
                //mServerFilePath += "/" + createdBy + "/touraudio.3gp";
                break;
            case PLACE:
                mServerFilePath += "/" + createdBy + "/" + tour + "/" + place + "/audio.3gp";
                break;
        }
        dialog = ProgressDialog.show(UploadFileActivity.this, "", "Uploading file...", true);


        new Thread(new Runnable() {
            public void run() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        messageText.setText("uploading started.....");
//                    }
//                });
            uploadFile(mFilePathToUpload);
            finish();

            }
        }).start();
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection urlConnection = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +mFilePathToUpload);

//            runOnUiThread(new Runnable() {
//                public void run() {
//                    messageText.setText("Source File not exist :"
//                            +mFilePathToUpload);
//                }
//            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL urlObject = new URL(UPLOAD_FILE_URL);

                // Open a HTTP  connection to  the URL
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setDoInput(true); // Allow Inputs
                urlConnection.setDoOutput(true); // Allow Outputs
                urlConnection.setUseCaches(false); // Don't use a Cached Copy
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                urlConnection.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(urlConnection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename="
                                + fileName + "" + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = urlConnection.getResponseCode();
                String serverResponseMessage = urlConnection.getResponseMessage();

                Log.i(TAG, "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " " + mServerFilePath;

                            //messageText.setText(msg);
                            Toast.makeText(UploadFileActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadFileActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e(TAG, "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadFileActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}