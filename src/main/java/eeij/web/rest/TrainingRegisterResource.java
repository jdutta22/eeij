package eeij.web.rest;

import eeij.domain.TrainingRegister;
import eeij.repository.TrainingRegisterRepository;
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
 * REST controller for managing {@link eeij.domain.TrainingRegister}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrainingRegisterResource {

    private final Logger log = LoggerFactory.getLogger(TrainingRegisterResource.class);

    private static final String ENTITY_NAME = "trainingRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingRegisterRepository trainingRegisterRepository;

    public TrainingRegisterResource(TrainingRegisterRepository trainingRegisterRepository) {
        this.trainingRegisterRepository = trainingRegisterRepository;
    }

    /**
     * {@code POST  /training-registers} : Create a new trainingRegister.
     *
     * @param trainingRegister the trainingRegister to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingRegister, or with status {@code 400 (Bad Request)} if the trainingRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-registers")
    public ResponseEntity<TrainingRegister> createTrainingRegister(@RequestBody TrainingRegister trainingRegister)
        throws URISyntaxException {
        log.debug("REST request to save TrainingRegister : {}", trainingRegister);
        if (trainingRegister.getId() != null) {
            throw new BadRequestAlertException("A new trainingRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingRegister result = trainingRegisterRepository.save(trainingRegister);
        return ResponseEntity
            .created(new URI("/api/training-registers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-registers/:id} : Updates an existing trainingRegister.
     *
     * @param id the id of the trainingRegister to save.
     * @param trainingRegister the trainingRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingRegister,
     * or with status {@code 400 (Bad Request)} if the trainingRegister is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-registers/{id}")
    public ResponseEntity<TrainingRegister> updateTrainingRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrainingRegister trainingRegister
    ) throws URISyntaxException {
        log.debug("REST request to update TrainingRegister : {}, {}", id, trainingRegister);
        if (trainingRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrainingRegister result = trainingRegisterRepository.save(trainingRegister);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingRegister.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /training-registers/:id} : Partial updates given fields of an existing trainingRegister, field will ignore if it is null
     *
     * @param id the id of the trainingRegister to save.
     * @param trainingRegister the trainingRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingRegister,
     * or with status {@code 400 (Bad Request)} if the trainingRegister is not valid,
     * or with status {@code 404 (Not Found)} if the trainingRegister is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/training-registers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingRegister> partialUpdateTrainingRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrainingRegister trainingRegister
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrainingRegister partially : {}, {}", id, trainingRegister);
        if (trainingRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingRegister> result = trainingRegisterRepository
            .findById(trainingRegister.getId())
            .map(existingTrainingRegister -> {
                if (trainingRegister.getUserAttendance() != null) {
                    existingTrainingRegister.setUserAttendance(trainingRegister.getUserAttendance());
                }
                if (trainingRegister.getCerificate() != null) {
                    existingTrainingRegister.setCerificate(trainingRegister.getCerificate());
                }

                return existingTrainingRegister;
            })
            .map(trainingRegisterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingRegister.getId().toString())
        );
    }

    /**
     * {@code GET  /training-registers} : get all the trainingRegisters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingRegisters in body.
     */
    @GetMapping("/training-registers")
    public List<TrainingRegister> getAllTrainingRegisters() {
        log.debug("REST request to get all TrainingRegisters");
        return trainingRegisterRepository.findAll();
    }

    /**
     * {@code GET  /training-registers/:id} : get the "id" trainingRegister.
     *
     * @param id the id of the trainingRegister to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingRegister, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-registers/{id}")
    public ResponseEntity<TrainingRegister> getTrainingRegister(@PathVariable Long id) {
        log.debug("REST request to get TrainingRegister : {}", id);
        Optional<TrainingRegister> trainingRegister = trainingRegisterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trainingRegister);
    }

    /**
     * {@code DELETE  /training-registers/:id} : delete the "id" trainingRegister.
     *
     * @param id the id of the trainingRegister to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-registers/{id}")
    public ResponseEntity<Void> deleteTrainingRegister(@PathVariable Long id) {
        log.debug("REST request to delete TrainingRegister : {}", id);
        trainingRegisterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
