package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Samuel_Tjokrosoesilo on 4/10/2015.
 */
@Entity
@Table(name = "Todo", schema = "", catalog = "")
public class TodoEntity {
    private Integer todoId;
    private String description;
    private Integer studyId;
    private String subject;
    private String subjectFirstName;
    private String subjectLastName;
    private String observationCodes;
    private String lastDateObserved;

    @Basic
    @Column(name = "TodoId", nullable = true, insertable = true, updatable = true)
    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    @Basic
    @Column(name = "Description", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "SubjectFirstName", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getSubjectFirstName() {
        return subjectFirstName;
    }

    public void setSubjectFirstName(String subjectFirstName) {
        this.subjectFirstName = subjectFirstName;
    }

    @Basic
    @Column(name = "SubjectLastName", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getSubjectLastName() {
        return subjectLastName;
    }

    public void setSubjectLastName(String subjectLastName) {
        this.subjectLastName = subjectLastName;
    }

    @Basic
    @Column(name = "ObservationCodes", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getObservationCodes() {
        return observationCodes;
    }

    public void setObservationCodes(String observationCodes) {
        this.observationCodes = observationCodes;
    }

    @Basic
    @Column(name = "LastDateObserved", nullable = true, insertable = true, updatable = true, length = 2000000000)
    public String getLastDateObserved() {
        return lastDateObserved;
    }

    public void setLastDateObserved(String lastDateObserved) {
        this.lastDateObserved = lastDateObserved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoEntity that = (TodoEntity) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (lastDateObserved != null ? !lastDateObserved.equals(that.lastDateObserved) : that.lastDateObserved != null)
            return false;
        if (observationCodes != null ? !observationCodes.equals(that.observationCodes) : that.observationCodes != null)
            return false;
        if (studyId != null ? !studyId.equals(that.studyId) : that.studyId != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (subjectFirstName != null ? !subjectFirstName.equals(that.subjectFirstName) : that.subjectFirstName != null)
            return false;
        if (subjectLastName != null ? !subjectLastName.equals(that.subjectLastName) : that.subjectLastName != null)
            return false;
        if (todoId != null ? !todoId.equals(that.todoId) : that.todoId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = todoId != null ? todoId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (studyId != null ? studyId.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (subjectFirstName != null ? subjectFirstName.hashCode() : 0);
        result = 31 * result + (subjectLastName != null ? subjectLastName.hashCode() : 0);
        result = 31 * result + (observationCodes != null ? observationCodes.hashCode() : 0);
        result = 31 * result + (lastDateObserved != null ? lastDateObserved.hashCode() : 0);
        return result;
    }
}
