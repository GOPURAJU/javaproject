package ls.lesm.service.impl;



import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.UserNameNotFoundException;
import ls.lesm.model.Designations;
import ls.lesm.model.History;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.UpdatedStatus;
import ls.lesm.model.User;
import ls.lesm.payload.request.TransferRequest;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;

@Service
public class TransferEmployeeService {

	@Autowired
	MasterEmployeeDetailsRepository employeeDetailsRepository;
	@Autowired
	UserRepository userRepo;
	@Autowired
	HistoryRepository historyRepo;
	@Autowired
	SalaryRepository salaryRepository;

	@Autowired
	SubDepartmentsRepository subDepartmentsRepository;

	@Autowired
	DesignationsRepository designationsRepository;

//1

	public List<MasterEmployeeDetails> getAllEmployeeUnderCL(Principal principal) {

		User loggedU = this.userRepo.findByUsername(principal.getName());

		String id = loggedU.getUsername();

		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(id);

		int dbPk = employee.getEmpId();

		List<MasterEmployeeDetails> K = employeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);

		return K;

	}

//2
	public List<MasterEmployeeDetails> transferService(String LancesoftId) {
		List<MasterEmployeeDetails> listOfEmp = new ArrayList<>();
		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(LancesoftId);

		if (employee == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		} else {
			List<MasterEmployeeDetails> employeeDesignation = this.employeeDetailsRepository
					.findByDesignations(employee.getDesignations());

			for (MasterEmployeeDetails masterEmployeeDetails : employeeDesignation) {

				listOfEmp.add(masterEmployeeDetails);

			}

			return listOfEmp;
		}
	}

//3

	public void saveAndnext(String lancesoftId, String assign_id) {

		MasterEmployeeDetails trasferingEmp = this.employeeDetailsRepository.findByLancesoft(lancesoftId);
		if (trasferingEmp == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		} else {

			List<MasterEmployeeDetails> employeeDesignation = this.employeeDetailsRepository
					.findBymasterEmployeeDetails_Id(trasferingEmp.getEmpId());

			MasterEmployeeDetails trasferingToEmp = this.employeeDetailsRepository.findByLancesoft(assign_id);
			if (trasferingToEmp == null) {
				throw new UserNameNotFoundException("Assingto Employee not there with this lancesoftId");
			}
			for (MasterEmployeeDetails masterEmployeeDetails : employeeDesignation) {

				masterEmployeeDetails.setSupervisor(trasferingToEmp);
				employeeDetailsRepository.save(masterEmployeeDetails);

			}
		}
	}

	/// 4

	public List<MasterEmployeeDetails> newsupervisiorIDs(Principal principal) {
		User user = this.userRepo.findByUsername(principal.getName());
		String lancesoft = user.getUsername();
		
		MasterEmployeeDetails masterEmployeeDetails = employeeDetailsRepository.findByLancesoft(lancesoft);
		if (masterEmployeeDetails == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}
		else {
		List<MasterEmployeeDetails> under_masterEmployeeDetails = employeeDetailsRepository
				.findBydesignations_Id(masterEmployeeDetails.getDesignations().getDesgId());
		return under_masterEmployeeDetails;
		}
	}

//5

	public void transferEmployeeToSameDesignation(String lancesoftId, String tansferLancesoftId,
			TransferRequest transferRequest, Principal principal) {

		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findByLancesoft(lancesoftId);
		if (trasferEmp == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}
		MasterEmployeeDetails trasferToEmp = employeeDetailsRepository.findByLancesoft(tansferLancesoftId);
		if (trasferToEmp == null) {
			throw new UserNameNotFoundException("trasferToEmp not there with this lancesoftId");
		}
		User loggedU = this.userRepo.findByUsername(principal.getName());

		String id = loggedU.getUsername();
		MasterEmployeeDetails updatedBy = this.employeeDetailsRepository.findByLancesoft(id);

		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
				 trasferEmp.getSubDepartments(), trasferEmp.getDepartments(),
				trasferEmp.getDesignations(), trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(),
				UpdatedStatus.TRANSFER, LocalDate.now(), updatedBy);

		historyRepo.save(historyEmp);
		
		
		
		Salary sal=salaryRepository.findBymasterEmployeeDetails_Id(trasferEmp.getEmpId()).get();

		trasferEmp.setSupervisor(trasferToEmp);

		trasferEmp.setLocation(transferRequest.getMasterEmployeeDetails().getLocation());

		employeeDetailsRepository.save(trasferEmp);
		salaryRepository.save(new Salary(transferRequest.getSalary().getSalary(), sal.getCreatedAt(), LocalDate.now(), sal.getCreatedBy(),
				trasferEmp));

	}

