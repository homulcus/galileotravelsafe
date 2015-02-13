/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
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
package galileonews.ejb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import galileonews.ejb.dao.ConfigsDaoBean;
import galileonews.jpa.Configs;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ConfigsServiceBean {

    private static final Logger log = Logger.getLogger(ConfigsServiceBean.class.getName());
    public static final String MAIL_PORT = "MAIL_PORT";
    public static final String MAIL_PROTOCOL = "MAIL_PROTOCOL";
    public static final String MAIL_HOST = "MAIL_HOST";
    public static final String MAIL_AUTH = "MAIL_AUTH";
    public static final String MAIL_USERNAME = "MAIL_USERNAME";
    public static final String MAIL_PASSWORD = "MAIL_PASSWORD";
    public static final String PASSWORD_EXPIRE_DAYS = "PASSWORD_EXPIRE_DAYS";
    private static final String MESSAGES = "ejbmessages";
    @PersistenceContext
    private EntityManager em;
    @EJB
    private ConfigsDaoBean configsDaoBean;

    public Configs getByKey(String configKey) {
        Query query = em.createNamedQuery("Configs.selectByConfigKey");
        query.setParameter("configKey", configKey);
        Configs configs = null;
        try {
            configs = (Configs) query.getSingleResult();
        } catch (NoResultException ex) {
        }
        return configs;
    }

    public List<String> saveCreate(Configs configs, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(configs.getConfigKey())) {
            errorList.add(messageSource.getString("config_key_required"));
        }
        if ("".equals(configs.getConfigDesc())) {
            errorList.add(messageSource.getString("config_desc_required"));
        }
        if ("".equals(configs.getConfigType())) {
            errorList.add(messageSource.getString("config_type_required"));
        }
        if ("".equals(configs.getConfigValue())) {
            errorList.add(messageSource.getString("config_value_required"));
        }
        if (errorList.isEmpty()) {
            try {
                configsDaoBean.insert(configs);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("config_key_already_registered"));
                        duplicate = true;
                        break;
                    }
                    cause = cause.getCause();
                }
                if (!duplicate) {
                    errorList.add(ex.toString());
                    log.severe(ex.toString());
                }
            }
        }
        return errorList;
    }

    public List<String> saveUpdate(Configs configs, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (configs.getConfigId() == null || configs.getConfigId() == 0) {
            errorList.add(messageSource.getString("config_id_required"));
        }
        if ("".equals(configs.getConfigKey())) {
            errorList.add(messageSource.getString("config_key_required"));
        }
        if ("".equals(configs.getConfigDesc())) {
            errorList.add(messageSource.getString("config_desc_required"));
        }
        if ("".equals(configs.getConfigType())) {
            errorList.add(messageSource.getString("config_type_required"));
        }
        if ("".equals(configs.getConfigValue())) {
            errorList.add(messageSource.getString("config_value_required"));
        }
        if (errorList.isEmpty()) {
            try {
                configsDaoBean.update(configs);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Configs configs, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (configs.getConfigId() == null || configs.getConfigId() == 0) {
            errorList.add(messageSource.getString("config_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                configsDaoBean.delete(configs.getConfigId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
