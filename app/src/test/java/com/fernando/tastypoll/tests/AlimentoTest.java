package com.fernando.tastypoll.tests;

import com.fernando.tastypoll.clases.Alimento;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class AlimentoTest {

    private Alimento alimento;

    @Before
    public void setUp() {
        alimento = new Alimento("pasta_001", "Pasta Carbonara", "http://test.com/pasta.jpg",
                CategoriaPlato.PRIMERO, TipoDieta.OMNIVORA);
    }

    @Test
    public void testConstructorValido() {
        assertNotNull(alimento);
        assertEquals("pasta_001", alimento.getId());
        assertEquals("Pasta Carbonara", alimento.getNombre());
        assertEquals("http://test.com/pasta.jpg", alimento.getUrlImagen());
        assertEquals(CategoriaPlato.PRIMERO, alimento.getCategoria());
        assertEquals(TipoDieta.OMNIVORA, alimento.getTipoDieta());
    }

    @Test
    public void testGetters() {
        assertEquals("pasta_001", alimento.getId());
        assertEquals("Pasta Carbonara", alimento.getNombre());
        assertEquals("http://test.com/pasta.jpg", alimento.getUrlImagen());
        assertEquals(CategoriaPlato.PRIMERO, alimento.getCategoria());
        assertEquals(TipoDieta.OMNIVORA, alimento.getTipoDieta());
    }

    @Test
    public void testSetSeleccionado() {
        assertTrue(alimento.setSeleccionado(true));
        assertFalse(alimento.setSeleccionado(false));
    }

    @Test
    public void testAlimentoVegano() {
        Alimento alimentoVegano = new Alimento("ensalada_001", "Ensalada Verde",
                "http://test.com/ensalada.jpg",
                CategoriaPlato.ENTRADA, TipoDieta.VEGANA);

        assertEquals(TipoDieta.VEGANA, alimentoVegano.getTipoDieta());
        assertEquals(CategoriaPlato.ENTRADA, alimentoVegano.getCategoria());
    }
}