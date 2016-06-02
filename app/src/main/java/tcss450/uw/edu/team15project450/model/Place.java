package tcss450.uw.edu.team15project450.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This model class will be used to create a Place object. Audio and image not
 * yet implemented.
 *
 * Created by Gabe on 4/28/2016.
 */
public class Place implements Serializable {
    private int mPlaceID;
    private String mTitle;
    private String mDescription;
    private String mInstruction;
    private double mLatitude;
    private double mLongitude;
    private String mAudioFilePath;
    //private Image mImage;
    private String mTourTitle;
    private String mTourCreatedBy;
    private String mDateCreated;
    private String mDateModified;

    public static final String ID = "placeID",
            TITLE = "title", DESC = "description", INSTRUCT = "instruction",
            LAT = "latitude", LONG = "longitude", AUDIO = "audio", TOUR = "tourTitle",
            CREATED_BY = "tourCreatedBy", DATE_CREATED = "dateCreated",
            DATE_MOD = "dateModified";

    public Place(int id, String title, String description
            , String instruction, double latitude, double longitude
            , String audioFilePath, String tourTitle, String tourCreatedBy
            , String dateCreated, String dateModified) {
        mPlaceID = id;
        setTitle(title);
        mDescription = description;
        mInstruction = instruction;
        setLatitude(latitude);
        setLongitude(longitude);
        mAudioFilePath = audioFilePath;
        setTourTitle(tourTitle);
        setTourCreatedBy(tourCreatedBy);
        setDateCreated(dateCreated);
        setDateModified(dateModified);
    }

    public int getPlaceID() { return mPlaceID; }

    public void setID(int id) {
        mPlaceID = id;
    }

    public String getTitle() { return mTitle; }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException("Title must be supplied.");
        mTitle = title;
    }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) { mDescription = description; }

    public String getInstruction() { return mInstruction; }

    public void setInstruction(String instruction) { mInstruction = instruction; }

    public double getLatitude() { return mLatitude; }

    public void setLatitude(double latitude) {
        if (latitude > 90 || latitude < -90)
            throw new IllegalArgumentException("Latitude is invalid.");
        mLatitude = latitude;
    }

    public double getLongitude() { return mLongitude; }

    public void setLongitude(double longitude) {
        if (longitude > 180 || longitude < -180)
            throw new IllegalArgumentException("Longitude is invalid.");
        mLongitude = longitude;
    }

    public String getAudioFilePath() { return mAudioFilePath; }

    public void setAudioFilePath(String audioFilePath) {
        mAudioFilePath = audioFilePath;
    }

    public String getTourTitle() { return mTourTitle; }

    public void setTourTitle(String tourTitle) {
        if (tourTitle == null)
            throw new IllegalArgumentException("Tour title must be supplied");
        mTourTitle = tourTitle;
    }

    public String getTourCreatedBy() { return mTourCreatedBy; }

    public void setTourCreatedBy(String createdBy) {
        if (createdBy == null)
            throw new IllegalArgumentException("Creator must be supplied");
        mTourCreatedBy = createdBy;
    }

    public String getDateCreated() { return mDateCreated; }

    public void setDateCreated(String dateCreated) {
        if (dateCreated == null)
            throw new IllegalArgumentException("Date created must be supplied");
        mDateCreated = dateCreated;
    }

    public String getDateModified() { return mDateModified; }

    public void setDateModified(String dateModified) {
        if (dateModified == null)
            throw new IllegalArgumentException("Date modified must be supplied");
        mDateModified = dateModified;
    }

    @Override
    public String toString() {
        return "Title: " + mTitle + " Description: " + mDescription
                + " Instruction: " + mInstruction + " Latitude: " + mLatitude
                + " Longitude: " + mLongitude + "Audio file path: " + mAudioFilePath
                + " Tour: " + mTourTitle + " Created By: " + mTourCreatedBy
                + " Date Created: " + mDateCreated + " Date Modified: " + mDateModified;
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns place array list if success.
     * @param placeJSON
     * @param placeList
     * @return reason or null if successful.
     */
    public static String parsePlaceJSON(String placeJSON, ArrayList<Place> placeList) {
        String reason = null;
    if (placeJSON != null) {
        try {
            JSONArray arr = new JSONArray(placeJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Place place = new Place(obj.getInt(Place.ID), obj.getString(Place.TITLE),
                        obj.getString(Place.DESC), obj.getString(Place.INSTRUCT),
                        obj.getDouble(Place.LAT), obj.getDouble(Place.LONG), obj.getString(Place.AUDIO),
                        obj.getString(Place.TOUR), obj.getString(Place.CREATED_BY),
                        obj.getString(Place.DATE_CREATED), obj.getString(Place.DATE_MOD));
                placeList.add(place);
            }
        } catch (JSONException e) {
            reason =  "Unable to parse data, Reason: " + e.getMessage();
        }
    }
    return reason;
    }
}