///lead transfer
//	public void transferServiceMR(String lancesoftId, Principal principal) {
//
////		
////		User loggedU=this.userRepo.findByUsername(principal.getName());
////	
////		String id=loggedU.getUsername();
////	
////		MasterEmployeeDetails currentemployee=this.employeeDetailsRepository.findByLancesoft(id);
////	
////		int dbPk=currentemployee.getEmpId();
////		
////		List<MasterEmployeeDetails> K = employeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
////		for (MasterEmployeeDetails l : K) {
////			System.out.println(l);
////		}
//
//		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(lancesoftId);
//
//		Designations desg = employee.getDesignations();
//		SubDepartments sub_dep = employee.getSubDepartments();
//		MasterEmployeeDetails sup = employee.getSupervisor();
//		Integer i = sup.getEmpId();
//		List<MasterEmployeeDetails> H = this.employeeDetailsRepository
//				.findByDesignations_IdANDSubDepartmentsANDSupervisor(desg.getDesgId(), sub_dep.getSubDepartId(), i);
//
//		for (MasterEmployeeDetails masterEmployeeDetails : H) {
//
//			System.out.println(masterEmployeeDetails);
//
//		}
//
//	}

	// updated Add-ons///// need modifications............... HR dashborard

	public List<Designations> GetAllDesignation() {
		List<Designations> designations = designationsRepository.findAll();

		return designations;
	}

	public List<MasterEmployeeDetails> GetEmployees(int designation) {

		List<MasterEmployeeDetails> masterEmployeeDetails = employeeDetailsRepository
				.findBydesignations_Id(designation);

		return masterEmployeeDetails;

	}

	public List<MasterEmployeeDetails> getnewSupervisiorsinHrDashboard(String lancesoftId) {

		MasterEmployeeDetails masterEmployeeDetails = employeeDetailsRepository.findByLancesoft(lancesoftId);
		if (masterEmployeeDetails == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}
		
		
		
		Integer s = masterEmployeeDetails.getSupervisor().getEmpId();

		MasterEmployeeDetails masterEmployeeDetails1 = employeeDetailsRepository.findById(s).get();
		Integer designation = masterEmployeeDetails1.getDesignations().getDesgId();

		List<MasterEmployeeDetails> masterEmployeeDetails2 = employeeDetailsRepository
				.findBydesignations_Id(designation);

		return masterEmployeeDetails2;

	}

	///// ManagerDashboard Lead and consultant
	/// consultants dropdown
	public List<MasterEmployeeDetails> consultantsinManagerDashboard(Principal principal) {

		User loggedU = this.userRepo.findByUsername(principal.getName());

		String id = loggedU.getUsername();

		MasterEmployeeDetails manager = this.employeeDetailsRepository.findByLancesoft(id);
		if (manager == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}

		Integer manager_id = manager.getEmpId();

		List<MasterEmployeeDetails> leads = this.employeeDetailsRepository.findBymasterEmployeeDetails_Id(manager_id);

		List<MasterEmployeeDetails> consultantslist = new ArrayList<MasterEmployeeDetails>();

		for (MasterEmployeeDetails leads_id : leads) {

			List<MasterEmployeeDetails> consultants = this.employeeDetailsRepository
					.findBymasterEmployeeDetails_Id(leads_id.getEmpId());

			for (MasterEmployeeDetails M : consultants) {

				consultantslist.add(M);
			}

		}

		return consultantslist;
	}

	public List<MasterEmployeeDetails> leadsinManagerDashboard(Principal principal) {
		User loggedU = this.userRepo.findByUsername(principal.getName());

		String id = loggedU.getUsername();

		MasterEmployeeDetails manager = this.employeeDetailsRepository.findByLancesoft(id);
		if (manager == null) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}

		Integer manager_id = manager.getEmpId();

		List<MasterEmployeeDetails> leads = this.employeeDetailsRepository.findBymasterEmployeeDetails_Id(manager_id);

		List<MasterEmployeeDetails> leadslist = new ArrayList<MasterEmployeeDetails>();
		for (MasterEmployeeDetails leads_id : leads) {
			leadslist.add(leads_id);
			
		}
		return leadslist;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	
