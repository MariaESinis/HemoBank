package com.hemoglobank.api.repository;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/** Views do schema reporting, consultadas com JDBC puro. */
@Repository
public class ReportingRepository {

    private static final String SQL_DONATION_DETAIL =
            "SELECT * FROM reporting.vw_donation_detail LIMIT ?";

    // A própria view já filtra "vencem nos próximos 7 dias".
    private static final String SQL_BAGS_EXPIRING_SOON =
            "SELECT * FROM reporting.vw_bags_expiring_soon";

    private final DataSource dataSource;

    public ReportingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> listDonationDetail(int limit) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DONATION_DETAIL)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return JdbcRows.toList(rs);
            }
        }
    }

    public List<Map<String, Object>> listBagsExpiringSoon() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BAGS_EXPIRING_SOON);
             ResultSet rs = ps.executeQuery()) {
            return JdbcRows.toList(rs);
        }
    }
}
