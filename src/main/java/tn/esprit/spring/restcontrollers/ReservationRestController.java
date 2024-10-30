package tn.esprit.spring.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dao.entities.Reservation;
import tn.esprit.spring.services.reservation.IReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("reservation")
@AllArgsConstructor
public class ReservationRestController {

    IReservationService service;

    @PostMapping("addOrUpdate")
    public Reservation addOrUpdate(@RequestBody Reservation r) { // Changed to public
        return service.addOrUpdate(r);
    }

    @GetMapping("findAll")
    public List<Reservation> findAll() { // Changed to public
        return service.findAll();
    }

    @GetMapping("findById")
    public Reservation findById(@RequestParam String id) { // Changed to public
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Reservation r) { // Changed to public
        service.delete(r);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam String id) { // Changed to public
        service.deleteById(id);
    }

    @PostMapping("ajouterReservationEtAssignerAChambreEtAEtudiant")
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestParam Long numChambre, @RequestParam long cin) { // Changed to public
        return service.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @GetMapping("getReservationParAnneeUniversitaire")
    public long getReservationParAnneeUniversitaire(@RequestParam LocalDate debutAnnee, @RequestParam LocalDate finAnnee) { // Changed to public
        return service.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @DeleteMapping("annulerReservation")
    public String annulerReservation(@RequestParam long cinEtudiant) { // Changed to public
        return service.annulerReservation(cinEtudiant);
    }
}
