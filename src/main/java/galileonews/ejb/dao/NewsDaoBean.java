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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}