package isot.bank.application.service;

import isot.bank.application.service.dto.PayeesDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link isot.bank.application.domain.Payees}.
 */
public interface PayeesService {

    /**
     * Save a payees.
     *
     * @param payeesDTO the entity to save.
     * @return the persisted entity.
     */
    PayeesDTO save(PayeesDTO payeesDTO);

    /**
     * Get all the payees.
     *
     * @return the list of entities.
     */
    List<PayeesDTO> findAll();


    /**
     * Get the "id" payees.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PayeesDTO> findOne(Long id);

    /**
     * Delete the "id" payees.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
