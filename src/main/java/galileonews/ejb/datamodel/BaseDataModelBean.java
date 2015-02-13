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
package galileonews.ejb.datamodel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
public class BaseDataModelBean implements DataModelDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List getAll(String id, Map<String, Object> param, int first, int max) {
        Query query = em.createNamedQuery(id);
        query.setFirstResult(first);
        if (max > 0) {
            query.setMaxResults(max);
        }
        if ((param != null) && (param.size() > 0)) {
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        }
        return query.getResultList();
    }

    @Override
    public Long getAllCount(String id, Map<String, Object> param) {
        Query query = em.createNamedQuery(id);
        if ((param != null) && (param.size() > 0)) {
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        }
        Long count = (long) 0;
        try {
            Object obj = query.getSingleResult();
            if (obj.getClass().getSimpleName().equals("Long")) {
                count = (Long) obj;
                return count;
            }
            if (obj.getClass().getSimpleName().equals("BigDecimal")) {
                BigDecimal bd = (BigDecimal) obj;
                count = bd.longValue();
                return count;
            }
            if (obj instanceof Object[]) {
                Object[] objArray = (Object[]) obj;
                if (objArray.length >= 1) {
                    if (objArray[0] != null) {
                        Object objValue = objArray[0];
                        if (objValue.getClass().getSimpleName().equals("Long")) {
                            count = (Long) objValue;
                            return count;
                        }
                        if (objValue.getClass().getSimpleName().equals("BigDecimal")) {
                            BigDecimal bd = (BigDecimal) objValue;
                            count = bd.longValue();
                            return count;
                        }
                    }
                }
            }
        } catch (NoResultException ex) {
        }
        return count;
    }
}