//	
//
//	public void updateLocation(int employeeId, String newLocation, int newsupervisor, Double newsalary,
//			MasterEmployeeDetails updatedBy) {
//
//		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findById(employeeId).get();
//
//		MasterEmployeeDetails newmasterEmployeeDetails = employeeDetailsRepository.findById(newsupervisor).get();
//
//		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
//				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
//				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
//				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
//				trasferEmp.getExitAt(), trasferEmp.getSubDepartments(), trasferEmp.getDepartments(),
//				trasferEmp.getDesignations(), trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(),
//				UpdatedStatus.TRANSFER, LocalDate.now(), updatedBy);
//
//		historyRepo.save(historyEmp);
//
//		trasferEmp.setLocation(newLocation);
//
//		trasferEmp.setSupervisor(newmasterEmployeeDetails);
//
//		employeeDetailsRepository.save(trasferEmp);
//
//		salaryRepository.save(new Salary(newsalary, LocalDate.now(), trasferEmp));
//
//		//
//
//	}
//
//	public List<MasterEmployeeDetails> GetEmployeesByDesignationandSubD(int empId) {
//
//		MasterEmployeeDetails employeeDetails = employeeDetailsRepository.findById(empId).get();
//
//		List<MasterEmployeeDetails> masterEmployeeDetails = employeeDetailsRepository
//				.findBydesignations_Id(employeeDetails.getDesignations().getDesgId());
//
//		String sub_dep = employeeDetails.getSubDepartments().getSubDepartmentNames();
//
//		List<MasterEmployeeDetails> employees = new ArrayList<MasterEmployeeDetails>();
//
//		for (MasterEmployeeDetails me : masterEmployeeDetails) {
//
//			if (me.getSubDepartments().getSubDepartmentNames().equals(sub_dep)) {
//
//				employees.add(me);
//			}
//
//		}
//
//		System.out.println(employees);
//
//		return employees;
//
//	}
//
//	public List<MasterEmployeeDetails> updateEmployeeDetailsUpdate(int updatedSupervisio_id, int superviserId) {
//
//		List<MasterEmployeeDetails> list = employeeDetailsRepository.findBymasterEmployeeDetails_Id(superviserId);
//		System.out.println(list);
//		MasterEmployeeDetails list1 = employeeDetailsRepository.findById(updatedSupervisio_id).get();
//		System.out.println(list1);
//
//		for (MasterEmployeeDetails mes : list) {
//			System.out.println(mes);
//
//			mes.setSupervisor(list1);
//
//			employeeDetailsRepository.save(mes);
//
//		}
//
//		return list;
//
//	}
//
//	public void updateDetails(int employeeId, String newLocation, Double newsalary, int sub_department,
//			int newsupervisor, MasterEmployeeDetails updatedBy) {
//
//		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findById(employeeId).get();
//
//		MasterEmployeeDetails newmasterEmployeeDetails = employeeDetailsRepository.findById(newsupervisor).get();
//
//		// code
//
//		SubDepartments sudp = subDepartmentsRepository.findById(sub_department).get();
//
//		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
//				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
//				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
//				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
//				trasferEmp.getExitAt(), trasferEmp.getSubDepartments(), trasferEmp.getDepartments(),
//				trasferEmp.getDesignations(), trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(),
//				UpdatedStatus.TRANSFER, LocalDate.now(), updatedBy);
//
//		historyRepo.save(historyEmp);
//
//		trasferEmp.setLocation(newLocation);
//
//		trasferEmp.setSubDepartments(sudp);
//		trasferEmp.setSupervisor(newmasterEmployeeDetails);
//
//		salaryRepository.save(new Salary(newsalary, LocalDate.now(), trasferEmp));
//
//		employeeDetailsRepository.save(trasferEmp);
//
//	}

}

