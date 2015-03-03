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
package galileonews.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
public class Gtids extends Base implements Serializable {

    private static final long serialVersionUID = 8631883668074219724L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gtid_id", nullable = false)
    private Integer gtidId;
    @Basic(optional = true)
    @Column(name = "gtid_pcc", length = 10)
    private String gtidPcc;
    @Basic(optional = true)
    @Column(name = "gtid_gtid", length = 10)
    private String gtidGtid;
    @Basic(optional = true)
    @Column(name = "gtid_signon", length = 10)
    private String gtidSignon;
    @Column(name = "gtid_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gtidTimestamp;

    public Integer getGtidId() {
        return gtidId;
    }

    public void setGtidId(Integer gtidId) {
        this.gtidId = gtidId;
    }

    public String getGtidPcc() {
        return gtidPcc;
    }

    public void setGtidPcc(String gtidPcc) {
        this.gtidPcc = gtidPcc;
    }

    public String getGtidGtid() {
        return gtidGtid;
    }

    public void setGtidGtid(String gtidGtid) {
        this.gtidGtid = gtidGtid;
    }

    public String getGtidSignon() {
        return gtidSignon;
    }

    public void setGtidSignon(String gtidSignon) {
        this.gtidSignon = gtidSignon;
    }

    public Date getGtidTimestamp() {
        return gtidTimestamp;
    }

    public void setGtidTimestamp(Date gtidTimestamp) {
        this.gtidTimestamp = gtidTimestamp;
    }

}
