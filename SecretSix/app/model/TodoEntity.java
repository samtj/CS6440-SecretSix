package model;

import javax.persistence.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
@Entity
@Table(name = "Todo", schema = "", catalog = "")
public class TodoEntity {
    private int todoId;
    private String description;
    private int status;
    private int patientId;

    @Id
    @Column(name = "TodoId", nullable = false, insertable = true, updatable = true)
    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    @Basic
    @Column(name = "Description", nullable = false, insertable = true, updatable = true, length = 2000000000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "Status", nullable = false, insertable = true, updatable = true)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "PatientId", nullable = false, insertable = true, updatable = true)
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoEntity that = (TodoEntity) o;

        if (patientId != that.patientId) return false;
        if (status != that.status) return false;
        if (todoId != that.todoId) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = todoId;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + patientId;
        return result;
    }
}
