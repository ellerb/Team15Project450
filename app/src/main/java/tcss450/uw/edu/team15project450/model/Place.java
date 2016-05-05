package tcss450.uw.edu.team15project450.model;

import java.io.Serializable;

/**
 * This model class will be used to create a Place object. Audio and image not
 * yet implemented.
 *
 * Created by Gabe on 4/28/2016.
 */
public class Place implements Serializable {
    private String mTitle;
    private String mDescription;
    private String mInstruction;
    private int mLatitude;
    private int mLongitude;
    //private Audio mAudio;
    //private Image mImage;
    private int mTourID;

    public Place(String title, String description
            , String instruction) {
        setTitle(title);
        setDescription(description);
        mInstruction = instruction;
    }

    public String getTitle() { return mTitle; }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException("Title must be supplied");
        mTitle = title;
    }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("Tour description must be supplied.");
        if (description.length() < 25 )
            throw new IllegalArgumentException("Tour description must be at least 25 characters long.");
        mDescription = description;
    }

    public String getInstruction() { return mInstruction; }

    public void setInstruction(String instruction) {
        mInstruction = instruction;
    }

    @Override
    public String toString() {
        return "Title: " + mTitle + " Description: " + mDescription
                + "Instruction: " + mInstruction;
    }
}
