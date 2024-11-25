package tn.esprit.spring;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceTest {

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        // Arrange
        Universite universite = new Universite();
        universite.setNomUniversite("Esprit");
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Act
        Universite result = universiteService.addOrUpdate(universite);

        // Assert
        assertNotNull(result);
        assertEquals("Esprit", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    void testFindAll() {
        // Arrange
        Universite universite1 = new Universite();
        universite1.setNomUniversite("Esprit");
        Universite universite2 = new Universite();
        universite2.setNomUniversite("ENSI");
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));

        // Act
        List<Universite> result = universiteService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Esprit");
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        // Act
        Universite result = universiteService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Esprit", result.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        // Arrange
        long id = 1L;

        // Act
        universiteService.deleteById(id);

        // Assert
        verify(universiteRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete() {
        // Arrange
        Universite universite = new Universite();
        universite.setNomUniversite("Esprit");

        // Act
        universiteService.delete(universite);

        // Assert
        verify(universiteRepository, times(1)).delete(universite);
    }
}
