package com.example.productservicebackend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.example.productservicebackend.entities.Image;
import com.example.productservicebackend.repos.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservicebackend.entities.Categorie;
import com.example.productservicebackend.entities.Produit;
import com.example.productservicebackend.repos.ProduitRepository;

@Service
public class ProduitServiceImpl implements ProduitService {

	@Autowired
	private ProduitRepository produitRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Override
	public Produit saveProduit(Produit p) {
		return produitRepository.save(p);
	}


	@Override
	public Produit updateProduit(Produit p) {
		// Récupérer le produit existant pour comparer l'ancienne image
		Produit existingProduit = this.getProduit(p.getIdProduit());
		Object objOldImage = existingProduit.getImage(); // Peut retourner Object
		Object objNewImage = p.getImage(); // Peut retourner Object

		Long oldProdImageId = null;
		Long newProdImageId = null;

		// Vérifier et caster l'ancienne image
		if (objOldImage instanceof Image) {
			Image oldImage = (Image) objOldImage;
			oldProdImageId = oldImage.getIdImage();
		}

		// Vérifier et caster la nouvelle image
		if (objNewImage instanceof Image) {
			Image newImage = (Image) objNewImage;
			newProdImageId = newImage.getIdImage();
		}

		// Sauvegarder le produit
		Produit prodUpdated = produitRepository.save(p);

		// Supprimer l'ancienne image si elle a changé
		if (oldProdImageId != null && !oldProdImageId.equals(newProdImageId)) {
			imageRepository.deleteById(oldProdImageId);
		}

		return prodUpdated;
	}

	@Override
	public void deleteProduit(Produit p) {
		produitRepository.delete(p);
	}

	@Override
	public void deleteProduitById(Long id) {
		// Récupérer le produit par son ID
		Produit p = getProduit(id);

		// Vérifier si une image est associée et la supprimer
		if (p.getImage() != null) {
			Object objImage = p.getImage(); // Si la méthode retourne Object
			if (objImage instanceof Image) { // Vérification explicite du type
				Image image = (Image) objImage;
				imageRepository.deleteById(image.getIdImage());
			}
		}

		// Supprimer le produit
		produitRepository.deleteById(id);
	}




	@Override
	public Produit getProduit(Long id) {
		return produitRepository.findById(id).orElse(null); // Gestion de l'absence de produit
	}

	@Override
	public List<Produit> getAllProduits() {
		return produitRepository.findAll();
	}

	@Override
	public List<Produit> findByNomProduit(String nom) {
		return produitRepository.findByNomProduit(nom);
	}

	@Override
	public List<Produit> findByNomProduitContains(String nom) {
		return produitRepository.findByNomProduitContains(nom);
	}

	@Override
	public List<Produit> findByNomPrix(String nom, Double prix) {
		return produitRepository.findByNomPrix(nom, prix);
	}

	@Override
	public List<Produit> findByCategorie(Categorie categorie) {
		return produitRepository.findByCategorie(categorie);
	}

	@Override
	public List<Produit> findByCategorieIdCat(Long id) {
		return produitRepository.findByCategorieIdCat(id);
	}

	@Override
	public List<Produit> findByOrderByNomProduitAsc() {
		return produitRepository.findByOrderByNomProduitAsc();
	}

	@Override
	public List<Produit> trierProduitsNomsPrix() {
		return produitRepository.trierProduitsNomsPrix();
	}
}
