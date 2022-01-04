package eeij.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eeij.IntegrationTest;
import eeij.domain.UserDetails;
import eeij.repository.UserDetailsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDetailsResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_CASTE = "AAAAAAAAAA";
    private static final String UPDATED_CASTE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DISCIPLINE = "AAAAAAAAAA";
    private static final String UPDATED_DISCIPLINE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_BANK_ACCOUNT = 1;
    private static final Integer UPDATED_BANK_ACCOUNT = 2;

    private static final String DEFAULT_IFSC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_IFSC_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .designation(DEFAULT_DESIGNATION)
            .caste(DEFAULT_CASTE)
            .address(DEFAULT_ADDRESS)
            .department(DEFAULT_DEPARTMENT)
            .discipline(DEFAULT_DISCIPLINE)
            .state(DEFAULT_STATE)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .ifscCode(DEFAULT_IFSC_CODE);
        return userDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createUpdatedEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .designation(UPDATED_DESIGNATION)
            .caste(UPDATED_CASTE)
            .address(UPDATED_ADDRESS)
            .department(UPDATED_DEPARTMENT)
            .discipline(UPDATED_DISCIPLINE)
            .state(UPDATED_STATE)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .ifscCode(UPDATED_IFSC_CODE);
        return userDetails;
    }

    @BeforeEach
    public void initTest() {
        userDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createUserDetails() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();
        // Create the UserDetails
        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testUserDetails.getCaste()).isEqualTo(DEFAULT_CASTE);
        assertThat(testUserDetails.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserDetails.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testUserDetails.getDiscipline()).isEqualTo(DEFAULT_DISCIPLINE);
        assertThat(testUserDetails.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserDetails.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testUserDetails.getIfscCode()).isEqualTo(DEFAULT_IFSC_CODE);
    }

    @Test
    @Transactional
    void createUserDetailsWithExistingId() throws Exception {
        // Create the UserDetails with an existing ID
        userDetails.setId(1L);

        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].caste").value(hasItem(DEFAULT_CASTE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].discipline").value(hasItem(DEFAULT_DISCIPLINE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].ifscCode").value(hasItem(DEFAULT_IFSC_CODE)));
    }

    @Test
    @Transactional
    void getUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.caste").value(DEFAULT_CASTE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.discipline").value(DEFAULT_DISCIPLINE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT))
            .andExpect(jsonPath("$.ifscCode").value(DEFAULT_IFSC_CODE));
    }

    @Test
    @Transactional
    void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).get();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);
        updatedUserDetails
            .designation(UPDATED_DESIGNATION)
            .caste(UPDATED_CASTE)
            .address(UPDATED_ADDRESS)
            .department(UPDATED_DEPARTMENT)
            .discipline(UPDATED_DISCIPLINE)
            .state(UPDATED_STATE)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .ifscCode(UPDATED_IFSC_CODE);

        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testUserDetails.getCaste()).isEqualTo(UPDATED_CASTE);
        assertThat(testUserDetails.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserDetails.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testUserDetails.getDiscipline()).isEqualTo(UPDATED_DISCIPLINE);
        assertThat(testUserDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserDetails.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testUserDetails.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void putNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails
            .designation(UPDATED_DESIGNATION)
            .caste(UPDATED_CASTE)
            .address(UPDATED_ADDRESS)
            .discipline(UPDATED_DISCIPLINE)
            .state(UPDATED_STATE)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .ifscCode(UPDATED_IFSC_CODE);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testUserDetails.getCaste()).isEqualTo(UPDATED_CASTE);
        assertThat(testUserDetails.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserDetails.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testUserDetails.getDiscipline()).isEqualTo(UPDATED_DISCIPLINE);
        assertThat(testUserDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserDetails.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testUserDetails.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void fullUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails
            .designation(UPDATED_DESIGNATION)
            .caste(UPDATED_CASTE)
            .address(UPDATED_ADDRESS)
            .department(UPDATED_DEPARTMENT)
            .discipline(UPDATED_DISCIPLINE)
            .state(UPDATED_STATE)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .ifscCode(UPDATED_IFSC_CODE);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testUserDetails.getCaste()).isEqualTo(UPDATED_CASTE);
        assertThat(testUserDetails.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserDetails.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testUserDetails.getDiscipline()).isEqualTo(UPDATED_DISCIPLINE);
        assertThat(testUserDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserDetails.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testUserDetails.getIfscCode()).isEqualTo(UPDATED_IFSC_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();
        userDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        int databaseSizeBeforeDelete = userDetailsRepository.findAll().size();

        // Delete the userDetails
        restUserDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
