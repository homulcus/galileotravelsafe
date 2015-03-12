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
import galileonews.ejb.dao.NewsDaoBean;
import galileonews.ejb.service.UsersServiceBean;
import galileonews.jpa.News;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    public NewsOutput getNews(NewsInput newsInput, @Context HttpServletRequest request) {
        NewsOutput newsOutput = new NewsOutput();

        List<String> errorList = usersServiceBean.logIn(newsInput.getUserName(),
                newsInput.getPassword(), request.getLocale());

        if (!errorList.isEmpty()) {
            newsOutput.setErrorList(new ErrorList());
            newsOutput.getErrorList().getError().addAll(errorList);
            return newsOutput;
        }

        String lineSeparator = System.getProperty("line.separator");
        Boolean newsImportant = newsInput.getImportant();
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("newsImportant", newsImportant);
        for (News news : newsDaoBean.selectByCriteria(paramMap)) {
            Msg msg = new Msg();
            msg.setId(BigInteger.valueOf(news.getNewsId().longValue()));
            String[] lines = news.getNewsText().split(lineSeparator);
            msg.getLine().addAll(Arrays.asList(lines));
            newsOutput.getMsg().add(msg);
        }

        return newsOutput;
    }
}
