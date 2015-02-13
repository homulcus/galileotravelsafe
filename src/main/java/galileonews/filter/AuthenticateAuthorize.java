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
package galileonews.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import galileonews.ejb.dao.UsersDaoBean;
import galileonews.ejb.service.ConfigsServiceBean;
import galileonews.jpa.Configs;
import galileonews.jsf.beans.VisitBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@WebFilter(filterName = "AuthenticateAuthorize", urlPatterns = {"/faces/secure/*"})
public class AuthenticateAuthorize implements Filter {

    private static final Logger log = Logger.getLogger(AuthenticateAuthorize.class.getName());
    @Inject
    UsersDaoBean usersDaoBean;
    @EJB
    private ConfigsServiceBean configsServiceBean;
    @Inject
    private VisitBean visit;

    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        if ((visit != null) && (visit.getUsers() == null)) {
            visit.setSecurePath(req.getContextPath() + req.getServletPath() + req.getPathInfo());
            res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
        } else {
            if ((visit == null) || (visit.getUsers() == null)) {
                if (visit == null) {
                    visit = new VisitBean();
                    req.getSession().setAttribute("visit", visit);
                }
                visit.setSecurePath(req.getContextPath() + req.getServletPath() + req.getPathInfo());
                res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
                return;
            }

            Map<String, Object> param = new HashMap<>();
            param.put("userId", visit.getUsers().getUserId());
            param.put("urlRole", req.getRequestURI());
            List userList;
            try {
                userList = usersDaoBean.selectByUserIdUrl(param);
            } catch (Exception ex) {
                log.severe(ex.toString());
                return;
            }
            if (userList.size() <= 0) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            if (!req.getRequestURI().equals(req.getContextPath() + "/faces/secure/change_password.xhtml")) {
                Configs configs = configsServiceBean.getByKey(ConfigsServiceBean.PASSWORD_EXPIRE_DAYS);
                Integer expireDays = new Integer(configs.getConfigValue());

                DateTime now = new DateTime();
                DateTime lastPwdChange = new DateTime(visit.getUsers().getUserLastPwdChange().getTime());
                DateTime expired = lastPwdChange.plusDays(expireDays);
                if (now.isAfter(expired)) {
                    res.sendRedirect(req.getContextPath() + "/faces/secure/change_password.xhtml");
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        log.info("Init AuthenticateAuthorize filter");
    }

    @Override
    public void destroy() {
        log.info("Destroy AuthenticateAuthorize filter");
    }
}
