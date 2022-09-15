package ls.lesm.model;



import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
	
	public Attachment(String fileName, String fileType, byte[] content) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		Content = content;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attachment_Id;
	private String fileName;
	private String fileType;
	@Lob
	private byte[] Content;

	private LocalDate createdAt;// timeStamp;
	
	@Column(length=20)
	private String createdBy;// principal
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_fk")
	private MasterEmployeeDetails masterEmployeeDetails;

}
