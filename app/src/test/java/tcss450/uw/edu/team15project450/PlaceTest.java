package tcss450.uw.edu.team15project450;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import tcss450.uw.edu.team15project450.model.Place;

/**
 * Created by Gabe on 6/1/2016.
 */
public class PlaceTest {

    private Place mPlace1;
    private Place mPlace2;

    @Before
    public void setUp() {
        mPlace1 = new Place(22, "PLACE TITLE", "DESCRIPTION"
                , "INSTRUCTION", 85, -122, "/AUDIO", "TOUR TITLE"
                , "CREATED BY", "6/1/2016", "6/1/2016");
        mPlace2 = new Place(22, "PLACE TITLE", "DESCRIPTION"
                , "INSTRUCTION", 85, -122, "/AUDIO", "TOUR TITLE"
                , "CREATED BY", "6/1/2016", "6/1/2016");
    }

    @Test
    public void testSetNegativePlaceId() {
        try {
            mPlace1.setID(-2);
            fail("Place Id can be negative.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetPlaceId() {
        assertEquals(22, mPlace2.getPlaceID());
    }

    @Test
    public void testSetNullPlaceTitle() {
        try {
            mPlace1.setTitle(null);
            fail("Place title can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetTitle() {
        assertEquals("PLACE TITLE", mPlace2.getTitle());
    }

    @Test
    public void testGetDescription() {
        assertEquals("DESCRIPTION", mPlace2.getDescription());
    }

    @Test
    public void testGetInstruction() {
        assertEquals("INSTRUCTION", mPlace2.getInstruction());
    }

    @Test
    public void testSetLatitudeOutsideRange() {
        try {
            mPlace1.setLatitude(121);
            fail("Latitude can be set outside range.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetLatitude() {
        assertEquals(85, mPlace2.getLatitude(), .00000000001);
    }

    @Test
    public void testSetLongitudeOutsideRange() {
        try {
            mPlace1.setLongitude(200);
            fail("Longitude can be set outside range.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetLongitude() {
        assertEquals(-122, mPlace2.getLongitude(), .00000000001);
    }

    @Test
    public void testGetAudioFilePath() {
        assertEquals("/AUDIO", mPlace2.getAudioFilePath());
    }

    @Test
    public void testSetNullTourTitle() {
        try {
            mPlace1.setTourTitle(null);
            fail("Tour title can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetTourTitle() {
        assertEquals("TOUR TITLE", mPlace1.getTourTitle());
    }

    @Test
    public void testSetNullCreatedBy() {
        try {
            mPlace1.setTourCreatedBy(null);
            fail("TourCreatedBy can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetTourCreatedBy() {
        assertEquals("CREATED BY", mPlace1.getTourCreatedBy());
    }

    @Test
    public void testSetNullDateCreated() {
        try {
            mPlace1.setDateCreated(null);
            fail("DateCreated can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDateCreated() {
        assertEquals("6/1/2016", mPlace1.getDateCreated());
    }

    @Test
    public void testSetNullDateModified() {
        try {
            mPlace1.setDateModified(null);
            fail("DateModified can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDateModified() {
        assertEquals("6/1/2016", mPlace1.getDateModified());
    }

    @Test
    public void testParsePlaceJSON() {
        String courseJSON = "[{\"placeID\": 22,\"title\":\"PLACE TITLE\",\"description\":" +
                "\"DESCRIPTION\",\"instruction\":\"INSTRUCTION\",\"latitude\":89,\"longitude\":" +
                "120,\"audio\":\"AUDIO\",\"tourTitle\":\"TOUR TITLE\", \"tourCreatedBy\": " +
                "\"USER\",\"dateCreated\":\"6/1/2016\",\"dateModified\":\"6/1/2016\"}," +
                "{\"placeID\": 22,\"title\":\"PLACE TITLE\",\"description\":\"DESCRIPTION\"," +
                "\"instruction\":\"INSTRUCTION\",\"latitude\":89,\"longitude\":120,\"audio\":" +
                "\"AUDIO\",\"tourTitle\":\"TOUR TITLE\", \"tourCreatedBy\": \"USER\"," +
                "\"dateCreated\":\"6/1/2016\",\"dateModified\":\"6/1/2016\"}]";
        String message =  Place.parsePlaceJSON(courseJSON
                , new ArrayList<Place>());
        assertTrue("JSON With Valid String", message == null);
    }

}
