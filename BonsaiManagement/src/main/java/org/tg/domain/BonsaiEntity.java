package org.tg.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity(name="Bonsai")
@Table(name="\"BONSAI\"")
@XmlRootElement
public class BonsaiEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private BonsaiImage image;
    
    @Column(name="\"codigo\"")
    @Digits(integer = 4, fraction = 0)
    private Integer codigo;

    @Size(max = 500)
    @Column(length = 500, name="\"descricao\"")
    private String descricao;

    @Column(name="\"dataAquisicao\"")
    @Temporal(TemporalType.DATE)
    private Date dataAquisicao;

    @Size(max = 100)
    @Column(length = 100, name="\"nome\"")
    @NotNull
    private String nome;

    @Column(name="\"ESTILO\"")
    @Enumerated(EnumType.STRING)
    private BonsaiEstilo estilo;

    @Digits(integer = 1,  fraction = 2)
    @Column(precision = 3, scale = 2, name="\"tamanho\"")
    private BigDecimal tamanho;

    @Column(name="\"CLASSIFICACAO\"")
    @Enumerated(EnumType.STRING)
    private BonsaiClassificacao classificacao;

    @XmlTransient
    public BonsaiImage getImage() {
        return image;
    }

    public void setImage(BonsaiImage image) {
        this.image = image;
    }
    
    public Integer getCodigo() {
        return this.codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataAquisicao() {
        return this.dataAquisicao;
    }

    public void setDataAquisicao(Date dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BonsaiEstilo getEstilo() {
        return estilo;
    }

    public void setEstilo(BonsaiEstilo estilo) {
        this.estilo = estilo;
    }

    public BigDecimal getTamanho() {
        return this.tamanho;
    }

    public void setTamanho(BigDecimal tamanho) {
        this.tamanho = tamanho;
    }

    public BonsaiClassificacao getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(BonsaiClassificacao classificacao) {
        this.classificacao = classificacao;
    }

}
