package com.example.demo.Controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.ApiResponse;
import com.example.demo.Entities.GrievanceEntity;
import com.example.demo.Entities.UserEntity;
import com.example.demo.DaoServices.EmailService;
import com.example.demo.DaoServices.GrievanceService;
import com.example.demo.DaoServices.UserService;
import com.example.demo.Dto.StatusUpdateRequest;


@RestController
@RequestMapping("/grievances")
public class GrievanceController {

	@Autowired
    private GrievanceService grievanceService;
	
	@Autowired
	private UserService userser;
	
	@Autowired
	private EmailService mailer;

	@PostMapping("/submit-grievance")
	public ResponseEntity<ApiResponse<GrievanceEntity>> create(@RequestBody GrievanceEntity grievance) {
        GrievanceEntity saved = grievanceService.submitGrievance(grievance);
		return ResponseEntity.ok(new ApiResponse<>("success", "Grievance submitted", saved));
	}

	@GetMapping("/list-grievances")
	public ResponseEntity<ApiResponse<List<GrievanceEntity>>> list() {
		List<GrievanceEntity> all = grievanceService.findAll();
		return ResponseEntity.ok(new ApiResponse<>("success", "Fetched grievances", all));
	}

	@GetMapping("/list-grievances/assigned/{managerId}")
	public ResponseEntity<ApiResponse<List<GrievanceEntity>>> listAssigned(@PathVariable("managerId") String managerId) {
		List<GrievanceEntity> all = grievanceService.findAllAssignedToManager(managerId);
		return ResponseEntity.ok(new ApiResponse<>("success", "Fetched grievances for manager", all));
	}

	@PostMapping("/update-status/{id}")
	public ResponseEntity<ApiResponse<GrievanceEntity>> updateStatus(@PathVariable("id") Long id, @RequestBody StatusUpdateRequest request) {
		GrievanceEntity updated = grievanceService.updateStatusAndResult(id, request.getStatus(), request.getResult());
		if (updated == null) {
			return ResponseEntity.status(404).body(new ApiResponse<>("error", "Grievance not found", null));
		}
		return ResponseEntity.ok(new ApiResponse<>("success", "Grievance updated", updated));
	}
	
	
	@GetMapping("/submissionAlert/{id}")
	public ResponseEntity<ApiResponse<Object>> sendGreivanceSubmittedEmail(@PathVariable("id") String id) {
	    UserEntity u = userser.findByuserId(id); 
	    GrievanceEntity g = grievanceService.findBySubmitterId(id); 
	    if (u == null || g == null) {
	        return ResponseEntity.status(404).body(new ApiResponse<>("error", "User or grievance not found", null));
	    }
	    mailer.sendGrievanceSubmissionEmail(u, g);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Grievance submission email sent", null));
	}
}


