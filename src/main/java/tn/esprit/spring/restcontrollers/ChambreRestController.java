package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.services.chambre.IChambreService;

import java.util.List;

@RestController
@RequestMapping("chambre")
@AllArgsConstructor
public class ChambreRestController {

    IChambreService service;

    @PostMapping("addOrUpdate")
    public Chambre addOrUpdate(@RequestBody Chambre c) { // Changed to public
        return service.addOrUpdate(c);
    }

    @GetMapping("findAll")
    public List<Chambre> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Chambre findById(@RequestParam long id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Chambre c) { // Changed to public
        service.delete(c);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) { // Changed to public
        service.deleteById(id);
    }

    @GetMapping("getChambresParNomBloc")
    public List<Chambre> getChambresParNomBloc(@RequestParam String nomBloc) {
        return service.getChambresParNomBloc(nomBloc);
    }

    @GetMapping("nbChambreParTypeEtBloc")
    public long nbChambreParTypeEtBloc(@RequestParam TypeChambre type, @RequestParam long idBloc) { // Changed to public
        return service.nbChambreParTypeEtBloc(type, idBloc);
    }

    @GetMapping("getChambresNonReserveParNomFoyerEtTypeChambre")
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(@RequestParam String nomFoyer, @RequestParam TypeChambre type) { // Changed to public
        return service.getChambresNonReserveParNomFoyerEtTypeChambre(nomFoyer, type);
    }
}
