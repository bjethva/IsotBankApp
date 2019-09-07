package isot.bank.application.service.impl;

import isot.bank.application.service.PayeesService;
import isot.bank.application.domain.Payees;
import isot.bank.application.repository.PayeesRepository;
import isot.bank.application.service.dto.PayeesDTO;
import isot.bank.application.service.mapper.PayeesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Payees}.
 */
@Service
@Transactional
public class PayeesServiceImpl implements PayeesService {

    private final Logger log = LoggerFactory.getLogger(PayeesServiceImpl.class);

    private final PayeesRepository payeesRepository;

    private final PayeesMapper payeesMapper;

    public PayeesServiceImpl(PayeesRepository payeesRepository, PayeesMapper payeesMapper) {
        this.payeesRepository = payeesRepository;
        this.payeesMapper = payeesMapper;
    }

    /**
     * Save a payees.
     *
     * @param payeesDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PayeesDTO save(PayeesDTO payeesDTO) {
        log.debug("Request to save Payees : {}", payeesDTO);
        Payees payees = payeesMapper.toEntity(payeesDTO);
        payees = payeesRepository.save(payees);
        return payeesMapper.toDto(payees);
    }

    /**
     * Get all the payees.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PayeesDTO> findAll() {
        log.debug("Request to get all Payees");
        return payeesRepository.findAll().stream()
            .map(payeesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one payees by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PayeesDTO> findOne(Long id) {
        log.debug("Request to get Payees : {}", id);
        return payeesRepository.findById(id)
            .map(payeesMapper::toDto);
    }

    /**
     * Delete the payees by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payees : {}", id);
        payeesRepository.deleteById(id);
    }
}
