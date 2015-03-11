package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
@Entity
@Table(name = "Exam", schema = "", catalog = "")
public class ExamEntity {
    private int examId;
    private int studyId;
    private int patientId;
    private int type;
    private double weight;
    private double height;
    private String timestamp;

    @Basic
    @Column(name = "ExamId", nullable = false, insertable = true, updatable = true)
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "StudyId", nullable = false, insertable = true, updatable = true)
    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    @Basic
    @Column(name = "PatientId", nullable = false, insertable = true, updatable = true)
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Basic
    @Column(name = "Type", nullable = false, insertable = true, updatable = true)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Weight", nullable = false, insertable = true, updatable = true, precision = 10)
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "Height", nullable = false, insertable = true, updatable = true, precision = 10)
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Basic
    @Column(name = "Timestamp", nullable = false, insertable = true, updatable = true, length = 2000000000)
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamEntity that = (ExamEntity) o;

        if (examId != that.examId) return false;
        if (Double.compare(that.height, height) != 0) return false;
        if (patientId != that.patientId) return false;
        if (studyId != that.studyId) return false;
        if (type != that.type) return false;
        if (Double.compare(that.weight, weight) != 0) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = examId;
        result = 31 * result + studyId;
        result = 31 * result + patientId;
        result = 31 * result + type;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
