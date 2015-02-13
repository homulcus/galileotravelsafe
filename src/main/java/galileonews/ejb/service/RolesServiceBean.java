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
import galileonews.ejb.dao.RolesDaoBean;
import galileonews.jpa.Roles;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class RolesServiceBean {

    private static final Logger log = Logger.getLogger(RolesServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private RolesDaoBean rolesDaoBean;

    public List<String> saveCreate(Roles roles, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(roles.getRoleName())) {
            errorList.add(messageSource.getString("role_name_required"));
        }
        if ("".equals(roles.getRoleDesc())) {
            errorList.add(messageSource.getString("role_desc_required"));
        }
        if ("".equals(roles.getRoleMenu())) {
            errorList.add(messageSource.getString("role_menu_required"));
        }
        if (errorList.isEmpty()) {
            try {
                rolesDaoBean.insert(roles);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("role_name_already_registered"));
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

    public List<String> saveUpdate(Roles roles, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (roles.getRoleId() == null || roles.getRoleId() == 0) {
            errorList.add(messageSource.getString("role_id_required"));
        }
        if ("".equals(roles.getRoleName())) {
            errorList.add(messageSource.getString("role_name_required"));
        }
        if ("".equals(roles.getRoleDesc())) {
            errorList.add(messageSource.getString("role_desc_required"));
        }
        if ("".equals(roles.getRoleMenu())) {
            errorList.add(messageSource.getString("role_menu_required"));
        }
        if (errorList.isEmpty()) {
            try {
                rolesDaoBean.update(roles);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Roles roles, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (roles.getRoleId() == null || roles.getRoleId() == 0) {
            errorList.add(messageSource.getString("role_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                rolesDaoBean.delete(roles.getRoleId());
            } catch (Exception ex) {
                if (ex.toString().contains("ConstraintViolationException")) {
                    errorList.add(messageSource.getString("role_used"));
                } else {
                    errorList.add(ex.toString());
                    log.severe(ex.toString());
                }
            }
        }
        return errorList;
    }
}
