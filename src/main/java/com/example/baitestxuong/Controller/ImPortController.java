package com.example.baitestxuong.Controller;

import com.example.baitestxuong.Repositroy.HistroyImporrtRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/import-history")
public class ImPortController {
    @Autowired
    HistroyImporrtRepositroy importHistoryRepository;

    @GetMapping("/list")
    public String listImportHistory(Model model) {
        model.addAttribute("historyList", importHistoryRepository.findAll());
        return "/import/history";  // Giao diện
    }
}
