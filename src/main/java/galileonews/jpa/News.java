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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "news")
@NamedQueries({
    @NamedQuery(name = "News.selectAll",
            query = "SELECT n FROM News n"),
    @NamedQuery(name = "News.selectAllCount",
            query = "SELECT COUNT(n) FROM News n")
})
public class News implements Serializable {

    private static final long serialVersionUID = -2454935027264327325L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "news_id", nullable = false)
    private Integer newsId;
    @Lob
    @Size(max = 65535)
    @Column(name = "news_text", length = 65535)
    private String newsText;
    @Column(name = "news_create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date newsCreateDate;
    @Size(max = 50)
    @Column(name = "news_create_by", length = 50)
    private String newsCreateBy;
    @Column(name = "news_valid_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date newsValidFrom;
    @Column(name = "news_valid_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date newsValidTo;
    @Column(name = "news_important")
    private Boolean newsImportant;
    @Size(max = 10)
    @Column(name = "news_pcc", length = 10)
    private String newsPcc;

    public News() {
    }

    public News(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsText() {
        return newsText;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public Date getNewsCreateDate() {
        return newsCreateDate;
    }

    public void setNewsCreateDate(Date newsCreateDate) {
        this.newsCreateDate = newsCreateDate;
    }

    public String getNewsCreateBy() {
        return newsCreateBy;
    }

    public void setNewsCreateBy(String newsCreateBy) {
        this.newsCreateBy = newsCreateBy;
    }

    public Date getNewsValidFrom() {
        return newsValidFrom;
    }

    public void setNewsValidFrom(Date newsValidFrom) {
        this.newsValidFrom = newsValidFrom;
    }

    public Date getNewsValidTo() {
        return newsValidTo;
    }

    public void setNewsValidTo(Date newsValidTo) {
        this.newsValidTo = newsValidTo;
    }

    public Boolean getNewsImportant() {
        return newsImportant;
    }

    public void setNewsImportant(Boolean newsImportant) {
        this.newsImportant = newsImportant;
    }

    public String getNewsPcc() {
        return newsPcc;
    }

    public void setNewsPcc(String newsPcc) {
        this.newsPcc = newsPcc;
    }

}
