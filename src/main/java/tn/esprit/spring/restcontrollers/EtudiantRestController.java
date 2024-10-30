package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.services.etudiant.IEtudiantService;

import java.util.List;

@RestController
@RequestMapping("etudiant")
@AllArgsConstructor
public class EtudiantRestController {

    IEtudiantService service;

    @PostMapping("addOrUpdate")
    public Etudiant addOrUpdate(@RequestBody Etudiant e) { // Changed to public
        return service.addOrUpdate(e);
    }

    @GetMapping("findAll")
    public List<Etudiant> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Etudiant findById(@RequestParam long id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Etudiant e) { // Changed to public
        service.delete(e);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) { // Changed to public
        service.deleteById(id);
    }
}

