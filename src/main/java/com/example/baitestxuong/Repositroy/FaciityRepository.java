package com.example.baitestxuong.Repositroy;

import com.example.baitestxuong.Model.Facility;
import com.example.baitestxuong.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FaciityRepository extends JpaRepository<Facility, UUID> {
    @Repository
    public interface FacilityRepository extends JpaRepository<Facility, UUID> {
    }

}
