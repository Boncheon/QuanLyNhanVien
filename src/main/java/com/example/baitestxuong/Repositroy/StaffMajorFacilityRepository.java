package com.example.baitestxuong.Repositroy;

import com.example.baitestxuong.Model.StaffMajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, UUID> {
    List<StaffMajorFacility> findByIdStaff_Id(UUID idStaff);

    boolean existsByIdStaff_IdAndIdMajorFacility_IdDepartmentFacility_IdFacility_Id(UUID staffId, UUID facilityId);
}
