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

import galileonews.jpa.News;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class NewsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(News news) {
        em.persist(news);
        em.flush();
    }

    public void delete(Integer newsId) {
        News news = em.find(News.class, newsId);
        em.remove(news);
        em.flush();
    }

    public void update(News news) {
        em.merge(news);
        em.flush();
    }

    public News find(Integer newsId) {
        return em.find(News.class, newsId);
    }

    public List<News> selectAll() {
        Query query = em.createNamedQuery("News.selectAll");
        try {
            return query.getResultList();
        } catch (NoResultException ex) {
        }
        return new ArrayList<>();
    }

    public List<News> selectByCriteria(Map<String, Object> paramMap) {
        StringBuilder jql = new StringBuilder();
        StringBuilder where = new StringBuilder();
        StringBuilder order = new StringBuilder();
        jql.append("SELECT n FROM News n ");
        if (paramMap.containsKey("today")) {
            if (where.length() <= 0) {
                where.append("WHERE ");
            }
            where.append(":today BETWEEN n.newsValidFrom AND n.newsValidTo");
        }
        if (paramMap.containsKey("newsImportant")) {
            if (where.length() <= 0) {
                where.append("WHERE ");
            } else {
                where.append(" AND ");
            }
            where.append("n.newsImportant = :newsImportant");
        }        
        if (paramMap.containsKey("newsAscending")) {
            Boolean newsAscending = (Boolean) paramMap.get("newsAscending");
            if(newsAscending) {
                order.append(" ORDER BY n.newsValidFrom");
            } else {
                order.append(" ORDER BY n.newsValidFrom DESC");
            }            
        } else {
            order.append(" ORDER BY n.newsValidFrom");
        }
        jql.append(where).append(order);
        Query query = em.createQuery(jql.toString());
        if (paramMap.containsKey("today")) {
            Date today = (Date) paramMap.get("today");
            query.setParameter("today", today);
        }
        if (paramMap.containsKey("newsImportant")) {
            query.setParameter("newsImportant", paramMap.get("newsImportant"));
        }
        List<News> newsList = new ArrayList<>();
        try {
            newsList = query.getResultList();
        } catch (NoResultException ex) {
        }
        return newsList;
    }

}
