package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Eventmbr;
import com.innvo.repository.EventmbrRepository;
import com.innvo.repository.search.EventmbrSearchRepository;

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
 * Test class for the EventmbrResource REST controller.
 *
 * @see EventmbrResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EventmbrResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));



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
    private EventmbrRepository eventmbrRepository;

    @Inject
    private EventmbrSearchRepository eventmbrSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEventmbrMockMvc;

    private Eventmbr eventmbr;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventmbrResource eventmbrResource = new EventmbrResource();
        ReflectionTestUtils.setField(eventmbrResource, "eventmbrSearchRepository", eventmbrSearchRepository);
        ReflectionTestUtils.setField(eventmbrResource, "eventmbrRepository", eventmbrRepository);
        this.restEventmbrMockMvc = MockMvcBuilders.standaloneSetup(eventmbrResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        eventmbr = new Eventmbr();
        eventmbr.setStatus(DEFAULT_STATUS);
        eventmbr.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        eventmbr.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        eventmbr.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createEventmbr() throws Exception {
        int databaseSizeBeforeCreate = eventmbrRepository.findAll().size();

        // Create the Eventmbr

        restEventmbrMockMvc.perform(post("/api/eventmbrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventmbr)))
                .andExpect(status().isCreated());

        // Validate the Eventmbr in the database
        List<Eventmbr> eventmbrs = eventmbrRepository.findAll();
        assertThat(eventmbrs).hasSize(databaseSizeBeforeCreate + 1);
        Eventmbr testEventmbr = eventmbrs.get(eventmbrs.size() - 1);
        assertThat(testEventmbr.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventmbr.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testEventmbr.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testEventmbr.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventmbrRepository.findAll().size();
        // set the field null
        eventmbr.setStatus(null);

        // Create the Eventmbr, which fails.

        restEventmbrMockMvc.perform(post("/api/eventmbrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventmbr)))
                .andExpect(status().isBadRequest());

        List<Eventmbr> eventmbrs = eventmbrRepository.findAll();
        assertThat(eventmbrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventmbrRepository.findAll().size();
        // set the field null
        eventmbr.setDomain(null);

        // Create the Eventmbr, which fails.

        restEventmbrMockMvc.perform(post("/api/eventmbrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventmbr)))
                .andExpect(status().isBadRequest());

        List<Eventmbr> eventmbrs = eventmbrRepository.findAll();
        assertThat(eventmbrs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventmbrs() throws Exception {
        // Initialize the database
        eventmbrRepository.saveAndFlush(eventmbr);

        // Get all the eventmbrs
        restEventmbrMockMvc.perform(get("/api/eventmbrs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(eventmbr.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getEventmbr() throws Exception {
        // Initialize the database
        eventmbrRepository.saveAndFlush(eventmbr);

        // Get the eventmbr
        restEventmbrMockMvc.perform(get("/api/eventmbrs/{id}", eventmbr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(eventmbr.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventmbr() throws Exception {
        // Get the eventmbr
        restEventmbrMockMvc.perform(get("/api/eventmbrs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventmbr() throws Exception {
        // Initialize the database
        eventmbrRepository.saveAndFlush(eventmbr);

		int databaseSizeBeforeUpdate = eventmbrRepository.findAll().size();

        // Update the eventmbr
        eventmbr.setStatus(UPDATED_STATUS);
        eventmbr.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        eventmbr.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        eventmbr.setDomain(UPDATED_DOMAIN);

        restEventmbrMockMvc.perform(put("/api/eventmbrs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(eventmbr)))
                .andExpect(status().isOk());

        // Validate the Eventmbr in the database
        List<Eventmbr> eventmbrs = eventmbrRepository.findAll();
        assertThat(eventmbrs).hasSize(databaseSizeBeforeUpdate);
        Eventmbr testEventmbr = eventmbrs.get(eventmbrs.size() - 1);
        assertThat(testEventmbr.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventmbr.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testEventmbr.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testEventmbr.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteEventmbr() throws Exception {
        // Initialize the database
        eventmbrRepository.saveAndFlush(eventmbr);

		int databaseSizeBeforeDelete = eventmbrRepository.findAll().size();

        // Get the eventmbr
        restEventmbrMockMvc.perform(delete("/api/eventmbrs/{id}", eventmbr.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Eventmbr> eventmbrs = eventmbrRepository.findAll();
        assertThat(eventmbrs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
