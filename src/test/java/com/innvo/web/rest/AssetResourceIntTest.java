package com.innvo.web.rest;

import com.innvo.Application;
import com.innvo.domain.Asset;
import com.innvo.repository.AssetRepository;
import com.innvo.repository.search.AssetSearchRepository;

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
 * Test class for the AssetResource REST controller.
 *
 * @see AssetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AssetResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_DETAILS = "AAAAA";
    private static final String UPDATED_DETAILS = "BBBBB";


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
    private AssetRepository assetRepository;

    @Inject
    private AssetSearchRepository assetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAssetMockMvc;

    private Asset asset;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssetResource assetResource = new AssetResource();
        ReflectionTestUtils.setField(assetResource, "assetRepository", assetRepository);
        ReflectionTestUtils.setField(assetResource, "assetSearchRepository", assetSearchRepository);
        this.restAssetMockMvc = MockMvcBuilders.standaloneSetup(assetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        asset = new Asset();
        asset.setName(DEFAULT_NAME);
        asset.setDescription(DEFAULT_DESCRIPTION);
        asset.setDetails(DEFAULT_DETAILS);
        asset.setStatus(DEFAULT_STATUS);
        asset.setLastmodifiedby(DEFAULT_LASTMODIFIEDBY);
        asset.setLastmodifieddate(DEFAULT_LASTMODIFIEDDATE);
        asset.setDomain(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // Create the Asset

        restAssetMockMvc.perform(post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asset)))
                .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assets.get(assets.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAsset.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testAsset.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAsset.getLastmodifiedby()).isEqualTo(DEFAULT_LASTMODIFIEDBY);
        assertThat(testAsset.getLastmodifieddate()).isEqualTo(DEFAULT_LASTMODIFIEDDATE);
        assertThat(testAsset.getDomain()).isEqualTo(DEFAULT_DOMAIN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setName(null);

        // Create the Asset, which fails.

        restAssetMockMvc.perform(post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asset)))
                .andExpect(status().isBadRequest());

        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setStatus(null);

        // Create the Asset, which fails.

        restAssetMockMvc.perform(post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asset)))
                .andExpect(status().isBadRequest());

        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assets
        restAssetMockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].lastmodifiedby").value(hasItem(DEFAULT_LASTMODIFIEDBY.toString())))
                .andExpect(jsonPath("$.[*].lastmodifieddate").value(hasItem(DEFAULT_LASTMODIFIEDDATE_STR)))
                .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())));
    }

    @Test
    @Transactional
    public void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastmodifiedby").value(DEFAULT_LASTMODIFIEDBY.toString()))
            .andExpect(jsonPath("$.lastmodifieddate").value(DEFAULT_LASTMODIFIEDDATE_STR))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

		int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        asset.setName(UPDATED_NAME);
        asset.setDescription(UPDATED_DESCRIPTION);
        asset.setDetails(UPDATED_DETAILS);
        asset.setStatus(UPDATED_STATUS);
        asset.setLastmodifiedby(UPDATED_LASTMODIFIEDBY);
        asset.setLastmodifieddate(UPDATED_LASTMODIFIEDDATE);
        asset.setDomain(UPDATED_DOMAIN);

        restAssetMockMvc.perform(put("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asset)))
                .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assets.get(assets.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAsset.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testAsset.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAsset.getLastmodifiedby()).isEqualTo(UPDATED_LASTMODIFIEDBY);
        assertThat(testAsset.getLastmodifieddate()).isEqualTo(UPDATED_LASTMODIFIEDDATE);
        assertThat(testAsset.getDomain()).isEqualTo(UPDATED_DOMAIN);
    }

    @Test
    @Transactional
    public void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

		int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Get the asset
        restAssetMockMvc.perform(delete("/api/assets/{id}", asset.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
