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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import galileonews.ejb.dao.ConfigsDaoBean;
import galileonews.ejb.datamodel.ConfigsDataModelBean;
import galileonews.ejb.service.ConfigsServiceBean;
import galileonews.jpa.Configs;
import galileonews.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("configs")
@SessionScoped
public class ConfigsBean implements Serializable {

    private static final long serialVersionUID = 6740043921854343232L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Configs configs = null;
    @Inject
    private VisitBean visit;
    @EJB
    private ConfigsDaoBean configsDaoBean;
    @EJB
    private ConfigsServiceBean configsServiceBean;
    @EJB
    private ConfigsDataModelBean configsDataModelBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(ConfigsDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(ConfigsDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(configsDataModelBean);
        configs = new Configs();
    }

    public void findConfig() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String configId = req.getParameter("configId");
        if (configId != null) {
            Integer configIdInt = Integer.valueOf(configId);
            configs = configsDaoBean.find(configIdInt);
        }
    }

    public String create() {
        configs = new Configs();
        return "/secure/configsCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/configsCreate";

        List<String> errorList = configsServiceBean.saveCreate(configs, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/configs?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/configsUpdate?configId=" + configs.getConfigId();

        List<String> errorList = configsServiceBean.saveUpdate(configs, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/configs?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/configsDelete";

        List<String> errorList = configsServiceBean.saveDelete(configs, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/configs?faces-redirect=true";
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

    public Configs getConfigs() {
        return configs;
    }

    public void setConfigs(Configs configs) {
        this.configs = configs;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
