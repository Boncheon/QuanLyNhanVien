package com.example.baitestxuong.Repositroy;

import com.example.baitestxuong.Model.Major;
import com.example.baitestxuong.Model.MajorFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MajorFacilityRepository extends JpaRepository<MajorFacility, UUID> {

    @Query("SELECT mf.idMajor FROM MajorFacility mf WHERE mf.idDepartmentFacility.idFacility.id = :facilityId AND mf.idDepartmentFacility.idDepartment.id = :departmentId")
    List<Major> findMajorsByFacilityAndDepartment(UUID facilityId, UUID departmentId);

    @Query("SELECT mf FROM MajorFacility mf WHERE mf.idDepartmentFacility.idFacility.id = :facilityId AND mf.idDepartmentFacility.idDepartment.id = :departmentId AND mf.idMajor.id = :majorId")
    Optional<MajorFacility> findByDepartmentFacilityAndMajor(@Param("facilityId") UUID facilityId,
                                                             @Param("departmentId") UUID departmentId,
                                                             @Param("majorId") UUID majorId);

}
