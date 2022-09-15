package ls.lesm.payload.response;

import java.util.List;

import lombok.Data;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.InternalExpenses;
@Data
public class EmployeeFullDetailsResponse {
	
	private EmployeeDetailsResponse detailsResponse;
	private Double salary;
	private List<InternalExpenses> internalExpenses;
	private List<EmployeesAtClientsDetails> employeeAtClientsDetails;

}
