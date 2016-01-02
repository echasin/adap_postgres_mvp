package com.innvo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.innvo.domain.enumeration.Status;

/**
 * A Segment.
 */
@Entity
@Table(name = "segment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "segment")
public class Segment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String name;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String description;

    @Column(name = "origineta")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private ZonedDateTime origineta;

    @Column(name = "origin_ata")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private ZonedDateTime originAta;

    @Column(name = "destinationeta")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private ZonedDateTime destinationeta;

    @Column(name = "destinationata")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private ZonedDateTime destinationata;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Status status;

    @Column(name = "lastmodifiedby")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lastmodifiedby;

    @Column(name = "lastmodifieddate")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private ZonedDateTime lastmodifieddate;

    @Column(name = "domain")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
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
    @JoinColumn(name = "assetorigin_id")
    private Asset assetorigin;

    @ManyToOne
    @JoinColumn(name = "assetdestination_id")
    private Asset assetdestination;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

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

    public ZonedDateTime getOrigineta() {
        return origineta;
    }

    public void setOrigineta(ZonedDateTime origineta) {
        this.origineta = origineta;
    }

    public ZonedDateTime getOriginAta() {
        return originAta;
    }

    public void setOriginAta(ZonedDateTime originAta) {
        this.originAta = originAta;
    }

    public ZonedDateTime getDestinationeta() {
        return destinationeta;
    }

    public void setDestinationeta(ZonedDateTime destinationeta) {
        this.destinationeta = destinationeta;
    }

    public ZonedDateTime getDestinationata() {
        return destinationata;
    }

    public void setDestinationata(ZonedDateTime destinationata) {
        this.destinationata = destinationata;
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

    public Asset getAssetorigin() {
        return assetorigin;
    }

    public void setAssetorigin(Asset asset) {
        this.assetorigin = asset;
    }

    public Asset getAssetdestination() {
        return assetdestination;
    }

    public void setAssetdestination(Asset asset) {
        this.assetdestination = asset;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Segment segment = (Segment) o;
        return Objects.equals(id, segment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Segment{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", origineta='" + origineta + "'" +
            ", originAta='" + originAta + "'" +
            ", destinationeta='" + destinationeta + "'" +
            ", destinationata='" + destinationata + "'" +
            ", status='" + status + "'" +
            ", lastmodifiedby='" + lastmodifiedby + "'" +
            ", lastmodifieddate='" + lastmodifieddate + "'" +
            ", domain='" + domain + "'" +
            '}';
    }
}
