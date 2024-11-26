package tn.esprit.spring.services.bloc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {

    private final BlocRepository repo;
    private final ChambreRepository chambreRepository;
    private final FoyerRepository foyerRepository;

    @Override
    public Bloc addOrUpdate2(Bloc b) { // Cascade
        if (b.getChambres() != null) {
            for (Chambre c : b.getChambres()) {
                c.setBloc(b);
                chambreRepository.save(c);
            }
        }
        return repo.save(b);
    }

    @Override
    public Bloc addOrUpdate(Bloc b) {
        if (b.getChambres() != null) {
            b = repo.save(b);
            for (Chambre chambre : b.getChambres()) {
                chambre.setBloc(b);
                chambreRepository.save(chambre);
            }
        }
        return b;
    }

    @Override
    public List<Bloc> findAll() {
        return repo.findAll();
    }

    @Override
    public Bloc findById(long id) {
        // Check if the optional contains a value before accessing it
        Optional<Bloc> optionalBloc = repo.findById(id);
        if (optionalBloc.isPresent()) {
            return optionalBloc.get();
        } else {
            throw new RuntimeException("Bloc with ID " + id + " not found."); // Improve error handling
        }
    }

    @Override
    public void deleteById(long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new RuntimeException("Bloc with ID " + id + " does not exist.");
        }
    }

    @Override
    public void delete(Bloc b) {
        if (b.getChambres() != null) {
            for (Chambre chambre : b.getChambres()) {
                chambreRepository.delete(chambre);
            }
        }
        repo.delete(b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        Bloc b = repo.findByNomBloc(nomBloc);
        if (b == null) {
            throw new RuntimeException("Bloc with name " + nomBloc + " not found.");
        }

        List<Chambre> chambres = new ArrayList<>();
        for (Long nu : numChambre) {
            Chambre chambre = chambreRepository.findByNumeroChambre(nu);
            if (chambre != null) {
                chambre.setBloc(b);
                chambres.add(chambre);
                chambreRepository.save(chambre);
            } else {
                throw new RuntimeException("Chambre with number " + nu + " not found.");
            }
        }
        return b;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        Bloc b = repo.findByNomBloc(nomBloc);
        if (b == null) {
            throw new RuntimeException("Bloc with name " + nomBloc + " not found.");
        }

        Foyer f = foyerRepository.findByNomFoyer(nomFoyer);
        if (f == null) {
            throw new RuntimeException("Foyer with name " + nomFoyer + " not found.");
        }

        b.setFoyer(f);
        return repo.save(b);
    }
}
