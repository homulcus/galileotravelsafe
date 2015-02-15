/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package galileonews.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "nk_users", columnNames = {"user_name"})})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Users.selectByUserIdUrl",
            query = "SELECT "
            + "users.user_id,"
            + "user_name "
            + "FROM "
            + "users,"
            + "users_roles,"
            + "urls_roles "
            + "WHERE "
            + "users.user_id = ?1 AND "
            + "users.user_id = users_roles.user_id AND "
            + "users_roles.role_id = urls_roles.role_id AND "
            + "urls_roles.url_role = ?2 ")
})
@NamedQueries({
    @NamedQuery(name = "Users.selectAll",
            query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.selectAllCount",
            query = "SELECT COUNT(u) FROM Users u"),
    @NamedQuery(name = "Users.selectByUserName",
            query = "SELECT u FROM Users u WHERE u.userName = :userName"),
    @NamedQuery(name = "Users.selectLikeUserName",
            query = "SELECT u FROM Users u WHERE u.userName LIKE :userName"),
    @NamedQuery(name = "Users.selectLikeUserNameCount",
            query = "SELECT COUNT(u) FROM Users u WHERE u.userName LIKE :userName")
})
public class Users extends Base implements Serializable {
    private static final long serialVersionUID = 441074696137512623L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Basic(optional = false)
    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;
    @Column(name = "user_password", length = 128)
    private String userPassword;
    @Column(name = "user_full_name", length = 200)
    private String userFullName;
    @Column(name = "user_desc", length = 200)
    private String userDesc;
    @Column(name = "user_create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userCreateDate;
    @Column(name = "user_create_by", length = 50)
    private String userCreateBy;
    @Column(name = "user_last_pwd_change")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userLastPwdChange;

    public Users() {
        userName = "";
        userPassword = "";
        userFullName = "";
        userDesc = "";
        userLastPwdChange = new Date();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Date getUserLastPwdChange() {
        return userLastPwdChange;
    }

    public void setUserLastPwdChange(Date userLastPwdChange) {
        this.userLastPwdChange = userLastPwdChange;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Date getUserCreateDate() {
        return userCreateDate;
    }

    public void setUserCreateDate(Date userCreateDate) {
        this.userCreateDate = userCreateDate;
    }

    public String getUserCreateBy() {
        return userCreateBy;
    }

    public void setUserCreateBy(String userCreateBy) {
        this.userCreateBy = userCreateBy;
    }

}
