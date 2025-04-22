package com.example.baitestxuong.Controller;

import com.example.baitestxuong.Model.*;
import com.example.baitestxuong.Repositroy.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired private StaffRepository staffRepository;
    @Autowired private FaciityRepository facilityRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private MajorRepository majorRepository;
    @Autowired private StaffMajorFacilityRepository staffMajorFacilityRepository;
    @Autowired private MajorFacilityRepository majorFacilityRepository;
    @Autowired private DepartmentFacilityRepository departmentFacilityRepository;

    @GetMapping("/hien-thi")
    public String hienthiStaff(Model model) {
        model.addAttribute("listNhanvien", staffRepository.findAllByOrderByStaffCode());
        model.addAttribute("staff", new Staff());
        return "/nhanVien/hienThi";
    }

    @PostMapping("/add")
    public String themNhanvien(@Valid @ModelAttribute("staff") Staff staff, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listNhanvien", staffRepository.findAllByOrderByStaffCode());
            return "/nhanVien/hienThi";
        }
        staff.setStatus((short) 1);
        staffRepository.save(staff);
        return "redirect:/staff/hien-thi";
    }

    @GetMapping("/view-update/{id}")
    public String nhanVienViewUpdate(@PathVariable("id") UUID id, Model model) {
        Staff staff = staffRepository.findById(id).orElse(null);
        if (staff == null) {
            return "redirect:/staff/hien-thi";
        }
        model.addAttribute("staff", staff);
        return "/nhanVien/viewUpdate";
    }

    @PostMapping("/update")
    public String suaNhanvien(@Valid @ModelAttribute("staff") Staff staff, BindingResult result) {
        if (result.hasErrors()) {
            return "/nhanVien/viewUpdate";
        }
        staff.setStatus((short) 1);
        staffRepository.save(staff);
        return "redirect:/staff/hien-thi";
    }

