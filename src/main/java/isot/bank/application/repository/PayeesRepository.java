package isot.bank.application.repository;

import isot.bank.application.domain.Payees;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Payees entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayeesRepository extends JpaRepository<Payees, Long> {

    @Query("select payees from Payees payees where payees.userTOPayee.login = ?#{principal.username}")
    List<Payees> findByUserTOPayeeIsCurrentUser();

}
