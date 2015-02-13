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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "users_roles")
@NamedQueries({
    @NamedQuery(name = "UsersRoles.selectAll",
            query = "SELECT u FROM UsersRoles u"),
    @NamedQuery(name = "UsersRoles.selectAllCount",
            query = "SELECT COUNT(u) FROM UsersRoles u"),
    @NamedQuery(name = "UsersRoles.selectByUserId",
            query = "SELECT u FROM UsersRoles u WHERE "
            + "u.usersRolesPK.userId = :userId")
})
public class UsersRoles extends Base implements Serializable {

    private static final long serialVersionUID = 3329607397720573326L;
    @EmbeddedId
    protected UsersRolesPK usersRolesPK;
    @Column(name = "users_roles_desc", length = 100)
    private String usersRolesDesc;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    public UsersRoles() {
    }

    public UsersRoles(int userId, int roleId) {
        this.usersRolesPK = new UsersRolesPK(userId, roleId);
    }

    public UsersRolesPK getUsersRolesPK() {
        return usersRolesPK;
    }

    public void setUsersRolesPK(UsersRolesPK usersRolesPK) {
        this.usersRolesPK = usersRolesPK;
    }

    public String getUsersRolesDesc() {
        return usersRolesDesc;
    }

    public void setUsersRolesDesc(String usersRolesDesc) {
        this.usersRolesDesc = usersRolesDesc;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

}
