package tcss450.uw.edu.team15project450.model;

import java.io.Serializable;

/**
 * Created by Gabe on 4/28/2016.
 */
public class Place implements Serializable {
    private String mTitle;
    private String mDescription;
    private String mInstruction;
    //private mLocation;
    //private Audio mAudio;
    //private Image mImage;

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
        if (description.length() < 50 )
            throw new IllegalArgumentException("Tour description must be at least 50 characters long.");
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
