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
 * A Score.
 */
@Entity
@Table(name = "score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "score")
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private Double value;

    @Size(max = 50)
    @Column(name = "text", length = 50)
    private String text;

    @Size(max = 100)
    @Column(name = "rulename", length = 100)
    private String rulename;

    @Column(name = "runid")
    private Long runid;
    
    @Column(name = "rundate", nullable = false)
    private ZonedDateTime rundate;


    @Size(max = 25)
    @Column(name = "ruleversion", length = 25)
    private String ruleversion;

    @Column(name = "details")
    private String details;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "lastmodifiedby")
    private String lastmodifiedby;

    @Column(name = "lastmodifieddate", nullable = false)
    private ZonedDateTime lastmodifieddate;

    @Column(name = "domain")
    private String domain;

    @ManyToOne
    private Objrecordtype objrecordtype;

    @ManyToOne
    private Objclassification objclassification;

    @ManyToOne
    private Objcategory objcategory;

    @ManyToOne
    private Objtype objtype;

    @ManyToOne
    private Asset asset;
    
    @ManyToOne
    private Route route;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }

    public String getRuleversion() {
        return ruleversion;
    }

    public void setRuleversion(String ruleversion) {
        this.ruleversion = ruleversion;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Score score = (Score) o;

        if ( ! Objects.equals(id, score.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", text='" + text + "'" +
            ", rulename='" + rulename + "'" +
            ", ruleversion='" + ruleversion + "'" +
            ", details='" + details + "'" +
            ", status='" + status + "'" +
            ", lastmodifiedby='" + lastmodifiedby + "'" +
            ", lastmodifieddate='" + lastmodifieddate + "'" +
            ", domain='" + domain + "'" +
            '}';
    }

    /**
     * @param runid the runid to set
     */
    public void setRunid(Long runid) {
        this.runid = runid;
    }

    /**
     * @param rundate the rundate to set
     */
    public void setRundate(ZonedDateTime rundate) {
        this.rundate = rundate;
    }

    /**
     * @return the runid
     */
    public Long getRunid() {
        return runid;
    }

    /**
     * @return the rundate
     */
    public ZonedDateTime getRundate() {
        return rundate;
    }

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }
}
