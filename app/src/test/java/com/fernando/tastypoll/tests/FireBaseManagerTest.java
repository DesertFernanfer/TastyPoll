package com.fernando.tastypoll.tests;

import com.fernando.tastypoll.clases.FireBaseManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class FireBaseManagerTest {

    private FireBaseManager firebaseManager;

    @Before
    public void setUp() {
        firebaseManager = new FireBaseManager();
    }

    @Test
    public void testConstructor() {
        assertNotNull(firebaseManager);
    }

    @Test
    public void testExtraerEncuestasConListaVacia() {
        ArrayList<String> idsVacios = new ArrayList<>();
        ArrayList<?> resultado = firebaseManager.extraerEncuestasDeUsuario(idsVacios);
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    public void testExtraerEncuestasConListaNull() {
        ArrayList<?> resultado = firebaseManager.extraerEncuestasDeUsuario(null);
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    public void testExtraerEncuestasConIdsValidos() {
        ArrayList<String> ids = new ArrayList<>();
        //Ids de encuesta que existen en la base de datos y no sean borrados
        ids.add("encuesta_001");
        ids.add("encuesta_002");

        // Este test verificará que el metodo maneja IDs válidos
        // En un entorno real, requeriría mock de Firestore
        ArrayList<?> resultado = firebaseManager.extraerEncuestasDeUsuario(ids);
        assertNotNull(resultado);
    }

    // Tests para validar métodos que no requieren Firebase
    @Test
    public void testValidacionesBasicas() {
        // Test que verifica que las interfaces están definidas
        assertNotNull(FireBaseManager.OnEncuestaCreada.class);
        assertNotNull(FireBaseManager.OnEncuestaEliminadaListener.class);
        assertNotNull(FireBaseManager.OnEncuestaDesactivada.class);
    }
}