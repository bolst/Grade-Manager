package src;

import java.util.ArrayList;

public class Course {
    private String _courseID;
    private ArrayList<CourseComponent> _gradeDist;

    public Course(String id) {
        _courseID = id;
        _gradeDist = new ArrayList<CourseComponent>();
    }

    public void addComponent(CourseComponent c) {
        _gradeDist.add(c);
    }

    public void addGrade(CourseComponent c, double g) {
        int idx = _gradeDist.indexOf(c);
        _gradeDist.get(idx).giveGrade(g);
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

    public String courseComponents() {
        StringBuilder sb = new StringBuilder();
        for (CourseComponent c : _gradeDist) {
            sb.append(c.toString() + "\n");
        }
        return sb.toString();
    }
}

class CourseComponent {
    private String _name;
    private double _weight;
    private ArrayList<Double> _evals;

    public CourseComponent(String name, double weight) {
        _name = name;
        _weight = weight;
        _evals = new ArrayList<Double>();
    }

    public void giveGrade(double g) {
        _evals.add(g);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_name + '(' + _weight + "%): ");

        for (double e : _evals) {
            sb.append(e + ' ');
        }

        return sb.toString();
    }
}
