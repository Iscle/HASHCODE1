import java.util.ArrayList;

public class Photo {
    public int id;
    public char orientation;
    public ArrayList<String> tags;

    public Photo(int id, char orientation, ArrayList<String> tags) {
        this.id = id;
        this.orientation = orientation;
        this.tags = tags;
    }
}
