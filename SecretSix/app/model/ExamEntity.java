package model;

import javax.persistence.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/19/2015.
 */
@Entity
@Table(name = "Exam", schema = "", catalog = "")
public class ExamEntity {
    private Integer examId;
    private Integer studyId;
    private String subject;
    private Integer type;
    private Double weight;
    private Double height;
    private String timestamp;

    @Id
    @Column(name = "ExamId", nullable = true, insertable = true, updatable = true)
    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "StudyId", nullable = true, insertable = true, updatable = true)
    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    @Basic
    @Column(name = "Subject", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "Type", nullable = true, insertable = true, updatable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Weight", nullable = true, insertable = true, updatable = true, precision = 10)
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "Height", nullable = true, insertable = true, updatable = true, precision = 10)
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Basic
    @Column(name = "Timestamp", nullable = true, insertable = true, updatable = true, length = 2000000000)
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

        if (examId != null ? !examId.equals(that.examId) : that.examId != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (studyId != null ? !studyId.equals(that.studyId) : that.studyId != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = examId != null ? examId.hashCode() : 0;
        result = 31 * result + (studyId != null ? studyId.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
