package tn.esprit.spring.services.chambre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChambreServiceTest {

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    ChambreService chambreService;

    private Chambre chambre;


    @BeforeEach
    public void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
    }


    @Test
    public void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);
        Chambre result = chambreService.addOrUpdate(chambre);
        assertNotNull(result);
        assertEquals(TypeChambre.SIMPLE, result.getTypeC());
        verify(chambreRepository, times(1)).save(chambre);
    }


    @Test
    public void testFindById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));
        Chambre result = chambreService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }


    @Test
    public void testDeleteById() {
        doNothing().when(chambreRepository).deleteById(1L);
        chambreService.deleteById(1L);
        verify(chambreRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testGetChambresParNomBloc() {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(chambreRepository.findByBlocNomBloc("BlocA")).thenReturn(chambres);
        List<Chambre> result = chambreService.getChambresParNomBloc("BlocA");
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findByBlocNomBloc("BlocA");
    }
}
