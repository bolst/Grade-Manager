package src.front;

import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.ActionEvent;

import src.Course;

public class Controller {

    private static Controller instance = null;
    public ArrayList<Course> courses;

    // TODO: allow user to save/upload files with a dialog
    private static final String json_ID = "dat.json";

    private Controller() {
        courses = new ArrayList<Course>();
        for (int i = 0; i < 5; i++)
            courses.add(null);
    }

    public static Controller instance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    public void init() {
        GUI.instance();
        link();
    }

    private static void link() {

        for (int i = 0; i < GUI.instance().coursePanels.size(); i++) {

            final int index = i;

            GUI.instance().coursePanels.get(i).btn_inputSyllabus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CoursePanel coursePanel = GUI.instance().coursePanels.get(index);
                    Course course = Controller.instance().courses.get(index);
                    String course_id = coursePanel.combo_courseName.getSelectedItem().toString()
                            + coursePanel.txt_courseId.getText();
                    if (course == null)
                        Controller.instance().courses.add(index, new Course(course_id));
                    GUI.instance().syllabusWindow(Controller.instance().courses.get(index));

                }
            });

            GUI.instance().coursePanels.get(i).btn_enterGrades.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Course course = Controller.instance().courses.get(index);

                    if (course == null)
                        return;

                    GUI.instance().gradeWindow(course);

                    int mark = (int) ((course.currentMark() * 100) / 100.0);
                    GUI.instance().coursePanels.get(index).ezl_currentGrade.setValue(String.valueOf(mark));
                }
            });
        }

        GUI.instance().btn_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JSONParser prs = new JSONParser();

                try (Reader rdr = new FileReader(json_ID)) {

                    JSONArray data = (JSONArray) prs.parse(rdr);
                    Iterator<JSONObject> it = data.iterator();
                    int i = 0;
                    while (it.hasNext() && i < 5) {
                        Controller.instance().courses.add(i++, new Course(it.next()));
                    }

                    Controller.instance().updateGUI(Controller.instance().courses);

                } catch (IOException exc) {
                    GUI.instance().showMessage(exc.toString());
                } catch (ParseException exc) {
                    GUI.instance().showMessage(exc.toString());

                }
            }
        });

        GUI.instance().btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JSONArray json = new JSONArray();
                for (Course c : Controller.instance().courses) {
                    if (c != null) {
                        json.add(c.json());
                    }
                }

                try (FileWriter file = new FileWriter(json_ID)) {
                    file.write(json.toJSONString());
                } catch (IOException exc) {
                    System.out.println("Error writing to file: " + exc.toString());
                }
            }
        });

    }

    private void updateGUI(ArrayList<Course> courses) {
        for (int i = 0; i < 5; i++) {
            Course course = Controller.instance().courses.get(i);
            final String courseName = course.courseID().substring(0, 4);
            final String courseId = course.courseID().substring(4, 8);
            final int mark = (int) ((course.currentMark() * 100) / 100.0);

            GUI.instance().coursePanels.get(i).combo_courseName.setSelectedItem(COURSE_NAME.valueOf(courseName));
            GUI.instance().coursePanels.get(i).txt_courseId.setText(courseId);
            GUI.instance().coursePanels.get(i).ezl_currentGrade.setValue(String.valueOf(mark));
        }
    }
}
