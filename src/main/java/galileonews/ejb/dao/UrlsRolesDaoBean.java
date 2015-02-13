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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import galileonews.jpa.UrlsRoles;
import galileonews.jpa.UrlsRolesPK;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UrlsRolesDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(UrlsRoles urlsRoles) {
        em.persist(urlsRoles);
        em.flush();
    }

    public void delete(UrlsRolesPK urlsRolesPK) {
        UrlsRoles urlsRoles = em.find(UrlsRoles.class, urlsRolesPK);
        em.remove(urlsRoles);
        em.flush();
    }

    public void update(UrlsRoles urlsRoles) {
        em.merge(urlsRoles);
        em.flush();
    }

    public UrlsRoles find(UrlsRolesPK urlsRolesPK) {
        return em.find(UrlsRoles.class, urlsRolesPK);
    }

}
