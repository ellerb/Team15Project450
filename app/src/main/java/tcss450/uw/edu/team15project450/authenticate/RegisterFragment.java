package tcss450.uw.edu.team15project450.authenticate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import tcss450.uw.edu.team15project450.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private RegisterUserListener mListener;
    private final static String REGISTER_USER_URL =
            "http://cssgate.insttech.washington.edu/~_450atm15/addUser.php?";

    private EditText mUserIdText;
    private EditText mPwdText;
    private EditText mNameText;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterUserListener {
        void registerUser(String url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        Button activityRegButton = (Button) getActivity().findViewById(R.id.register_button);
        activityRegButton.setVisibility(View.GONE);

        mUserIdText = (EditText) v.findViewById(R.id.reg_username);
        mPwdText = (EditText) v.findViewById(R.id.reg_pwd);
        mNameText = (EditText) v.findViewById(R.id.reg_name);
        Button registerButton = (Button) v.findViewById(R.id.reg_add_user_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserIdText.getText().toString();
                String pwd = mPwdText.getText().toString();
                String name = mNameText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(v.getContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    mNameText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(userId)) {
                    Toast.makeText(v.getContext(), "Enter username", Toast.LENGTH_SHORT).show();
                    mUserIdText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    mPwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters", Toast.LENGTH_SHORT)
                            .show();
                    mPwdText.requestFocus();
                    return;
                }

                String url = buildRegisterURL(v);
                mListener.registerUser(url);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterUserListener) {
            mListener = (RegisterUserListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement RegisterUserListener");
        }
    }

    // method that will build the url for calling the AsyncTask.
    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(REGISTER_USER_URL);

        try {

            String userid = mUserIdText.getText().toString();
            sb.append("userid=");
            sb.append(userid);

            String pwd = mPwdText.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            String name = mNameText.getText().toString();
            sb.append("&name=");
            sb.append(URLEncoder.encode(name, "UTF-8"));

            Log.i("RegisterFragment", sb.toString());

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

}
