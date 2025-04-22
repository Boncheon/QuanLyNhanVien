package com.example.baitestxuong.Repositroy;

import com.example.baitestxuong.Model.Department;
import com.example.baitestxuong.Model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {
}
