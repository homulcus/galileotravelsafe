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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "roles", uniqueConstraints = {
    @UniqueConstraint(name = "nk_roles", columnNames = {"role_name"})})
@NamedQueries({
    @NamedQuery(name = "Roles.selectAll",
            query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.selectAllCount",
            query = "SELECT COUNT(r) FROM Roles r"),
    @NamedQuery(name = "Roles.isAdminByUserName",
            query = "SELECT r "
            + "FROM Roles r, Users u, UsersRoles ur "
            + "WHERE "
            + "r = ur.roles AND "
            + "u = ur.users AND "
            + "u.userName = :userName AND "
            + "r.roleName = 'ADM'"),
    @NamedQuery(name = "Roles.isOwnerByUserName",
            query = "SELECT r "
            + "FROM Roles r, Users u, UsersRoles ur "
            + "WHERE "
            + "r = ur.roles AND "
            + "u = ur.users AND "
            + "u.userName = :userName AND "
            + "r.roleName = 'OWN'"),
    @NamedQuery(name = "Roles.selectMenuByUserName",
            query = "SELECT r.roleMenu "
            + "FROM Roles r, Users u, UsersRoles ur "
            + "WHERE "
            + "r = ur.roles AND "
            + "u = ur.users AND "
            + "u.userName = :userName"),
    @NamedQuery(name = "Roles.selectByRoleName",
            query = "SELECT r FROM Roles r WHERE r.roleName = :roleName")
})
public class Roles extends Base implements Serializable {

    private static final long serialVersionUID = 4787287322570404361L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;
    @Column(name = "role_desc", length = 100)
    private String roleDesc;
    @Column(name = "role_menu", length = 50)
    private String roleMenu;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleMenu() {
        return roleMenu;
    }

    public void setRoleMenu(String roleMenu) {
        this.roleMenu = roleMenu;
    }

}
