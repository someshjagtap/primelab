package com.mycompany.primelab.web.rest;

import static com.mycompany.primelab.domain.ProjectAsserts.*;
import static com.mycompany.primelab.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.primelab.IntegrationTest;
import com.mycompany.primelab.domain.Project;
import com.mycompany.primelab.domain.enumeration.Category;
import com.mycompany.primelab.domain.enumeration.Department;
import com.mycompany.primelab.domain.enumeration.Division;
import com.mycompany.primelab.domain.enumeration.Priority;
import com.mycompany.primelab.domain.enumeration.Reason;
import com.mycompany.primelab.domain.enumeration.Type;
import com.mycompany.primelab.repository.ProjectRepository;
import com.mycompany.primelab.service.dto.ProjectDTO;
import com.mycompany.primelab.service.mapper.ProjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final Reason DEFAULT_REASON = Reason.BUSSINESS;
    private static final Reason UPDATED_REASON = Reason.DELERSHIP;

    private static final Type DEFAULT_TYPE = Type.INTERNAL;
    private static final Type UPDATED_TYPE = Type.EXTERNAL;

    private static final Division DEFAULT_DIVISION = Division.COMPRESSER;
    private static final Division UPDATED_DIVISION = Division.FILTERS;

    private static final Category DEFAULT_CATEGORY = Category.QUALITY_A;
    private static final Category UPDATED_CATEGORY = Category.QUALITY_B;

    private static final Priority DEFAULT_PRIORITY = Priority.HIGH;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final Department DEFAULT_DEPARTMENT = Department.STARTEGY;
    private static final Department UPDATED_DEPARTMENT = Department.FINANCE;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMockMvc;

    private Project project;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .projectName(DEFAULT_PROJECT_NAME)
            .reason(DEFAULT_REASON)
            .type(DEFAULT_TYPE)
            .division(DEFAULT_DIVISION)
            .category(DEFAULT_CATEGORY)
            .priority(DEFAULT_PRIORITY)
            .department(DEFAULT_DEPARTMENT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .location(DEFAULT_LOCATION)
            .status(DEFAULT_STATUS);
        return project;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .projectName(UPDATED_PROJECT_NAME)
            .reason(UPDATED_REASON)
            .type(UPDATED_TYPE)
            .division(UPDATED_DIVISION)
            .category(UPDATED_CATEGORY)
            .priority(UPDATED_PRIORITY)
            .department(UPDATED_DEPARTMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    void createProject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);
        var returnedProjectDTO = om.readValue(
            restProjectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProjectDTO.class
        );

        // Validate the Project in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProject = projectMapper.toEntity(returnedProjectDTO);
        assertProjectUpdatableFieldsEquals(returnedProject, getPersistedProject(returnedProject));
    }

    @Test
    @Transactional
    void createProjectWithExistingId() throws Exception {
        // Create the Project with an existing ID
        project.setId(1L);
        ProjectDTO projectDTO = projectMapper.toDto(project);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProjectFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProjectFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName equals to
        defaultProjectFiltering("projectName.equals=" + DEFAULT_PROJECT_NAME, "projectName.equals=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName in
        defaultProjectFiltering(
            "projectName.in=" + DEFAULT_PROJECT_NAME + "," + UPDATED_PROJECT_NAME,
            "projectName.in=" + UPDATED_PROJECT_NAME
        );
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName is not null
        defaultProjectFiltering("projectName.specified=true", "projectName.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName contains
        defaultProjectFiltering("projectName.contains=" + DEFAULT_PROJECT_NAME, "projectName.contains=" + UPDATED_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByProjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where projectName does not contain
        defaultProjectFiltering("projectName.doesNotContain=" + UPDATED_PROJECT_NAME, "projectName.doesNotContain=" + DEFAULT_PROJECT_NAME);
    }

    @Test
    @Transactional
    void getAllProjectsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where reason equals to
        defaultProjectFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllProjectsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where reason in
        defaultProjectFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllProjectsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where reason is not null
        defaultProjectFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type equals to
        defaultProjectFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProjectsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type in
        defaultProjectFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProjectsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type is not null
        defaultProjectFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where division equals to
        defaultProjectFiltering("division.equals=" + DEFAULT_DIVISION, "division.equals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllProjectsByDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where division in
        defaultProjectFiltering("division.in=" + DEFAULT_DIVISION + "," + UPDATED_DIVISION, "division.in=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllProjectsByDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where division is not null
        defaultProjectFiltering("division.specified=true", "division.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where category equals to
        defaultProjectFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProjectsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where category in
        defaultProjectFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllProjectsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where category is not null
        defaultProjectFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority equals to
        defaultProjectFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority in
        defaultProjectFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProjectsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where priority is not null
        defaultProjectFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where department equals to
        defaultProjectFiltering("department.equals=" + DEFAULT_DEPARTMENT, "department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where department in
        defaultProjectFiltering("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT, "department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllProjectsByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where department is not null
        defaultProjectFiltering("department.specified=true", "department.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate equals to
        defaultProjectFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate in
        defaultProjectFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is not null
        defaultProjectFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where endDate equals to
        defaultProjectFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where endDate in
        defaultProjectFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllProjectsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where endDate is not null
        defaultProjectFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where location equals to
        defaultProjectFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where location in
        defaultProjectFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where location is not null
        defaultProjectFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByLocationContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where location contains
        defaultProjectFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where location does not contain
        defaultProjectFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status equals to
        defaultProjectFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status in
        defaultProjectFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status is not null
        defaultProjectFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllProjectsByStatusContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status contains
        defaultProjectFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllProjectsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status does not contain
        defaultProjectFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    private void defaultProjectFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProjectShouldBeFound(shouldBeFound);
        defaultProjectShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .reason(UPDATED_REASON)
            .type(UPDATED_TYPE)
            .division(UPDATED_DIVISION)
            .category(UPDATED_CATEGORY)
            .priority(UPDATED_PRIORITY)
            .department(UPDATED_DEPARTMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS);
        ProjectDTO projectDTO = projectMapper.toDto(updatedProject);

        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectDTO))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProjectToMatchAllProperties(updatedProject);
    }

    @Test
    @Transactional
    void putNonExistingProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .type(UPDATED_TYPE)
            .division(UPDATED_DIVISION)
            .priority(UPDATED_PRIORITY)
            .department(UPDATED_DEPARTMENT)
            .startDate(UPDATED_START_DATE);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProject, project), getPersistedProject(project));
    }

    @Test
    @Transactional
    void fullUpdateProjectWithPatch() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the project using partial update
        Project partialUpdatedProject = new Project();
        partialUpdatedProject.setId(project.getId());

        partialUpdatedProject
            .projectName(UPDATED_PROJECT_NAME)
            .reason(UPDATED_REASON)
            .type(UPDATED_TYPE)
            .division(UPDATED_DIVISION)
            .category(UPDATED_CATEGORY)
            .priority(UPDATED_PRIORITY)
            .department(UPDATED_DEPARTMENT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .location(UPDATED_LOCATION)
            .status(UPDATED_STATUS);

        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProject))
            )
            .andExpect(status().isOk());

        // Validate the Project in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectUpdatableFieldsEquals(partialUpdatedProject, getPersistedProject(partialUpdatedProject));
    }

    @Test
    @Transactional
    void patchNonExistingProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        project.setId(longCount.incrementAndGet());

        // Create the Project
        ProjectDTO projectDTO = projectMapper.toDto(project);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(projectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Project in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the project
        restProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, project.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return projectRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Project getPersistedProject(Project project) {
        return projectRepository.findById(project.getId()).orElseThrow();
    }

    protected void assertPersistedProjectToMatchAllProperties(Project expectedProject) {
        assertProjectAllPropertiesEquals(expectedProject, getPersistedProject(expectedProject));
    }

    protected void assertPersistedProjectToMatchUpdatableProperties(Project expectedProject) {
        assertProjectAllUpdatablePropertiesEquals(expectedProject, getPersistedProject(expectedProject));
    }
}
