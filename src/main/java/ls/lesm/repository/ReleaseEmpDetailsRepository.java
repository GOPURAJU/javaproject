package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ls.lesm.model.ReleaseEmpDetails;

public interface ReleaseEmpDetailsRepository  extends JpaRepository<ReleaseEmpDetails,Integer> {
	
	
	
	@Query("FROM ReleaseEmpDetails g where g.masterEmployeeDetailsId.id = :masterEmployeeDetailsId")
    Optional<ReleaseEmpDetails>  findBymasterEmployeeDetails_Id(@Param("masterEmployeeDetailsId")Integer id);
	
	
	

}
