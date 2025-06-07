package com.fernando.tastypoll.tests;

import com.fernando.tastypoll.clases.Encuesta;
import com.fernando.tastypoll.clases.Alimento;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Enums.TipoDieta;
import Enums.CategoriaPlato;

public class EncuestaTest {

    private Encuesta encuesta;
    private ArrayList<Alimento> alimentos;
    private ArrayList<String> emails;
    private HashMap<String, Integer> votos;

    @Before
    public void setUp() {
        alimentos = new ArrayList<>();
        alimentos.add(new Alimento("pasta_001", "Pasta", "url", CategoriaPlato.PRIMERO, TipoDieta.OMNIVORA));

        emails = new ArrayList<>();
        votos = new HashMap<>();

        encuesta = new Encuesta("Almuerzo Oficina", "¿Qué pedimos?", true, 60, true,
                TipoDieta.OMNIVORA, true, alimentos, emails, votos);
    }

    @Test
    public void testConstructorValido() {
        assertNotNull(encuesta);
        assertEquals("Almuerzo Oficina", encuesta.getNombre());
        assertEquals("¿Qué pedimos?", encuesta.getDescripcion());
        assertTrue(encuesta.tieneEmail());
        assertTrue(encuesta.getEstaActiva());
        assertEquals(TipoDieta.OMNIVORA, encuesta.getTipoDieta());
        assertTrue(encuesta.getTienealimentosPredeterminados());
        assertEquals(1, encuesta.getAlimentos().size());
    }

    @Test
    public void testConstructorConId() {
        Encuesta encuestaConId = new Encuesta("test_123", "Test", "Desc", false, 30, false,
                TipoDieta.VEGANA, false, null, null, null);

        assertEquals("test_123", encuestaConId.getId());
        assertEquals("Test", encuestaConId.getNombre());
        assertFalse(encuestaConId.tieneEmail());
        assertFalse(encuestaConId.getEstaActiva());
        assertEquals(TipoDieta.VEGANA, encuestaConId.getTipoDieta());
        assertNotNull(encuestaConId.getAlimentos()); // Se inicializa automáticamente
    }

    @Test
    public void testConstructorDePrueba() {
        ArrayList<Alimento> alimentosPrueba = new ArrayList<>();
        alimentosPrueba.add(new Alimento("test_001", "Test Food", "url",
                CategoriaPlato.PRIMERO, TipoDieta.OMNIVORA));

        Encuesta encuestaPrueba = new Encuesta(alimentosPrueba);

        assertEquals("nombre", encuestaPrueba.getNombre());
        assertEquals("descripcion", encuestaPrueba.getDescripcion());
        assertTrue(encuestaPrueba.tieneEmail());
        assertTrue(encuestaPrueba.getEstaActiva());
        assertEquals(TipoDieta.OMNIVORA, encuestaPrueba.getTipoDieta());
        assertEquals(1, encuestaPrueba.getAlimentos().size());
    }

    @Test
    public void testToHashMap() {
        Map<String, Object> map = encuesta.toHashMap();

        assertNotNull(map);
        assertEquals("Almuerzo Oficina", map.get("nombre"));
        assertEquals("¿Qué pedimos?", map.get("descripcion"));
        assertEquals(true, map.get("activa"));
        assertEquals(true, map.get("tieneEmail"));
        assertEquals("OMNIVORA", map.get("tipoDieta"));
        assertEquals(true, map.get("tienealimentosPredeterminados"));

        // Verificar fechas
        assertNotNull(map.get("fechaCreacion"));
        assertNotNull(map.get("fechaVencimiento"));

        // Verificar arrays
        assertTrue(map.get("emails") instanceof ArrayList);
        assertTrue(map.get("alimentos") instanceof ArrayList);
        assertTrue(map.get("votos") instanceof HashMap);
    }

    @Test
    public void testFechas() {
        assertNotNull(encuesta.getFechaCreacion());
        assertNotNull(encuesta.getFechaVencimiento());

        // La fecha de vencimiento debe ser posterior a la de creación
        assertTrue(encuesta.getFechaVencimiento().getSeconds() > encuesta.getFechaCreacion().getSeconds());
    }

    @Test
    public void testSetEstaActiva() {
        assertTrue(encuesta.getEstaActiva());

        encuesta.setEstaActiva(false);
        assertFalse(encuesta.getEstaActiva());

        encuesta.setEstaActiva(true);
        assertTrue(encuesta.getEstaActiva());
    }

    @Test
    public void testGettersCompletos() {
        assertEquals("Almuerzo Oficina", encuesta.getNombre());
        assertEquals("¿Qué pedimos?", encuesta.getDescripcion());
        assertTrue(encuesta.getTieneEmail());
        assertEquals(TipoDieta.OMNIVORA, encuesta.getTipoDieta());
        assertTrue(encuesta.getTienealimentosPredeterminados());
        assertNotNull(encuesta.getAlimentos());
        assertNotNull(encuesta.getEmails());
        assertNotNull(encuesta.getVotos());
        assertEquals("generic", encuesta.getId());
    }
}