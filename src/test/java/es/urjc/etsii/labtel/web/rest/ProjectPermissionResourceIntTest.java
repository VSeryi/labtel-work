package es.urjc.etsii.labtel.web.rest;

import es.urjc.etsii.labtel.LabtelApp;

import es.urjc.etsii.labtel.domain.ProjectPermission;
import es.urjc.etsii.labtel.repository.ProjectPermissionRepository;
import es.urjc.etsii.labtel.repository.search.ProjectPermissionSearchRepository;
import es.urjc.etsii.labtel.service.ProjectPermissionService;
import es.urjc.etsii.labtel.service.dto.ProjectPermissionDTO;
import es.urjc.etsii.labtel.service.mapper.ProjectPermissionMapper;
import es.urjc.etsii.labtel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Collections;
import java.util.List;


import static es.urjc.etsii.labtel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.urjc.etsii.labtel.domain.enumeration.TypePermission;
/**
 * Test class for the ProjectPermissionResource REST controller.
 *
 * @see ProjectPermissionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabtelApp.class)
public class ProjectPermissionResourceIntTest {

    private static final TypePermission DEFAULT_PERMISSION = TypePermission.READ;
    private static final TypePermission UPDATED_PERMISSION = TypePermission.READ_WRITE;

    @Autowired
    private ProjectPermissionRepository projectPermissionRepository;

    @Autowired
    private ProjectPermissionMapper projectPermissionMapper;

    @Autowired
    private ProjectPermissionService projectPermissionService;

    /**
     * This repository is mocked in the es.urjc.etsii.labtel.repository.search test package.
     *
     * @see es.urjc.etsii.labtel.repository.search.ProjectPermissionSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProjectPermissionSearchRepository mockProjectPermissionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectPermissionMockMvc;

    private ProjectPermission projectPermission;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectPermissionResource projectPermissionResource = new ProjectPermissionResource(projectPermissionService);
        this.restProjectPermissionMockMvc = MockMvcBuilders.standaloneSetup(projectPermissionResource)
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
    public static ProjectPermission createEntity(EntityManager em) {
        ProjectPermission projectPermission = new ProjectPermission()
            .permission(DEFAULT_PERMISSION);
        return projectPermission;
    }

    @Before
    public void initTest() {
        projectPermission = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectPermission() throws Exception {
        int databaseSizeBeforeCreate = projectPermissionRepository.findAll().size();

        // Create the ProjectPermission
        ProjectPermissionDTO projectPermissionDTO = projectPermissionMapper.toDto(projectPermission);
        restProjectPermissionMockMvc.perform(post("/api/project-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectPermissionDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectPermission in the database
        List<ProjectPermission> projectPermissionList = projectPermissionRepository.findAll();
        assertThat(projectPermissionList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectPermission testProjectPermission = projectPermissionList.get(projectPermissionList.size() - 1);
        assertThat(testProjectPermission.getPermission()).isEqualTo(DEFAULT_PERMISSION);

        // Validate the ProjectPermission in Elasticsearch
        verify(mockProjectPermissionSearchRepository, times(1)).save(testProjectPermission);
    }

    @Test
    @Transactional
    public void createProjectPermissionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectPermissionRepository.findAll().size();

        // Create the ProjectPermission with an existing ID
        projectPermission.setId(1L);
        ProjectPermissionDTO projectPermissionDTO = projectPermissionMapper.toDto(projectPermission);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectPermissionMockMvc.perform(post("/api/project-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectPermission in the database
        List<ProjectPermission> projectPermissionList = projectPermissionRepository.findAll();
        assertThat(projectPermissionList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProjectPermission in Elasticsearch
        verify(mockProjectPermissionSearchRepository, times(0)).save(projectPermission);
    }

    @Test
    @Transactional
    public void getAllProjectPermissions() throws Exception {
        // Initialize the database
        projectPermissionRepository.saveAndFlush(projectPermission);

        // Get all the projectPermissionList
        restProjectPermissionMockMvc.perform(get("/api/project-permissions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())));
    }
    
    @Test
    @Transactional
    public void getProjectPermission() throws Exception {
        // Initialize the database
        projectPermissionRepository.saveAndFlush(projectPermission);

        // Get the projectPermission
        restProjectPermissionMockMvc.perform(get("/api/project-permissions/{id}", projectPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectPermission.getId().intValue()))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectPermission() throws Exception {
        // Get the projectPermission
        restProjectPermissionMockMvc.perform(get("/api/project-permissions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectPermission() throws Exception {
        // Initialize the database
        projectPermissionRepository.saveAndFlush(projectPermission);

        int databaseSizeBeforeUpdate = projectPermissionRepository.findAll().size();

        // Update the projectPermission
        ProjectPermission updatedProjectPermission = projectPermissionRepository.findById(projectPermission.getId()).get();
        // Disconnect from session so that the updates on updatedProjectPermission are not directly saved in db
        em.detach(updatedProjectPermission);
        updatedProjectPermission
            .permission(UPDATED_PERMISSION);
        ProjectPermissionDTO projectPermissionDTO = projectPermissionMapper.toDto(updatedProjectPermission);

        restProjectPermissionMockMvc.perform(put("/api/project-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectPermissionDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectPermission in the database
        List<ProjectPermission> projectPermissionList = projectPermissionRepository.findAll();
        assertThat(projectPermissionList).hasSize(databaseSizeBeforeUpdate);
        ProjectPermission testProjectPermission = projectPermissionList.get(projectPermissionList.size() - 1);
        assertThat(testProjectPermission.getPermission()).isEqualTo(UPDATED_PERMISSION);

        // Validate the ProjectPermission in Elasticsearch
        verify(mockProjectPermissionSearchRepository, times(1)).save(testProjectPermission);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectPermission() throws Exception {
        int databaseSizeBeforeUpdate = projectPermissionRepository.findAll().size();

        // Create the ProjectPermission
        ProjectPermissionDTO projectPermissionDTO = projectPermissionMapper.toDto(projectPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectPermissionMockMvc.perform(put("/api/project-permissions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectPermission in the database
        List<ProjectPermission> projectPermissionList = projectPermissionRepository.findAll();
        assertThat(projectPermissionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProjectPermission in Elasticsearch
        verify(mockProjectPermissionSearchRepository, times(0)).save(projectPermission);
    }

    @Test
    @Transactional
    public void deleteProjectPermission() throws Exception {
        // Initialize the database
        projectPermissionRepository.saveAndFlush(projectPermission);

        int databaseSizeBeforeDelete = projectPermissionRepository.findAll().size();

        // Get the projectPermission
        restProjectPermissionMockMvc.perform(delete("/api/project-permissions/{id}", projectPermission.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectPermission> projectPermissionList = projectPermissionRepository.findAll();
        assertThat(projectPermissionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProjectPermission in Elasticsearch
        verify(mockProjectPermissionSearchRepository, times(1)).deleteById(projectPermission.getId());
    }

    @Test
    @Transactional
    public void searchProjectPermission() throws Exception {
        // Initialize the database
        projectPermissionRepository.saveAndFlush(projectPermission);
        when(mockProjectPermissionSearchRepository.search(queryStringQuery("id:" + projectPermission.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(projectPermission), PageRequest.of(0, 1), 1));
        // Search the projectPermission
        restProjectPermissionMockMvc.perform(get("/api/_search/project-permissions?query=id:" + projectPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectPermission.class);
        ProjectPermission projectPermission1 = new ProjectPermission();
        projectPermission1.setId(1L);
        ProjectPermission projectPermission2 = new ProjectPermission();
        projectPermission2.setId(projectPermission1.getId());
        assertThat(projectPermission1).isEqualTo(projectPermission2);
        projectPermission2.setId(2L);
        assertThat(projectPermission1).isNotEqualTo(projectPermission2);
        projectPermission1.setId(null);
        assertThat(projectPermission1).isNotEqualTo(projectPermission2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectPermissionDTO.class);
        ProjectPermissionDTO projectPermissionDTO1 = new ProjectPermissionDTO();
        projectPermissionDTO1.setId(1L);
        ProjectPermissionDTO projectPermissionDTO2 = new ProjectPermissionDTO();
        assertThat(projectPermissionDTO1).isNotEqualTo(projectPermissionDTO2);
        projectPermissionDTO2.setId(projectPermissionDTO1.getId());
        assertThat(projectPermissionDTO1).isEqualTo(projectPermissionDTO2);
        projectPermissionDTO2.setId(2L);
        assertThat(projectPermissionDTO1).isNotEqualTo(projectPermissionDTO2);
        projectPermissionDTO1.setId(null);
        assertThat(projectPermissionDTO1).isNotEqualTo(projectPermissionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectPermissionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectPermissionMapper.fromId(null)).isNull();
    }
}
