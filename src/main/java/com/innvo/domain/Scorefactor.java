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
 * A Scorefactor.
 */
@Entity
@Table(name = "scorefactor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scorefactor")
public class Scorefactor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @Size(max = 100)
    @Column(name = "matchattribute", length = 100)
    private String matchattribute;

    @Size(max = 100)
    @Column(name = "matchvalue", length = 100)
    private String matchvalue;

    @Column(name = "scorevalue")
    private Double scorevalue;

    @Size(max = 100)
    @Column(name = "scoretext", length = 100)
    private String scoretext;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatchattribute() {
        return matchattribute;
    }

    public void setMatchattribute(String matchattribute) {
        this.matchattribute = matchattribute;
    }

    public String getMatchvalue() {
        return matchvalue;
    }

    public void setMatchvalue(String matchvalue) {
        this.matchvalue = matchvalue;
    }

    public Double getScorevalue() {
        return scorevalue;
    }

    public void setScorevalue(Double scorevalue) {
        this.scorevalue = scorevalue;
    }

    public String getScoretext() {
        return scoretext;
    }

    public void setScoretext(String scoretext) {
        this.scoretext = scoretext;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scorefactor scorefactor = (Scorefactor) o;
        return Objects.equals(id, scorefactor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scorefactor{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", matchattribute='" + matchattribute + "'" +
            ", matchvalue='" + matchvalue + "'" +
            ", scorevalue='" + scorevalue + "'" +
            ", scoretext='" + scoretext + "'" +
            ", status='" + status + "'" +
            ", lastmodifiedby='" + lastmodifiedby + "'" +
            ", lastmodifieddate='" + lastmodifieddate + "'" +
            ", domain='" + domain + "'" +
            '}';
    }
}
