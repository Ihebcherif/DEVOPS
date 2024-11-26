package tn.esprit.spring.services.foyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.entities.Universite;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;
import tn.esprit.spring.dao.repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    private final FoyerRepository repo;
    private final UniversiteRepository universiteRepository;
    private final BlocRepository blocRepository;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return repo.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return repo.findAll();
    }

    @Override
    public Foyer findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foyer with id " + id + " not found"));
    }

    @Override
    public void deleteById(long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new IllegalArgumentException("Foyer with id " + id + " does not exist");
        }
    }

    @Override
    public void delete(Foyer f) {
        repo.delete(f);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Foyer f = findById(idFoyer); // Child
        Universite u = universiteRepository.findByNomUniversite(nomUniversite);

        if (u == null) {
            throw new IllegalArgumentException("Universite with name " + nomUniversite + " not found");
        }

        // On affecte le child au parent
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new IllegalArgumentException("Universite with id " + idUniversite + " not found"));
        u.setFoyer(null);
        return universiteRepository.save(u);
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        List<Bloc> blocs = foyer.getBlocs(); // Retrieve blocs before saving
        foyer = repo.save(foyer); // Save Foyer
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new IllegalArgumentException("Universite with id " + idUniversite + " not found"));

        // Assign Foyer to Blocs
        for (Bloc bloc : blocs) {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        }

        // Assign Foyer to Universite
        u.setFoyer(foyer);
        universiteRepository.save(u);
        return foyer;
    }

    @Override
    public Foyer ajoutFoyerEtBlocs(Foyer foyer) {
        List<Bloc> blocs = foyer.getBlocs(); // Retrieve blocs before saving
        foyer = repo.save(foyer); // Save Foyer

        // Assign Foyer to each Bloc
        for (Bloc b : blocs) {
            b.setFoyer(foyer);
            blocRepository.save(b);
        }
        return foyer;
    }
}
