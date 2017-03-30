package org.tg.domain;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="AcoesAttachment")
public class AcoesAttachment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public AcoesAttachment() {
        super();
    }
    
    public AcoesAttachment(Long id, String fileName) {
        this.setId(id);
        this.fileName = fileName;
    }

    @Size(max = 200)
    private String fileName;
    
    @ManyToOne
    @JoinColumn(name = "ACOES_ID", referencedColumnName = "ID")
    private AcoesEntity acoes;

    @Lob
    private byte[] content;

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public AcoesEntity getAcoes() {
        return this.acoes;
    }

    public void setAcoes(AcoesEntity acoes) {
        this.acoes = acoes;
    }
}
