package es.urjc.etsii.labtel.web.rest;

import es.urjc.etsii.labtel.LabtelApp;

import es.urjc.etsii.labtel.domain.ProjectItem;
import es.urjc.etsii.labtel.repository.ProjectItemRepository;
import es.urjc.etsii.labtel.repository.search.ProjectItemSearchRepository;
import es.urjc.etsii.labtel.service.ProjectItemService;
import es.urjc.etsii.labtel.service.dto.ProjectItemDTO;
import es.urjc.etsii.labtel.service.mapper.ProjectItemMapper;
import es.urjc.etsii.labtel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static es.urjc.etsii.labtel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectItemResource REST controller.
 *
 * @see ProjectItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabtelApp.class)
public class ProjectItemResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Mock
    private ProjectItemRepository projectItemRepositoryMock;

    @Autowired
    private ProjectItemMapper projectItemMapper;

    @Mock
    private ProjectItemService projectItemServiceMock;

    @Autowired
    private ProjectItemService projectItemService;

    /**
     * This repository is mocked in the es.urjc.etsii.labtel.repository.search test package.
     *
     * @see es.urjc.etsii.labtel.repository.search.ProjectItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectItemSearchRepository mockProjectItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectItemMockMvc;

    private ProjectItem projectItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectItemResource projectItemResource = new ProjectItemResource(projectItemService);
        this.restProjectItemMockMvc = MockMvcBuilders.standaloneSetup(projectItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectItem createEntity(EntityManager em) {
        ProjectItem projectItem = new ProjectItem()
            .quantity(DEFAULT_QUANTITY);
        return projectItem;
    }

    @Before
    public void initTest() {
        projectItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectItem() throws Exception {
        int databaseSizeBeforeCreate = projectItemRepository.findAll().size();

        // Create the ProjectItem
        ProjectItemDTO projectItemDTO = projectItemMapper.toDto(projectItem);
        restProjectItemMockMvc.perform(post("/api/project-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectItem in the database
        List<ProjectItem> projectItemList = projectItemRepository.findAll();
        assertThat(projectItemList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectItem testProjectItem = projectItemList.get(projectItemList.size() - 1);
        assertThat(testProjectItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the ProjectItem in Elasticsearch
        verify(mockProjectItemSearchRepository, times(1)).save(testProjectItem);
    }

    @Test
    @Transactional
    public void createProjectItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectItemRepository.findAll().size();

        // Create the ProjectItem with an existing ID
        projectItem.setId(1L);
        ProjectItemDTO projectItemDTO = projectItemMapper.toDto(projectItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectItemMockMvc.perform(post("/api/project-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectItem in the database
        List<ProjectItem> projectItemList = projectItemRepository.findAll();
        assertThat(projectItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProjectItem in Elasticsearch
        verify(mockProjectItemSearchRepository, times(0)).save(projectItem);
    }

    @Test
    @Transactional
    public void getAllProjectItems() throws Exception {
        // Initialize the database
        projectItemRepository.saveAndFlush(projectItem);

        // Get all the projectItemList
        restProjectItemMockMvc.perform(get("/api/project-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProjectItemsWithEagerRelationshipsIsEnabled() throws Exception {
        ProjectItemResource projectItemResource = new ProjectItemResource(projectItemServiceMock);
        when(projectItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProjectItemMockMvc = MockMvcBuilders.standaloneSetup(projectItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProjectItemMockMvc.perform(get("/api/project-items?eagerload=true"))
        .andExpect(status().isOk());

        verify(projectItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProjectItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProjectItemResource projectItemResource = new ProjectItemResource(projectItemServiceMock);
            when(projectItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProjectItemMockMvc = MockMvcBuilders.standaloneSetup(projectItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProjectItemMockMvc.perform(get("/api/project-items?eagerload=true"))
        .andExpect(status().isOk());

            verify(projectItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProjectItem() throws Exception {
        // Initialize the database
        projectItemRepository.saveAndFlush(projectItem);

        // Get the projectItem
        restProjectItemMockMvc.perform(get("/api/project-items/{id}", projectItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingProjectItem() throws Exception {
        // Get the projectItem
        restProjectItemMockMvc.perform(get("/api/project-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectItem() throws Exception {
        // Initialize the database
        projectItemRepository.saveAndFlush(projectItem);

        int databaseSizeBeforeUpdate = projectItemRepository.findAll().size();

        // Update the projectItem
        ProjectItem updatedProjectItem = projectItemRepository.findById(projectItem.getId()).get();
        // Disconnect from session so that the updates on updatedProjectItem are not directly saved in db
        em.detach(updatedProjectItem);
        updatedProjectItem
            .quantity(UPDATED_QUANTITY);
        ProjectItemDTO projectItemDTO = projectItemMapper.toDto(updatedProjectItem);

        restProjectItemMockMvc.perform(put("/api/project-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectItemDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectItem in the database
        List<ProjectItem> projectItemList = projectItemRepository.findAll();
        assertThat(projectItemList).hasSize(databaseSizeBeforeUpdate);
        ProjectItem testProjectItem = projectItemList.get(projectItemList.size() - 1);
        assertThat(testProjectItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the ProjectItem in Elasticsearch
        verify(mockProjectItemSearchRepository, times(1)).save(testProjectItem);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectItem() throws Exception {
        int databaseSizeBeforeUpdate = projectItemRepository.findAll().size();

        // Create the ProjectItem
        ProjectItemDTO projectItemDTO = projectItemMapper.toDto(projectItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectItemMockMvc.perform(put("/api/project-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectItem in the database
        List<ProjectItem> projectItemList = projectItemRepository.findAll();
        assertThat(projectItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProjectItem in Elasticsearch
        verify(mockProjectItemSearchRepository, times(0)).save(projectItem);
    }

    @Test
    @Transactional
    public void deleteProjectItem() throws Exception {
        // Initialize the database
        projectItemRepository.saveAndFlush(projectItem);

        int databaseSizeBeforeDelete = projectItemRepository.findAll().size();

        // Get the projectItem
        restProjectItemMockMvc.perform(delete("/api/project-items/{id}", projectItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectItem> projectItemList = projectItemRepository.findAll();
        assertThat(projectItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProjectItem in Elasticsearch
        verify(mockProjectItemSearchRepository, times(1)).deleteById(projectItem.getId());
    }

    @Test
    @Transactional
    public void searchProjectItem() throws Exception {
        // Initialize the database
        projectItemRepository.saveAndFlush(projectItem);
        when(mockProjectItemSearchRepository.search(queryStringQuery("id:" + projectItem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projectItem), PageRequest.of(0, 1), 1));
        // Search the projectItem
        restProjectItemMockMvc.perform(get("/api/_search/project-items?query=id:" + projectItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectItem.class);
        ProjectItem projectItem1 = new ProjectItem();
        projectItem1.setId(1L);
        ProjectItem projectItem2 = new ProjectItem();
        projectItem2.setId(projectItem1.getId());
        assertThat(projectItem1).isEqualTo(projectItem2);
        projectItem2.setId(2L);
        assertThat(projectItem1).isNotEqualTo(projectItem2);
        projectItem1.setId(null);
        assertThat(projectItem1).isNotEqualTo(projectItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectItemDTO.class);
        ProjectItemDTO projectItemDTO1 = new ProjectItemDTO();
        projectItemDTO1.setId(1L);
        ProjectItemDTO projectItemDTO2 = new ProjectItemDTO();
        assertThat(projectItemDTO1).isNotEqualTo(projectItemDTO2);
        projectItemDTO2.setId(projectItemDTO1.getId());
        assertThat(projectItemDTO1).isEqualTo(projectItemDTO2);
        projectItemDTO2.setId(2L);
        assertThat(projectItemDTO1).isNotEqualTo(projectItemDTO2);
        projectItemDTO1.setId(null);
        assertThat(projectItemDTO1).isNotEqualTo(projectItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectItemMapper.fromId(null)).isNull();
    }
}
