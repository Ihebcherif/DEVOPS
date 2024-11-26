package tn.esprit.spring.services.etudiant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.repositories.EtudiantRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EtudiantService implements IEtudiantService {
    private final EtudiantRepository repo;

    @Override
    public Etudiant addOrUpdate(Etudiant e) {
        return repo.save(e);
    }

    @Override
    public List<Etudiant> findAll() {
        return repo.findAll();
    }

    @Override
    public Etudiant findById(long id) {

        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Etudiant with id " + id + " not found"));
    }

    @Override
    public void deleteById(long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new IllegalArgumentException("Etudiant with id " + id + " does not exist");
        }
    }

    @Override
    public void delete(Etudiant e) {
        repo.delete(e);
    }
}
