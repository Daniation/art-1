package com.artmarketplace.dao;

import com.artmarketplace.config.DatabaseConnection;
import com.artmarketplace.model.Artwork;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtworkDAO {

    public boolean addArtwork(Artwork artwork) {
        String sql = "INSERT INTO artworks (title, description, price, image_path, seller_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getDescription());
            stmt.setDouble(3, artwork.getPrice());
            stmt.setString(4, artwork.getImagePath());
            stmt.setInt(5, artwork.getSellerId());
            stmt.setString(6, "available");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateArtwork(Artwork artwork) {
        String sql = "UPDATE artworks SET title = ?, description = ?, price = ?, image_path = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getDescription());
            stmt.setDouble(3, artwork.getPrice());
            stmt.setString(4, artwork.getImagePath());
            stmt.setInt(5, artwork.getId());
            stmt.setInt(6, artwork.getSellerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteArtwork(int artworkId, int sellerId) {
        String sql = "DELETE FROM artworks WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, artworkId);
            stmt.setInt(2, sellerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Artwork getArtworkById(int id) {
        String sql = "SELECT a.*, u.full_name as seller_name FROM artworks a JOIN users u ON a.seller_id = u.id WHERE a.id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractArtwork(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Artwork> getAllAvailableArtworks() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name as seller_name FROM artworks a JOIN users u ON a.seller_id = u.id WHERE a.status = 'available' ORDER BY a.created_at DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                artworks.add(extractArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    public List<Artwork> getArtworksBySeller(int sellerId) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name as seller_name FROM artworks a JOIN users u ON a.seller_id = u.id WHERE a.seller_id = ? ORDER BY a.created_at DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                artworks.add(extractArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    public List<Artwork> searchArtworks(String keyword) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name as seller_name FROM artworks a JOIN users u ON a.seller_id = u.id WHERE a.status = 'available' AND (a.title LIKE ? OR a.description LIKE ?) ORDER BY a.created_at DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                artworks.add(extractArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    public boolean markAsSold(int artworkId) {
        String sql = "UPDATE artworks SET status = 'sold' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, artworkId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Artwork extractArtwork(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setId(rs.getInt("id"));
        artwork.setTitle(rs.getString("title"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        artwork.setImagePath(rs.getString("image_path"));
        artwork.setSellerId(rs.getInt("seller_id"));
        artwork.setStatus(rs.getString("status"));
        try {
            artwork.setSellerName(rs.getString("seller_name"));
        } catch (SQLException ignored) {}
        return artwork;
    }
}
