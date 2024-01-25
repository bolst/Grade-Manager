package src.front;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JPanel;

enum COURSE_NAME {
    BIOL, CHEM, COMP, ELEC, GART, GENG, KINE, MATH, MECH, PHYS, STAT
}

public class CoursePanel extends JPanel {

    protected JComboBox<COURSE_NAME> combo_courseName;
    protected JTextField txt_courseId;
    protected JButton btn_inputSyllabus;
    protected JButton btn_enterGrades;
    protected EzLabel ezl_currentGrade;

    public CoursePanel() {

        // Initializing components
        combo_courseName = new JComboBox<COURSE_NAME>();
        txt_courseId = new JTextField();
        btn_inputSyllabus = new JButton("INPUT SYLLABUS DETAIL");
        btn_enterGrades = new JButton("ENTER GRADES");
        ezl_currentGrade = new EzLabel("Current Grade", "", "%");

        // Adding options to components
        combo_courseName.setModel(new DefaultComboBoxModel<COURSE_NAME>(COURSE_NAME.values()));
        txt_courseId.setColumns(7);

        // Adding components to panel
        add(combo_courseName);
        add(txt_courseId);
        add(btn_inputSyllabus);
        add(btn_enterGrades);
        add(ezl_currentGrade);
    }

}
