package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Universite;
import tn.esprit.spring.services.universite.IUniversiteService;

import java.util.List;

@RestController
@RequestMapping("universite")
@AllArgsConstructor
public class UniversiteRestController {

    IUniversiteService service;

    @PostMapping("addOrUpdate")
    public Universite addOrUpdate(@RequestBody Universite u) { // Changed to public
        return service.addOrUpdate(u);
    }

    @GetMapping("findAll")
    public List<Universite> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Universite findById(@RequestParam long id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Universite u) { // Changed to public
        service.delete(u);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) { // Changed to public
        service.deleteById(id);
    }
}
