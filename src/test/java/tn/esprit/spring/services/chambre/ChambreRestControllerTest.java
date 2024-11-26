package tn.esprit.spring.services.chambre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.restcontrollers.ChambreRestController;
import tn.esprit.spring.services.chambre.IChambreService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChambreRestController.class)
 class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService chambreService;

    private Chambre chambre;


    @BeforeEach
     void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
    }

    @Test
     void testAddOrUpdate() throws Exception {
        when(chambreService.addOrUpdate(Mockito.any(Chambre.class))).thenReturn(chambre);
        mockMvc.perform(post("/chambre/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idChambre\": 1, \"typeC\": \"SIMPLE\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1));
    }


    @Test
     void testFindById() throws Exception {
        when(chambreService.findById(anyLong())).thenReturn(chambre);
        mockMvc.perform(get("/chambre/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1));
    }


    @Test
     void testGetChambresParNomBloc() throws Exception {
        when(chambreService.getChambresParNomBloc("BlocA")).thenReturn(Collections.singletonList(chambre));
        mockMvc.perform(get("/chambre/getChambresParNomBloc?nomBloc=BlocA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idChambre").value(1));
    }
}
