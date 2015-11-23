package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Location;
import com.innvo.repository.LocationRepository;
import com.innvo.repository.search.LocationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innvo.domain.enumeration.Status;

/**
 * Test class for the LocationResource REST controller.
 *
 * @see LocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LocationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_ISPRIMARY = false;
    private static final Boolean UPDATED_ISPRIMARY = true;
    private static final String DEFAULT_ADDRESS1 = "AAAAA";
    private static final String UPDATED_ADDRESS1 = "BBBBB";
    private static final String DEFAULT_ADDRESS2 = "AAAAA";
    private static final String UPDATED_ADDRESS2 = "BBBBB";
    private static final String DEFAULT_CITYNAME = "AAAAA";
    private static final String UPDATED_CITYNAME = "BBBBB";
    private static final String DEFAULT_CITYNAMEALIAS = "AAAAA";
    private static final String UPDATED_CITYNAMEALIAS = "BBBBB";
    private static final String DEFAULT_COUNTYNAME = "AAAAA";
    private static final String UPDATED_COUNTYNAME = "BBBBB";
    private static final String DEFAULT_COUNTYFIPS = "AAAAA";
    private static final String UPDATED_COUNTYFIPS = "BBBBB";
    private static final String DEFAULT_COUNTYANSI = "AAAAA";
    private static final String UPDATED_COUNTYANSI = "BBBBB";
    private static final String DEFAULT_STATENAME = "AAAAA";
    private static final String UPDATED_STATENAME = "BBBBB";
    private static final String DEFAULT_STATECODE = "AAAAA";
    private static final String UPDATED_STATECODE = "BBBBB";
    private static final String DEFAULT_STATEFIPS = "AAAAA";
    private static final String UPDATED_STATEFIPS = "BBBBB";
    private static final String DEFAULT_STATEISO = "AAAAA";
    private static final String UPDATED_STATEISO = "BBBBB";
    private static final String DEFAULT_STATEANSI = "AAAAA";
    private static final String UPDATED_STATEANSI = "BBBBB";
    private static final String DEFAULT_ZIPPOSTCODE = "AAAAA";
    private static final String UPDATED_ZIPPOSTCODE = "BBBBB";
    private static final String DEFAULT_COUNTRYNAME = "AAAAA";
    private static final String UPDATED_COUNTRYNAME = "BBBBB";
    private static final String DEFAULT_COUNTRYISO2 = "AA";
    private static final String UPDATED_COUNTRYISO2 = "BB";
    private static final String DEFAULT_COUNTRYISO3 = "AAA";
    private static final String UPDATED_COUNTRYISO3 = "BBB";

    private static final Double DEFAULT_LATITUDEDD = 1D;
    private static final Double UPDATED_LATITUDEDD = 2D;

    private static final Double DEFAULT_LONGITUDEDD = 1D;
    private static final Double UPDATED_LONGITUDEDD = 2D;


private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.Pending;
    private static final String DEFAULT_LASTMODIFIEDBY = "AAAAA";
    private static final String UPDATED_LASTMODIFIEDBY = "BBBBB";

    private static final ZonedDateTime DEFAULT_LASTMODIFIEDDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LASTMODIFIEDDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LASTMODIFIEDDATE_STR = dateTimeFormatter.format(DEFAULT_LASTMODIFIEDDATE);
    private static final String DEFAULT_DOMAIN = "AAAAA";
    private static final String UPDATED_DOMAIN = "BBBBB";

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private LocationSearchRepository locationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocationMockMvc;

    private Location location;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationResource locationResource = new LocationResource();
        ReflectionTestUtils.setField(locationResource, "locationRepository", locationRepository);
        ReflectionTestUtils.setField(locationResource, "locationSearchRepository", locationSearchRepository);
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        location = new Location();
        location.setIsprimary(DEFAULT_ISPRIMARY);
        location.setAddress1(DEFAULT_ADDRESS1);
        location.setAddress2(DEFAULT_ADDRESS2);
        location.setCityname(DEFAULT_CITYNAME);
        location.setCitynamealias(DEFAULT_CITYNAMEALIAS);
        location.setCountyname(DEFAULT_COUNTYNAME);
        location.setCountyfips(DEFAULT_COUNTYFIPS);
        location.setCountyansi(DEFAULT_COUNTYANSI);
        location.setStatename(DEFAULT_STATENAME);
        location.setStatecode(DEFAULT_STATECODE);
        location.setStatefips(DEFAULT_STATEFIPS);
        location.setStateiso(DEFAULT_STATEISO);
        location.setStateansi(DEFAULT_STATEANSI);
        location.setZippostcode(DEFAULT_ZIPPOSTCODE);
        location.setCountryname(DEFAULT_COUNTRYNAME);
        location.setCountryiso2(DEFAULT_COUNTRYISO2);
        location.setCountryiso3(DEFAULT_COUNTRYISO3);
        location.setLatitudedd(DEFAULT_LATITUDEDD);
        location.setLongitudedd(DEFAULT_LONGITUDEDD);
        location.setStatus(DEFAULT_STATUS);
        location.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        location.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        location.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createLocation() throws Exception {
        int databaseSizeBeforeCreate = locationRepository.findAll().size();

        // Create the Location

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isCreated());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeCreate + 1);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getIsprimary()).isEqualTo(DEFAULT_ISPRIMARY);
        assertThat(testLocation.getAddress1()).isEqualTo(DEFAULT_ADDRESS1);
        assertThat(testLocation.getAddress2()).isEqualTo(DEFAULT_ADDRESS2);
        assertThat(testLocation.getCityname()).isEqualTo(DEFAULT_CITYNAME);
        assertThat(testLocation.getCitynamealias()).isEqualTo(DEFAULT_CITYNAMEALIAS);
        assertThat(testLocation.getCountyname()).isEqualTo(DEFAULT_COUNTYNAME);
        assertThat(testLocation.getCountyfips()).isEqualTo(DEFAULT_COUNTYFIPS);
        assertThat(testLocation.getCountyansi()).isEqualTo(DEFAULT_COUNTYANSI);
        assertThat(testLocation.getStatename()).isEqualTo(DEFAULT_STATENAME);
        assertThat(testLocation.getStatecode()).isEqualTo(DEFAULT_STATECODE);
        assertThat(testLocation.getStatefips()).isEqualTo(DEFAULT_STATEFIPS);
        assertThat(testLocation.getStateiso()).isEqualTo(DEFAULT_STATEISO);
        assertThat(testLocation.getStateansi()).isEqualTo(DEFAULT_STATEANSI);
        assertThat(testLocation.getZippostcode()).isEqualTo(DEFAULT_ZIPPOSTCODE);
        assertThat(testLocation.getCountryname()).isEqualTo(DEFAULT_COUNTRYNAME);
        assertThat(testLocation.getCountryiso2()).isEqualTo(DEFAULT_COUNTRYISO2);
        assertThat(testLocation.getCountryiso3()).isEqualTo(DEFAULT_COUNTRYISO3);
        assertThat(testLocation.getLatitudedd()).isEqualTo(DEFAULT_LATITUDEDD);
        assertThat(testLocation.getLongitudedd()).isEqualTo(DEFAULT_LONGITUDEDD);
        assertThat(testLocation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocation.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testLocation.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testLocation.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationRepository.findAll().size();
        // set the field null
        location.setStatus(null);

        // Create the Location, which fails.

        restLocationMockMvc.perform(post("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isBadRequest());

        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocations() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get all the locations
        restLocationMockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
                .andExpect(jsonPath("$.[*].isprimary").value(hasItem(DEFAULT_ISPRIMARY.booleanValue())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS2.toString())))
                .andExpect(jsonPath("$.[*].cityname").value(hasItem(DEFAULT_CITYNAME.toString())))
                .andExpect(jsonPath("$.[*].citynamealias").value(hasItem(DEFAULT_CITYNAMEALIAS.toString())))
                .andExpect(jsonPath("$.[*].countyname").value(hasItem(DEFAULT_COUNTYNAME.toString())))
                .andExpect(jsonPath("$.[*].countyfips").value(hasItem(DEFAULT_COUNTYFIPS.toString())))
                .andExpect(jsonPath("$.[*].countyansi").value(hasItem(DEFAULT_COUNTYANSI.toString())))
                .andExpect(jsonPath("$.[*].statename").value(hasItem(DEFAULT_STATENAME.toString())))
                .andExpect(jsonPath("$.[*].statecode").value(hasItem(DEFAULT_STATECODE.toString())))
                .andExpect(jsonPath("$.[*].statefips").value(hasItem(DEFAULT_STATEFIPS.toString())))
                .andExpect(jsonPath("$.[*].stateiso").value(hasItem(DEFAULT_STATEISO.toString())))
                .andExpect(jsonPath("$.[*].stateansi").value(hasItem(DEFAULT_STATEANSI.toString())))
                .andExpect(jsonPath("$.[*].zippostcode").value(hasItem(DEFAULT_ZIPPOSTCODE.toString())))
                .andExpect(jsonPath("$.[*].countryname").value(hasItem(DEFAULT_COUNTRYNAME.toString())))
                .andExpect(jsonPath("$.[*].countryiso2").value(hasItem(DEFAULT_COUNTRYISO2.toString())))
                .andExpect(jsonPath("$.[*].countryiso3").value(hasItem(DEFAULT_COUNTRYISO3.toString())))
                .andExpect(jsonPath("$.[*].latitudedd").value(hasItem(DEFAULT_LATITUDEDD.doubleValue())))
                .andExpect(jsonPath("$.[*].longitudedd").value(hasItem(DEFAULT_LONGITUDEDD.doubleValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.isprimary").value(DEFAULT_ISPRIMARY.booleanValue()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS2.toString()))
            .andExpect(jsonPath("$.cityname").value(DEFAULT_CITYNAME.toString()))
            .andExpect(jsonPath("$.citynamealias").value(DEFAULT_CITYNAMEALIAS.toString()))
            .andExpect(jsonPath("$.countyname").value(DEFAULT_COUNTYNAME.toString()))
            .andExpect(jsonPath("$.countyfips").value(DEFAULT_COUNTYFIPS.toString()))
            .andExpect(jsonPath("$.countyansi").value(DEFAULT_COUNTYANSI.toString()))
            .andExpect(jsonPath("$.statename").value(DEFAULT_STATENAME.toString()))
            .andExpect(jsonPath("$.statecode").value(DEFAULT_STATECODE.toString()))
            .andExpect(jsonPath("$.statefips").value(DEFAULT_STATEFIPS.toString()))
            .andExpect(jsonPath("$.stateiso").value(DEFAULT_STATEISO.toString()))
            .andExpect(jsonPath("$.stateansi").value(DEFAULT_STATEANSI.toString()))
            .andExpect(jsonPath("$.zippostcode").value(DEFAULT_ZIPPOSTCODE.toString()))
            .andExpect(jsonPath("$.countryname").value(DEFAULT_COUNTRYNAME.toString()))
            .andExpect(jsonPath("$.countryiso2").value(DEFAULT_COUNTRYISO2.toString()))
            .andExpect(jsonPath("$.countryiso3").value(DEFAULT_COUNTRYISO3.toString()))
            .andExpect(jsonPath("$.latitudedd").value(DEFAULT_LATITUDEDD.doubleValue()))
            .andExpect(jsonPath("$.longitudedd").value(DEFAULT_LONGITUDEDD.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

		int databaseSizeBeforeUpdate = locationRepository.findAll().size();

        // Update the location
        location.setIsprimary(UPDATED_ISPRIMARY);
        location.setAddress1(UPDATED_ADDRESS1);
        location.setAddress2(UPDATED_ADDRESS2);
        location.setCityname(UPDATED_CITYNAME);
        location.setCitynamealias(UPDATED_CITYNAMEALIAS);
        location.setCountyname(UPDATED_COUNTYNAME);
        location.setCountyfips(UPDATED_COUNTYFIPS);
        location.setCountyansi(UPDATED_COUNTYANSI);
        location.setStatename(UPDATED_STATENAME);
        location.setStatecode(UPDATED_STATECODE);
        location.setStatefips(UPDATED_STATEFIPS);
        location.setStateiso(UPDATED_STATEISO);
        location.setStateansi(UPDATED_STATEANSI);
        location.setZippostcode(UPDATED_ZIPPOSTCODE);
        location.setCountryname(UPDATED_COUNTRYNAME);
        location.setCountryiso2(UPDATED_COUNTRYISO2);
        location.setCountryiso3(UPDATED_COUNTRYISO3);
        location.setLatitudedd(UPDATED_LATITUDEDD);
        location.setLongitudedd(UPDATED_LONGITUDEDD);
        location.setStatus(UPDATED_STATUS);
        location.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        location.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        location.setDomain(UPDATED_DOMAIN);

        restLocationMockMvc.perform(put("/api/locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(location)))
                .andExpect(status().isOk());

        // Validate the Location in the database
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeUpdate);
        Location testLocation = locations.get(locations.size() - 1);
        assertThat(testLocation.getIsprimary()).isEqualTo(UPDATED_ISPRIMARY);
        assertThat(testLocation.getAddress1()).isEqualTo(UPDATED_ADDRESS1);
        assertThat(testLocation.getAddress2()).isEqualTo(UPDATED_ADDRESS2);
        assertThat(testLocation.getCityname()).isEqualTo(UPDATED_CITYNAME);
        assertThat(testLocation.getCitynamealias()).isEqualTo(UPDATED_CITYNAMEALIAS);
        assertThat(testLocation.getCountyname()).isEqualTo(UPDATED_COUNTYNAME);
        assertThat(testLocation.getCountyfips()).isEqualTo(UPDATED_COUNTYFIPS);
        assertThat(testLocation.getCountyansi()).isEqualTo(UPDATED_COUNTYANSI);
        assertThat(testLocation.getStatename()).isEqualTo(UPDATED_STATENAME);
        assertThat(testLocation.getStatecode()).isEqualTo(UPDATED_STATECODE);
        assertThat(testLocation.getStatefips()).isEqualTo(UPDATED_STATEFIPS);
        assertThat(testLocation.getStateiso()).isEqualTo(UPDATED_STATEISO);
        assertThat(testLocation.getStateansi()).isEqualTo(UPDATED_STATEANSI);
        assertThat(testLocation.getZippostcode()).isEqualTo(UPDATED_ZIPPOSTCODE);
        assertThat(testLocation.getCountryname()).isEqualTo(UPDATED_COUNTRYNAME);
        assertThat(testLocation.getCountryiso2()).isEqualTo(UPDATED_COUNTRYISO2);
        assertThat(testLocation.getCountryiso3()).isEqualTo(UPDATED_COUNTRYISO3);
        assertThat(testLocation.getLatitudedd()).isEqualTo(UPDATED_LATITUDEDD);
        assertThat(testLocation.getLongitudedd()).isEqualTo(UPDATED_LONGITUDEDD);
        assertThat(testLocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocation.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testLocation.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testLocation.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteLocation() throws Exception {
        // Initialize the database
        locationRepository.saveAndFlush(location);

		int databaseSizeBeforeDelete = locationRepository.findAll().size();

        // Get the location
        restLocationMockMvc.perform(delete("/api/locations/{id}", location.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Location> locations = locationRepository.findAll();
        assertThat(locations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
