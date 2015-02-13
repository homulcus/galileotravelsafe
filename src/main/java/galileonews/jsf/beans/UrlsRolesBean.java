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
import galileonews.ejb.dao.UrlsRolesDaoBean;
import galileonews.ejb.datamodel.RolesDataModelBean;
import galileonews.ejb.datamodel.UrlsRolesDataModelBean;
import galileonews.ejb.service.UrlsRolesServiceBean;
import galileonews.jpa.Roles;
import galileonews.jpa.UrlsRoles;
import galileonews.jpa.UrlsRolesPK;
import galileonews.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("urlsRoles")
@SessionScoped
public class UrlsRolesBean implements Serializable {

    private static final long serialVersionUID = 5766078539863127506L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private UrlsRoles urlsRoles = null;
    private List<SelectItem> roleNameList = null;
    @Inject
    private VisitBean visit;
    @EJB
    private UrlsRolesServiceBean urlsRolesServiceBean;
    @EJB
    private RolesDataModelBean rolesDataModelBean;
    @EJB
    private UrlsRolesDataModelBean urlsRolesDataModelBean;
    @EJB
    private UrlsRolesDaoBean urlsRolesDaoBean;

    public String create() {
        urlsRoles = new UrlsRoles();
        urlsRoles.setUrlsRolesPK(new UrlsRolesPK());
        urlsRoles.setRoles(new Roles());
        return "/secure/urls_rolesCreate?faces-redirect=true";
    }

    @PostConstruct
    public void init() {
        dataModel.setSelect(UrlsRolesDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(UrlsRolesDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(urlsRolesDataModelBean);
        urlsRoles = new UrlsRoles();
        urlsRoles.setUrlsRolesPK(new UrlsRolesPK());
    }

    public void findUrlRole() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String urlRole = req.getParameter("urlRole");
        String roleId = req.getParameter("roleId");
        if (roleId != null) {
            Integer roleIdInt = Integer.valueOf(roleId);
            urlsRoles = new UrlsRoles(urlRole, roleIdInt);
            if (urlsRoles.getUrlsRolesPK().getUrlRole() != null
                    && urlsRoles.getUrlsRolesPK().getRoleId() != 0) {
                urlsRoles = urlsRolesDaoBean.find(urlsRoles.getUrlsRolesPK());
            }
        }

    }

    @SuppressWarnings("unchecked")
    public String saveCreate() {
        String result = "/secure/urls_rolesCreate";

        List<String> errorList = urlsRolesServiceBean.saveCreate(urlsRoles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/urls_roles?faces-redirect=true";
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public String saveDelete() {
        String result = "/secure/urls_rolesDelete";

        List<String> errorList = urlsRolesServiceBean.saveDelete(urlsRoles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/urls_roles?faces-redirect=true";
        }

        return result;
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

    public UrlsRoles getUrlsRoles() {
        return urlsRoles;
    }

    public void setUrlsRoles(UrlsRoles urlsRoles) {
        this.urlsRoles = urlsRoles;
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

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }
}
