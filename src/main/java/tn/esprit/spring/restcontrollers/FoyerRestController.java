package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Foyer;
import tn.esprit.spring.dao.entities.Universite;
import tn.esprit.spring.services.foyer.IFoyerService;

import java.util.List;

@RestController
@RequestMapping("foyer")
@AllArgsConstructor
public class FoyerRestController {

    IFoyerService service;

    @PostMapping("addOrUpdate")
    public Foyer addOrUpdate(@RequestBody Foyer f) { // Changed to public
        return service.addOrUpdate(f);
    }

    @GetMapping("findAll")
    public List<Foyer> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Foyer findById(@RequestParam long id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Foyer f) { // Changed to public
        service.delete(f);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) { // Changed to public
        service.deleteById(id);
    }

    @PutMapping("affecterFoyerAUniversite")
    public Universite affecterFoyerAUniversite(@RequestParam long idFoyer, @RequestParam String nomUniversite) { // Changed to public
        return service.affecterFoyerAUniversite(idFoyer, nomUniversite);
    }

    @PutMapping("desaffecterFoyerAUniversite")
    public Universite desaffecterFoyerAUniversite(@RequestParam long idUniversite) { // Changed to public
        return service.desaffecterFoyerAUniversite(idUniversite);
    }

    @PostMapping("ajouterFoyerEtAffecterAUniversite")
    public Foyer ajouterFoyerEtAffecterAUniversite(@RequestBody Foyer foyer, @RequestParam long idUniversite) {
        return service.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);
    }
}
