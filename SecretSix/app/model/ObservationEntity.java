package model;

import javax.persistence.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/19/2015.
 */
@Entity
@Table(name = "Observation", schema = "", catalog = "")
public class ObservationEntity {
    private String observationId;
    private Integer examId;
    private String code;
    private String display;
    private String system;
    private Integer quantity;
    private String unit;
    private String comment;
    private String subject;
    private String dateObserved;

    @Id
    @Column(name = "ObservationId", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getObservationId() {
        return observationId;
    }

    public void setObservationId(String observationId) {
        this.observationId = observationId;
    }

    @Basic
    @Column(name = "ExamId", nullable = true, insertable = true, updatable = true)
    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    @Basic
    @Column(name = "Code", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "Display", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Basic
    @Column(name = "System", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Basic
    @Column(name = "Quantity", nullable = true, insertable = true, updatable = true)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "Unit", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "Comment", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
    @Column(name = "DateObserved", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getDateObserved() {
        return dateObserved;
    }

    public void setDateObserved(String dateObserved) {
        this.dateObserved = dateObserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservationEntity that = (ObservationEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (dateObserved != null ? !dateObserved.equals(that.dateObserved) : that.dateObserved != null) return false;
        if (display != null ? !display.equals(that.display) : that.display != null) return false;
        if (examId != null ? !examId.equals(that.examId) : that.examId != null) return false;
        if (observationId != null ? !observationId.equals(that.observationId) : that.observationId != null)
            return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (system != null ? !system.equals(that.system) : that.system != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = observationId != null ? observationId.hashCode() : 0;
        result = 31 * result + (examId != null ? examId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (display != null ? display.hashCode() : 0);
        result = 31 * result + (system != null ? system.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (dateObserved != null ? dateObserved.hashCode() : 0);
        return result;
    }
}
