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

import galileonews.ejb.dao.AttachmentsDaoBean;
import galileonews.ejb.datamodel.AttachmentsDataModelBean;
import galileonews.ejb.service.AttachmentsServiceBean;
import galileonews.jpa.Attachments;
import galileonews.jsf.model.DatabaseDataModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("attachments")
@SessionScoped
public class AttachmentsBean implements Serializable {

    private static final long serialVersionUID = -2442484105547083843L;
    private Integer noOfRows = 15;
    private Integer fastStep = 10;
    private DataModel dataModel;
    private DatabaseDataModel databaseDataModel;
    private Attachments attachments;
    @Inject
    private VisitBean visit;
    @EJB
    private AttachmentsDaoBean attachmentsDaoBean;
    @EJB
    private AttachmentsDataModelBean attachmentsDataModelBean;
    @EJB
    private AttachmentsServiceBean attachmentsServiceBean;

    @PostConstruct
    void init() {
        databaseDataModel = new DatabaseDataModel();
        databaseDataModel.setSelect(AttachmentsDataModelBean.SELECT_ALL);
        databaseDataModel.setSelectCount(AttachmentsDataModelBean.SELECT_ALL_COUNT);
        databaseDataModel.setSelectParam(null);
        databaseDataModel.setWrappedData(attachmentsDataModelBean);
        dataModel = databaseDataModel;
    }
    
    public String create() {
        attachments = new Attachments();
        return "/secure/attachmentsCreate?faces-redirect=true";
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(Integer noOfRows) {
        this.noOfRows = noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public void setFastStep(Integer fastStep) {
        this.fastStep = fastStep;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public DatabaseDataModel getDatabaseDataModel() {
        return databaseDataModel;
    }

    public void setDatabaseDataModel(DatabaseDataModel databaseDataModel) {
        this.databaseDataModel = databaseDataModel;
    }

    public Attachments getAttachment() {
        return attachments;
    }

    public void setAttachment(Attachments attachment) {
        this.attachments = attachment;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

    public AttachmentsDaoBean getAttachmentsDaoBean() {
        return attachmentsDaoBean;
    }

    public void setAttachmentsDaoBean(AttachmentsDaoBean attachmentsDaoBean) {
        this.attachmentsDaoBean = attachmentsDaoBean;
    }

    public AttachmentsDataModelBean getAttachmentsDataModelBean() {
        return attachmentsDataModelBean;
    }

    public void setAttachmentsDataModelBean(AttachmentsDataModelBean attachmentsDataModelBean) {
        this.attachmentsDataModelBean = attachmentsDataModelBean;
    }

    public AttachmentsServiceBean getAttachmentsServiceBean() {
        return attachmentsServiceBean;
    }

    public void setAttachmentsServiceBean(AttachmentsServiceBean attachmentsServiceBean) {
        this.attachmentsServiceBean = attachmentsServiceBean;
    }

}
