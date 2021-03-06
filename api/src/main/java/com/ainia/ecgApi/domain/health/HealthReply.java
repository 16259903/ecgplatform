package com.ainia.ecgApi.domain.health;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ainia.ecgApi.core.bean.Domain;
import com.ainia.ecgApi.domain.health.HealthRule.Level;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>健康回复实体</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthReply.java
 * @author pq
 * @createdDate 2013-7-7
 * @version 0.1
 */
@Entity
public class HealthReply implements Domain {
	
	public static final String EXAMINATION_ID = "examinationId";
	public static final String CREATED_DATE = "createdDate";

	private Long id;
	private String type;
	private String reason;
	private String result;
	private String content;
	private Level  level;
	private Long employeeId;
	private Long examinationId;
	private Date createdDate;
	private Date lastUpdated;
	private Integer version;
	
	@PrePersist
	public void onCreate() {
		this.createdDate = new Date();
		this.lastUpdated = new Date();
	}
	@PreUpdate
	public void onUpdate() {
		this.lastUpdated = new Date();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	public Long getId() {
		return id;
	}	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@NotBlank
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	@NotBlank
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+08:00")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}

}
