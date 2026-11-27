package com.hemoglobank.api.service;

import com.hemoglobank.api.exception.ConsultaException;
import com.hemoglobank.api.repository.ReportingRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

    private static final int LIMITE_PADRAO = 200;
    private static final int LIMITE_MAXIMO = 1000;

    private final ReportingRepository repository;

    public ReportingService(ReportingRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> doacoesDetalhadas(Integer limite) {
        int l = (limite == null) ? LIMITE_PADRAO : Math.max(1, Math.min(limite, LIMITE_MAXIMO));
        try {
            return repository.listDonationDetail(l);
        } catch (SQLException e) {
            throw new ConsultaException("Falha ao consultar reporting.vw_donation_detail", e);
        }
    }

    public List<Map<String, Object>> bolsasAVencer() {
        try {
            return repository.listBagsExpiringSoon();
        } catch (SQLException e) {
            throw new ConsultaException("Falha ao consultar reporting.vw_bags_expiring_soon", e);
        }
    }
}
