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
import galileonews.jpa.UsersRoles;
import galileonews.jpa.UsersRolesPK;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersRolesDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(UsersRoles usersRoles) {
        em.persist(usersRoles);
        em.flush();
    }

    public void delete(UsersRolesPK usersRolesPK) {
        UsersRoles usersRoles = em.find(UsersRoles.class, usersRolesPK);
        em.remove(usersRoles);
        em.flush();
    }

    public void update(UsersRoles usersRoles) {
        em.merge(usersRoles);
        em.flush();
    }

    public List<UsersRoles> selectByUserId(int userId) {
        Query query = em.createNamedQuery("UsersRoles.selectByUserId");
        query.setParameter("userId", userId);
        try {
            return query.getResultList();
        } catch (NoResultException ex) {
        }
        return new ArrayList<>();
    }

    public UsersRoles find(UsersRolesPK usersRolesPK) {
        return em.find(UsersRoles.class, usersRolesPK);
    }

}
