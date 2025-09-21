package com.example.demo.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "grievances")
public class GrievanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String issue;

	private String department;

	@ManyToOne
	@JoinColumn(name = "submitted_by_id")
	private UserEntity submittedBy;

	private String status;

	private String result;

	@ElementCollection
	@CollectionTable(name = "grievance_assignees", joinColumns = @JoinColumn(name = "grievance_id"))
	@Column(name = "assignee_user_id")
	private List<String> assignedToIds = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public UserEntity getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(UserEntity submittedBy) {
		this.submittedBy = submittedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getAssignedToIds() {
		return assignedToIds;
	}

	public void setAssignedToIds(List<String> assignedToIds) {
		this.assignedToIds = assignedToIds;
	}
}


