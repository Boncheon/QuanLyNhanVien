package com.example.baitestxuong.Controller;

import com.example.baitestxuong.Model.Staff;
import com.example.baitestxuong.Repositroy.StaffRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffRepository staffRepository;

    @GetMapping("/hien-thi")
    public String hienthiStaff (Model model, @ModelAttribute  Staff staff){
        model.addAttribute("listNhanvien", staffRepository.findAllByOrderByStaffCode());
        return "/nhanVien/hienThi";
    }
    @PostMapping("/add")
    public String themNhanvien (@Valid @ModelAttribute("staff") Staff staff, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("listNhanvien",staffRepository.findAllByOrderByStaffCode());
            return "/nhanVien/hienThi";
        }
        staff.setStatus((short) 1);
        staffRepository.save(staff);
        return "redirect:/staff/hien-thi";
    }
//    @GetMapping("/view-update/{id}")
//    public String nhanVienviewUpdate(Model model ,@PathVariable("id") UUID id){
//        model.addAttribute("nhanVien",staffRepository.findById(id).get());
//        return "/nhanVien/viewUpdate";
//    }
//    @PostMapping("/update")
//    public String suaNhanvien (Staff staff){
//        staffRepository.save(staff);
//        return "redirect:/staff/hien-thi";
//    }
@GetMapping("/view-update/{id}")
public String nhanVienViewUpdate(@PathVariable("id") UUID id, Model model) {

    Staff nhanVien = staffRepository.findById(id).orElse(null);
    if (nhanVien == null) {
        model.addAttribute("error", "Không tìm thấy nhân viên!");
        return "redirect:/staff/hien-thi";
    }

    model.addAttribute("staff", nhanVien);
    return "/nhanVien/viewUpdate";
}

    @PostMapping("/update")
    public String suaNhanvien(
                              @ModelAttribute("staff") @Valid Staff staff,
                              BindingResult result,
                              Model model) {

        // Kiểm tra lỗi validation
        if (result.hasErrors()) {
            return "/nhanVien/viewUpdate";  // Trả về lại form nếu có lỗi validation
        }
        staffRepository.save(staff);

        // Sau khi cập nhật thành công, chuyển hướng về danh sách nhân viên
        return "redirect:/staff/hien-thi";
    }
    @GetMapping("/delete")
    public String xoa(@RequestParam("id") UUID id){
        staffRepository.deleteById(id);
        return "redirect:/staff/hien-thi";
    }
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable UUID id) {
        Staff staff = staffRepository.findById(id).orElse(null);
        if (staff == null) {
            return "redirect:/staff/hien-thi?error=notfound";
        }
        staff.setStatus((short) (staff.getStatus() == 1 ? 0 : 1));
        staffRepository.save(staff);
        return "redirect:/staff/hien-thi";
    }
//    @GetMapping("/detail")
//    public String quanLyboMon(@PathVariable("id")){
//
//    }
}
