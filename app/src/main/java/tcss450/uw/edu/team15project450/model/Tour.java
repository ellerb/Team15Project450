package tcss450.uw.edu.team15project450.model;

public class Tour {
    private String mTitle;
    private String mLocation;

    public Tour(String title, String location) {
        setTitle(title);
        setLocation(location);
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title must be supplied.");
        }
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setLocation(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Location must be supplied.");
        }
        mLocation = location;
    }

    @Override
    public String toString() {
        return "Title: " + mTitle + " Location: " + mLocation;
    }

}
