package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dbContext.ConnectDB;
import model.Image;
public class ImageDAO {
    public void createImage(Image image) {
        String sql = "INSERT INTO image (user_id, title, file_path, description, price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, image.getUser_id());
            stmt.setString(2, image.getTitle());
            stmt.setString(3, image.getFile_path());
            stmt.setString(4, image.getDescription());
            stmt.setDouble(5, image.getPrice());
            stmt.setDate(6, new java.sql.Date(image.getCreated_at().getTime()));
            stmt.setDate(7, new java.sql.Date(image.getUpdated_at().getTime()));
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Retrieve an image by its ID
    public Image getImageById(int id) {
        String sql = "SELECT * FROM image WHERE id = ?";
        try (Connection con = ConnectDB.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToImage(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Update an image's information
    public void updateImage(Image image) {
        String sql = "UPDATE image SET title = ?, file_path = ?, description = ?, price = ?, updated_at = ? WHERE id = ?";
        try (Connection con = ConnectDB.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, image.getTitle());
            stmt.setString(2, image.getFile_path());
            stmt.setString(3, image.getDescription());
            stmt.setDouble(4, image.getPrice());
            stmt.setDate(5, new java.sql.Date(image.getUpdated_at().getTime()));
            stmt.setInt(6, image.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Delete an image from the database
    public void deleteImage(int id) {
        String sql = "DELETE FROM image WHERE id = ?";
        try (Connection con = ConnectDB.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Get a list of all images in the database
    public List<Image> getAllImages() {
        List<Image> images = new ArrayList<>();
        String sql = "SELECT * FROM image";
        try (Connection con = ConnectDB.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Image image = mapResultSetToImage(rs);
                images.add(image);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    // Map a ResultSet to an Image object
    private Image mapResultSetToImage(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String title = rs.getString("title");
        String filePath = rs.getString("file_path");
        String description = rs.getString("description");
        double price = rs.getDouble("price");
        Date createdAt = rs.getDate("created_at");
        Date updatedAt = rs.getDate("updated_at");
        return new Image(id, userId, title, filePath, description, price, createdAt, updatedAt);
    }

}