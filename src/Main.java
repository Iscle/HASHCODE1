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

    public void doTheJob2(String filename) {
        ArrayList<Photo> verticalPhotos = new ArrayList<>();
        photoArray = new ArrayList<>();

        for (Photo p:photos) {
            if (p.orientation == 'H') {
                // Add it to the slides array directly
                ArrayList<Photo> a = new ArrayList<>();
                a.add(p);
                photoArray.add(a);
            } else {
                // Add it to the vertical photos array to treat them later
                verticalPhotos.add(p);
            }
        }

        Collections.sort(verticalPhotos, (o1, o2) -> Integer.compare(o2.tags.size(), o1.tags.size()));

        for (int i = 0; i < verticalPhotos.size(); i++) {
            ArrayList<Photo> photosTmp = new ArrayList<>();
            Photo p1 = verticalPhotos.get(i);
            photosTmp.add(p1);

            if (i + 1 != verticalPhotos.size()) {
                Photo p2 = verticalPhotos.get(i + 1);
                photosTmp.add(p2);
                verticalPhotos.remove(p2);
                photoArray.add(photosTmp);
            }

            verticalPhotos.remove(p1);
            i--;
        }

        Collections.sort(photoArray, (o1, o2) -> {
            int c1 = 0;
            int c2 = 0;

            if (o1.size() == 1) {
                c1 = o1.get(0).tags.size();
            } else {
                c1 = o1.get(0).tags.size() + o1.get(1).tags.size();
            }

            if (o2.size() == 1) {
                c2 = o2.get(0).tags.size();
            } else {
                c2 = o2.get(0).tags.size() + o2.get(1).tags.size();
            }

            return Integer.compare(c2, c1);
        });
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
            //main.doTheJob(filename);
            main.doTheJob2(filename);
            main.writeFile(filename);
        }
    }
}
