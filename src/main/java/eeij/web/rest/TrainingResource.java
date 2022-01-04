package eeij.web.rest;

import eeij.domain.Training;
import eeij.repository.TrainingRepository;
import eeij.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link eeij.domain.Training}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrainingResource {

    private final Logger log = LoggerFactory.getLogger(TrainingResource.class);

    private static final String ENTITY_NAME = "training";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingRepository trainingRepository;

    public TrainingResource(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * {@code POST  /trainings} : Create a new training.
     *
     * @param training the training to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new training, or with status {@code 400 (Bad Request)} if the training has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trainings")
    public ResponseEntity<Training> createTraining(@RequestBody Training training) throws URISyntaxException {
        log.debug("REST request to save Training : {}", training);
        if (training.getId() != null) {
            throw new BadRequestAlertException("A new training cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Training result = trainingRepository.save(training);
        return ResponseEntity
            .created(new URI("/api/trainings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trainings/:id} : Updates an existing training.
     *
     * @param id the id of the training to save.
     * @param training the training to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated training,
     * or with status {@code 400 (Bad Request)} if the training is not valid,
     * or with status {@code 500 (Internal Server Error)} if the training couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trainings/{id}")
    public ResponseEntity<Training> updateTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Training training
    ) throws URISyntaxException {
        log.debug("REST request to update Training : {}, {}", id, training);
        if (training.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, training.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Training result = trainingRepository.save(training);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, training.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trainings/:id} : Partial updates given fields of an existing training, field will ignore if it is null
     *
     * @param id the id of the training to save.
     * @param training the training to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated training,
     * or with status {@code 400 (Bad Request)} if the training is not valid,
     * or with status {@code 404 (Not Found)} if the training is not found,
     * or with status {@code 500 (Internal Server Error)} if the training couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trainings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Training> partialUpdateTraining(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Training training
    ) throws URISyntaxException {
        log.debug("REST request to partial update Training partially : {}, {}", id, training);
        if (training.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, training.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Training> result = trainingRepository
            .findById(training.getId())
            .map(existingTraining -> {
                if (training.getTrainingName() != null) {
                    existingTraining.setTrainingName(training.getTrainingName());
                }
                if (training.getTrainingMode() != null) {
                    existingTraining.setTrainingMode(training.getTrainingMode());
                }
                if (training.getTrainingType() != null) {
                    existingTraining.setTrainingType(training.getTrainingType());
                }
                if (training.getTrainingStartDate() != null) {
                    existingTraining.setTrainingStartDate(training.getTrainingStartDate());
                }
                if (training.getTraingEndDate() != null) {
                    existingTraining.setTraingEndDate(training.getTraingEndDate());
                }
                if (training.getTrainingYear() != null) {
                    existingTraining.setTrainingYear(training.getTrainingYear());
                }
                if (training.getTrainingRegistration() != null) {
                    existingTraining.setTrainingRegistration(training.getTrainingRegistration());
                }

                return existingTraining;
            })
            .map(trainingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, training.getId().toString())
        );
    }

    /**
     * {@code GET  /trainings} : get all the trainings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainings in body.
     */
    @GetMapping("/trainings")
    public List<Training> getAllTrainings() {
        log.debug("REST request to get all Trainings");
        return trainingRepository.findAll();
    }

    /**
     * {@code GET  /trainings/:id} : get the "id" training.
     *
     * @param id the id of the training to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the training, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trainings/{id}")
    public ResponseEntity<Training> getTraining(@PathVariable Long id) {
        log.debug("REST request to get Training : {}", id);
        Optional<Training> training = trainingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(training);
    }

    /**
     * {@code DELETE  /trainings/:id} : delete the "id" training.
     *
     * @param id the id of the training to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trainings/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        log.debug("REST request to delete Training : {}", id);
        trainingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
