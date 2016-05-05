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
 * A Fragment that allows the user to login.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class LoginFragment extends Fragment {

    private final static String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm15/login.php?";

    private LoginInteractionListener mListener;

    private EditText mUserIdText;
    private EditText mPwdText;

    /**
     * The interface to be implemented by activities containing this fragment,
     */
    public interface LoginInteractionListener {
        void login(String url, String userId, String pwd);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mUserIdText = (EditText) v.findViewById(R.id.username);
        mPwdText = (EditText) v.findViewById(R.id.pwd);
        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserIdText.getText().toString();
                String pwd = mPwdText.getText().toString();
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
                mListener.login(url, userId, pwd);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginInteractionListener");
        }
    }

    private String buildRegisterURL(View v) {

        StringBuilder sb = new StringBuilder(LOGIN_URL);

        try {

            String userid = mUserIdText.getText().toString();
            sb.append("userid=");
            sb.append(userid);

            String pwd = mPwdText.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            Log.i("LoginFragment", sb.toString());

        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }
}