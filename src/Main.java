import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Main {
    public static final String[] FILENAMES = {"a.txt", "b.txt", "c.txt", "d.txt", "e.txt"};

    ArrayList<Photo> photos;
    ArrayList<ArrayList<Photo>> photoArray;

    public void readFile(String filename) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("datasets/" + filename));
            int photoCount = Integer.valueOf(bufferedReader.readLine());

            photos = new ArrayList<>();
            int id = 0;

            for (int i = 0; i < photoCount; i++) {
                String line = bufferedReader.readLine();
                String[] words = line.split(" ");
                char orientation = words[0].charAt(0);
                int tagCount = Integer.parseInt(words[1]);
                ArrayList<String> tags = new ArrayList<>();
                for (int j = 0; j < tagCount; j++) {
                    tags.add(words[2 + j]);
                }
                photos.add(new Photo(id++, orientation, tags));
            }
            System.out.println("Photos count: " + photos.size());
        } catch (FileNotFoundException e) {
            System.out.println("Could not open the file!");
        } catch (IOException e) {
            System.out.println("Error de la ostia");
            e.printStackTrace();
        }
    }

    public void doTheJob(String filename) {
        Collections.sort(photos, (o1, o2) -> Integer.compare(o2.tags.size(), o1.tags.size()));
        photoArray = new ArrayList<>();

        for (int i = 0; i < photos.size(); i++) {
            ArrayList<Photo> photosTmp = new ArrayList<>();
            Photo p1 = photos.get(i);
            photosTmp.add(p1);
            if (p1.orientation == 'V') {
                int diferenceBetween = 0;
                Photo best = null;
                for (int j = i + 1; j < photos.size(); j++) {
                    Photo p2 = photos.get(j);
                    if (p2.orientation == 'V') {
                        int tmp = tagNumber(p1, p2);
                        if (tmp > diferenceBetween) {
                            diferenceBetween = tmp;
                            best = p2;
                        }
                    }
                }
                if (best != null) {
                    photosTmp.add(best);
                    photos.remove(best);
                }
            }
            photos.remove(p1);
            i--;
            photoArray.add(photosTmp);
        }

    }

    public int tagNumber(Photo p1, Photo p2) {
        HashSet<String> tags = new HashSet<>();

        tags.addAll(p1.tags);
        tags.addAll(p2.tags);

        return tags.size();
    }

    public void writeFile(String filename) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("output/" + filename));
            bufferedWriter.write(photoArray.size() + "\r");

            for (ArrayList<Photo> photos : photoArray) {
                if (photos.size() == 1) {
                    bufferedWriter.write(photos.get(0).id + "\r");
                } else {
                    bufferedWriter.write(photos.get(0).id + " " + photos.get(1).id + "\r");
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        for (String filename:FILENAMES) {
            System.out.println("Working on " + filename);
            main.readFile(filename);
            main.doTheJob(filename);
            main.writeFile(filename);
        }
    }
}
