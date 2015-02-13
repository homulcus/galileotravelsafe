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
package galileonews.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import galileonews.ejb.dao.UsersDaoBean;
import galileonews.ejb.dao.UsersRolesDaoBean;
import galileonews.ejb.datamodel.RolesDataModelBean;
import galileonews.ejb.datamodel.UsersRolesDataModelBean;
import galileonews.ejb.service.UsersRolesServiceBean;
import galileonews.jpa.Roles;
import galileonews.jpa.Users;
import galileonews.jpa.UsersRoles;
import galileonews.jpa.UsersRolesPK;
import galileonews.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("usersRoles")
@SessionScoped
public class UsersRolesBean implements Serializable {

    private static final long serialVersionUID = 5103448448088460333L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private UsersRoles usersRoles = null;
    private List<SelectItem> roleNameList = null;
    @Inject
    private VisitBean visit;
    @EJB
    private UsersRolesDataModelBean usersRolesDataModelBean;
    @EJB
    private UsersRolesServiceBean usersRolesServiceBean;
    @EJB
    private RolesDataModelBean rolesDataModelBean;
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private UsersRolesDaoBean usersRolesDaoBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(UsersRolesDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(UsersRolesDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(usersRolesDataModelBean);
        usersRoles = new UsersRoles();
        usersRoles.setUsersRolesPK(new UsersRolesPK());
        usersRoles.setUsers(new Users());
        usersRoles.setRoles(new Roles());
    }

    public String create() {
        usersRoles = new UsersRoles();
        usersRoles.setUsersRolesPK(new UsersRolesPK());
        usersRoles.setUsers(new Users());
        usersRoles.setRoles(new Roles());
        return "/secure/users_rolesCreate?faces-redirect=true";
    }

    public void findUserRole() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String userId = req.getParameter("userId");
        Integer userIdInt = 0;
        if (userId != null) {
            userIdInt = Integer.valueOf(userId);
        }
        String roleId = req.getParameter("roleId");
        Integer roleIdInt = 0;
        if (roleId != null) {
            roleIdInt = Integer.valueOf(roleId);
        }
        usersRoles = new UsersRoles(userIdInt, roleIdInt);
        if (usersRoles.getUsersRolesPK().getUserId() != 0 && usersRoles.getUsersRolesPK().getRoleId() != 0) {
            usersRoles = usersRolesDaoBean.find(usersRoles.getUsersRolesPK());
        }
    }

    public List<String> autoCompleteUsers(String suggest) {
        List<String> userNameList = new ArrayList<>();
        List userList = usersDaoBean.selectLikeUserName(suggest);
        for (Object usersObject : userList) {
            Users users = (Users) usersObject;
            userNameList.add(users.getUserName());
        }
        return userNameList;
    }

    public List<SelectItem> getRoleNameList() {
        roleNameList = new ArrayList<>();
        List rolesList;

        rolesList = rolesDataModelBean.getAll(RolesDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object rolesList1 : rolesList) {
            Roles roles = (Roles) rolesList1;
            SelectItem selectItem = new SelectItem(roles.getRoleName());
            roleNameList.add(selectItem);
        }

        return roleNameList;
    }

    public String saveCreate() {
        String result = "/secure/users_rolesCreate";

        List<String> errorList = usersRolesServiceBean.saveCreate(usersRoles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/users_roles?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/users_rolesDelete";

        List<String> errorList = usersRolesServiceBean.saveDelete(usersRoles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/users_roles?faces-redirect=true";
        }

        return result;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public DatabaseDataModel getDataModel() {
        return dataModel;
    }

    public UsersRoles getUsersRoles() {
        return usersRoles;
    }

    public void setUsersRoles(UsersRoles usersRoles) {
        this.usersRoles = usersRoles;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
