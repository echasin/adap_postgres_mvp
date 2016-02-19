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
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "asset")
public class Asset implements Serializable {

    @Id
    @SequenceGenerator(allocationSize = 1, initialValue = 100000, sequenceName = "asset_id_seq", name = "asset_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_id_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String name;

    //@NotNull
    @Size(max = 20)
    @Column(name = "nameshort", length = 20, nullable = true)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String nameshort;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String description;

    @Column(name = "details")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Status status;

    @Column(name = "lastmodifiedby")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lastmodifiedby;

    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    //@JsonSerialize(using = CustomDateTimeSerializer.class)
    //@JsonDeserialize(using = CustomDateTimeDeserializer.class)
    //@Column(name = "lastmodifieddate")
    //private DateTime lastmodifieddate;
    @Column(name = "lastmodifieddate", nullable = false)
    private ZonedDateTime lastmodifieddate;

    @Column(name = "domain")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String domain;

    @ManyToOne
    @Field(type = FieldType.Object, index = FieldIndex.not_analyzed)
    private Objrecordtype objrecordtype;

    @ManyToOne
    @Field(type = FieldType.Object, index = FieldIndex.not_analyzed)
    private Objclassification objclassification;

    @ManyToOne
    @Field(type = FieldType.Object, index = FieldIndex.not_analyzed)
    private Objcategory objcategory;

    @ManyToOne
    @Field(type = FieldType.Object, index = FieldIndex.not_analyzed)
    private Objtype objtype;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Location> locations = new HashSet<>();
    
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "asset")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Score> scores = new HashSet<>();

    @OneToMany(mappedBy = "id")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Eventmbr> eventmbrs = new HashSet<>();

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

    public String getNameshort() {
        return nameshort;
    }

    public void setNameshort(String nameshort) {
        this.nameshort = nameshort;
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

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Set<Eventmbr> getEventmbrs() {
        return eventmbrs;
    }

    public void setEventmbrs(Set<Eventmbr> eventmbrs) {
        this.eventmbrs = eventmbrs;
    }
    
    

    public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Asset asset = (Asset) o;

        if (!Objects.equals(id, asset.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Asset{"
                + "id=" + id
                + ", name='" + name + "'"
                + ", description='" + description + "'"
                + ", details='" + details + "'"
                + ", status='" + status + "'"
                + ", lastmodifiedby='" + lastmodifiedby + "'"
                + ", lastmodifieddate='" + getLastmodifieddate() + "'"
                + ", domain='" + domain + "'"
                + '}';
    }

  

}
