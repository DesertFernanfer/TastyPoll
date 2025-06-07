package com.fernando.tastypoll.tests;

import com.fernando.tastypoll.clases.Usuario;
import com.fernando.tastypoll.clases.Encuesta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Map;
import Enums.TipoDieta;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioTest {

    private Usuario usuario;
    private ArrayList<Encuesta> encuestas;

    @Before
    public void setUp() {
        encuestas = new ArrayList<>();
        usuario = new Usuario("Fernando", "fernando@test.com", TipoDieta.OMNIVORA, encuestas);
    }

    @Test
    public void testConstructorValido() {
        assertNotNull(usuario);
        assertEquals("Fernando", usuario.getNombre());
        assertEquals("fernando@test.com", usuario.getEmail());
        assertEquals(TipoDieta.OMNIVORA, usuario.getTipoDieta());
        assertNotNull(usuario.getEncuestas());
        assertEquals(0, usuario.getEncuestas().size());
    }

    @Test
    public void testConstructorConEncuestasNull() {
        Usuario usuarioNullEncuestas = new Usuario("Test", "test@test.com", TipoDieta.VEGANA, null);
        assertNotNull(usuarioNullEncuestas.getEncuestas());
        assertEquals(0, usuarioNullEncuestas.getEncuestas().size());
    }

    @Test
    public void testToHashMap() {
        Map<String, Object> map = usuario.toHashMap();

        assertNotNull(map);
        assertEquals("Fernando", map.get("nombre"));
        assertEquals("fernando@test.com", map.get("email"));
        assertEquals("OMNIVORA", map.get("tipoDieta"));
        assertTrue(map.get("encuestas") instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) map.get("encuestas")).size());
    }

    @Test
    public void testEliminarEncuesta() {
        // Crear encuesta de prueba
        ArrayList<String> emails = new ArrayList<>();
        Encuesta encuesta = new Encuesta("Test", "Descripción", true, 60, true,
                TipoDieta.OMNIVORA, false, null, emails, null);

        // Agregar y verificar
        usuario.getEncuestas().add(encuesta);
        assertEquals(1, usuario.getEncuestas().size());

        // Eliminar y verificar
        usuario.eliminarEncuesta(encuesta);
        assertEquals(0, usuario.getEncuestas().size());
    }

    @Test
    public void testEliminarEncuestaInexistente() {
        ArrayList<String> emails = new ArrayList<>();
        Encuesta encuesta = new Encuesta("Test", "Descripción", true, 60, true,
                TipoDieta.OMNIVORA, false, null, emails, null);

        // Intentar eliminar encuesta que no existe
        int sizeBefore = usuario.getEncuestas().size();
        usuario.eliminarEncuesta(encuesta);
        assertEquals(sizeBefore, usuario.getEncuestas().size());
    }
}
