package com.covid.spchacker.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

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

	@Autowired
	CovidService covidService;

	@PostMapping("/covid")
	public ResponseEntity<Iterable<Patients>> save(@RequestBody List<Patients> allPatients ) throws Exception{
		Iterable<Patients> result  = null;
		try {
			result = covidService.savePatents(allPatients);
		}catch(Exception ex) {
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/covid")
	public ResponseEntity<Map<String ,List<Patients>>> getAllStateData(@RequestHeader("auth") String auth) throws Exception{
		Map<String ,List<Patients>> result = null;
		try {
			result = covidService.getAllStateData(auth);
		}catch(Exception ex) {
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@GetMapping("/covidsingle")
	public ResponseEntity<List<Patients>> getStateData(@PathParam("state")String state , @RequestHeader("auth") String auth) throws Exception{
		List<Patients> result = null;
		try {
			result = covidService.getSingleStateData(state, auth);
		} catch(Exception ex) {
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/date")
	public ResponseEntity<List<Patients>> geteDataByDateCondition(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth, @RequestParam(name="startDate",required=false)Date startDate,
			@RequestParam(name="endDate",required=false)Date endDate , @RequestParam(name="status",defaultValue = "admit" , required = false)String status) throws Exception{

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
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(result,HttpStatus.OK); 

	}

	@GetMapping("/gender")
	public ResponseEntity<List<Patients>> geteDataByGender(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth,@RequestParam(name="gender",required=true)char gender ) throws Exception{

		List<Patients> result = null;
		try {
			if(StringUtils.isEmpty(state)) {
				result =  covidService.getDataByGender(auth, gender);
			} else {
				result =  covidService.getStateDataByGender(state, auth, gender);
			}
		} catch(Exception ex) {
			if(ex.getMessage().equals("Invalid Request -- either invalid auth or user is logout")) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.OK); 
	}
}
