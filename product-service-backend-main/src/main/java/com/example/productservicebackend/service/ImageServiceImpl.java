package com.example.productservicebackend.service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.productservicebackend.entities.Image;
import com.example.productservicebackend.entities.Produit;
import com.example.productservicebackend.repos.ImageRepository;
import com.example.productservicebackend.repos.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType; import org.springframework.http.ResponseEntity; import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class ImageServiceImpl implements ImageService{
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProduitRepository produitRepository;
    @Autowired
    ProduitService produitService;

    @Override
    public Image uplaodImage(MultipartFile file) throws IOException {
        return imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes()).build() );
    }
    @Override
    public Image getImageDetails(Long id) throws IOException{
        final Optional<Image> dbImage = imageRepository. findById (id);

        return Image.builder()
                .idImage(dbImage.get().getIdImage())
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(dbImage.get().getImage()).build() ;
    }
    @Override

    public Image uplaodImageProd(MultipartFile file,Long idProd)

            throws IOException { Produit p = new Produit(); p.setIdProduit(idProd);
        return imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes())
                .produit(p).build() );
    }
    @Override
    public List<Image> getImagesParProd(Long prodId) {
        Produit p = produitRepository.findById(prodId).get();
        return p.getImages();
    }




    @Override
    public ResponseEntity<byte[]> getImage(Long id) throws IOException{
        final Optional<Image> dbImage = imageRepository. findById (id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(dbImage.get().getImage());
    }

    @Override
    public void deleteImage(Long id) { imageRepository.deleteById(id);
    }


}
