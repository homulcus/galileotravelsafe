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
import galileonews.jpa.Configs;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ConfigsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Configs configs) {
        em.persist(configs);
        em.flush();
    }

    public void delete(Integer configId) {
        Configs configs = em.find(Configs.class, configId);
        em.remove(configs);
        em.flush();
    }

    public void update(Configs configs) {
        em.merge(configs);
        em.flush();
    }

    public Configs find(Integer configId) {
        return em.find(Configs.class, configId);
    }
}
