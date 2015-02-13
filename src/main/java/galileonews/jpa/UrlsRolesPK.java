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
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Embeddable
public class UrlsRolesPK extends Base implements Serializable {

    private static final long serialVersionUID = -6879142730005091902L;
    @Basic(optional = false)
    @Column(name = "url_role", length = 250)
    private String urlRole;
    @Basic(optional = false)
    @Column(name = "role_id")
    private int roleId;

    public UrlsRolesPK() {
    }

    public UrlsRolesPK(String urlRole, int roleId) {
        this.urlRole = urlRole;
        this.roleId = roleId;
    }

    public String getUrlRole() {
        return urlRole;
    }

    public void setUrlRole(String urlRole) {
        this.urlRole = urlRole;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
