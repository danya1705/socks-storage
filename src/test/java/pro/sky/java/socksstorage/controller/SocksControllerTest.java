package pro.sky.java.socksstorage.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.java.socksstorage.entity.Socks;
import pro.sky.java.socksstorage.repository.SocksRepository;
import pro.sky.java.socksstorage.service.SocksService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class SocksControllerTest {

    public static final long ID = 123L;
    public static final String COLOR = "red";
    public static final int COTTON_PART = 75;
    public static final int COTTON_PART_TWO = 25;
    public static final int COTTON_PART_TOO_HIGH = 150;
    public static final int COTTON_PART_TOO_LOW = -1;
    public static final int QUANTITY = 50;
    public static final int QUANTITY_NOT_NATURAL = 0;
    public static final int UPD_QUANTITY = 20;
    public static final String WRONG_COLOR = "not the color";
    public static final int QUANTITY_TOO_BIG = 1000;

    @MockBean
    private SocksRepository socksRepository;
    @SpyBean
    private SocksService socksService;
    @InjectMocks
    private SocksController socksController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void applyIncomePositiveTest() throws Exception {

        Socks socks = new Socks();
        socks.setId(ID);
        socks.setColor(COLOR);
        socks.setCottonPart(COTTON_PART);
        socks.setQuantity(QUANTITY);

        JSONObject income = new JSONObject();
        income.put("color", COLOR);
        income.put("cottonPart", COTTON_PART);
        income.put("quantity", UPD_QUANTITY);

        when(socksRepository.findByColorAndCottonPart(eq(COLOR), eq(COTTON_PART))).thenReturn(Optional.of(socks));
        when(socksRepository.save(any(Socks.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(income.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.color").value(COLOR))
                .andExpect(jsonPath("$.cottonPart").value(COTTON_PART))
                .andExpect(jsonPath("$.quantity").value(QUANTITY + UPD_QUANTITY));

        // Should return response status 400 when cottonPart is over than 100
        income.put("cottonPart", COTTON_PART_TOO_HIGH);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(income.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when cottonPart is lower than 0
        income.put("cottonPart", COTTON_PART_TOO_LOW);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(income.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when quantity is lower than 1
        income.put("cottonPart", COTTON_PART);
        income.put("quantity", QUANTITY_NOT_NATURAL);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(income.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when color is blanc
        income.put("quantity", UPD_QUANTITY);
        income.put("color", "");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(income.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void applyOutcome() throws Exception {

        Socks socks = new Socks();
        socks.setId(ID);
        socks.setColor(COLOR);
        socks.setCottonPart(COTTON_PART);
        socks.setQuantity(QUANTITY);

        JSONObject outcome = new JSONObject();
        outcome.put("color", COLOR);
        outcome.put("cottonPart", COTTON_PART);
        outcome.put("quantity", UPD_QUANTITY);

        when(socksRepository.findByColorAndCottonPart(eq(COLOR), eq(COTTON_PART))).thenReturn(Optional.of(socks));
        when(socksRepository.save(any(Socks.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.color").value(COLOR))
                .andExpect(jsonPath("$.cottonPart").value(COTTON_PART))
                .andExpect(jsonPath("$.quantity").value(QUANTITY - UPD_QUANTITY));

        // Should return response status 400 when cottonPart is over than 100
        outcome.put("cottonPart", COTTON_PART_TOO_HIGH);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when cottonPart is lower than 0
        outcome.put("cottonPart", COTTON_PART_TOO_LOW);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when quantity is lower than 1
        outcome.put("cottonPart", COTTON_PART);
        outcome.put("quantity", QUANTITY_NOT_NATURAL);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when color is blanc
        outcome.put("quantity", UPD_QUANTITY);
        outcome.put("color", "");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when there is no socks with such color and cottonPart in storage
        outcome.put("color", WRONG_COLOR);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Should return response status 400 when there is not enough socks for outcome
        outcome.put("color", COLOR);
        outcome.put("quantity", QUANTITY_TOO_BIG);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(outcome.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSocksQuantity() throws Exception {

        Socks socksOne = new Socks();
        socksOne.setColor(COLOR);
        socksOne.setCottonPart(COTTON_PART);
        socksOne.setQuantity(100);

        Socks socksTwo = new Socks();
        socksTwo.setColor(COLOR);
        socksTwo.setCottonPart(COTTON_PART_TWO);
        socksTwo.setQuantity(10);

        when(socksRepository.findByColorAndCottonPart(eq(COLOR), eq(COTTON_PART))).thenReturn(Optional.of(socksOne));
        when(socksRepository.findByColorAndCottonPart(eq(COLOR), eq(COTTON_PART_TWO))).thenReturn(Optional.of(socksTwo));
        when(socksRepository.findAllByColorAndCottonPartGreaterThan(eq(COLOR), eq(50))).thenReturn(List.of(socksOne));
        when(socksRepository.findAllByColorAndCottonPartLessThan(eq(COLOR), eq(50))).thenReturn(List.of(socksTwo));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .param("color", COLOR)
                        .param("operation", "equal")
                        .param("cottonPart", String.valueOf(COTTON_PART)))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .param("color", COLOR)
                        .param("operation", "equal")
                        .param("cottonPart", String.valueOf(COTTON_PART_TWO)))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .param("color", COLOR)
                        .param("operation", "moreThan")
                        .param("cottonPart", String.valueOf(50)))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .param("color", COLOR)
                        .param("operation", "lessThan")
                        .param("cottonPart", String.valueOf(50)))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }
}