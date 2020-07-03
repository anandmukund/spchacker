package com.covid.spchacker.entity;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PatientsRepository extends CrudRepository<Patients, Long> {
 
    @Query("SELECT p FROM Patients p WHERE p.state = :state")
    public List<Patients> getAllPatientsByState(@Param("state") String state);
    
    @Query("SELECT p FROM Patients p WHERE p.status = :status")
    public List<Patients> getAllPatientsByStatus(@Param("status") String status);
    
    @Query("SELECT p FROM Patients p WHERE p.gender = :gender")
    public List<Patients> getAllPatientsByGender(@Param("gender") char gender);
    
    @Query("SELECT p FROM Patients p WHERE p.gender = :gender and p.state = :state")
    public List<Patients> getAllPatientsByGenderAndState(@Param("gender") char gender , @Param("state") String state);

    @Query("SELECT p FROM Patients p WHERE p.admitdate BETWEEN :startDate and :endDate")
    public List<Patients> getAllPatientsAdmitBetweenDate(@Param("startDate") Date startDate , @Param("endDate") Date endDate);
    
    @Query("SELECT p FROM Patients p WHERE p.releasedate BETWEEN :startDate and :endDate")
    public List<Patients> getAllPatientsReleaseBetweenDate(@Param("startDate") Date startDate , @Param("endDate") Date endDate);

    @Query("SELECT p FROM Patients p WHERE p.admitdate BETWEEN :startDate and :endDate and p.state =:state")
    public List<Patients> getAllPatientsForstateAdmitBetweenDate(@Param("startDate") Date startDate , @Param("endDate") Date endDate, String state);
    
    @Query("SELECT p FROM Patients p WHERE p.releasedate BETWEEN :startDate and :endDate and p.state =:state")
    public List<Patients> getAllPatientsstateReleaseBetweenDate(@Param("startDate") Date startDate , @Param("endDate") Date endDate , String state);


}
