package com.artmarketplace.dao;

import com.artmarketplace.config.DatabaseConnection;
import com.artmarketplace.model.Cart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean addToCart(Cart cart) {
        String sql = "INSERT INTO cart (buyer_id, artwork_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE id=id";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cart.getBuyerId());
            stmt.setInt(2, cart.getArtworkId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(int cartId) {
        String sql = "DELETE FROM cart WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(int buyerId) {
        String sql = "DELETE FROM cart WHERE buyer_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buyerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cart> getCartItems(int buyerId) {
        List<Cart> items = new ArrayList<>();
        String sql = "SELECT c.id, c.buyer_id, c.artwork_id, a.title, a.price, a.image_path " +
                     "FROM cart c JOIN artworks a ON c.artwork_id = a.id WHERE c.buyer_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buyerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setBuyerId(rs.getInt("buyer_id"));
                cart.setArtworkId(rs.getInt("artwork_id"));
                cart.setArtworkTitle(rs.getString("title"));
                cart.setPrice(rs.getDouble("price"));
                cart.setImagePath(rs.getString("image_path"));
                items.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean isInCart(int buyerId, int artworkId) {
        String sql = "SELECT COUNT(*) FROM cart WHERE buyer_id = ? AND artwork_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buyerId);
            stmt.setInt(2, artworkId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
