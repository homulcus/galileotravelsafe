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

import galileonews.ejb.dao.NewsDaoBean;
import galileonews.ejb.datamodel.NewsDataModelBean;
import galileonews.ejb.service.NewsServiceBean;
import galileonews.jpa.News;
import galileonews.jsf.model.DatabaseDataModel;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("news")
@SessionScoped
public class NewsBean implements Serializable {

    private static final long serialVersionUID = 7606808154907219715L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private News news = null;
    @Inject
    private VisitBean visit;
    @EJB
    private NewsDaoBean newsDaoBean;
    @EJB
    private NewsDataModelBean newsDataModelBean;
    @EJB
    private NewsServiceBean newsServiceBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(NewsDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(NewsDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(newsDataModelBean);
        news = new News();
    }

    public void findNews() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String newsId = req.getParameter("newsId");
        if (newsId != null) {
            Integer newsIdInt = Integer.valueOf(newsId);
            news = newsDaoBean.find(newsIdInt);
        }
    }

    public String create() {
        news = new News();
        return "/secure/newsCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/newsCreate";

        List<String> errorList = newsServiceBean.saveCreate(news, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/news?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/newsUpdate";

        List<String> errorList = newsServiceBean.saveUpdate(news, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/news?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/newsDelete";

        List<String> errorList = newsServiceBean.saveDelete(news, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/news?faces-redirect=true";
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

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
