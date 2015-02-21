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
package galileonews.ejb.service;

import galileonews.ejb.dao.NewsDaoBean;
import galileonews.jpa.News;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class NewsServiceBean {

    private static final Logger log = Logger.getLogger(NewsServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private NewsDaoBean newsDaoBean;

    public List<String> saveCreate(News news, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(news.getNewsText())) {
            errorList.add(messageSource.getString("news_text_required"));
        }
        if (news.getNewsValidFrom() == null) {
            errorList.add(messageSource.getString("news_valid_from_required"));
        }
        if (news.getNewsValidTo() == null) {
            errorList.add(messageSource.getString("news_valid_to_required"));
        }
        if (errorList.isEmpty()) {
            try {
                newsDaoBean.insert(news);
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

    public List<String> saveUpdate(News news, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (news.getNewsId() == null || news.getNewsId() == 0) {
            errorList.add(messageSource.getString("news_id_required"));
        }
        if ("".equals(news.getNewsText())) {
            errorList.add(messageSource.getString("news_text_required"));
        }
        if (news.getNewsValidFrom() == null) {
            errorList.add(messageSource.getString("news_valid_from_required"));
        }
        if (news.getNewsValidTo() == null) {
            errorList.add(messageSource.getString("news_valid_to_required"));
        }
        if (errorList.isEmpty()) {
            try {
                newsDaoBean.update(news);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(News news, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (news.getNewsId() == null || news.getNewsId() == 0) {
            errorList.add(messageSource.getString("news_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                newsDaoBean.delete(news.getNewsId());
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
