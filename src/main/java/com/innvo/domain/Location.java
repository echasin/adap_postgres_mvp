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
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.innvo.domain.enumeration.Status;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "location")
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "isprimary")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Boolean isprimary;

    @Size(max = 100)
    @Column(name = "address1", length = 100)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String address1;

    @Size(max = 100)
    @Column(name = "address2", length = 100)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String address2;

    @Size(max = 50)
    @Column(name = "cityname", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String cityname;

    @Size(max = 50)
    @Column(name = "citynamealias", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String citynamealias;

    @Size(max = 50)
    @Column(name = "countyname", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countyname;

    @Size(max = 50)
    @Column(name = "countyfips", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countyfips;

    @Size(max = 50)
    @Column(name = "countyansi", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countyansi;

    @Size(max = 50)
    @Column(name = "statename", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String statename;

    @Size(max = 50)
    @Column(name = "statecode", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String statecode;

    @Size(max = 50)
    @Column(name = "statefips", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String statefips;

    @Size(max = 50)
    @Column(name = "stateiso", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String stateiso;

    @Size(max = 50)
    @Column(name = "stateansi", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String stateansi;

    @Size(max = 50)
    @Column(name = "zippostcode", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String zippostcode;

    @Size(max = 50)
    @Column(name = "countryname", length = 50)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryname;

    @Size(max = 2)
    @Column(name = "countryiso2", length = 2)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryiso2;

    @Size(max = 3)
    @Column(name = "countryiso3", length = 3)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryiso3;

    @Column(name = "latitudedd")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Double latitudedd;

    @Column(name = "longitudedd")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Double longitudedd;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private Status status;

    @Column(name = "lastmodifiedby")
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String lastmodifiedby;

    @Column(name = "lastmodifieddate", nullable = false)
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
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

    @ManyToOne
    @Field(type = FieldType.Object, index = FieldIndex.not_analyzed)
    private Asset asset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsprimary() {
        return isprimary;
    }

    public void setIsprimary(Boolean isprimary) {
        this.isprimary = isprimary;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitynamealias() {
        return citynamealias;
    }

    public void setCitynamealias(String citynamealias) {
        this.citynamealias = citynamealias;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public String getCountyfips() {
        return countyfips;
    }

    public void setCountyfips(String countyfips) {
        this.countyfips = countyfips;
    }

    public String getCountyansi() {
        return countyansi;
    }

    public void setCountyansi(String countyansi) {
        this.countyansi = countyansi;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getStatefips() {
        return statefips;
    }

    public void setStatefips(String statefips) {
        this.statefips = statefips;
    }

    public String getStateiso() {
        return stateiso;
    }

    public void setStateiso(String stateiso) {
        this.stateiso = stateiso;
    }

    public String getStateansi() {
        return stateansi;
    }

    public void setStateansi(String stateansi) {
        this.stateansi = stateansi;
    }

    public String getZippostcode() {
        return zippostcode;
    }

    public void setZippostcode(String zippostcode) {
        this.zippostcode = zippostcode;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountryiso2() {
        return countryiso2;
    }

    public void setCountryiso2(String countryiso2) {
        this.countryiso2 = countryiso2;
    }

    public String getCountryiso3() {
        return countryiso3;
    }

    public void setCountryiso3(String countryiso3) {
        this.countryiso3 = countryiso3;
    }

    public Double getLatitudedd() {
        return latitudedd;
    }

    public void setLatitudedd(Double latitudedd) {
        this.latitudedd = latitudedd;
    }

    public Double getLongitudedd() {
        return longitudedd;
    }

    public void setLongitudedd(Double longitudedd) {
        this.longitudedd = longitudedd;
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

        Location location = (Location) o;

        if ( ! Objects.equals(id, location.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", isprimary='" + isprimary + "'" +
            ", address1='" + address1 + "'" +
            ", address2='" + address2 + "'" +
            ", cityname='" + cityname + "'" +
            ", citynamealias='" + citynamealias + "'" +
            ", countyname='" + countyname + "'" +
            ", countyfips='" + countyfips + "'" +
            ", countyansi='" + countyansi + "'" +
            ", statename='" + statename + "'" +
            ", statecode='" + statecode + "'" +
            ", statefips='" + statefips + "'" +
            ", stateiso='" + stateiso + "'" +
            ", stateansi='" + stateansi + "'" +
            ", zippostcode='" + zippostcode + "'" +
            ", countryname='" + countryname + "'" +
            ", countryiso2='" + countryiso2 + "'" +
            ", countryiso3='" + countryiso3 + "'" +
            ", latitudedd='" + latitudedd + "'" +
            ", longitudedd='" + longitudedd + "'" +
            ", status='" + status + "'" +
            ", lastmodifiedby='" + lastmodifiedby + "'" +
            ", lastmodifieddate='" + lastmodifieddate + "'" +
            ", domain='" + domain + "'" +
            '}';
    }
}
