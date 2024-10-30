package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.services.bloc.IBlocService;

import java.util.List;

@RestController
@RequestMapping("bloc")
@AllArgsConstructor
public class BlocRestController {

    IBlocService service;

    @PostMapping("addOrUpdate")
    public Bloc addOrUpdate(@RequestBody Bloc b) { // Changed to public
        return service.addOrUpdate(b);
    }

    @GetMapping("findAll")
    public List<Bloc> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Bloc findById(@RequestParam long id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Bloc b) { // Changed to public
        service.delete(b);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) { // Changed to public
        service.deleteById(id);
    }

    @PutMapping("affecterChambresABloc")
    public Bloc affecterChambresABloc(@RequestBody List<Long> numChambre, @RequestParam String nomBloc) { // Changed to public
        return service.affecterChambresABloc(numChambre, nomBloc);
    }

    @PutMapping("affecterBlocAFoyer")
    public Bloc affecterBlocAFoyer(@RequestParam String nomBloc, @RequestParam String nomFoyer) { // Changed to public
        return service.affecterBlocAFoyer(nomBloc, nomFoyer);
    }
}

