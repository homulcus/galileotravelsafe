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
package galileonews.jpa;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "configs", uniqueConstraints = {
    @UniqueConstraint(name = "nk_configs", columnNames = {"config_key"})})
@NamedQueries({
    @NamedQuery(name = "Configs.selectAll",
            query = "SELECT c FROM Configs c"),
    @NamedQuery(name = "Configs.selectAllCount",
            query = "SELECT COUNT(c) FROM Configs c"),
    @NamedQuery(name = "Configs.selectByConfigKey",
            query = "SELECT c FROM Configs c WHERE c.configKey = :configKey")
})
public class Configs extends Base implements Serializable {

    private static final long serialVersionUID = -3326625845798373117L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "config_id", nullable = false)
    private Integer configId;
    @Basic(optional = false)
    @Column(name = "config_key", length = 30)
    private String configKey;
    @Column(name = "config_desc", length = 100)
    private String configDesc;
    @Column(name = "config_type", length = 100)
    private String configType;
    @Lob
    @Column(name = "config_value", length = 65535)
    private String configValue;

    public Integer getConfigId() {
        return configId;
    }

    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

}
