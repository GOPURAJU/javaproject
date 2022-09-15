package ls.lesm.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ls.lesm.exception.UserNameNotFoundException;
import ls.lesm.model.Attachment;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ResponseData;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.service.impl.AttachmentService;


@RestController

@CrossOrigin("*")
public class ResumeController {
	
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	AttachmentService attachmentService;
	
	
	@PostMapping("/upload")
	public ResponseData uploadingFile(@RequestParam MultipartFile file, @RequestParam String id) throws Exception {

		MasterEmployeeDetails employeeDetails = masterEmployeeDetailsRepository.findByLancesoft(id);
		if(employeeDetails==null)
		{
			throw new UserNameNotFoundException("Employee with that username not found");
		}

		int emp_id = employeeDetails.getEmpId();

		Attachment attachment = attachmentService.saveAttachment(file, emp_id);

		String m = Integer.toString(attachment.getAttachment_Id());

		String downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/emp/download/").path(m)
				.toUriString();

		return new ResponseData(attachment.getFileName(), downloadURl, file.getContentType(), file.getSize());

	}

	@GetMapping("/download/{attachment_Id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String attachment_Id) throws Exception {
		Attachment attachment = null;
		int i = Integer.parseInt(attachment_Id);
		attachment = attachmentService.getAttachment(i);
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(attachment.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
				.body(new ByteArrayResource(attachment.getContent()));
	}
	
	

}
