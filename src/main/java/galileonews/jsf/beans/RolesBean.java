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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import galileonews.ejb.dao.RolesDaoBean;
import galileonews.ejb.datamodel.RolesDataModelBean;
import galileonews.ejb.service.RolesServiceBean;
import galileonews.jpa.Roles;
import galileonews.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("roles")
@SessionScoped
public class RolesBean implements Serializable {

    private static final long serialVersionUID = -6514366669834651448L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Roles roles = null;
    @Inject
    private VisitBean visit;
    @EJB
    private RolesDaoBean rolesDaoBean;
    @EJB
    private RolesDataModelBean rolesDataModelBean;
    @EJB
    private RolesServiceBean rolesServiceBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(RolesDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(RolesDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(rolesDataModelBean);
        roles = new Roles();
    }

    public void findRole() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String roleId = req.getParameter("roleId");
        if (roleId != null) {
            Integer roleIdInt = Integer.valueOf(roleId);
            roles = rolesDaoBean.find(roleIdInt);
        }
    }

    public String create() {
        roles = new Roles();
        return "/secure/rolesCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/rolesCreate";

        List<String> errorList = rolesServiceBean.saveCreate(roles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/roles?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/rolesUpdate";

        List<String> errorList = rolesServiceBean.saveUpdate(roles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/roles?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/rolesDelete";

        List<String> errorList = rolesServiceBean.saveDelete(roles, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/roles?faces-redirect=true";
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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
