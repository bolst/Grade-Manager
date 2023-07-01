package src;

import net.miginfocom.swing.MigLayout;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GUI {
    public JFrame frame;
    private JTextField txt_courseid1, txt_courseid2, txt_courseid3, txt_courseid4, txt_courseid5;
    private JComboBox<COURSE_NAMES> combo_coursename1, combo_coursename2, combo_coursename3, combo_coursename4,
            combo_coursename5;
    private EzLabel lbl_avg1, lbl_avg2, lbl_avg3, lbl_avg4, lbl_avg5;
    private final String json_ID = "dat.json";

    enum COURSE_NAMES {
        BIOL, CHEM, COMP, ELEC, GART, GENG, KINE, MATH, MECH, PHYS, STAT
    }

    public GUI() {
        this.initialize();
        this.frame.setVisible(true);
    }

    public void syllabusWindow(Course course) {
        // Create a JTextArea for input
        JTextArea textArea = new JTextArea(10, 30);
        if (!course.courseComponents().isEmpty()) {
            textArea.setText(course.courseComponents());
        }

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Create a Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // clear any previous components
                course.clearComponents();

                // Perform save operation here
                String[] lines = textArea.getText().split("\n");

                for (String l : lines) {
                    l.replace(" ", "");
                    String[] s = l.split(",");
                    String comp_id = s[0];
                    double weight = Double.parseDouble(s[1]);
                    course.addComponent(new CourseComponent(comp_id, weight));
                }
            }
        });

        // Add the Save button to the bottom right of the panel
        panel.add(saveButton, BorderLayout.SOUTH);

        // Show the JOptionPane with the panel
        JOptionPane.showMessageDialog(null, panel, "Input Window", JOptionPane.PLAIN_MESSAGE);
    }

    public void gradeWindow(Course course) {

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        ArrayList<String> label_texts = new ArrayList<String>();
        for (CourseComponent comp : course._gradeDist)
            label_texts.add(comp.name());

        JTextField[] textFields = new JTextField[label_texts.size()];

        for (int i = 0; i < label_texts.size(); i++) {
            JLabel label = new JLabel(label_texts.get(i));
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(label, gbc);

            JTextField textField = new JTextField(20);

            // fill textfield with previous inputs if they exist
            if (!course._gradeDist.get(i).evals().isEmpty()) {
                textField.setText(course._gradeDist.get(i).evals());
            }

            gbc.gridx = 1;
            panel.add(textField, gbc);

            textFields[i] = textField;
        }

        // Create a Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < textFields.length; i++) {

                    // current course component name
                    String comp_id = label_texts.get(i);

                    // clearing previous entries
                    course.clearGrades(comp_id);

                    // remove whitespace in input area
                    textFields[i].setText(textFields[i].getText().replace(" ", ""));

                    // get inputted grades
                    String[] grades = textFields[i].getText().split(",");

                    // add grades to respective course component
                    for (String ig : grades) {
                        course.addGrade(comp_id, Double.parseDouble(ig));
                    }
                }
            }
        });

        // add save button
        gbc.gridx = 0;
        gbc.gridy = label_texts.size();
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5); // Add some extra padding for the button
        panel.add(saveButton, gbc);

        // Show the JOptionPane with the panel
        JOptionPane.showMessageDialog(null, panel, "Input Window", JOptionPane.PLAIN_MESSAGE);
    }

    private void initialize() {
        frame = new JFrame("Grade Manager");
        frame.setAlwaysOnTop(false);
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(
                new MigLayout("", "[][][][][][grow]", "[][][][grow][][][grow][][][grow][][][grow][][][grow]"));

        JLabel lbl_1 = new JLabel("1.");
        frame.getContentPane().add(lbl_1, "cell 0 3,alignx trailing");

        combo_coursename1 = new JComboBox<COURSE_NAMES>();
        combo_coursename1.setModel(new DefaultComboBoxModel<COURSE_NAMES>(COURSE_NAMES.values()));
        frame.getContentPane().add(combo_coursename1, "cell 1 3");

        txt_courseid1 = new JTextField();
        frame.getContentPane().add(txt_courseid1, "cell 2 3");
        txt_courseid1.setColumns(7);

        JButton btn_syl1 = new JButton("INPUT SYLLABUS DETAIL");
        frame.getContentPane().add(btn_syl1, "cell 3 3");

        JButton btn_enterGrade1 = new JButton("Enter Grades");
        frame.getContentPane().add(btn_enterGrade1, "cell 4 3");

        lbl_avg1 = new EzLabel("Current grade:", "", "%");
        frame.getContentPane().add(lbl_avg1, "cell 5 3");

        JLabel lbl_2 = new JLabel("2.");
        frame.getContentPane().add(lbl_2, "cell 0 6,alignx trailing");

        combo_coursename2 = new JComboBox<COURSE_NAMES>();
        combo_coursename2.setModel(new DefaultComboBoxModel<COURSE_NAMES>(COURSE_NAMES.values()));
        frame.getContentPane().add(combo_coursename2, "cell 1 6");

        txt_courseid2 = new JTextField();
        txt_courseid2.setColumns(7);
        frame.getContentPane().add(txt_courseid2, "cell 2 6");

        JButton btn_syl2 = new JButton("INPUT SYLLABUS DETAIL");
        frame.getContentPane().add(btn_syl2, "cell 3 6");

        JButton btn_enterGrade2 = new JButton("Enter Grades");
        frame.getContentPane().add(btn_enterGrade2, "cell 4 6");

        lbl_avg2 = new EzLabel("Current grade:", "", "%");
        frame.getContentPane().add(lbl_avg2, "cell 5 6");

        JLabel lbl_3 = new JLabel("3.");
        frame.getContentPane().add(lbl_3, "cell 0 9,alignx trailing");

        combo_coursename3 = new JComboBox<COURSE_NAMES>();
        combo_coursename3.setModel(new DefaultComboBoxModel<COURSE_NAMES>(COURSE_NAMES.values()));
        frame.getContentPane().add(combo_coursename3, "cell 1 9");

        txt_courseid3 = new JTextField();
        txt_courseid3.setColumns(7);
        frame.getContentPane().add(txt_courseid3, "cell 2 9");

        JButton btn_syl3 = new JButton("INPUT SYLLABUS DETAIL");
        frame.getContentPane().add(btn_syl3, "cell 3 9");

        JButton btn_enterGrade3 = new JButton("Enter Grades");
        frame.getContentPane().add(btn_enterGrade3, "cell 4 9");

        lbl_avg3 = new EzLabel("Current grade:", "", "%");
        frame.getContentPane().add(lbl_avg3, "cell 5 9");

        JLabel lbl_4 = new JLabel("4.");
        frame.getContentPane().add(lbl_4, "cell 0 12,alignx trailing");

        combo_coursename4 = new JComboBox<COURSE_NAMES>();
        combo_coursename4.setModel(new DefaultComboBoxModel<COURSE_NAMES>(COURSE_NAMES.values()));
        frame.getContentPane().add(combo_coursename4, "cell 1 12");

        txt_courseid4 = new JTextField();
        txt_courseid4.setColumns(7);
        frame.getContentPane().add(txt_courseid4, "cell 2 12");

        JButton btn_syl4 = new JButton("INPUT SYLLABUS DETAIL");
        frame.getContentPane().add(btn_syl4, "cell 3 12");

        JButton btn_enterGrade4 = new JButton("Enter Grades");
        frame.getContentPane().add(btn_enterGrade4, "cell 4 12");

        lbl_avg4 = new EzLabel("Current grade:", "", "%");
        frame.getContentPane().add(lbl_avg4, "cell 5 12");

        JLabel lbl_5 = new JLabel("5.");
        frame.getContentPane().add(lbl_5, "cell 0 15,alignx trailing");

        combo_coursename5 = new JComboBox<COURSE_NAMES>();
        combo_coursename5.setModel(new DefaultComboBoxModel<COURSE_NAMES>(COURSE_NAMES.values()));
        frame.getContentPane().add(combo_coursename5, "cell 1 15");

        txt_courseid5 = new JTextField();
        txt_courseid5.setColumns(7);
        frame.getContentPane().add(txt_courseid5, "cell 2 15");

        JButton btn_syl5 = new JButton("INPUT SYLLABUS DETAIL");
        frame.getContentPane().add(btn_syl5, "cell 3 15");

        JButton btn_enterGrade5 = new JButton("Enter Grades");
        frame.getContentPane().add(btn_enterGrade5, "cell 4 15");

        lbl_avg5 = new EzLabel("Current grade:", "", "%");
        frame.getContentPane().add(lbl_avg5, "cell 5 15");

        JButton btn_save = new JButton("SAVE");
        frame.getContentPane().add(btn_save, "cell 2 2");

        JButton btn_load = new JButton("LOAD");
        frame.getContentPane().add(btn_load, "cell 1 2");

        btn_syl1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_id = combo_coursename1.getSelectedItem().toString() + txt_courseid1.getText();
                if (GradeManager._courses.get(0) == null)
                    GradeManager._courses.add(0, new Course(course_id));
                else
                    syllabusWindow(GradeManager._courses.get(0));
                return;
            }
        });

        btn_syl2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_id = combo_coursename2.getSelectedItem().toString() + txt_courseid2.getText();
                if (GradeManager._courses.get(1) == null)
                    GradeManager._courses.add(1, new Course(course_id));
                else
                    syllabusWindow(GradeManager._courses.get(1));
                return;
            }
        });

        btn_syl3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_id = combo_coursename3.getSelectedItem().toString() + txt_courseid3.getText();
                if (GradeManager._courses.get(2) == null)
                    GradeManager._courses.add(2, new Course(course_id));
                else
                    syllabusWindow(GradeManager._courses.get(2));
                return;
            }
        });

        btn_syl4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_id = combo_coursename4.getSelectedItem().toString() + txt_courseid4.getText();
                if (GradeManager._courses.get(3) == null)
                    GradeManager._courses.add(3, new Course(course_id));
                else
                    syllabusWindow(GradeManager._courses.get(3));
                return;
            }
        });

        btn_syl5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_id = combo_coursename5.getSelectedItem().toString() + txt_courseid5.getText();
                if (GradeManager._courses.get(4) == null)
                    GradeManager._courses.add(4, new Course(course_id));
                else
                    syllabusWindow(GradeManager._courses.get(4));
                return;
            }
        });

        btn_enterGrade1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GradeManager._courses.get(0) == null)
                    return;

                gradeWindow(GradeManager._courses.get(0));

                lbl_avg1.setValue(String.valueOf((int) (GradeManager._courses.get(0).currentMark() * 100) / 100.0));
            }
        });

        btn_enterGrade2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GradeManager._courses.get(1) == null)
                    return;

                gradeWindow(GradeManager._courses.get(1));

                lbl_avg2.setValue(String.valueOf((int) (GradeManager._courses.get(1).currentMark() * 100) / 100.0));
            }
        });

        btn_enterGrade3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GradeManager._courses.get(2) == null)
                    return;

                gradeWindow(GradeManager._courses.get(2));

                lbl_avg3.setValue(String.valueOf((int) (GradeManager._courses.get(2).currentMark() * 100) / 100.0));
            }
        });

        btn_enterGrade4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GradeManager._courses.get(3) == null)
                    return;

                gradeWindow(GradeManager._courses.get(3));

                lbl_avg4.setValue(String.valueOf((int) (GradeManager._courses.get(3).currentMark() * 100) / 100.0));
            }
        });

        btn_enterGrade5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (GradeManager._courses.get(4) == null)
                    return;

                gradeWindow(GradeManager._courses.get(4));

                lbl_avg5.setValue(String.valueOf((int) (GradeManager._courses.get(4).currentMark() * 100) / 100.0));
            }
        });

        btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JSONArray json = new JSONArray();
                for (Course c : GradeManager._courses) {
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

        btn_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JSONParser prs = new JSONParser();

                try (Reader rdr = new FileReader(json_ID)) {

                    JSONArray data = (JSONArray) prs.parse(rdr);
                    Iterator<JSONObject> it = data.iterator();
                    int i = 0;
                    while (it.hasNext() && i < 5) {
                        GradeManager._courses.add(i++, new Course(it.next()));
                    }

                    updateGUI(GradeManager._courses);

                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(frame, exc.toString());
                } catch (ParseException exc) {
                    JOptionPane.showMessageDialog(frame, exc.toString());

                }
            }

        });
    }

    private void updateGUI(ArrayList<Course> courses) {

        if (courses.get(0) != null) {
            String id1_pre = courses.get(0).courseID().substring(0, 4);
            String id1_pos = courses.get(0).courseID().substring(4, 8);
            combo_coursename1.setSelectedItem(COURSE_NAMES.valueOf(id1_pre));
            txt_courseid1.setText(id1_pos);
            lbl_avg1.setValue(String.valueOf((int) (GradeManager._courses.get(0).currentMark() * 100) / 100.0));
        }

        if (courses.get(1) != null) {
            String id2_pre = courses.get(1).courseID().substring(0, 4);
            String id2_pos = courses.get(1).courseID().substring(4, 8);
            combo_coursename2.setSelectedItem(COURSE_NAMES.valueOf(id2_pre));
            txt_courseid2.setText(id2_pos);
            lbl_avg2.setValue(String.valueOf((int) (GradeManager._courses.get(1).currentMark() * 100) / 100.0));
        }

        if (courses.get(2) != null) {
            String id3_pre = courses.get(2).courseID().substring(0, 4);
            String id3_pos = courses.get(2).courseID().substring(4, 8);
            combo_coursename3.setSelectedItem(COURSE_NAMES.valueOf(id3_pre));
            txt_courseid3.setText(id3_pos);
            lbl_avg3.setValue(String.valueOf((int) (GradeManager._courses.get(2).currentMark() * 100) / 100.0));
        }

        if (courses.get(3) != null) {
            String id4_pre = courses.get(3).courseID().substring(0, 4);
            String id4_pos = courses.get(3).courseID().substring(4, 8);
            combo_coursename4.setSelectedItem(COURSE_NAMES.valueOf(id4_pre));
            txt_courseid4.setText(id4_pos);
            lbl_avg4.setValue(String.valueOf((int) (GradeManager._courses.get(3).currentMark() * 100) / 100.0));
        }

        if (courses.get(4) != null) {
            String id5_pre = courses.get(4).courseID().substring(0, 4);
            String id5_pos = courses.get(4).courseID().substring(4, 8);
            combo_coursename5.setSelectedItem(COURSE_NAMES.valueOf(id5_pre));
            txt_courseid5.setText(id5_pos);
            lbl_avg5.setValue(String.valueOf((int) (GradeManager._courses.get(0).currentMark() * 100) / 100.0));
        }

    }
}

class EzLabel extends JPanel {
    private JLabel IDLabel;
    private JLabel valueLabel;
    private JLabel unitLabel;

    public EzLabel(String labelText, String valueText, String unitText) {
        setLayout(new MigLayout("insets 0", "[grow,fill]", "[]"));
        // Create and add the IDLabel component
        IDLabel = new JLabel(labelText);
        add(IDLabel);

        // Create and add the text field component
        valueLabel = new JLabel(valueText);
        add(valueLabel);

        // Create and add the unit IDLabel component (if provided)
        if (unitText != null) {
            unitLabel = new JLabel(unitText);
            if (!valueText.isEmpty()) {
                add(unitLabel);
            }
        }

    }

    // Getter for the text field value
    public String getValue() {
        return valueLabel.getText();
    }

    // Setter for the text field value
    public void setValue(String value) {
        valueLabel.setText(value);
        if (!value.isEmpty() && unitLabel != null)
            add(unitLabel);
    }

    // Additional getters and setters for IDLabel, unit, etc. can be added as needed
}