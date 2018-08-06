package mobile;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.Collections;
import javax.validation.ConstraintViolationException;

@RestController
public class MobileResource
{

	@Autowired
	private MobileRepository mobileRepository;

  @GetMapping("/mobiles")
  public Iterable<Mobile> retrieveAllMobiles()
  {
    return mobileRepository.findAll();
  }

  @GetMapping("/mobiles/{id}")
  public Mobile retrieveMobile(@PathVariable long id)
  {
  	Optional<Mobile> mobile = mobileRepository.findById(id);
  	if (!mobile.isPresent())
  		throw new MobileNotFoundException("id-" + id);
  	return mobile.get();
  }

  @DeleteMapping("/mobiles/{id}")
  public void deleteMobile(@PathVariable long id)
  {
    Optional<Mobile> mobile = mobileRepository.findById(id);
    if (!mobile.isPresent())
      throw new MobileNotFoundException("id-" + id);
	   mobileRepository.deleteById(id);
  }

  @PostMapping("/mobiles")
  public ResponseEntity<Object> createMobile(@RequestBody Mobile mobile)
  {
		try
		{

			Mobile savedMobile = mobileRepository.save(mobile);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(savedMobile.getId()).toUri();
			return ResponseEntity.created(location).build();
		}
		catch(Exception e)
		{
			throw new MobileConflictException("id-/{id}");
		}
  }

	@PutMapping("/mobiles/{id}")
  public ResponseEntity<Object> updateMobile(@RequestBody Mobile mobile, @PathVariable long id)
  {
    Optional<Mobile> mobileOptional = mobileRepository.findById(id);
    if (!mobileOptional.isPresent())
      throw new MobileNotFoundException("id-" + id);

  	mobile.setId(id);

  	mobileRepository.save(mobile);

  	return ResponseEntity.noContent().build();
  }
}
