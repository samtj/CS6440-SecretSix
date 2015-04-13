package model;

import javax.persistence.*;

/**
 * Created by Samuel_Tjokrosoesilo on 4/13/2015.
 */
@Entity
@Table(name = "Study", schema = "", catalog = "")
public class StudyEntity {
    private Integer studyId;
    private String description;
    private Integer assignedTo;
    private String observationCodes;
    private Integer frequency;
    private Integer active;
    private Integer status;

    @Id
    @Column(name = "StudyId", nullable = true, insertable = true, updatable = true)
    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
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
    @Column(name = "AssignedTo", nullable = true, insertable = true, updatable = true)
    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
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
    @Column(name = "Frequency", nullable = true, insertable = true, updatable = true)
    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Basic
    @Column(name = "Active", nullable = true, insertable = true, updatable = true)
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Basic
    @Column(name = "Status", nullable = true, insertable = true, updatable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyEntity that = (StudyEntity) o;

        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (assignedTo != null ? !assignedTo.equals(that.assignedTo) : that.assignedTo != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (frequency != null ? !frequency.equals(that.frequency) : that.frequency != null) return false;
        if (observationCodes != null ? !observationCodes.equals(that.observationCodes) : that.observationCodes != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (studyId != null ? !studyId.equals(that.studyId) : that.studyId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyId != null ? studyId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (assignedTo != null ? assignedTo.hashCode() : 0);
        result = 31 * result + (observationCodes != null ? observationCodes.hashCode() : 0);
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
