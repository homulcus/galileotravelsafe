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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import galileonews.ejb.dao.RolesDaoBean;
import galileonews.ejb.dao.UsersDaoBean;
import galileonews.ejb.dao.UsersRolesDaoBean;
import galileonews.jpa.Roles;
import galileonews.jpa.Users;
import galileonews.jpa.UsersRoles;
import galileonews.jpa.UsersRolesPK;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersRolesServiceBean {

    private static final Logger log = Logger.getLogger(UsersRolesServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private RolesDaoBean rolesDaoBean;
    @EJB
    private UsersRolesDaoBean usersRolesDaoBean;

    public List<String> saveCreate(UsersRoles usersRoles, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(usersRoles.getUsers().getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(usersRoles.getRoles().getRoleName())) {
            errorList.add(messageSource.getString("role_name_required"));
        }
        Users users = usersDaoBean.selectByUserName(usersRoles.getUsers().getUserName());
        if (users == null) {
            errorList.add(messageSource.getString("user_name_not_registered"));
        }
        if (errorList.isEmpty() && users != null) {
            Roles roles = rolesDaoBean.selectByRoleName(usersRoles.getRoles().getRoleName());
            usersRoles.setUsers(users);
            usersRoles.setRoles(roles);
            usersRoles.setUsersRolesPK(new UsersRolesPK());
            usersRoles.getUsersRolesPK().setRoleId(roles.getRoleId());
            usersRoles.getUsersRolesPK().setUserId(users.getUserId());
            usersRoles.setUsersRolesDesc(users.getUserName() + " " + roles.getRoleName());
            try {
                usersRolesDaoBean.insert(usersRoles);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(UsersRoles usersRoles, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(usersRoles.getUsers().getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(usersRoles.getRoles().getRoleName())) {
            errorList.add(messageSource.getString("role_name_required"));
        }
        if (errorList.isEmpty()) {
            try {
                usersRolesDaoBean.delete(usersRoles.getUsersRolesPK());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

}
