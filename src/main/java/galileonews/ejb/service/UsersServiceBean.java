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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import galileonews.ejb.dao.UsersDaoBean;
import galileonews.ejb.dao.UsersRolesDaoBean;
import galileonews.jpa.Users;
import galileonews.jpa.UsersRoles;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersServiceBean {

    private static final Logger log = Logger.getLogger(UsersServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private AppServiceBean appServiceBean;
    @EJB
    private UsersRolesDaoBean usersRolesDaoBean;

    public List<String> logIn(String userName, String userPassword,
            Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }
        Users users = usersDaoBean.selectByUserName(userName);
        if (users == null) {
            errorList.add(messageSource.getString("user_name_not_registered"));
            return errorList;
        }
        if (!appServiceBean.hashPassword(userPassword).equals(users.getUserPassword())) {
            errorList.add(messageSource.getString("user_password_not_match"));
        }
        return errorList;
    }

    public List<String> saveChangePassword(Users users,
            String userPassword1, String userPassword2, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        errorList = this.checkPassword(userPassword1, userPassword2, errorList, messageSource);

        if (errorList.isEmpty()) {
            try {
                users.setUserPassword(appServiceBean.hashPassword(userPassword1));
                users.setUserLastPwdChange(new Date());
                usersDaoBean.update(users);
            } catch (Exception ex) {
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveCreate(Users users,
            String userPassword1, String userPassword2, String roleName,
            Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(users.getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }

        errorList = this.checkPassword(userPassword1, userPassword2, errorList, messageSource);

        if (errorList.isEmpty()) {
            try {
                users.setUserPassword(appServiceBean.hashPassword(userPassword1));
                users.setUserLastPwdChange(new Date());
                usersDaoBean.insert(users);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("user_name_already_registered"));
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

    private List<String> checkPassword(String userPassword1, String userPassword2, List<String> errorList, ResourceBundle messageSource) {

        if ("".equals(userPassword1)) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword2)) {
            errorList.add(messageSource.getString("reconfirm_user_password_required"));
        }

        if (!userPassword1.equals(userPassword2)) {
            errorList.add(messageSource.getString("user_password_reconfirm_not_match"));
        } else if (userPassword1.length() < 5) {
            errorList.add(messageSource.getString("user_password_not_long_enough"));
        }

        return errorList;
    }

    public List<String> saveUpdate(Users users, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (users.getUserId() == null || users.getUserId() == 0) {
            errorList.add(messageSource.getString("user_id_required"));
        }
        if ("".equals(users.getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }

        if (errorList.isEmpty()) {
            try {
                usersDaoBean.update(users);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> resetPassword(Users users, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (users.getUserId() == null || users.getUserId() == 0) {
            errorList.add(messageSource.getString("user_id_required"));
        }
        if ("".equals(users.getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }

        if (errorList.isEmpty()) {
            try {
                users.setUserPassword(appServiceBean.hashPassword(users.getUserName()));
                usersDaoBean.update(users);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Users users, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (users.getUserId() == null || users.getUserId() == 0) {
            errorList.add(messageSource.getString("user_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                List<UsersRoles> usersRolesList = usersRolesDaoBean.selectByUserId(users.getUserId());
                for (UsersRoles usersRoles : usersRolesList) {
                    usersRolesDaoBean.delete(usersRoles.getUsersRolesPK());
                }

                usersDaoBean.delete(users.getUserId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
