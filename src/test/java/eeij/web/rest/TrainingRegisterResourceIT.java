package eeij.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eeij.IntegrationTest;
import eeij.domain.TrainingRegister;
import eeij.repository.TrainingRegisterRepository;
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
 * Integration tests for the {@link TrainingRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingRegisterResourceIT {

    private static final Float DEFAULT_USER_ATTENDANCE = 1F;
    private static final Float UPDATED_USER_ATTENDANCE = 2F;

    private static final Boolean DEFAULT_CERIFICATE = false;
    private static final Boolean UPDATED_CERIFICATE = true;

    private static final String ENTITY_API_URL = "/api/training-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingRegisterRepository trainingRegisterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingRegisterMockMvc;

    private TrainingRegister trainingRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingRegister createEntity(EntityManager em) {
        TrainingRegister trainingRegister = new TrainingRegister().userAttendance(DEFAULT_USER_ATTENDANCE).cerificate(DEFAULT_CERIFICATE);
        return trainingRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingRegister createUpdatedEntity(EntityManager em) {
        TrainingRegister trainingRegister = new TrainingRegister().userAttendance(UPDATED_USER_ATTENDANCE).cerificate(UPDATED_CERIFICATE);
        return trainingRegister;
    }

    @BeforeEach
    public void initTest() {
        trainingRegister = createEntity(em);
    }

    @Test
    @Transactional
    void createTrainingRegister() throws Exception {
        int databaseSizeBeforeCreate = trainingRegisterRepository.findAll().size();
        // Create the TrainingRegister
        restTrainingRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isCreated());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingRegister testTrainingRegister = trainingRegisterList.get(trainingRegisterList.size() - 1);
        assertThat(testTrainingRegister.getUserAttendance()).isEqualTo(DEFAULT_USER_ATTENDANCE);
        assertThat(testTrainingRegister.getCerificate()).isEqualTo(DEFAULT_CERIFICATE);
    }

    @Test
    @Transactional
    void createTrainingRegisterWithExistingId() throws Exception {
        // Create the TrainingRegister with an existing ID
        trainingRegister.setId(1L);

        int databaseSizeBeforeCreate = trainingRegisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrainingRegisters() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        // Get all the trainingRegisterList
        restTrainingRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].userAttendance").value(hasItem(DEFAULT_USER_ATTENDANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].cerificate").value(hasItem(DEFAULT_CERIFICATE.booleanValue())));
    }

    @Test
    @Transactional
    void getTrainingRegister() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        // Get the trainingRegister
        restTrainingRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingRegister.getId().intValue()))
            .andExpect(jsonPath("$.userAttendance").value(DEFAULT_USER_ATTENDANCE.doubleValue()))
            .andExpect(jsonPath("$.cerificate").value(DEFAULT_CERIFICATE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTrainingRegister() throws Exception {
        // Get the trainingRegister
        restTrainingRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrainingRegister() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();

        // Update the trainingRegister
        TrainingRegister updatedTrainingRegister = trainingRegisterRepository.findById(trainingRegister.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingRegister are not directly saved in db
        em.detach(updatedTrainingRegister);
        updatedTrainingRegister.userAttendance(UPDATED_USER_ATTENDANCE).cerificate(UPDATED_CERIFICATE);

        restTrainingRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrainingRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrainingRegister))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
        TrainingRegister testTrainingRegister = trainingRegisterList.get(trainingRegisterList.size() - 1);
        assertThat(testTrainingRegister.getUserAttendance()).isEqualTo(UPDATED_USER_ATTENDANCE);
        assertThat(testTrainingRegister.getCerificate()).isEqualTo(UPDATED_CERIFICATE);
    }

    @Test
    @Transactional
    void putNonExistingTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingRegisterWithPatch() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();

        // Update the trainingRegister using partial update
        TrainingRegister partialUpdatedTrainingRegister = new TrainingRegister();
        partialUpdatedTrainingRegister.setId(trainingRegister.getId());

        partialUpdatedTrainingRegister.userAttendance(UPDATED_USER_ATTENDANCE);

        restTrainingRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingRegister))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
        TrainingRegister testTrainingRegister = trainingRegisterList.get(trainingRegisterList.size() - 1);
        assertThat(testTrainingRegister.getUserAttendance()).isEqualTo(UPDATED_USER_ATTENDANCE);
        assertThat(testTrainingRegister.getCerificate()).isEqualTo(DEFAULT_CERIFICATE);
    }

    @Test
    @Transactional
    void fullUpdateTrainingRegisterWithPatch() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();

        // Update the trainingRegister using partial update
        TrainingRegister partialUpdatedTrainingRegister = new TrainingRegister();
        partialUpdatedTrainingRegister.setId(trainingRegister.getId());

        partialUpdatedTrainingRegister.userAttendance(UPDATED_USER_ATTENDANCE).cerificate(UPDATED_CERIFICATE);

        restTrainingRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingRegister))
            )
            .andExpect(status().isOk());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
        TrainingRegister testTrainingRegister = trainingRegisterList.get(trainingRegisterList.size() - 1);
        assertThat(testTrainingRegister.getUserAttendance()).isEqualTo(UPDATED_USER_ATTENDANCE);
        assertThat(testTrainingRegister.getCerificate()).isEqualTo(UPDATED_CERIFICATE);
    }

    @Test
    @Transactional
    void patchNonExistingTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingRegister() throws Exception {
        int databaseSizeBeforeUpdate = trainingRegisterRepository.findAll().size();
        trainingRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingRegister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingRegister in the database
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingRegister() throws Exception {
        // Initialize the database
        trainingRegisterRepository.saveAndFlush(trainingRegister);

        int databaseSizeBeforeDelete = trainingRegisterRepository.findAll().size();

        // Delete the trainingRegister
        restTrainingRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingRegister> trainingRegisterList = trainingRegisterRepository.findAll();
        assertThat(trainingRegisterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
