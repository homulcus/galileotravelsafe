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
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import galileonews.jpa.Users;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersDaoBean {

    @PersistenceContext
    private EntityManager em;

    public Users selectByUserName(String userName) {
        Query query = em.createNamedQuery("Users.selectByUserName");
        query.setParameter("userName", userName);
        try {
            Users users = (Users) query.getSingleResult();
            if (users != null) {
                return users;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

    public void insert(Users users) {
        em.persist(users);
        em.flush();
    }

    public void delete(Integer userId) {
        Users users = em.find(Users.class, userId);
        if (users != null) {
            em.remove(users);
            em.flush();
        }
    }

    public void update(Users users) {
        em.merge(users);
        em.flush();
    }

    public List selectByUserIdUrl(Map<String, Object> param) {
        Query query = em.createNamedQuery("Users.selectByUserIdUrl");
        query.setParameter(1, param.get("userId"));
        query.setParameter(2, param.get("urlRole"));
        try {
            return query.getResultList();
        } catch (NoResultException ex) {
        }
        return new ArrayList<>();
    }

    public List selectLikeUserName(String userName) {
        Query query = em.createNamedQuery("Users.selectLikeUserName");
        query.setParameter("userName", userName + "%");
        List usersList = null;
        try {
            usersList = query.getResultList();
        } catch (NoResultException ex) {
        }
        return usersList;
    }

    public Users find(Integer userId) { 
        return em.find(Users.class, userId);
    }

}
