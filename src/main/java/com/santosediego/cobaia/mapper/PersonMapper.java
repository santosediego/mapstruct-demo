package com.santosediego.cobaia.mapper;

import com.santosediego.cobaia.dto.AddressDTO;
import com.santosediego.cobaia.dto.PersonDTO;
import com.santosediego.cobaia.dto.PersonSummarizedDTO;
import com.santosediego.cobaia.entities.Address;
import com.santosediego.cobaia.entities.Person;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    /* https://www.byiorio.com.br/product/4/item/28 */

    PersonDTO toPersonDTO(Person person);

    Person toPerson(PersonDTO personDTO);

    PersonSummarizedDTO toPersonSummarizedDTO(Person person);

    // O DTO não carrega a pessoa; o vínculo é refeito em linkAddresses.
    @Mapping(target = "person", ignore = true)
    Address toAddress(AddressDTO addressDTO);

    // Depois de converter PersonDTO em Person, liga cada endereço à pessoa.
    // Sem isso, os endereços seriam gravados com PERSON_ID nulo.
    @AfterMapping
    default void linkAddresses(@MappingTarget Person person) {
        if (person.getAddresses() != null) {
            person.getAddresses().forEach(address -> address.setPerson(person));
        }
    }
}
