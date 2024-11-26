package tn.esprit.spring.services.chambre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Reservation;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {

    private static final String AVAILABLE_MESSAGE = "Le nombre de place disponible pour la chambre ";
    private static final String COMPLETE_MESSAGE = "La chambre ";
    private static final String IS_COMPLETE = " est complete";

    private final ChambreRepository repo;
    private final BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chambre not found with id: " + id));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        LocalDate[] academicYearRange = getAcademicYearRange();
        LocalDate startOfAcademicYear = academicYearRange[0];
        LocalDate endOfAcademicYear = academicYearRange[1];

        List<Chambre> listChambreDispo = new ArrayList<>();
        for (Chambre c : repo.findAll()) {
            if (c.getTypeC().equals(type) && c.getBloc().getFoyer().getNomFoyer().equals(nomFoyer)) {
                long numReservations = c.getReservations().stream()
                        .filter(reservation -> reservation.getAnneeUniversitaire().isAfter(startOfAcademicYear) &&
                                reservation.getAnneeUniversitaire().isBefore(endOfAcademicYear))
                        .count();

                if ((type.equals(TypeChambre.SIMPLE) && numReservations == 0) ||
                        (type.equals(TypeChambre.DOUBLE) && numReservations < 2) ||
                        (type.equals(TypeChambre.TRIPLE) && numReservations < 3)) {
                    listChambreDispo.add(c);
                }
            }
        }
        return listChambreDispo;
    }

    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => {} ayant une capacité {}", b.getNomBloc(), b.getCapaciteBloc());
            if (b.getChambres().isEmpty()) {
                log.info("Pas de chambre disponible dans ce bloc");
            } else {
                log.info("La liste des chambres pour ce bloc: ");
                b.getChambres().forEach(c -> log.info("NumChambre: {} type: {}", c.getNumeroChambre(), c.getTypeC()));
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        if (totalChambre == 0) {
            log.info("Aucune chambre enregistrée.");
            return;
        }

        double pSimple = ((double) repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / totalChambre;
        double pDouble = ((double) repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / totalChambre;
        double pTriple = ((double) repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / totalChambre;

        log.info("Nombre total des chambres: {}", totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égal à {}", pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égal à {}", pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égal à {}", pTriple);
    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        LocalDate[] academicYearRange = getAcademicYearRange();
        LocalDate startOfAcademicYear = academicYearRange[0];
        LocalDate endOfAcademicYear = academicYearRange[1];

        for (Chambre c : repo.findAll()) {
            long nbReservations = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                    c.getIdChambre(), true, startOfAcademicYear, endOfAcademicYear);

            String message;
            if (c.getTypeC().equals(TypeChambre.SIMPLE) && nbReservations == 0) {
                message = AVAILABLE_MESSAGE + c.getTypeC() + " " + c.getNumeroChambre() + " est 1";
            } else if (c.getTypeC().equals(TypeChambre.DOUBLE) && nbReservations < 2) {
                message = AVAILABLE_MESSAGE + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (2 - nbReservations);
            } else if (c.getTypeC().equals(TypeChambre.TRIPLE) && nbReservations < 3) {
                message = AVAILABLE_MESSAGE + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (3 - nbReservations);
            } else {
                message = COMPLETE_MESSAGE + c.getTypeC() + " " + c.getNumeroChambre() + IS_COMPLETE;
            }
            log.info(message);
        }
    }

    private LocalDate[] getAcademicYearRange() {
        int year = LocalDate.now().getYear() % 100;
        LocalDate startOfAcademicYear;
        LocalDate endOfAcademicYear;

        if (LocalDate.now().getMonthValue() <= 7) {
            startOfAcademicYear = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            endOfAcademicYear = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            startOfAcademicYear = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            endOfAcademicYear = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        return new LocalDate[]{startOfAcademicYear, endOfAcademicYear};
    }
}
