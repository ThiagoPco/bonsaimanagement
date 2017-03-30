package org.tg.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="Agenda")
@Table(name="\"AGENDA\"")
@XmlRootElement
public class AgendaEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    private TipoAcaoEntity tipo;

    @Column(name="\"dataPrevista\"")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataPrevista;

    @Column(name="\"acaoRealizada\"")
    private Boolean acaoRealizada;

    @Size(max = 500)
    @Column(length = 500, name="\"descricao\"")
    private String descricao;

    @ManyToOne(optional=true)
    @JoinColumn(name = "BONSAI_ID", referencedColumnName = "ID")
    private BonsaiEntity bonsai;

    public TipoAcaoEntity getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoAcaoEntity tipoAcao) {
        this.tipo = tipoAcao;
    }

    public Date getDataPrevista() {
        return this.dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Boolean getAcaoRealizada() {
        return this.acaoRealizada;
    }

    public void setAcaoRealizada(Boolean acaoRealizada) {
        this.acaoRealizada = acaoRealizada;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BonsaiEntity getBonsai() {
        return this.bonsai;
    }

    public void setBonsai(BonsaiEntity bonsai) {
        this.bonsai = bonsai;
    }

}
