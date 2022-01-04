package eeij.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eeij.IntegrationTest;
import eeij.domain.Training;
import eeij.repository.TrainingRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TrainingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingResourceIT {

    private static final String DEFAULT_TRAINING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRAINING_MODE = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_MODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRAINING_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRAINING_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAINING_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TRAING_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRAING_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TRAINING_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_YEAR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TRAINING_REGISTRATION = false;
    private static final Boolean UPDATED_TRAINING_REGISTRATION = true;

    private static final String ENTITY_API_URL = "/api/trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingMockMvc;

    private Training training;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createEntity(EntityManager em) {
        Training training = new Training()
            .trainingName(DEFAULT_TRAINING_NAME)
            .trainingMode(DEFAULT_TRAINING_MODE)
            .trainingType(DEFAULT_TRAINING_TYPE)
            .trainingStartDate(DEFAULT_TRAINING_START_DATE)
            .traingEndDate(DEFAULT_TRAING_END_DATE)
            .trainingYear(DEFAULT_TRAINING_YEAR)
            .trainingRegistration(DEFAULT_TRAINING_REGISTRATION);
        return training;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createUpdatedEntity(EntityManager em) {
        Training training = new Training()
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingMode(UPDATED_TRAINING_MODE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .trainingStartDate(UPDATED_TRAINING_START_DATE)
            .traingEndDate(UPDATED_TRAING_END_DATE)
            .trainingYear(UPDATED_TRAINING_YEAR)
            .trainingRegistration(UPDATED_TRAINING_REGISTRATION);
        return training;
    }

    @BeforeEach
    public void initTest() {
        training = createEntity(em);
    }

    @Test
    @Transactional
    void createTraining() throws Exception {
        int databaseSizeBeforeCreate = trainingRepository.findAll().size();
        // Create the Training
        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isCreated());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeCreate + 1);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getTrainingName()).isEqualTo(DEFAULT_TRAINING_NAME);
        assertThat(testTraining.getTrainingMode()).isEqualTo(DEFAULT_TRAINING_MODE);
        assertThat(testTraining.getTrainingType()).isEqualTo(DEFAULT_TRAINING_TYPE);
        assertThat(testTraining.getTrainingStartDate()).isEqualTo(DEFAULT_TRAINING_START_DATE);
        assertThat(testTraining.getTraingEndDate()).isEqualTo(DEFAULT_TRAING_END_DATE);
        assertThat(testTraining.getTrainingYear()).isEqualTo(DEFAULT_TRAINING_YEAR);
        assertThat(testTraining.getTrainingRegistration()).isEqualTo(DEFAULT_TRAINING_REGISTRATION);
    }

    @Test
    @Transactional
    void createTrainingWithExistingId() throws Exception {
        // Create the Training with an existing ID
        training.setId(1L);

        int databaseSizeBeforeCreate = trainingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrainings() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get all the trainingList
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(training.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].trainingMode").value(hasItem(DEFAULT_TRAINING_MODE)))
            .andExpect(jsonPath("$.[*].trainingType").value(hasItem(DEFAULT_TRAINING_TYPE)))
            .andExpect(jsonPath("$.[*].trainingStartDate").value(hasItem(DEFAULT_TRAINING_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].traingEndDate").value(hasItem(DEFAULT_TRAING_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].trainingYear").value(hasItem(DEFAULT_TRAINING_YEAR)))
            .andExpect(jsonPath("$.[*].trainingRegistration").value(hasItem(DEFAULT_TRAINING_REGISTRATION.booleanValue())));
    }

    @Test
    @Transactional
    void getTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get the training
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, training.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(training.getId().intValue()))
            .andExpect(jsonPath("$.trainingName").value(DEFAULT_TRAINING_NAME))
            .andExpect(jsonPath("$.trainingMode").value(DEFAULT_TRAINING_MODE))
            .andExpect(jsonPath("$.trainingType").value(DEFAULT_TRAINING_TYPE))
            .andExpect(jsonPath("$.trainingStartDate").value(DEFAULT_TRAINING_START_DATE.toString()))
            .andExpect(jsonPath("$.traingEndDate").value(DEFAULT_TRAING_END_DATE.toString()))
            .andExpect(jsonPath("$.trainingYear").value(DEFAULT_TRAINING_YEAR))
            .andExpect(jsonPath("$.trainingRegistration").value(DEFAULT_TRAINING_REGISTRATION.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTraining() throws Exception {
        // Get the training
        restTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();

        // Update the training
        Training updatedTraining = trainingRepository.findById(training.getId()).get();
        // Disconnect from session so that the updates on updatedTraining are not directly saved in db
        em.detach(updatedTraining);
        updatedTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingMode(UPDATED_TRAINING_MODE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .trainingStartDate(UPDATED_TRAINING_START_DATE)
            .traingEndDate(UPDATED_TRAING_END_DATE)
            .trainingYear(UPDATED_TRAINING_YEAR)
            .trainingRegistration(UPDATED_TRAINING_REGISTRATION);

        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTraining.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTraining.getTrainingMode()).isEqualTo(UPDATED_TRAINING_MODE);
        assertThat(testTraining.getTrainingType()).isEqualTo(UPDATED_TRAINING_TYPE);
        assertThat(testTraining.getTrainingStartDate()).isEqualTo(UPDATED_TRAINING_START_DATE);
        assertThat(testTraining.getTraingEndDate()).isEqualTo(UPDATED_TRAING_END_DATE);
        assertThat(testTraining.getTrainingYear()).isEqualTo(UPDATED_TRAINING_YEAR);
        assertThat(testTraining.getTrainingRegistration()).isEqualTo(UPDATED_TRAINING_REGISTRATION);
    }

    @Test
    @Transactional
    void putNonExistingTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, training.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingMode(UPDATED_TRAINING_MODE)
            .trainingStartDate(UPDATED_TRAINING_START_DATE)
            .traingEndDate(UPDATED_TRAING_END_DATE)
            .trainingRegistration(UPDATED_TRAINING_REGISTRATION);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTraining.getTrainingMode()).isEqualTo(UPDATED_TRAINING_MODE);
        assertThat(testTraining.getTrainingType()).isEqualTo(DEFAULT_TRAINING_TYPE);
        assertThat(testTraining.getTrainingStartDate()).isEqualTo(UPDATED_TRAINING_START_DATE);
        assertThat(testTraining.getTraingEndDate()).isEqualTo(UPDATED_TRAING_END_DATE);
        assertThat(testTraining.getTrainingYear()).isEqualTo(DEFAULT_TRAINING_YEAR);
        assertThat(testTraining.getTrainingRegistration()).isEqualTo(UPDATED_TRAINING_REGISTRATION);
    }

    @Test
    @Transactional
    void fullUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining
            .trainingName(UPDATED_TRAINING_NAME)
            .trainingMode(UPDATED_TRAINING_MODE)
            .trainingType(UPDATED_TRAINING_TYPE)
            .trainingStartDate(UPDATED_TRAINING_START_DATE)
            .traingEndDate(UPDATED_TRAING_END_DATE)
            .trainingYear(UPDATED_TRAINING_YEAR)
            .trainingRegistration(UPDATED_TRAINING_REGISTRATION);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
        Training testTraining = trainingList.get(trainingList.size() - 1);
        assertThat(testTraining.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTraining.getTrainingMode()).isEqualTo(UPDATED_TRAINING_MODE);
        assertThat(testTraining.getTrainingType()).isEqualTo(UPDATED_TRAINING_TYPE);
        assertThat(testTraining.getTrainingStartDate()).isEqualTo(UPDATED_TRAINING_START_DATE);
        assertThat(testTraining.getTraingEndDate()).isEqualTo(UPDATED_TRAING_END_DATE);
        assertThat(testTraining.getTrainingYear()).isEqualTo(UPDATED_TRAINING_YEAR);
        assertThat(testTraining.getTrainingRegistration()).isEqualTo(UPDATED_TRAINING_REGISTRATION);
    }

    @Test
    @Transactional
    void patchNonExistingTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, training.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(training))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTraining() throws Exception {
        int databaseSizeBeforeUpdate = trainingRepository.findAll().size();
        training.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(training)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        int databaseSizeBeforeDelete = trainingRepository.findAll().size();

        // Delete the training
        restTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, training.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Training> trainingList = trainingRepository.findAll();
        assertThat(trainingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
