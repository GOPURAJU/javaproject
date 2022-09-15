package ls.lesm.restcontroller;



import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.service.impl.PromoteEmpServiceImp;
import ls.lesm.service.impl.ReleaseEmpServiceImp;
@Component
@RestController
@CrossOrigin("*")
public class ReleaseEmpController {

	@Autowired
	private ReleaseEmpServiceImp releaseEmpServiceImp;
	@Autowired
	MasterEmployeeDetailsRepository detailsRepository;

	@Autowired
	ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	PromoteEmpServiceImp promoteEmpServiceImp;

	@GetMapping("/getallemployees")
	public List<MasterEmployeeDetails> getEmployees() {

		List<MasterEmployeeDetails> a = releaseEmpServiceImp.getAllEmp();

		return a;

	}

	@GetMapping("/getemp/lancesoftId")
	public ResponseEntity<?> emp(@PathVariable String lancesoftId) {
		releaseEmpServiceImp.get(lancesoftId);
		return new ResponseEntity<>("Request Sent Successfully", HttpStatus.CREATED);

	}

	@GetMapping("/AssignToDropDown/{lancesoftId}")
	public List<MasterEmployeeDetails> AssignTo(@PathVariable  String lancesoftId) {
		MasterEmployeeDetails employee = detailsRepository.findByLancesoft(lancesoftId);

		int d = detailsRepository.findById(employee.getEmpId()).get().getDesignations().getDesgId();

		return detailsRepository.findBydesignations_Id(d);
	}

	@GetMapping("/setSupervisorForTransfer/{oldemp}/{newemp}")

	ResponseEntity<String> release_EmployeeDetails(@PathVariable  String oldemp , @PathVariable String newemp) {

		releaseEmpServiceImp.releaseEmployeeDetailss(oldemp, newemp);

		return new ResponseEntity<String>("assigned successfully", HttpStatus.ACCEPTED);

	}

	@GetMapping("/send/{lancesoftId}/{empstatus}")

	ResponseEntity<String> request(@PathVariable String lancesoftId, @PathVariable boolean empstatus, Principal principal) {

		releaseEmpServiceImp.approveRequest(lancesoftId, empstatus, principal);
		return new ResponseEntity<String>("Updated Employee Status ", HttpStatus.ACCEPTED);

	}

}

