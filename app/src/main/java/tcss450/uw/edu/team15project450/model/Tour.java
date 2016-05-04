package tcss450.uw.edu.team15project450.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
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

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns course list if success.
     * @param tourJSON
     * @return reason or null if successful.
     */
//    public static String parseCourseJSON(String tourJSON, List<Tour> tourList) {
//        String reason = null;
//        if (tourJSON != null) {
//            try {
//                JSONArray arr = new JSONArray(tourJSON);
//
//                for (int i = 0; i < arr.length(); i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    Tour course = new Tour(obj.getString(Tour.ID), obj.getString(Course.SHORT_DESC)
//                            , obj.getString(Course.LONG_DESC), obj.getString(Course.PRE_REQS));
//                    tourList.add(course);
//                }
//            } catch (JSONException e) {
//                reason =  "Unable to parse data, Reason: " + e.getMessage();
//            }
//        }
//        return reason;
//    }
}