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
package galileonews.jsf.beans;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("app")
@ApplicationScoped
public class AppBean implements Serializable {
    private static final long serialVersionUID = -7180283077575184379L;

    public String getBaseURL() {
        final HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String baseURL;
        if (!req.isSecure()) {
            baseURL = "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/faces/index.xhtml";
        } else {
            baseURL = "https://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/faces/index.xhtml";
        }
        return baseURL;
    }

}
