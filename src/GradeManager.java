package src;

import java.util.ArrayList;

public class GradeManager {

    public static ArrayList<Course> _courses;

    public static void main(String[] args) {
        _courses = new ArrayList<Course>();
        for (int i = 0; i < 5; i++)
            _courses.add(null);
        GUI gui = new GUI();
    }
}
