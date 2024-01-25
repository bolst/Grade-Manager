package src.front;

import net.miginfocom.swing.MigLayout;
import src.Course;
import src.CourseComponent;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.util.ArrayList;

import src.util.Pair;

public class GUI {
    private static GUI instance = null;

    protected JFrame frame;
    protected ArrayList<CoursePanel> coursePanels;
    protected JButton btn_load, btn_save;

    private GUI() {
        this.initialize();
        this.frame.setVisible(true);
    }

    public static GUI instance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    public void syllabusWindow(Course course) {
        final String instructionStr = "Accepted format is \"type,weight\" and a newline for each entry";
        JLabel instructionLabel = new JLabel(instructionStr);

        // Create a JTextArea for input
        JTextArea textArea = new JTextArea(10, 30);
        if (!course.courseComponents().isEmpty()) {
            textArea.setText(course.courseComponents());
        }

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(instructionLabel, BorderLayout.NORTH);
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

    public Pair<Integer, File> saveWindow() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(getJSONFilter());

        int x = fc.showSaveDialog(frame);
        File y = fc.getSelectedFile();
        return new Pair<Integer, File>(x, y);
    }

    public Pair<Integer, File> loadWindow() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(getJSONFilter());

        int x = fc.showOpenDialog(frame);
        File y = fc.getSelectedFile();
        return new Pair<Integer, File>(x, y);
    }

    private FileFilter getJSONFilter() {
        return new FileFilter() {
            public String getDescription() {
                return "JSON Files (*.json)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".json");
                }
            }
        };
    }

    private void initialize() {
        frame = new JFrame("Grade Manager");
        frame.setAlwaysOnTop(false);
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(
                new MigLayout("", "[][][][][][grow]", "[][][][grow][][][grow][][][grow][][][grow][][][grow]"));

        coursePanels = new ArrayList<CoursePanel>(5);
        for (int i = 0; i < 5; i++) {
            CoursePanel coursePanel = new CoursePanel();
            coursePanels.add(i, coursePanel);
            frame.getContentPane().add(coursePanel, String.format("cell 0 %d,growx", i + 3));
        }

        JPanel saveLoadPanel = new JPanel();
        btn_save = new JButton("SAVE");
        btn_load = new JButton("LOAD");
        saveLoadPanel.add(btn_save);
        saveLoadPanel.add(btn_load);
        frame.getContentPane().add(saveLoadPanel, "cell 0 2");
    }
}
