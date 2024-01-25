package src;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Course {
    private String _courseID;
    public ArrayList<CourseComponent> _gradeDist;

    public Course(String id) {
        _courseID = id;
        _gradeDist = new ArrayList<CourseComponent>();
    }

    public Course(JSONObject data) {
        _courseID = (String) data.get("id");

        _gradeDist = new ArrayList<CourseComponent>();
        JSONArray grades = (JSONArray) data.get("distribution");
        Iterator<JSONObject> it = grades.iterator();
        while (it.hasNext())
            _gradeDist.add(new CourseComponent(it.next()));
    }

    public void addComponent(CourseComponent c) {
        if (_gradeDist.contains(c))
            return;
        _gradeDist.add(c);
    }

    public void clearComponents() {
        _gradeDist.clear();
    }

    public void addGrade(String comp_id, double g) {
        int idx = 0;

        for (int i = 0; i < _gradeDist.size(); i++) {
            if (_gradeDist.get(i).name() == comp_id) {
                idx = i;
                break;
            }
        }
        _gradeDist.get(idx).giveGrade(g);
    }

    public void clearGrades(String comp_id) {
        int idx = 0;
        for (int i = 0; i < _gradeDist.size(); i++) {
            if (_gradeDist.get(i).name() == comp_id) {
                idx = i;
                break;
            }
        }
        _gradeDist.get(idx).clearEvals();
    }

    public double currentMark() {
        double retval = 0.0;
        double total_weight = 0.0;
        for (CourseComponent c : _gradeDist) {
            retval += c.weightedAvg();
            total_weight += c.weight();
        }

        return retval / total_weight * 100.0;
    }

    public String courseID() {
        return _courseID;
    }

    public void setID(String id) {
        _courseID = id;
    }

    public String courseComponents() {
        StringBuilder sb = new StringBuilder();
        for (CourseComponent c : _gradeDist) {
            sb.append(c.ID() + "\n");
        }
        return sb.toString();
    }

    public JSONObject json() {
        JSONObject retval = new JSONObject();
        retval.put("id", _courseID);

        JSONArray dist = new JSONArray();
        for (CourseComponent c : _gradeDist)
            dist.add(c.json());

        retval.put("distribution", dist);

        return retval;
    }

}
