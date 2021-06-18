package one.digitalinnovation.personapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;

@Service
public class PersonService {
	
	private PersonRepository personRepository;
	
	private final PersonMapper personMapper = PersonMapper.INSTANCE;
	
	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	// Method Create 
	public MessageResponseDTO createPerson(PersonDTO personDTO) {
		Person personToSave = personMapper.toModel(personDTO);
		
		Person savedPerson = personRepository.save(personToSave);
		return createdMessageResponse(savedPerson.getId(), "Created");
	}
	
	// Method Read
	public List<PersonDTO> listAll() {
		List<Person> allPeople = personRepository.findAll();
		
		return allPeople.stream()
				.map(personMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	// Method Read with Id
	public PersonDTO findById(Long id) throws PersonNotFoundException{
		Person person = verifyIfExists(id);

		return personMapper.toDTO(person);
	}
	
	// Method Update
		public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
			verifyIfExists(id);
			
			Person personToUpdate = personMapper.toModel(personDTO);
			
			Person updatedPerson = personRepository.save(personToUpdate);
			return createdMessageResponse(updatedPerson.getId(), "Updated");
		}
	
	// Method Delete
	public void deleteById(Long id) throws PersonNotFoundException{
		verifyIfExists(id);
	
		personRepository.deleteById(id);
	}
	
	// Methods Privates
	private MessageResponseDTO createdMessageResponse(Long id, String message) {
		return MessageResponseDTO
				.builder()
				.message(message + " person with ID " + id)
				.build();
	}
	
	private Person verifyIfExists(Long id) throws PersonNotFoundException {
		return personRepository.findById(id)
			.orElseThrow(()-> new PersonNotFoundException(id));
	}
	
}
