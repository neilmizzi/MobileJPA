package mobile;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "mobiles", path = "mobiles")
public interface MobileRepository extends PagingAndSortingRepository<Mobile, Long>
{
}
