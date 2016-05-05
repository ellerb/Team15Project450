package tcss450.uw.edu.team15project450.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This model class will be used to create a Tour object. Audio and image not
 * yet implemented.
 *
 * Created by Gabe on 4/28/2016.
 */
public class Tour implements Serializable {
    private int mID;
    private String mTitle;
    private String mDescription;
    //private Audio mTourAudioDescription;
    //private Image mTourImage;
    private ArrayList<Place> mPlaceList;
    private boolean mIsPublic;
    private boolean mIsPublished;
    private String mUserID;

    public Tour(int ID, String title, String description
            , ArrayList<Place> placeList, boolean isPublic
            , boolean isPublished, String userid) {
        mID = ID;
        setTitle(title);
        setDescription(description);
        mPlaceList = placeList;
        mIsPublic = isPublic;
        mIsPublished = isPublished;
        mUserID = userid;
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
        if (description.length() < 50 )
            throw new IllegalArgumentException("Tour description must be at least 50 characters long.");
        mDescription = description;
    }

    public ArrayList<Place> getPlaceList() { return mPlaceList; }

    public void setPlaceList(ArrayList<Place> tourPlaceList) {
        mPlaceList = tourPlaceList;
    }

    public boolean getIsPublic() { return mIsPublic; }

    public void setIsPublic(boolean isPublic) { mIsPublic = isPublic; }

    public boolean getIsPublished() { return mIsPublished; }

    public void setIsPublished(boolean isPublished) { mIsPublished = isPublished; }

    public int getID() { return mID; }

    public void setID(int ID) { mID = ID; }

    public String getUserID() { return mUserID; }

    public void setUserID(String userID) { mUserID = userID; }

    @Override
    public String toString() {
        return "Title: " + mTitle + " Description: " + mDescription;
    }

}