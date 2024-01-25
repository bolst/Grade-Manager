package src;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CourseComponent {
    private String _name;
    private double _weight;
    private ArrayList<Double> _evals;

    public CourseComponent(String name, double weight) {
        _name = name;
        _weight = weight;
        _evals = new ArrayList<Double>();
    }

    public CourseComponent(JSONObject data) {
        _name = (String) data.get("id");
        _weight = (Double) data.get("weight");

        _evals = new ArrayList<Double>();
        JSONArray json_evals = (JSONArray) data.get("grades");
        Iterator<Double> it = json_evals.iterator();
        while (it.hasNext())
            _evals.add(it.next());
    }

    public void giveGrade(double g) {
        _evals.add(g);
    }

    public void clearEvals() {
        _evals.clear();
    }

    public double avg() {
        double retval = 0;
        for (double e : _evals) {
            retval += e / _evals.size();
        }
        return retval;
    }

    public double weightedAvg() {
        return this.avg() * _weight / 100.0;
    }

    public String name() {
        return _name;
    }

    public double weight() {
        return _weight;
    }

    public String ID() {
        return _name + ',' + String.valueOf((int) (_weight * 100) / 100.0);
    }

    public String evals() {
        StringBuilder sb = new StringBuilder();

        if (_evals.isEmpty())
            return "";

        sb.append(String.valueOf((int) (_evals.get(0) * 100) / 100.0));

        for (int i = 1; i < _evals.size(); i++) {
            sb.append(',' + String.valueOf((int) (_evals.get(i) * 100) / 100.0));
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_name + '(' + _weight + "%): ");

        for (double e : _evals) {
            sb.append(e + ' ');
        }

        return sb.toString();
    }

    public JSONObject json() {
        JSONObject retval = new JSONObject();
        retval.put("id", _name);
        retval.put("weight", _weight);

        JSONArray grades = new JSONArray();
        for (Double e : _evals)
            grades.add(e);

        retval.put("grades", grades);

        return retval;
    }
}
