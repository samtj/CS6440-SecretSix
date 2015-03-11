package model;

import javax.persistence.*;

/**
 * Created by Samuel_Tjokrosoesilo on 3/10/2015.
 */
@Entity
@Table(name = "User", schema = "", catalog = "")
public class UserEntity {
    private int userId;
    private String userName;
    private int type;
    private String password;
    private int active;

    @Id
    @Column(name = "UserId", nullable = false, insertable = true, updatable = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "UserName", nullable = false, insertable = true, updatable = true, length = 2000000000)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    @Column(name = "Password", nullable = false, insertable = true, updatable = true, length = 2000000000)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "Active", nullable = false, insertable = true, updatable = true)
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (active != that.active) return false;
        if (type != that.type) return false;
        if (userId != that.userId) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + active;
        return result;
    }
}
