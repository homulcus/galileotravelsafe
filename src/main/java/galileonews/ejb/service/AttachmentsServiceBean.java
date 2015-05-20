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

import galileonews.ejb.dao.AttachmentsDaoBean;
import galileonews.jpa.Attachments;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class AttachmentsServiceBean {

    private static final Logger log = Logger.getLogger(AttachmentsServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private AttachmentsDaoBean attachmentDaoBean;

    public List<String> saveCreate(Attachments attachment, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (attachment.getNewsId() == null) {
            errorList.add(messageSource.getString("news_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                attachmentDaoBean.insert(attachment);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
