package com.ainia.ecgApi.controller.health;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ainia.ecgApi.core.crud.BaseController;
import com.ainia.ecgApi.core.crud.BaseService;
import com.ainia.ecgApi.core.crud.Query;
import com.ainia.ecgApi.core.exception.ServiceException;
import com.ainia.ecgApi.core.security.AuthUser;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.health.HealthExamination;
import com.ainia.ecgApi.domain.health.HealthReply;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.common.UploadService;
import com.ainia.ecgApi.service.common.UploadService.Type;
import com.ainia.ecgApi.service.health.HealthExaminationService;
import com.ainia.ecgApi.service.health.HealthReplyService;

/**
 * <p>HealthExamination controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * HealthExaminationController.java
 * @author pq
 * @createdDate 2013-07-07
 * @version 0.1
 */
@Controller
@RequestMapping("/api/examination")
public class HealthExaminationController extends BaseController<HealthExamination , Long> {

    @Autowired
    private HealthExaminationService healthExaminationService;
    @Autowired
    private HealthReplyService healthReplyService;
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private UploadService uploadService;
    
    @Override
    public BaseService<HealthExamination , Long> getBaseService() {
        return healthExaminationService;
    }

	@Override
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<AjaxResult> create(HealthExamination domain) {
		return new ResponseEntity<AjaxResult>(HttpStatus.METHOD_NOT_ALLOWED);
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseEntity<AjaxResult> delete(Long id) {
		return new ResponseEntity<AjaxResult>(HttpStatus.METHOD_NOT_ALLOWED);
	}
    /**
     * <p>创建健康监测回复</p>
     * @param id
     * @param reply
     * @return
     * ResponseEntity<AjaxResult>
     */
	@RequestMapping(value = "/{examinationId}/reply" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity<AjaxResult> reply(@PathVariable("examinationId") Long id , HealthReply reply) {
    	HealthExamination examination = healthExaminationService.get(id);
    	if (examination == null) {
    		return new ResponseEntity<AjaxResult>(HttpStatus.NOT_FOUND);
    	}
    	AuthUser authUser = authenticateService.getCurrentUser();
    	reply.setEmployeeId(authUser.getId());
    	reply.setExaminationId(id);
    	
    	healthExaminationService.reply(id , reply);
    	return new ResponseEntity<AjaxResult>(HttpStatus.CREATED);
    }

    /**
     * <p>获取健康监测回复</p>
     * @param id
     * @param reply
     * @return
     * ResponseEntity<AjaxResult>
     */
	@RequestMapping(value = "/{id}/reply" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity<?> findReply(@PathVariable("id") Long id) {
    	HealthExamination examination = healthExaminationService.get(id);
    	if (examination == null) {
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	Query<HealthReply> query = new Query();
    	query.eq(HealthReply.EXAMINATION_ID , id);
    	
    	return new ResponseEntity(healthReplyService.findAll(query) , HttpStatus.OK);
    }
	
    /**
     * <p>测试数据上传接口</p>
     * @param file
     * @return
     * ResponseEntity
     * @throws IOException 
     */
	@RequestMapping(value = "upload" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResponseEntity upload( @RequestParam("file") MultipartFile file) throws IOException {
		ResponseEntity entity = new ResponseEntity(HttpStatus.OK);
    	if (file.getBytes().length == 0) {
    		entity =  new ResponseEntity(HttpStatus.BAD_REQUEST);
    	}
		AuthUser authUser = authenticateService.getCurrentUser();	
		if (authUser == null) {
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		if (!User.class.getSimpleName().equals(authUser.getType())) {
			entity = new ResponseEntity(HttpStatus.FORBIDDEN);
		}
    	healthExaminationService.upload(file.getBytes());
    	return entity;
    }
	
	/**
	 * <p>获取心电图1</p>
	 * @return
	 * byte[]
	 * @throws IOException 
	 */
	@RequestMapping(value = "{id}/ecg1" , method = RequestMethod.GET)
	public void loadEcg1(@PathVariable("id") Long id , HttpServletResponse response)  {
		HealthExamination examination = healthExaminationService.get(id);
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(examination.getCreatedDate());
		//TODO 文件后缀名固定
		String ecg1Path = String.valueOf(examination.getUserId()) + "/" + dateStr + "-" + examination.getId() + "/ecg1.jpg";
		try {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(uploadService.load(Type.heart_img , ecg1Path));
			response.getOutputStream().flush();
		} catch (IOException e) {
			throw new ServiceException("examination.ecg1Path.notFound");
		}
	}

	/**
	 * <p>获取心电图1</p>
	 * @return
	 * byte[]
	 * @throws IOException 
	 */
	@RequestMapping(value = "{id}/ecg2" , method = RequestMethod.GET)
	public void loadEcg2(@PathVariable("id") Long id , HttpServletResponse response)  {
		HealthExamination examination = healthExaminationService.get(id);
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(examination.getCreatedDate());
		//TODO 文件后缀名固定
		String ecg1Path = String.valueOf(examination.getUserId()) + "/" + dateStr + "-" + examination.getId() + "/ecg2.jpg";
		try {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(uploadService.load(Type.heart_img , ecg1Path));
			response.getOutputStream().flush();
		} catch (IOException e) {
			throw new ServiceException("examination.ecg1Path.notFound");
		}
	}
	
	/**
	 * <p>获取心电图1</p>
	 * @return
	 * byte[]
	 * @throws IOException 
	 */
	@RequestMapping(value = "{id}/ecg3" , method = RequestMethod.GET)
	public void loadEcg3(@PathVariable("id") Long id , HttpServletResponse response)  {
		HealthExamination examination = healthExaminationService.get(id);
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(examination.getCreatedDate());
		//TODO 文件后缀名固定
		String ecg1Path = String.valueOf(examination.getUserId()) + "/" + dateStr + "-" + examination.getId() + "/ecg3.jpg";
		try {
			response.setContentType("image/jpeg");
			response.getOutputStream().write(uploadService.load(Type.heart_img , ecg1Path));
			response.getOutputStream().flush();
		} catch (IOException e) {
			throw new ServiceException("examination.ecg1Path.notFound");
		}
	}
}