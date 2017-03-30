package org.tg.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name="Acoes")
@Table(name="\"ACOES\"")
@XmlRootElement
public class AcoesEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=true)
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    private TipoAcaoEntity tipo;

    @Column(name="\"dataRealizada\"")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataRealizada;

    @Size(max = 500)
    @Column(length = 500, name="\"descricao\"")
    private String descricao;

    @OneToOne(optional=true, cascade=CascadeType.DETACH)
    @JoinColumn(name="AGENDA_ID", nullable=true)
    private AgendaEntity agenda;

    @ManyToOne(optional=true)
    @JoinColumn(name = "BONSAI_ID", referencedColumnName = "ID")
    private BonsaiEntity bonsai;

    public TipoAcaoEntity getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoAcaoEntity tipoAcao) {
        this.tipo = tipoAcao;
    }

    public Date getDataRealizada() {
        return this.dataRealizada;
    }

    public void setDataRealizada(Date dataRealizada) {
        this.dataRealizada = dataRealizada;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public AgendaEntity getAgenda() {
        return this.agenda;
    }

    public void setAgenda(AgendaEntity agenda) {
        this.agenda = agenda;
    }

    public BonsaiEntity getBonsai() {
        return this.bonsai;
    }

    public void setBonsai(BonsaiEntity bonsai) {
        this.bonsai = bonsai;
    }

}
