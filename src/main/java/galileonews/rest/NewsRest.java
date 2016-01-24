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
package galileonews.rest;

import galileonews.api.ErrorList;
import galileonews.api.Msg;
import galileonews.api.NewsInput;
import galileonews.api.NewsOutput;
import galileonews.ejb.dao.AttachmentsDaoBean;
import galileonews.ejb.dao.NewsDaoBean;
import galileonews.ejb.service.UsersServiceBean;
import galileonews.jpa.Attachments;
import galileonews.jpa.News;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Path("/")
public class NewsRest {

    private static final String MESSAGES = "ejbmessages";
    @EJB
    UsersServiceBean usersServiceBean;
    @EJB
    NewsDaoBean newsDaoBean;
    @EJB
    AttachmentsDaoBean attachmentsDaoBean;

    @POST
    @Path("")
    @Consumes({"application/xml"})
    @Produces({"application/xml"})
    public NewsOutput getNews(NewsInput newsInput, @Context HttpServletRequest request) {
        NewsOutput newsOutput = new NewsOutput();

        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, request.getLocale());
        List<String> errorList = usersServiceBean.logIn(newsInput.getUserName(),
                newsInput.getPassword(), request.getLocale());

        if (!errorList.isEmpty()) {
            newsOutput.setErrorList(new ErrorList());
            newsOutput.getErrorList().getError().addAll(errorList);
            return newsOutput;
        }

        String lineSeparator = System.getProperty("line.separator");
        Boolean newsImportant = newsInput.getImportant();
        Boolean newsAscending = newsInput.getAscending();
        Date today = newsInput.getToday();
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("today", today);
        paramMap.put("newsImportant", newsImportant);
        paramMap.put("newsAscending", newsAscending);
        List<News> newsList = newsDaoBean.selectByCriteria(paramMap);
        for (News news : newsList) {
            Msg msg = new Msg();
            msg.setId(BigInteger.valueOf(news.getNewsId().longValue()));
            String[] lines = news.getNewsText().split(lineSeparator);
            msg.getLine().addAll(Arrays.asList(lines));
            paramMap = new HashMap();
            paramMap.put("newsId", news);
            List<Attachments> attachmentsList = attachmentsDaoBean.selectByNews(news);
            if (!attachmentsList.isEmpty()) {
                msg.getLine().add(messageSource.getString("attachment_download"));

                StringBuilder attachmentURL = new StringBuilder();
                attachmentURL.append("http://");
                attachmentURL.append(request.getServerName());
                attachmentURL.append(":");
                attachmentURL.append(request.getServerPort());
                attachmentURL.append(request.getContextPath());
                attachmentURL.append("/news");
                attachmentURL.append("/id");
                for (Attachments attachments : attachmentsList) {
                    msg.getLine().add(attachmentURL.toString().
                            concat(attachments.getAttachmentId().toString()));
                }
            }
            newsOutput.getMsg().add(msg);
        }

        return newsOutput;
    }

    @GET
    @Path("id{attachmentId}")
    @Produces({MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getAttachment(@PathParam("attachmentId") String strAttachmentId) {
        Integer attachmentId = Integer.parseInt(strAttachmentId);
        Attachments attachments = attachmentsDaoBean.find(attachmentId);
        ResponseBuilder response = Response.ok((Object) attachments.getAttachmentContent());
	response.header("Content-Disposition", "attachment; filename="+ attachments.getAttachmentFileName());
        return response.build();
    }

}
