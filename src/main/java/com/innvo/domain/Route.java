package com.innvo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Route.
 */
@Entity
@Table(name = "route")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "route")
public class Route implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "details")
    private String details;

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

    @OneToMany(mappedBy = "route")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Segment> segments = new HashSet<>();
    
    @OneToMany(mappedBy = "route")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Score> scores = new HashSet<>();


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

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Route{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", details='" + details + "'" +
            ", status='" + status + "'" +
            ", lastmodifiedby='" + lastmodifiedby + "'" +
            ", lastmodifieddate='" + lastmodifieddate + "'" +
            ", domain='" + domain + "'" +
            '}';
    }

    /**
     * @return the scores
     */
    public Set<Score> getScores() {
        return scores;
    }

    /**
     * @param scores the scores to set
     */
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }
}
