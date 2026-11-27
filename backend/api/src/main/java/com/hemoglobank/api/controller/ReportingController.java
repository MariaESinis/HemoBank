package com.hemoglobank.api.controller;

import com.hemoglobank.api.service.ReportingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class ReportingController {

    private final ReportingService service;

    public ReportingController(ReportingService service) {
        this.service = service;
    }

    /** GET /api/relatorios/doacoes?limite=200  (reporting.vw_donation_detail) */
    @GetMapping("/doacoes")
    public List<Map<String, Object>> doacoes(@RequestParam(required = false) Integer limite) {
        return service.doacoesDetalhadas(limite);
    }

    /** GET /api/relatorios/bolsas-a-vencer  (reporting.vw_bags_expiring_soon) */
    @GetMapping("/bolsas-a-vencer")
    public List<Map<String, Object>> bolsasAVencer() {
        return service.bolsasAVencer();
    }
}
