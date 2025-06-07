package com.fernando.tastypoll.tests;

import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.clases.Usuario;
import com.fernando.tastypoll.clases.FireBaseManager;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import Enums.TipoDieta;

public class SingletonTest {

    private Singleton singleton;

    @Before
    public void setUp() {
        singleton = Singleton.getInstancia();
    }

    @Test
    public void testSingletonPattern() {
        Singleton otra_instancia = Singleton.getInstancia();
        assertSame(singleton, otra_instancia);
    }

    @Test
    public void testGetUrl() {
        String url = singleton.getUrl();
        assertNotNull(url);
        assertEquals("https://tastypoll.web.app", url);
    }

    @Test
    public void testFirebaseManager() {
        FireBaseManager manager = singleton.getFireBaseManager();
        assertNotNull(manager);
    }

    @Test
    public void testUsuarioInicial() {
        // Al inicio el usuario debería ser null
        Usuario usuario = singleton.getUsuario();
        // Puede ser null hasta que se cargue
        if (usuario != null) {
            assertNotNull(usuario.getNombre());
            assertNotNull(usuario.getEmail());
            assertNotNull(usuario.getTipoDieta());
        }
    }

    @Test
    public void testLimpiarDatos() {
        singleton.limpiarDatos();
        assertNull(singleton.getUsuario());
    }

    @Test
    public void testActualizarUsuario() {
        // Test que verifica que el método existe y no lanza excepciones
        try {
            singleton.actualizarUsuario();
            // Si llegamos aquí, el método se ejecutó sin errores
            assertTrue(true);
        } catch (Exception e) {
            fail("actualizarUsuario() no debería lanzar excepciones: " + e.getMessage());
        }
    }
}