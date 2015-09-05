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
package galileonews.ejb.dao;

import galileonews.ejb.datamodel.AttachmentsDataModelBean;
import galileonews.jpa.Attachments;
import galileonews.jpa.News;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class AttachmentsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Attachments attachment) {
        em.persist(attachment);
        em.flush();
    }

    public void delete(Integer attachmentId) {
        Attachments attachment = em.find(Attachments.class, attachmentId);
        em.remove(attachment);
        em.flush();
    }

    public void update(Attachments attachment) {
        em.merge(attachment);
        em.flush();
    }

    public Attachments find(Integer attachmentId) {
        return em.find(Attachments.class, attachmentId);
    }

    public List<Attachments> selectByNews(News news) {
        Query query = em.createNamedQuery(AttachmentsDataModelBean.SELECT_BY_NEWS);
        query.setParameter("newsId", news);
        List<Attachments> attachmentsList = query.getResultList();
        return attachmentsList;
    }
}
