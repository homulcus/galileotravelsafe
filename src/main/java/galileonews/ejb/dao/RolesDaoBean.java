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

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import galileonews.jpa.Roles;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class RolesDaoBean {

    @PersistenceContext
    private EntityManager em;

    public Boolean isAdminByUserName(String userName) {
        Query query = em.createNamedQuery("Roles.isAdminByUserName");
        query.setParameter("userName", userName);
        if (query.getResultList().size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public List<String> getMenuList(String userName) {
        Query query = em.createNamedQuery("Roles.selectMenuByUserName");
        query.setParameter("userName", userName);
        try {
            return (List<String>) query.getResultList();
        } catch (NoResultException ex) {
        }
        return new ArrayList<>();
    }

    public Roles selectByRoleName(String roleName) {
        Query query = em.createNamedQuery("Roles.selectByRoleName");
        query.setParameter("roleName", roleName);
        Roles roles = null;
        try {
            roles = (Roles) query.getSingleResult();
        } catch (NoResultException ex) {
        }
        return roles;
    }

    public void insert(Roles roles) {
        em.persist(roles);
        em.flush();
    }

    public void delete(Integer roleId) {
        Roles roles = em.find(Roles.class, roleId);
        em.remove(roles);
        em.flush();
    }

    public void update(Roles roles) {
        em.merge(roles);
        em.flush();
    }

    public Roles find(Integer roleId) {
        return em.find(Roles.class, roleId);
    }

}
