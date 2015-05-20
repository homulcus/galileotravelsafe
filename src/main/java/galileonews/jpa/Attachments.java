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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "attachments")
@NamedQueries({
    @NamedQuery(name = "Attachments.selectAll",
            query = "SELECT a FROM Attachments a"),
    @NamedQuery(name = "Attachments.selectAllCount",
            query = "SELECT COUNT(a) FROM Attachments a"),
    @NamedQuery(name = "Attachments.selectByNews",
            query = "SELECT a FROM Attachments a "
            + "WHERE a.newsId = :newsId"),
    @NamedQuery(name = "Attachments.selectByNewsCount",
            query = "SELECT COUNT(a) FROM Attachments a "
            + "WHERE a.newsId = :newsId")
})
public class Attachments extends Base implements Serializable {

    private static final long serialVersionUID = -6514552662020952310L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "attachment_id", nullable = false)
    private Integer attachmentId;
    @JoinColumn(name = "news_id", referencedColumnName = "news_id", nullable = false)
    @ManyToOne(optional = false)
    private News newsId;
    @Size(max = 250)
    @Column(name = "attachment_file_name", length = 250)
    private String attachmentFileName;
    @Size(max = 30)
    @Column(name = "attachment_file_type", length = 30)
    private String attachmentFileType;
    @Lob
    @Column(name = "attachment_content")
    private byte[] attachmentContent;

    public Attachments() {
    }

    public Attachments(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public String getAttachmentFileType() {
        return attachmentFileType;
    }

    public void setAttachmentFileType(String attachmentFileType) {
        this.attachmentFileType = attachmentFileType;
    }

    public byte[] getAttachmentContent() {
        return attachmentContent;
    }

    public void setAttachmentContent(byte[] attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

    public News getNewsId() {
        return newsId;
    }

    public void setNewsId(News newsId) {
        this.newsId = newsId;
    }

}
