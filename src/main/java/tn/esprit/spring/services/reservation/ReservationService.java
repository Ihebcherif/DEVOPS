package tn.esprit.spring.services.reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.entities.Reservation;
import tn.esprit.spring.dao.repositories.ChambreRepository;
import tn.esprit.spring.dao.repositories.EtudiantRepository;
import tn.esprit.spring.dao.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    private final ReservationRepository repo;
    private final ChambreRepository chambreRepository;
    private final EtudiantRepository etudiantRepository;

    @Override
    public Reservation addOrUpdate(Reservation r) {
        return repo.save(r);
    }

    @Override
    public List<Reservation> findAll() {
        return repo.findAll();
    }

    @Override
    public Reservation findById(String id) {
        return repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Reservation not found with ID: " + id));
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Reservation r) {
        repo.delete(r);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        LocalDate[] academicYear = getCurrentAcademicYear();
        LocalDate dateDebutAU = academicYear[0];
        LocalDate dateFinAU = academicYear[1];

        Chambre chambre = chambreRepository.findByNumeroChambre(numChambre);
        if (chambre == null) {
            throw new IllegalArgumentException("Chambre not found with number: " + numChambre);
        }

        Etudiant etudiant = etudiantRepository.findByCin(cin);
        if (etudiant == null) {
            throw new IllegalArgumentException("Etudiant not found with CIN: " + cin);
        }

        int numReservations = chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                chambre.getIdChambre(), dateDebutAU, dateFinAU);

        boolean canAdd = switch (chambre.getTypeC()) {
            case SIMPLE -> numReservations < 1;
            case DOUBLE -> numReservations < 2;
            case TRIPLE -> numReservations < 3;
        };

        if (!canAdd) {
            log.info("Chambre {} of type {} is fully booked.", chambre.getNumeroChambre(), chambre.getTypeC());
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());
        reservation.setIdReservation(dateDebutAU.getYear() + "/" + dateFinAU.getYear() + "-" +
                chambre.getBloc().getNomBloc() + "-" + chambre.getNumeroChambre() + "-" + etudiant.getCin());
        reservation.getEtudiants().add(etudiant);

        Reservation savedReservation = repo.save(reservation);
        chambre.getReservations().add(savedReservation);
        chambreRepository.save(chambre);

        return savedReservation;
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cinEtudiant) {
        // Find the reservation for the student by CIN and validity status
        var r = repo.findByEtudiantsCinAndEstValide(cinEtudiant, true);
        if (r == null) {
            throw new IllegalArgumentException("No valid reservation found for the given CIN: " + cinEtudiant);
        }

        // Find the associated room by reservation ID
        var c = chambreRepository.findByReservationsIdReservation(r.getIdReservation());
        if (c == null) {
            throw new IllegalArgumentException("No room found for reservation ID: " + r.getIdReservation());
        }

        // Remove the reservation from the room's reservations list
        c.getReservations().remove(r);
        chambreRepository.save(c);

        // Delete the reservation
        repo.delete(r);

        // Return success message
        return "La réservation " + r.getIdReservation() + " est annulée avec succès";
    }


    @Override
    public void affectReservationAChambre(String idRes, long idChambre) {
        Optional<Reservation> optionalReservation = repo.findById(idRes);
        Optional<Chambre> optionalChambre = chambreRepository.findById(idChambre);

        if (optionalReservation.isEmpty() || optionalChambre.isEmpty()) {
            throw new IllegalArgumentException("Invalid reservation or chambre ID.");
        }

        Reservation reservation = optionalReservation.get();
        Chambre chambre = optionalChambre.get();

        chambre.getReservations().add(reservation);
        chambreRepository.save(chambre);
    }

    @Override
    public void annulerReservations() {
        LocalDate[] academicYear = getCurrentAcademicYear();
        LocalDate dateDebutAU = academicYear[0];
        LocalDate dateFinAU = academicYear[1];

        List<Reservation> reservations = repo.findByEstValideAndAnneeUniversitaireBetween(true, dateDebutAU, dateFinAU);
        for (Reservation reservation : reservations) {
            reservation.setEstValide(false);
            repo.save(reservation);
            log.info("Reservation {} has been automatically canceled.", reservation.getIdReservation());
        }
    }

    private LocalDate[] getCurrentAcademicYear() {
        int year = LocalDate.now().getYear() % 100;
        LocalDate dateDebutAU;
        LocalDate dateFinAU;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        return new LocalDate[]{dateDebutAU, dateFinAU};
    }
}
