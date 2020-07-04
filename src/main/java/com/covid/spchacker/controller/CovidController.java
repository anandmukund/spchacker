package com.covid.spchacker.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.covid.spchacker.entity.Patients;
import com.covid.spchacker.service.CovidService;

@RestController
@RequestMapping("/data")
public class CovidController {

	private static final Logger logger = LoggerFactory.getLogger(CovidController.class);

	@Autowired
	CovidService covidService;

	@PostMapping("/covid")
	public ResponseEntity<Object> save(@RequestBody List<Patients> allPatients ) throws Exception{
		Iterable<Patients> result  = null;
		logger.info("Request recived for covid date save /update ...");
		try {
			result = covidService.savePatents(allPatients);
			logger.info("  covid date saved/updated  with  {}  records" , allPatients.size());
		}catch(Exception ex) {
			logger.error("Issue  with   covid date save /update  with  {}  records" , allPatients.size());
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
                 return new ResponseEntity<>(ex.getMessage() ,HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/covid")
	public ResponseEntity<Object> getAllStateData(@RequestHeader("auth") String auth) throws Exception{
		
		logger.info("Getting  all state  date   records......");
		Map<String ,List<Patients>> result = null;
		try {
			result = covidService.getAllStateData(auth);
			logger.info(" all state  date   records found with size {}......", result.size());
		}catch(Exception ex) {
			logger.error(" Issue with getting covid data {}......",ex.getLocalizedMessage());
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@GetMapping("/covidsingle")
	public ResponseEntity<Object> getStateData(@PathParam("state")String state , @RequestHeader("auth") String auth) throws Exception{
		logger.info("Getting   state {}  covid data ......" , state);
		List<Patients> result = null;
		try {
			result = covidService.getSingleStateData(state, auth);
			logger.info("   state {}  covid data found with size {} ......" , state , result.size());
		} catch(Exception ex) {
			logger.error(" Issue with getting data for   state {}  issue is  {} ......" , state , ex.getMessage());
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/date")
	public ResponseEntity<Object> geteDataByDateCondition(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth, @RequestParam(name="startDate",required=true)Date startDate,
			@RequestParam(name="endDate",required=true)Date endDate , @RequestParam(name="status",defaultValue = "admit" , required = false)String status) throws Exception{

		logger.info("Getting   state {}  covid data With date range from {} to {} with status {} ......" , state , startDate , endDate ,status);
		List<Patients> result = null;
		try {
			if(StringUtils.isEmpty(state) && status.equals("admit")) {
				result =  covidService.getDataByAdmitDate(startDate, endDate, auth);
			} 
			else if(StringUtils.isEmpty(state) && !status.equals("admit")) {
				result =  covidService.getDataByReleaseDate(startDate, endDate, auth);
			} 

			else if(!StringUtils.isEmpty(state) && status.equals("admit")) {
				result =  covidService.getDataByAdmitDateforState(startDate, endDate, auth, state);
			} 

			else if(!StringUtils.isEmpty(state) && !status.equals("admit")) {
				result =  covidService.getDataByReleaseDateforState(startDate, endDate, auth, state);
			} 
		} catch(Exception ex) {
			logger.error("Issue with getting data for   state {}  covid data With date range from {} to {} and issue is {}" , state , startDate , endDate , ex.getMessage());
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		logger.info("Found    state {}  covid data With date range from {} to {} with size {}" , state , startDate , endDate , result.size());
		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/gender")
	public ResponseEntity<Object> geteDataByGender(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth,@RequestParam(name="gender",required=true)char gender ) throws Exception{
		logger.info("Getting   state {}  covid data With date gender {}......" , state , gender );
		List<Patients> result = null;
		try {
			if(StringUtils.isEmpty(state)) {
				result =  covidService.getDataByGender(auth, gender);
			} else {
				result =  covidService.getStateDataByGender(state, auth, gender);
			}
		} catch(Exception ex) {
			logger.error("Issue with getting data  state {}  covid data With date gender {}  issue is {} " , state , gender,ex.getMessage() );
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(ex.getMessage() , HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		logger.info("dat found for    state {}  covid data With date gender {}...... size {}" , state , gender,result.size() );
		return new ResponseEntity<>(result,HttpStatus.OK); 
	}
}
