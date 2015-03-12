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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import galileonews.ejb.dao.RolesDaoBean;
import galileonews.ejb.dao.UsersDaoBean;
import galileonews.ejb.service.UsersServiceBean;
import galileonews.jpa.Users;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("logIn")
@RequestScoped
public class LogInBean implements Serializable {

    private static final Logger log = Logger.getLogger(LogInBean.class.getName());
    private static final long serialVersionUID = -8332370531968466715L;
    private String userName = null;
    private String userPassword = null;
    @Inject
    private VisitBean visit;
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private RolesDaoBean rolesDaoBean;
    @EJB
    private UsersServiceBean usersServiceBean;

    public String logIn() {

        List<String> errorList = usersServiceBean.logIn(userName, userPassword, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, error, ""));
            }
            return null;
        }

        Users users = usersDaoBean.selectByUserName(userName);
        visit.setUsers(users);
        visit.setIsAdmin(rolesDaoBean.isAdminByUserName(userName));
        final List<String> menuList = rolesDaoBean.getMenuList(userName);
        visit.setMenuList(menuList);

        return "/secure/index?faces-redirect=true";
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

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
