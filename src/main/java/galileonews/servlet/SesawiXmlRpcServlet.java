/*
 * Copyright 2014 Samuel Franklyn <sfranklyn at gmail.com>.
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
package galileonews.servlet;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import redstone.xmlrpc.XmlRpcServlet;
import galileonews.api.UsersRpc;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@WebServlet(name = "SesawiXmlRpcServlet", urlPatterns = {"/xml-rpc/*"})
public class SesawiXmlRpcServlet extends XmlRpcServlet {

    private static final long serialVersionUID = -5119589266774898357L;

    @EJB
    private UsersRpc usersRpc;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        getXmlRpcServer().addInvocationHandler("UsersRpc", usersRpc);
    }
}
