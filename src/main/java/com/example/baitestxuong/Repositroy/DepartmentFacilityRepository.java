package com.example.baitestxuong.Repositroy;

import com.example.baitestxuong.Model.Department;
import com.example.baitestxuong.Model.DepartmentFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentFacilityRepository extends JpaRepository<Department, UUID> {

    @Query("SELECT df.idDepartment FROM DepartmentFacility df WHERE df.idFacility.id = :facilityId")
    List<Department> findDepartmentsByFacilityId(UUID facilityId);
}
