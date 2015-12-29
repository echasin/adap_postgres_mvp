package com.innvo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import org.springframework.data.elasticsearch.annotations.Document;
import com.innvo.domain.util.CustomDateTimeDeserializer;
import com.innvo.domain.util.CustomDateTimeSerializer;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innvo.domain.enumeration.Status;
import java.time.ZonedDateTime;

/**
 * A Filter.
 */
@Entity
@Table(name = "filter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "filter")
public class Filter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 4000)
    @Column(name = "querysql", length = 4000)
    private String querysql;

    @Size(max = 4000)
    @Column(name = "queryspringdata", length = 4000)
    private String queryspringdata;

    @Size(max = 4000)
    @Column(name = "queryelastic", length = 4000)
    private String queryelastic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "lastmodifiedby")
    private String lastmodifiedby;

    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    //@JsonSerialize(using = CustomDateTimeSerializer.class)
    //@JsonDeserialize(using = CustomDateTimeDeserializer.class)
    //@Column(name = "lastmodifieddate")
    //private DateTime lastmodifieddate;
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
    private Location location;

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

    public String getQuerysql() {
        return querysql;
    }

    public void setQuerysql(String querysql) {
        this.querysql = querysql;
    }

    public String getQueryspringdata() {
        return queryspringdata;
    }

    public void setQueryspringdata(String queryspringdata) {
        this.queryspringdata = queryspringdata;
    }

    public String getQueryelastic() {
        return queryelastic;
    }

    public void setQueryelastic(String queryelastic) {
        this.queryelastic = queryelastic;
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

/**
    public DateTime getLastmodifieddate() {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(DateTime lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }
**/
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Filter filter = (Filter) o;

        if (!Objects.equals(id, filter.id)) {
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
        return "Filter{"
                + "id=" + id
                + ", name='" + name + "'"
                + ", description='" + description + "'"
                + ", querysql='" + querysql + "'"
                + ", queryspringdata='" + queryspringdata + "'"
                + ", queryelastic='" + queryelastic + "'"
                + ", status='" + status + "'"
                + ", lastmodifiedby='" + lastmodifiedby + "'"
                + ", lastmodifieddate='" + lastmodifieddate + "'"
                + ", domain='" + domain + "'"
                + '}';
    }
}
