package com.innvo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.innvo.domain.enumeration.Status;

/**
 * A Identifier.
 */
@Entity
@Table(name = "identifier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "identifier")
public class Identifier implements Serializable {

    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 100000, sequenceName = "identifier_id_seq", name = "identifier_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identifier_id_seq")
    ///Commented out by echasin 12/25/2105  Replace by the above 2 lines
    //@GeneratedValue(strategy = GenerationType.AUTO) 
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "value", length = 50, nullable = false)
    private String value;

    @Column(name = "effectivedatetime")
    private ZonedDateTime effectivedatetime;

    @Column(name = "enddatetime")
    private ZonedDateTime enddatetime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "lastmodifiedby")
    private String lastmodifiedby;

    @Column(name = "lastmodifieddate")
    private ZonedDateTime lastmodifieddate;

    @Column(name = "domain")
    private String domain;

    @ManyToOne
    @JoinColumn(name = "objrecordtype_id")
    private Objrecordtype objrecordtype;

    @ManyToOne
    @JoinColumn(name = "objclassification_id")
    private Objclassification objclassification;

    @ManyToOne
    @JoinColumn(name = "objcategory_id")
    private Objcategory objcategory;

    @ManyToOne
    @JoinColumn(name = "objtype_id")
    private Objtype objtype;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "vulnerability_id")
    private Vulnerability vulnerability;

    @ManyToOne
    @JoinColumn(name = "attackscenario_id")
    private Attackscenario attackscenario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getEffectivedatetime() {
        return effectivedatetime;
    }

    public void setEffectivedatetime(ZonedDateTime effectivedatetime) {
        this.effectivedatetime = effectivedatetime;
    }

    public ZonedDateTime getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(ZonedDateTime enddatetime) {
        this.enddatetime = enddatetime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public ZonedDateTime getLastmodifieddate() {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(ZonedDateTime lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Objrecordtype getObjrecordtype() {
        return objrecordtype;
    }

    public void setObjrecordtype(Objrecordtype objrecordtype) {
        this.objrecordtype = objrecordtype;
    }

    public Objclassification getObjclassification() {
        return objclassification;
    }

    public void setObjclassification(Objclassification objclassification) {
        this.objclassification = objclassification;
    }

    public Objcategory getObjcategory() {
        return objcategory;
    }

    public void setObjcategory(Objcategory objcategory) {
        this.objcategory = objcategory;
    }

    public Objtype getObjtype() {
        return objtype;
    }

    public void setObjtype(Objtype objtype) {
        this.objtype = objtype;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Vulnerability getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(Vulnerability vulnerability) {
        this.vulnerability = vulnerability;
    }

    public Attackscenario getAttackscenario() {
        return attackscenario;
    }

    public void setAttackscenario(Attackscenario attackscenario) {
        this.attackscenario = attackscenario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Identifier identifier = (Identifier) o;
        return Objects.equals(id, identifier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Identifier{"
                + "id=" + id
                + ", value='" + value + "'"
                + ", effectivedatetime='" + effectivedatetime + "'"
                + ", enddatetime='" + enddatetime + "'"
                + ", status='" + status + "'"
                + ", lastmodifiedby='" + lastmodifiedby + "'"
                + ", lastmodifieddate='" + lastmodifieddate + "'"
                + ", domain='" + domain + "'"
                + '}';
    }
}
