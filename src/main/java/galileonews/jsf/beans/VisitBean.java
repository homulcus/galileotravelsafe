/*
 * Copyright 2015 Samuel Franklyn <sfranklyn@gmail.com>.
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
package galileonews.jsf.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import galileonews.jpa.Users;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("visit")
@SessionScoped
public class VisitBean implements Serializable {

    private static final Locale localeId = new Locale("in", "ID");
    private static final Locale localeUs = new Locale("en", "US");
    private static final long serialVersionUID = 3254262106751953276L;
    private Users users = null;
    private Boolean isAdmin = null;
    private Boolean isOwner = null;
    private List menuList = null;
    private String securePath = null;
    private Locale locale = null;

    public Locale getLocale() {
        if (locale == null) {
            locale = localeUs;
        }
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        return locale;
    }

    public String chooseEnglish() {
        locale = localeUs;
        return null;
    }

    public String chooseIndonesia() {
        locale = localeId;
        return null;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List getMenuList() {
        return menuList;
    }

    public void setMenuList(List menuList) {
        this.menuList = menuList;
    }

    public String getSecurePath() {
        return securePath;
    }

    public void setSecurePath(String securePath) {
        this.securePath = securePath;
    }

}
