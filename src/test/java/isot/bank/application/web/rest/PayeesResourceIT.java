package isot.bank.application.web.rest;

import isot.bank.application.IsotBankApp;
import isot.bank.application.domain.Payees;
import isot.bank.application.repository.PayeesRepository;
import isot.bank.application.service.PayeesService;
import isot.bank.application.service.dto.PayeesDTO;
import isot.bank.application.service.mapper.PayeesMapper;
import isot.bank.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static isot.bank.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PayeesResource} REST controller.
 */
@SpringBootTest(classes = IsotBankApp.class)
public class PayeesResourceIT {

    private static final Integer DEFAULT_PAYEE_ID = 1;
    private static final Integer UPDATED_PAYEE_ID = 2;
    private static final Integer SMALLER_PAYEE_ID = 1 - 1;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private PayeesRepository payeesRepository;

    @Autowired
    private PayeesMapper payeesMapper;

    @Autowired
    private PayeesService payeesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPayeesMockMvc;

    private Payees payees;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayeesResource payeesResource = new PayeesResource(payeesService);
        this.restPayeesMockMvc = MockMvcBuilders.standaloneSetup(payeesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payees createEntity(EntityManager em) {
        Payees payees = new Payees()
            .payeeID(DEFAULT_PAYEE_ID)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .telephone(DEFAULT_TELEPHONE);
        return payees;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payees createUpdatedEntity(EntityManager em) {
        Payees payees = new Payees()
            .payeeID(UPDATED_PAYEE_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .telephone(UPDATED_TELEPHONE);
        return payees;
    }

    @BeforeEach
    public void initTest() {
        payees = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayees() throws Exception {
        int databaseSizeBeforeCreate = payeesRepository.findAll().size();

        // Create the Payees
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);
        restPayeesMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isCreated());

        // Validate the Payees in the database
        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeCreate + 1);
        Payees testPayees = payeesList.get(payeesList.size() - 1);
        assertThat(testPayees.getPayeeID()).isEqualTo(DEFAULT_PAYEE_ID);
        assertThat(testPayees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPayees.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPayees.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPayees.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    public void createPayeesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payeesRepository.findAll().size();

        // Create the Payees with an existing ID
        payees.setId(1L);
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayeesMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payees in the database
        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = payeesRepository.findAll().size();
        // set the field null
        payees.setEmail(null);

        // Create the Payees, which fails.
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);

        restPayeesMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isBadRequest());

        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = payeesRepository.findAll().size();
        // set the field null
        payees.setFirstName(null);

        // Create the Payees, which fails.
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);

        restPayeesMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isBadRequest());

        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = payeesRepository.findAll().size();
        // set the field null
        payees.setLastName(null);

        // Create the Payees, which fails.
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);

        restPayeesMockMvc.perform(post("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isBadRequest());

        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayees() throws Exception {
        // Initialize the database
        payeesRepository.saveAndFlush(payees);

        // Get all the payeesList
        restPayeesMockMvc.perform(get("/api/payees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payees.getId().intValue())))
            .andExpect(jsonPath("$.[*].payeeID").value(hasItem(DEFAULT_PAYEE_ID)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getPayees() throws Exception {
        // Initialize the database
        payeesRepository.saveAndFlush(payees);

        // Get the payees
        restPayeesMockMvc.perform(get("/api/payees/{id}", payees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payees.getId().intValue()))
            .andExpect(jsonPath("$.payeeID").value(DEFAULT_PAYEE_ID))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayees() throws Exception {
        // Get the payees
        restPayeesMockMvc.perform(get("/api/payees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayees() throws Exception {
        // Initialize the database
        payeesRepository.saveAndFlush(payees);

        int databaseSizeBeforeUpdate = payeesRepository.findAll().size();

        // Update the payees
        Payees updatedPayees = payeesRepository.findById(payees.getId()).get();
        // Disconnect from session so that the updates on updatedPayees are not directly saved in db
        em.detach(updatedPayees);
        updatedPayees
            .payeeID(UPDATED_PAYEE_ID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .telephone(UPDATED_TELEPHONE);
        PayeesDTO payeesDTO = payeesMapper.toDto(updatedPayees);

        restPayeesMockMvc.perform(put("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isOk());

        // Validate the Payees in the database
        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeUpdate);
        Payees testPayees = payeesList.get(payeesList.size() - 1);
        assertThat(testPayees.getPayeeID()).isEqualTo(UPDATED_PAYEE_ID);
        assertThat(testPayees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPayees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPayees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPayees.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingPayees() throws Exception {
        int databaseSizeBeforeUpdate = payeesRepository.findAll().size();

        // Create the Payees
        PayeesDTO payeesDTO = payeesMapper.toDto(payees);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPayeesMockMvc.perform(put("/api/payees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payeesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payees in the database
        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayees() throws Exception {
        // Initialize the database
        payeesRepository.saveAndFlush(payees);

        int databaseSizeBeforeDelete = payeesRepository.findAll().size();

        // Delete the payees
        restPayeesMockMvc.perform(delete("/api/payees/{id}", payees.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payees> payeesList = payeesRepository.findAll();
        assertThat(payeesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payees.class);
        Payees payees1 = new Payees();
        payees1.setId(1L);
        Payees payees2 = new Payees();
        payees2.setId(payees1.getId());
        assertThat(payees1).isEqualTo(payees2);
        payees2.setId(2L);
        assertThat(payees1).isNotEqualTo(payees2);
        payees1.setId(null);
        assertThat(payees1).isNotEqualTo(payees2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayeesDTO.class);
        PayeesDTO payeesDTO1 = new PayeesDTO();
        payeesDTO1.setId(1L);
        PayeesDTO payeesDTO2 = new PayeesDTO();
        assertThat(payeesDTO1).isNotEqualTo(payeesDTO2);
        payeesDTO2.setId(payeesDTO1.getId());
        assertThat(payeesDTO1).isEqualTo(payeesDTO2);
        payeesDTO2.setId(2L);
        assertThat(payeesDTO1).isNotEqualTo(payeesDTO2);
        payeesDTO1.setId(null);
        assertThat(payeesDTO1).isNotEqualTo(payeesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(payeesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(payeesMapper.fromId(null)).isNull();
    }
}
