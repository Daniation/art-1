package com.artmarketplace.dao;

import com.artmarketplace.config.DatabaseConnection;
import com.artmarketplace.model.Artwork;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDAO {

    public double getTotalEarnings(int sellerId) {
        String sql = "SELECT COALESCE(SUM(oi.price), 0) FROM order_items oi " +
                     "JOIN artworks a ON oi.artwork_id = a.id WHERE a.seller_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalArtworks(int sellerId) {
        String sql = "SELECT COUNT(*) FROM artworks WHERE seller_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAvailableArtworks(int sellerId) {
        String sql = "SELECT COUNT(*) FROM artworks WHERE seller_id = ? AND status = 'available'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSoldArtworks(int sellerId) {
        String sql = "SELECT COUNT(*) FROM artworks WHERE seller_id = ? AND status = 'sold'";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalOrders(int sellerId) {
        String sql = "SELECT COUNT(DISTINCT o.id) FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN artworks a ON oi.artwork_id = a.id WHERE a.seller_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
