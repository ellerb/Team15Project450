package tcss450.uw.edu.team15project450;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import tcss450.uw.edu.team15project450.model.Tour;

/**
 * Created by Gabe on 6/1/2016.
 */
public class TourTest {

    private Tour mTour1;
    private Tour mTour2;

    @Before
    public void setUp() {
        mTour1 = new Tour("TITLE", "DESCRIPTION THAT IS AT LEAST 25 CHARACTERS LONG", "/AUDIO", true
                , true, "CREATED BY", "6/1/2016", "6/1/2016");
        mTour2 = new Tour("TITLE", "DESCRIPTION THAT IS AT LEAST 25 CHARACTERS LONG", "/AUDIO", true
                , true, "CREATED BY", "6/1/2016", "6/1/2016");
    }

    @Test
    public void testSetNullTitle() {
        try {
            mTour1.setTitle(null);
            fail("Title can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetTitle() {
        assertEquals("TITLE", mTour2.getTitle());
    }

    @Test
    public void testSetNullDescription() {
        try {
            mTour1.setDescription(null);
            fail("Description can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetLessThan25CharDescription() {
        try {
            mTour1.setDescription("LESSTHAN25");
            fail("Description can be less than 25 characters.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDescription() {
        assertEquals("DESCRIPTION THAT IS AT LEAST 25 CHARACTERS LONG"
                , mTour2.getDescription());
    }

    @Test
    public void testGetAudioFilePath() {
        assertEquals("/AUDIO", mTour2.getAudioFilePath());
    }

    @Test
    public void testSetNullCreatedBy() {
        try {
            mTour1.setCreatedBy(null);
            fail("CreatedBy can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetTourCreatedBy() {
        assertEquals("CREATED BY", mTour2.getCreatedBy());
    }

    @Test
    public void testSetNullDateCreated() {
        try {
            mTour1.setDateCreated(null);
            fail("DateCreated can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDateCreated() {
        assertEquals("6/1/2016", mTour2.getDateCreated());
    }

    @Test
    public void testSetNullDateModified() {
        try {
            mTour1.setDateModified(null);
            fail("DateModified can be null.");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testGetDateModified() {
        assertEquals("6/1/2016", mTour2.getDateModified());
    }
}
