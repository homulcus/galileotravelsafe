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
import galileonews.ejb.datamodel.AttachmentsDataModelBean;
import galileonews.ejb.datamodel.NewsDataModelBean;
import galileonews.ejb.service.AttachmentsServiceBean;
import galileonews.ejb.service.NewsServiceBean;
import galileonews.jpa.Attachments;
import galileonews.jpa.News;
import galileonews.jsf.model.DatabaseDataModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    private DataModel dataModel;
    private DataModel attachmentsDataModel;
    private DatabaseDataModel databaseDataModel;
    private ListDataModel listDataModel;
    private News news;
    private Boolean important;
    private Boolean ascending;
    private Attachments selectedAttachments;
    private StreamedContent file;
    @Inject
    private VisitBean visit;
    @EJB
    private NewsDaoBean newsDaoBean;
    @EJB
    private NewsDataModelBean newsDataModelBean;
    @EJB
    private NewsServiceBean newsServiceBean;
    @EJB
    private AttachmentsServiceBean attachmentServiceBean;
    @EJB
    private AttachmentsDataModelBean attachmentsDataModelBean;

    @PostConstruct
    void init() {
        important = Boolean.FALSE;
        ascending = Boolean.TRUE;
        if (visit.getIsAdmin()) {
            databaseDataModel = new DatabaseDataModel();
            databaseDataModel.setSelect(NewsDataModelBean.SELECT_ALL);
            databaseDataModel.setSelectCount(NewsDataModelBean.SELECT_ALL_COUNT);
            databaseDataModel.setSelectParam(null);
            databaseDataModel.setWrappedData(newsDataModelBean);
            dataModel = databaseDataModel;
        } else {
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("today", new Date());
            paramMap.put("newsAscending", ascending);
            List<News> newsList = newsDaoBean.selectByCriteria(paramMap);
            listDataModel = new ListDataModel(newsList);
            dataModel = listDataModel;
        }
    }

    public String select() {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("newsImportant", important);
        paramMap.put("newsAscending", ascending);
        if (visit.getIsAdmin()) {
            List<News> newsList = newsDaoBean.selectByCriteria(paramMap);
            listDataModel = new ListDataModel(newsList);
            dataModel = listDataModel;
        } else {
            paramMap.put("today", new Date());
            List<News> newsList = newsDaoBean.selectByCriteria(paramMap);
            listDataModel = new ListDataModel(newsList);
            dataModel = listDataModel;
        }
        return "/secure/news?faces-redirect=true";
    }

    public String selectAll() {
        important = Boolean.FALSE;
        ascending = Boolean.TRUE;
        if (visit.getIsAdmin()) {
            databaseDataModel = new DatabaseDataModel();
            databaseDataModel.setSelect(NewsDataModelBean.SELECT_ALL);
            databaseDataModel.setSelectCount(NewsDataModelBean.SELECT_ALL_COUNT);
            databaseDataModel.setSelectParam(null);
            databaseDataModel.setWrappedData(newsDataModelBean);
            dataModel = databaseDataModel;
        } else {
            Map<String, Object> paramMap = new HashMap();
            paramMap.put("today", new Date());
            List<News> newsList = newsDaoBean.selectByCriteria(paramMap);
            listDataModel = new ListDataModel(newsList);
            dataModel = listDataModel;
        }
        return "/secure/news?faces-redirect=true";
    }

    public void findNews() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String newsId = req.getParameter("newsId");
        if (newsId != null) {
            Integer newsIdInt = Integer.valueOf(newsId);
            news = newsDaoBean.find(newsIdInt);
            DatabaseDataModel dbDataModel = new DatabaseDataModel();
            dbDataModel.setSelect(AttachmentsDataModelBean.SELECT_BY_NEWS);
            dbDataModel.setSelectCount(AttachmentsDataModelBean.SELECT_BY_NEWS_COUNT);
            Map<String, Object> param = new HashMap();
            param.put("newsId", news);
            dbDataModel.setSelectParam(param);
            dbDataModel.setWrappedData(newsDataModelBean);
            attachmentsDataModel = dbDataModel;
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

    public void handleFileUpload(FileUploadEvent event) {
        try {
            Attachments attachment = new Attachments();
            attachment.setNewsId(news);
            attachment.setAttachmentFileName(event.getFile().getFileName());
            attachment.setAttachmentFileType(event.getFile().getContentType());
            attachment.setAttachmentContent(IOUtils.toByteArray(event.getFile().getInputstream()));
            attachmentServiceBean.saveCreate(attachment, visit.getLocale());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            event.getFile().getFileName() + " is uploaded.", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            ex.getMessage(), ""));
        }

    }

    public StreamedContent getFile() {
        InputStream stream = new ByteArrayInputStream(selectedAttachments.getAttachmentContent());
        file = new DefaultStreamedContent(stream, selectedAttachments.getAttachmentFileType(),
                selectedAttachments.getAttachmentFileName());
        return file;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public DataModel getDataModel() {
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

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    public AttachmentsDataModelBean getAttachmentsDataModelBean() {
        return attachmentsDataModelBean;
    }

    public void setAttachmentsDataModelBean(AttachmentsDataModelBean attachmentsDataModelBean) {
        this.attachmentsDataModelBean = attachmentsDataModelBean;
    }

    public DataModel getAttachmentsDataModel() {
        return attachmentsDataModel;
    }

    public void setAttachmentsDataModel(DataModel attachmentsDataModel) {
        this.attachmentsDataModel = attachmentsDataModel;
    }

    public Attachments getSelectedAttachments() {
        return selectedAttachments;
    }

    public void setSelectedAttachments(Attachments selectedAttachments) {
        this.selectedAttachments = selectedAttachments;
    }

}
