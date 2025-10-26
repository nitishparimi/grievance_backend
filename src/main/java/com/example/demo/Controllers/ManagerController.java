package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DaoServices.GrievanceService;
import com.example.demo.DaoServices.UserService;
import com.example.demo.Entities.ApiResponse;
import com.example.demo.Entities.GrievanceEntity;
import com.example.demo.Entities.UserEntity;

@RestController
@RequestMapping("/manager")
public class ManagerController {
	@Autowired
	UserService userserv;
	
	@Autowired
	GrievanceService grievanceServ;
	
	@GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserEntity>> getUserProfile(@RequestParam("id") String id) {
        UserEntity user = userserv.findUser(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "User Logged in", user));
    }
	
	@PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestParam("id") String id) {
        boolean b = userserv.settingActiveSession(id);
        if (!b) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "User not found", null));
        }
        
        return ResponseEntity.ok(new ApiResponse<>("success", "Logged out successfully", null));
    }
	
	@GetMapping("/list-assigned-grievances/{managerId}")
	public ResponseEntity<ApiResponse<List<GrievanceEntity>>> listAssigned(@PathVariable("managerId") String managerId) {
		List<GrievanceEntity> all = grievanceServ.findAllAssignedToManager(managerId);
		return ResponseEntity.ok(new ApiResponse<>("success", "Fetched grievances for manager", all));
	}
}
