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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Path("/")
public class NewsRest {

    @GET
    @Path("")
    @Produces({"application/xml"})
    public String getNewsXML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<news>");
        
        sb.append("<msg>");
        sb.append("<id>xxxxx</id>");
        sb.append("<line>Silakan klik link dibawah ini</line>");
        sb.append("<line>http://server:port/appname/news/idxxxxxx</line>");
        sb.append("</msg>");
        
        sb.append("<msg>");
        sb.append("<id>yyyyy</id>");
        sb.append("<line>Line 1</line>");
        sb.append("<line></line>");
        sb.append("<line>Line 2</line>");
        sb.append("</msg>");

        sb.append("</news>");

        return sb.toString();
    }

}
