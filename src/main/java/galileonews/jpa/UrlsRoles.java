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
@Table(name = "urls_roles")
@NamedQueries({
    @NamedQuery(name = "UrlsRoles.selectAll",
            query = "SELECT u FROM UrlsRoles u ORDER BY u.roles.roleName,u.urlsRolesPK.urlRole"),
    @NamedQuery(name = "UrlsRoles.selectAllCount",
            query = "SELECT COUNT(u) FROM UrlsRoles u")
})
public class UrlsRoles extends Base implements Serializable {

    private static final long serialVersionUID = 187362993398442845L;
    @EmbeddedId
    protected UrlsRolesPK urlsRolesPK;
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;

    public UrlsRoles() {
    }

    public UrlsRoles(String urlRole, int roleId) {
        this.urlsRolesPK = new UrlsRolesPK(urlRole, roleId);
    }

    public UrlsRolesPK getUrlsRolesPK() {
        return urlsRolesPK;
    }

    public void setUrlsRolesPK(UrlsRolesPK urlsRolesPK) {
        this.urlsRolesPK = urlsRolesPK;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

}