//    @GetMapping("/delete")
//    public String xoa(@RequestParam("id") UUID id) {
//        staffRepository.deleteById(id);
//        return "redirect:/staff/hien-thi";
//    }

    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable UUID id) {
        Staff staff = staffRepository.findById(id).orElse(null);
        if (staff == null) {
            return "redirect:/staff/hien-thi";
        }
        if (staff.getStatus() == null || staff.getStatus() == 0) {
            staff.setStatus((short) 1);
        } else {
            staff.setStatus((short) 0);
        }
        staffRepository.save(staff);
        return "redirect:/staff/hien-thi";
    }

    @GetMapping("/detail/{id}")
    public String staffDetail(@PathVariable("id") UUID staffId,
                              @RequestParam(value = "facilityId", required = false) UUID facilityId,
                              @RequestParam(value = "departmentId", required = false) UUID departmentId,
                              Model model) {

        Staff staff = staffRepository.findById(staffId).orElseThrow();

        List<Facility> facilities = facilityRepository.findAll();
        List<Department> departments = new ArrayList<>();
        List<Major> majors = new ArrayList<>();

        if (facilityId != null) {
            departments = departmentFacilityRepository.findDepartmentsByFacilityId(facilityId);
        }

        if (facilityId != null && departmentId != null) {
            majors = majorFacilityRepository.findMajorsByFacilityAndDepartment(facilityId, departmentId);
        }

        List<StaffMajorFacility> assignedMajors = staffMajorFacilityRepository.findByIdStaff_Id(staffId);

        model.addAttribute("staff", staff);
        model.addAttribute("facilities", facilities);
        model.addAttribute("departments", departments);
        model.addAttribute("majors", majors);
        model.addAttribute("facilityId", facilityId);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("assignedMajors", assignedMajors);

        return "/nhanVien/detail";
    }

    @PostMapping("/addMajorFacility/{id}")
    public String addMajorFacility(@PathVariable("id") UUID staffId,
                                   @RequestParam("facilityId") UUID facilityId,
                                   @RequestParam("departmentId") UUID departmentId,
                                   @RequestParam("majorId") UUID majorId,
                                   RedirectAttributes redirectAttributes) {

        Staff staff = staffRepository.findById(staffId).orElseThrow();
        List<StaffMajorFacility> assignedMajors = staffMajorFacilityRepository.findByIdStaff_Id(staffId);

        // Kiểm tra nếu đã tồn tại cơ sở này rồi thì không cho thêm nữa
        boolean existsFacility = assignedMajors
                .stream()
                .anyMatch(smf -> smf.getIdMajorFacility()
                        .getIdDepartmentFacility()
                        .getIdFacility()
                        .getId()
                        .equals(facilityId));

        if (existsFacility) {
            redirectAttributes.addFlashAttribute("error", "Mỗi cơ sở chỉ được thêm 1 lần cho nhân viên này!");
            return "redirect:/staff/detail/" + staffId;
        }

        // Nếu cơ sở chưa tồn tại thì mới thêm chuyên ngành
        MajorFacility majorFacility = majorFacilityRepository
                .findByDepartmentFacilityAndMajor(facilityId, departmentId, majorId)
                .orElseThrow();

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setId(UUID.randomUUID());
        staffMajorFacility.setIdStaff(staff);
        staffMajorFacility.setIdMajorFacility(majorFacility);
        staffMajorFacility.setStatus((short) 1);
        staffMajorFacilityRepository.save(staffMajorFacility);

        return "redirect:/staff/detail/" + staffId;
    }

    @PostMapping("/delete-major")
    public String deleteMajorFacility(@RequestParam("staffMajorFacilityId") UUID id,
                                      @RequestParam("staffId") UUID staffId) {
        staffMajorFacilityRepository.deleteById(id);
        return "redirect:/staff/detail/" + staffId;
    }
    @GetMapping("/export-excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=nhanvien.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Staff> staffs = staffRepository.findAll();
        List<StaffMajorFacility> assignedMajors = staffMajorFacilityRepository.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh sách nhân viên");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"STT", "Mã nhân viên", "Họ và tên", "Email FPT", "Email FE", "Bộ môn - Chuyên ngành"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int index = 1;
        for (Staff staff : staffs) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(staff.getStaffCode());
            row.createCell(2).setCellValue(staff.getName());
            row.createCell(3).setCellValue(staff.getAccountFpt());
            row.createCell(4).setCellValue(staff.getAccountFe());

            // Lấy danh sách bộ môn chuyên ngành
            List<String> majors = new ArrayList<>();
            for (StaffMajorFacility smf : assignedMajors) {
                if (smf.getIdStaff().getId().equals(staff.getId())) {
                    String facility = smf.getIdMajorFacility().getIdDepartmentFacility().getIdFacility().getName();
                    String department = smf.getIdMajorFacility().getIdDepartmentFacility().getIdDepartment().getName();
                    String major = smf.getIdMajorFacility().getIdMajor().getName();
                    majors.add(department + " - " + major + " - " + facility);
                }
            }

            // Tạo dropdown combobox cho ô "Bộ môn - Chuyên ngành"
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(majors.toArray(new String[0]));
            CellRangeAddressList addressList = new CellRangeAddressList(row.getRowNum(), row.getRowNum(), 5, 5);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);

            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<Staff> staffs = readExcel(file);
            staffRepository.saveAll(staffs);
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi thì chuyển hướng lại và show message
            return "redirect:/staff/hien-thi?error";
        }
        return "redirect:/staff/hien-thi?success";
    }
    private List<Staff> readExcel(MultipartFile file) throws IOException {
        List<Staff> staffList = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ dòng tiêu đề
            Row row = sheet.getRow(i);

            if (row != null) {
                Staff staff = new Staff();
                staff.setStaffCode(row.getCell(1).getStringCellValue());
                staff.setName(row.getCell(2).getStringCellValue());
                staff.setAccountFpt(row.getCell(3).getStringCellValue());
                staff.setAccountFe(row.getCell(4).getStringCellValue());
                staff.setStatus((short) 1);

                staffList.add(staff);

                // Nếu cần import luôn Bộ môn - Chuyên ngành (cột 5)
                // Thì đọc tiếp row.getCell(5) và xử lý như phần dưới
            }
        }
        workbook.close();
        return staffList;
    }



}
