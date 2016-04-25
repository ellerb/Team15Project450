package tcss450.uw.edu.team15project450;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class CreateTourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
    }

    /** Description of createNewTour()
     *
     * @param button            description of button
     */
    public void createNewTour(View button) {

        final EditText titleField = (EditText) findViewById(R.id.AddTourTitle);
        String title = titleField.getText().toString();

        final EditText descriptionField = (EditText) findViewById(R.id.AddTourDescription);
        String description = descriptionField.getText().toString();

        final CheckBox audioCheckbox = (CheckBox) findViewById(R.id.AddAudioDescriptionCheckBoxResponse);
        boolean bAddAudioDescription = audioCheckbox.isChecked();

        final CheckBox imageCheckbox = (CheckBox) findViewById(R.id.AddImageCheckBoxResponse);
        boolean bAddCoverImage = imageCheckbox.isChecked();

        final CheckBox publicCheckbox = (CheckBox) findViewById(R.id.PublicCheckBoxResponse);
        boolean bIsPublic = publicCheckbox.isChecked();

    }
}
