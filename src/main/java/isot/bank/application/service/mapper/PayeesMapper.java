package isot.bank.application.service.mapper;

import isot.bank.application.domain.*;
import isot.bank.application.service.dto.PayeesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payees} and its DTO {@link PayeesDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PayeesMapper extends EntityMapper<PayeesDTO, Payees> {

    @Mapping(source = "userTOPayee.id", target = "userTOPayeeId")
    @Mapping(source = "userTOPayee.login", target = "userTOPayeeLogin")
    PayeesDTO toDto(Payees payees);

    @Mapping(source = "userTOPayeeId", target = "userTOPayee")
    Payees toEntity(PayeesDTO payeesDTO);

    default Payees fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payees payees = new Payees();
        payees.setId(id);
        return payees;
    }
}
