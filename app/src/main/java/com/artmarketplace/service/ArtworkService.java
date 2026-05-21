package com.artmarketplace.service;

import com.artmarketplace.dao.ArtworkDAO;
import com.artmarketplace.model.Artwork;
import java.util.List;

public class ArtworkService {
    private ArtworkDAO artworkDAO;

    public ArtworkService() {
        this.artworkDAO = new ArtworkDAO();
    }

    public boolean addArtwork(Artwork artwork) {
        return artworkDAO.addArtwork(artwork);
    }

    public boolean updateArtwork(Artwork artwork) {
        return artworkDAO.updateArtwork(artwork);
    }

    public boolean deleteArtwork(int artworkId, int sellerId) {
        return artworkDAO.deleteArtwork(artworkId, sellerId);
    }

    public Artwork getArtworkById(int id) {
        return artworkDAO.getArtworkById(id);
    }

    public List<Artwork> getAllAvailableArtworks() {
        return artworkDAO.getAllAvailableArtworks();
    }

    public List<Artwork> getArtworksBySeller(int sellerId) {
        return artworkDAO.getArtworksBySeller(sellerId);
    }

    public List<Artwork> searchArtworks(String keyword) {
        return artworkDAO.searchArtworks(keyword);
    }

    public boolean markAsSold(int artworkId) {
        return artworkDAO.markAsSold(artworkId);
    }
}
