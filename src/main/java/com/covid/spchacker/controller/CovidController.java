package com.covid.spchacker.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Iterable<Patients> save(@RequestBody List<Patients> allPatients ) throws Exception{
		 return covidService.savePatents(allPatients);

	}

	@GetMapping("/covid")
	public Map<String ,List<Patients>> getAllStateData(@RequestHeader("auth") String auth) throws Exception{
		 return covidService.getAllStateData(auth);

	}
	
	@GetMapping("/covidsingle")
	public List<Patients> getStateData(@PathParam("state")String state , @RequestHeader("auth") String auth) throws Exception{
		 return covidService.getSingleStateData(state, auth);

	}
	
	@GetMapping("/date")
	public List<Patients> geteDataByDateCondition(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth, @RequestParam(name="startDate",required=false)Date startDate,
			@RequestParam(name="endDate",required=false)Date endDate , @RequestParam(name="status",defaultValue = "admit" , required = false)String status) throws Exception{


		if(StringUtils.isEmpty(state) && status.equals("admit")) {
			return covidService.getDataByAdmitDate(startDate, endDate, auth);
		} 
		else if(StringUtils.isEmpty(state) && !status.equals("admit")) {
			return covidService.getDataByReleaseDate(startDate, endDate, auth);
		} 

		else if(!StringUtils.isEmpty(state) && status.equals("admit")) {
			return covidService.getDataByAdmitDateforState(startDate, endDate, auth, state);
		} 

		else if(!StringUtils.isEmpty(state) && !status.equals("admit")) {
			return covidService.getDataByReleaseDateforState(startDate, endDate, auth, state);
		} 

		return null;

	}
	
	@GetMapping("/gender")
	public List<Patients> geteDataByGender(@RequestParam(name="state",required=false)String state , @RequestHeader("auth") String auth,@RequestParam(name="gender",required=true)char gender ) throws Exception{


		if(StringUtils.isEmpty(state)) {
			return covidService.getDataByGender(auth, gender);
		} else {
			return covidService.getStateDataByGender(state, auth, gender);
		}
		
	}
}
