package tcss450.uw.edu.team15project450.authenticate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tcss450.uw.edu.team15project450.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterInteractionListener {
        public void register(String username, String pwd, String name);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_register, container, false);
        final EditText userIdText = (EditText) v.findViewById(R.id.reg_username);
        final EditText pwdText = (EditText) v.findViewById(R.id.reg_pwd);
        final EditText nameText = (EditText) v.findViewById(R.id.reg_name);
        Button registerButton = (Button) v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userIdText.getText().toString();
                String pwd = pwdText.getText().toString();
                String name = nameText.getText().toString();
                if (TextUtils.isEmpty(name))  {
                    Toast.makeText(v.getContext(), "Enter name"
                            , Toast.LENGTH_SHORT)
                            .show();
                    nameText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(userId))  {
                    Toast.makeText(v.getContext(), "Enter username"
                            , Toast.LENGTH_SHORT)
                            .show();
                    userIdText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd))  {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }

                ((SignInActivity) getActivity()).register(userId, pwd, name);
            }
        });

        return v;
    }


}
