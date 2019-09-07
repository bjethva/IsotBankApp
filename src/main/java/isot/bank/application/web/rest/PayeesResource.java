package isot.bank.application.web.rest;

import isot.bank.application.service.PayeesService;
import isot.bank.application.web.rest.errors.BadRequestAlertException;
import isot.bank.application.service.dto.PayeesDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link isot.bank.application.domain.Payees}.
 */
@RestController
@RequestMapping("/api")
public class PayeesResource {

    private final Logger log = LoggerFactory.getLogger(PayeesResource.class);

    private static final String ENTITY_NAME = "payees";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayeesService payeesService;

    public PayeesResource(PayeesService payeesService) {
        this.payeesService = payeesService;
    }

    /**
     * {@code POST  /payees} : Create a new payees.
     *
     * @param payeesDTO the payeesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payeesDTO, or with status {@code 400 (Bad Request)} if the payees has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payees")
    public ResponseEntity<PayeesDTO> createPayees(@Valid @RequestBody PayeesDTO payeesDTO) throws URISyntaxException {
        log.debug("REST request to save Payees : {}", payeesDTO);
        if (payeesDTO.getId() != null) {
            throw new BadRequestAlertException("A new payees cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayeesDTO result = payeesService.save(payeesDTO);
        return ResponseEntity.created(new URI("/api/payees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payees} : Updates an existing payees.
     *
     * @param payeesDTO the payeesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payeesDTO,
     * or with status {@code 400 (Bad Request)} if the payeesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payeesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payees")
    public ResponseEntity<PayeesDTO> updatePayees(@Valid @RequestBody PayeesDTO payeesDTO) throws URISyntaxException {
        log.debug("REST request to update Payees : {}", payeesDTO);
        if (payeesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PayeesDTO result = payeesService.save(payeesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, payeesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payees} : get all the payees.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payees in body.
     */
    @GetMapping("/payees")
    public List<PayeesDTO> getAllPayees() {
        log.debug("REST request to get all Payees");
        return payeesService.findAll();
    }

    /**
     * {@code GET  /payees/:id} : get the "id" payees.
     *
     * @param id the id of the payeesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payeesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payees/{id}")
    public ResponseEntity<PayeesDTO> getPayees(@PathVariable Long id) {
        log.debug("REST request to get Payees : {}", id);
        Optional<PayeesDTO> payeesDTO = payeesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payeesDTO);
    }

    /**
     * {@code DELETE  /payees/:id} : delete the "id" payees.
     *
     * @param id the id of the payeesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payees/{id}")
    public ResponseEntity<Void> deletePayees(@PathVariable Long id) {
        log.debug("REST request to delete Payees : {}", id);
        payeesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
