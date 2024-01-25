package src.front;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.ActionEvent;

import src.Course;
import src.util.Pair;

public class Controller {

    private static Controller instance = null;
    public ArrayList<Course> courses;

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
                Controller.instance().promptLoad();
            }
        });

        GUI.instance().btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Controller.instance().promptSave();
            }
        });

    }

    private void promptSave() {
        JSONArray json = new JSONArray();
        for (Course c : Controller.instance().courses) {
            if (c != null) {
                json.add(c.json());
            }
        }

        Pair<Integer, File> saveInstance = GUI.instance().saveWindow();

        if (saveInstance.first() == JFileChooser.APPROVE_OPTION) {
            File file = saveInstance.second();

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(json.toJSONString());
            } catch (IOException exc) {
                GUI.instance().showMessage("Error writing to file");
                System.out.println(exc.toString());
            }
        }
    }

    private void promptLoad() {
        Pair<Integer, File> loadInstance = GUI.instance().loadWindow();

        if (loadInstance.first() == JFileChooser.APPROVE_OPTION) {
            JSONParser prs = new JSONParser();
            File file = loadInstance.second();

            try (Reader rdr = new FileReader(file)) {

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
    }

    private void updateGUI(ArrayList<Course> courses) {
        for (int i = 0; i < 5; i++) {
            Course course = Controller.instance().courses.get(i);
            if (course == null)
                continue;
            final String courseName = course.courseID().substring(0, 4);
            final String courseId = course.courseID().substring(4, 8);
            final int mark = (int) ((course.currentMark() * 100) / 100.0);

            GUI.instance().coursePanels.get(i).combo_courseName.setSelectedItem(COURSE_NAME.valueOf(courseName));
            GUI.instance().coursePanels.get(i).txt_courseId.setText(courseId);
            GUI.instance().coursePanels.get(i).ezl_currentGrade.setValue(String.valueOf(mark));
        }
    }
}
