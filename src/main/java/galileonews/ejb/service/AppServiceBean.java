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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class AppServiceBean {
    private static final Logger log = Logger.getLogger(AppServiceBean.class.getName());
    private MessageDigest messageDigest = null;
    @EJB
    private ConfigsServiceBean ConfigsServiceBean;

    public AppServiceBean() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            log.severe(ex.toString());
        }
    }

    public String hashPassword(final String password) {
        byte[] hash = null;
        try {
            messageDigest.reset();
            hash = messageDigest.digest(password.getBytes());
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
        return getHexString(hash);
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void sendMail(String subject,
            String body, String sender, String recipients) {
        try {
            Session session;
            final Properties mailProperties = new Properties();
            mailProperties.setProperty("mail.transport.protocol",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_PROTOCOL).getConfigValue());
            mailProperties.setProperty("mail.host",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_HOST).getConfigValue());
            mailProperties.setProperty("mail.smtp.auth",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_AUTH).getConfigValue());
            mailProperties.setProperty("mail.smtp.port",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_PORT).getConfigValue());
            mailProperties.setProperty("mail.username",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_USERNAME).getConfigValue());
            mailProperties.setProperty("mail.password",
                    ConfigsServiceBean.getByKey(ConfigsServiceBean.MAIL_PASSWORD).getConfigValue());

            if ("true".equals(mailProperties.getProperty("mail.smtp.auth"))) {
                session = Session.getInstance(mailProperties,
                        new javax.mail.Authenticator() {

                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                String username = mailProperties.getProperty("mail.username");
                                String password = mailProperties.getProperty("mail.password");
                                return new PasswordAuthentication(username, password);
                            }
                        });
            } else {
                session = Session.getInstance(mailProperties);
            }

            MimeMessage message = new MimeMessage(session);
            message.setSender(new InternetAddress(sender));
            message.setFrom(new InternetAddress(sender));
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            message.setSentDate(new Date());
            Transport.send(message);
        } catch (MessagingException ex) {
            log.severe(ex.toString());
        }
    }
}
