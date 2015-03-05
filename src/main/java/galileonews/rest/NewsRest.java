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

import galileonews.api.NewsInput;
import galileonews.ejb.dao.NewsDaoBean;
import galileonews.ejb.service.UsersServiceBean;
import galileonews.jpa.News;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Path("/")
public class NewsRest {

    @EJB
    UsersServiceBean usersServiceBean;
    @EJB
    NewsDaoBean newsDaoBean;

    @POST
    @Path("")
    @Consumes({"application/xml"})
    @Produces({"application/xml"})
    public String getNews(NewsInput newsXML, @Context HttpServletRequest request) {

        List<String> errorList = usersServiceBean.logIn(newsXML.getUserName(),
                newsXML.getPassword(), request.getLocale());

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("<news>");
        sb.append(lineSeparator);

        if (!errorList.isEmpty()) {
            sb.append("<errorList>");
            sb.append(lineSeparator);
            for (String errorMessage : errorList) {
                sb.append("<error>");
                sb.append(errorMessage);
                sb.append("</error>");
                sb.append(lineSeparator);
            }
            sb.append("</errorList>");
            sb.append(lineSeparator);
            sb.append("</news>");
            return sb.toString();
        }

        for (News news : newsDaoBean.selectAll()) {
            sb.append("<msg>");
            sb.append(lineSeparator);
            sb.append("<id>");
            sb.append(news.getNewsId());
            sb.append("</id>");
            sb.append(lineSeparator);
            String[] lines = news.getNewsText().split(lineSeparator);
            for (String line : lines) {
                sb.append("<line>");
                sb.append(line.substring(0, line.length() - 1));
                sb.append("</line>");
                sb.append(lineSeparator);
            }
            sb.append("</msg>");
            sb.append(lineSeparator);
        }
        sb.append("</news>");

        return sb.toString();
    }

}
