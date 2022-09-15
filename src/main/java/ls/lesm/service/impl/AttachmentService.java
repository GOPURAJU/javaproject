package ls.lesm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ls.lesm.model.Attachment;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.AttachementRepo;
import ls.lesm.repository.MasterEmployeeDetailsRepository;


@Service
public class AttachmentService {
	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	@Autowired
	AttachementRepo attachementRepo;
	
	public Attachment saveAttachment(MultipartFile multipartFile,int id) throws Exception {
		

		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		try {
			if(fileName.contains(".."))
			{
				throw new Exception("filename contains the invalid path sequence");
			}
			Attachment attachment=new Attachment(fileName,multipartFile.getContentType(),multipartFile.getBytes());
			
			  // MasterEmployeeDetails employeeDetails = masterEmployeeDetailsRepository.findByLancesoft(id);
			
		
			// int emp_id=employeeDetails.getEmpId();
			 
			 
			MasterEmployeeDetails masterEmployeeDetails=masterEmployeeDetailsRepository.findById(id).get();
			
			attachment.setMasterEmployeeDetails(masterEmployeeDetails);
			 return attachementRepo.save(attachment);
			
		}
		catch (Exception e) {   
			 throw new Exception("file not saved"+fileName);
		}
		
		
		
	
	}

//	@Override
//	public Attachment getAttachment(int fileId) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public Attachment getAttachment(int Id) throws Exception {
		
		return attachementRepo.findById(Id).get();
                
              
	}
	
	
	
	
	
	
}
