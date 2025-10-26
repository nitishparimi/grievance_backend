package com.example.demo.DaoServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.GrievanceEntity;
import com.example.demo.Entities.UserEntity;
import com.example.demo.Repositories.GrievanceRepository;
import com.example.demo.Repositories.UserRepository;

@Service
public class GrievanceService {

    @Autowired
    private GrievanceRepository grievanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public GrievanceEntity submitGrievance(GrievanceEntity grievance) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication != null ? authentication.getName() : null;

        if (currentUserId != null) {
            UserEntity submitter = userRepository.findByBusinessId(currentUserId);
            if (submitter != null) {
                grievance.setSubmittedBy(submitter);
            }
        }

        if (grievance.getStatus() == null || grievance.getStatus().isEmpty()) {
            grievance.setStatus("OPEN");
        }

        if (grievance.getDepartment() != null) {
            List<UserEntity> managers = userRepository.findByRoleAndDepartment("MANAGER", grievance.getDepartment());
            if (managers != null && !managers.isEmpty()) {
                List<String> assigneeIds = managers.stream().map(UserEntity::getBusinessId).toList();
                grievance.setAssignedToIds(assigneeIds);
            }
        }

        GrievanceEntity saved = grievanceRepository.save(grievance);
        if (saved != null && saved.getSubmittedBy() != null) {
            emailService.sendGrievanceSubmissionEmail(saved.getSubmittedBy(), saved);
        }
        return saved;
    }

    public GrievanceEntity findBySubmitterId(String businessId) {
        List<GrievanceEntity> list = grievanceRepository.findBySubmittedByBusinessIdOrderByIdDesc(businessId);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<GrievanceEntity> findAll() {
        return grievanceRepository.findAll();
    }

    public List<GrievanceEntity> findAllAssignedToManager(String managerUserId) {
        return grievanceRepository.findByAssignedToManagerId(managerUserId);
    }
    
    public List<GrievanceEntity> findGrievanceByUser(String id) {
    	List<GrievanceEntity> list = grievanceRepository.findBySubmittedByBusinessIdOrderByIdDesc(id);
        return list;
    }

    public GrievanceEntity updateStatusAndResult(Long grievanceId, String status, String result) {
        GrievanceEntity grievance = grievanceRepository.findById(grievanceId).orElse(null);
        if (grievance == null) return null;
        if (status != null) grievance.setStatus(status);
        if (result != null) grievance.setResult(result);
        GrievanceEntity saved = grievanceRepository.save(grievance);
        if (saved.getSubmittedBy() != null) {
            UserEntity submitter = userRepository.findByBusinessId(saved.getSubmittedBy().getBusinessId());
            if (submitter != null) {
                emailService.sendGrievanceStatusUpdateEmail(submitter, saved);
            }
        }
        return saved;
    }
}
