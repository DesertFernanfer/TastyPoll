package com.fernando.tastypoll.clases;

import static java.security.AccessController.getContext;

import android.util.Log;
import android.widget.Toast;

import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.fernando.tastypoll.interfaces.OnAlimentosCargados;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Enums.CategoriaPlato;
import Enums.TipoDieta;

public class FireBaseManager {
    private static final String TAG = "FireBaseManager";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    public FireBaseManager() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    // ========== INTERFACES ==========
    public interface OnEncuestaCreada {
        void onSuccess();
        void onError(String error);
    }

    public interface OnEncuestaEliminadaListener {
        void onExito();
        void onError(String error);
    }

    // ========== MÉTODOS DE USUARIO ==========
    public void guardarUsuario(String nombre, String email, String password, TipoDieta tipoDieta, String uid) {
        if (uid == null || uid.isEmpty()) {
            Log.e(TAG, "UID no válido para guardar usuario");
            return;
        }

        Usuario usuario = new Usuario(nombre, email, tipoDieta, new ArrayList<>());

        firestore.collection("usuarios")
                .document(uid)
                .set(usuario.toHashMap())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Usuario guardado correctamente"))
                .addOnFailureListener(e -> Log.e(TAG, "Error al guardar usuario", e));
    }

    public void cargarUsuario(IUsuarioLlamada llamada) {
        String uid = mAuth.getUid();
        if (uid == null) {
            llamada.onError("Usuario no autenticado");
            return;
        }

        firestore.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            Usuario usuario = parseUsuarioFromDocument(documentSnapshot);
                            llamada.onUsuarioCargado(usuario);
                            Log.d(TAG, "Usuario cargado correctamente");
                        } catch (Exception e) {
                            Log.e(TAG, "Error al parsear usuario", e);
                            llamada.onError("Error al procesar datos del usuario");
                        }
                    } else {
                        llamada.onUsuarioNoExiste();
                        Log.d(TAG, "Usuario no existe");
                    }
                })
                .addOnFailureListener(e -> {
                    llamada.onError("Error al cargar el usuario");
                    Log.e(TAG, "Error al cargar el usuario", e);
                });
    }

    private Usuario parseUsuarioFromDocument(DocumentSnapshot document) {
        Map<String, Object> data = document.getData();
        if (data == null) throw new IllegalStateException("Documento de usuario vacío");

        String nombre = getStringFromData(data, "nombre", "");
        String email = getStringFromData(data, "email", "");
        TipoDieta tipoDieta = getTipoDietaFromData(data, "tipoDieta", TipoDieta.OMNIVORA);
        ArrayList<String> idsEncuestas = getArrayListFromData(data, "encuestas", new ArrayList<>());

        return new Usuario(nombre, email, tipoDieta, extraerEncuestasDeUsuario(idsEncuestas));
    }

    public void actualizarNombreUsuario(String nombre) {
        String uid = mAuth.getUid();
        if (uid == null || nombre == null || nombre.trim().isEmpty()) {
            Log.e(TAG, "Datos inválidos para actualizar nombre");
            return;
        }

        firestore.collection("usuarios")
                .document(uid)
                .update("nombre", nombre.trim())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Nombre actualizado correctamente"))
                .addOnFailureListener(e -> Log.e(TAG, "Error al actualizar el nombre", e));
    }

    public void actualizarTipoDieta(TipoDieta tipoDieta) {
        String uid = mAuth.getUid();
        if (uid == null || tipoDieta == null) {
            Log.e(TAG, "Datos inválidos para actualizar tipo de dieta");
            return;
        }

        firestore.collection("usuarios")
                .document(uid)
                .update("tipoDieta", tipoDieta.toString())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Tipo de dieta actualizado: " + tipoDieta))
                .addOnFailureListener(e -> Log.e(TAG, "Error al actualizar tipo de dieta", e));
    }

    // ========== MÉTODOS DE ALIMENTOS ==========
    public void getListaAlimentos(OnAlimentosCargados callback) {
        firestore.collection("alimentos")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    ArrayList<Alimento> listaAlimentos = new ArrayList<>();

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        try {
                            Alimento alimento = parseAlimentoFromDocument(doc);
                            listaAlimentos.add(alimento);
                        } catch (Exception e) {
                            Log.w(TAG, "Error al parsear alimento: " + doc.getId(), e);
                        }
                    }

                    callback.onAlimentosCargados(listaAlimentos);
                    Log.d(TAG, "Cargados " + listaAlimentos.size() + " alimentos");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar alimentos", e);
                    callback.onAlimentosCargados(new ArrayList<>());
                });
    }

    private Alimento parseAlimentoFromDocument(DocumentSnapshot doc) {
        String id = doc.getId();
        String nombre = doc.getString("nombre");
        String url = doc.getString("urlImagen");
        CategoriaPlato categoria = extraerCategoria(doc.getString("tipo"));
        TipoDieta tipoDieta = extraerDieta(doc.getString("dieta"));

        return new Alimento(id, nombre, url, categoria, tipoDieta);
    }

    // ========== MÉTODOS DE ENCUESTAS ==========
    public void crearEncuesta(Encuesta encuesta, OnEncuestaCreada callback) {
        String uid = mAuth.getUid();
        if (uid == null) {
            callback.onError("Usuario no autenticado");
            return;
        }

        if (encuesta == null) {
            callback.onError("Encuesta no válida");
            return;
        }

        Map<String, Object> encuestaMap = encuesta.toHashMap();
        encuestaMap.put("creadorUid", uid);

        firestore.collection("encuestas")
                .add(encuestaMap)
                .addOnSuccessListener(documentReference -> {
                    String id = documentReference.getId();
                    Log.d(TAG, "Encuesta creada con ID: " + id);
                    agregarEncuestaAUsuario(id, uid, callback);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al crear encuesta", e);
                    callback.onError("Error al crear la encuesta");
                });
    }

    private void agregarEncuestaAUsuario(String idEncuesta, String uid, OnEncuestaCreada callback) {
        firestore.collection("usuarios")
                .document(uid)
                .update("encuestas", FieldValue.arrayUnion(idEncuesta))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Encuesta vinculada al usuario correctamente");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al vincular encuesta al usuario", e);
                    callback.onError("Error al vincular encuesta con usuario");
                });
    }

    public void eliminarEncuesta(String idEncuesta, OnEncuestaEliminadaListener listener) {
        if (idEncuesta == null || idEncuesta.isEmpty()) {
            listener.onError("ID de encuesta no válido");
            return;
        }

        String uid = mAuth.getUid();
        if (uid == null) {
            listener.onError("Usuario no autenticado");
            return;
        }

        final int[] operacionesCompletadas = {0};
        final boolean[] hayError = {false};
        final StringBuilder mensajeError = new StringBuilder();

        // Eliminar de la colección "encuestas"
        firestore.collection("encuestas")
                .document(idEncuesta)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Encuesta eliminada de la colección");
                    verificarEliminacionCompleta(operacionesCompletadas, hayError, mensajeError, listener);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar encuesta", e);
                    hayError[0] = true;
                    mensajeError.append("Error al eliminar encuesta");
                    verificarEliminacionCompleta(operacionesCompletadas, hayError, mensajeError, listener);
                });

        // Eliminar de la lista del usuario
        firestore.collection("usuarios")
                .document(uid)
                .update("encuestas", FieldValue.arrayRemove(idEncuesta))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Encuesta eliminada de la lista del usuario");
                    verificarEliminacionCompleta(operacionesCompletadas, hayError, mensajeError, listener);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar encuesta del usuario", e);
                    hayError[0] = true;
                    if (mensajeError.length() > 0) mensajeError.append(". ");
                    mensajeError.append("Error al actualizar usuario");
                    verificarEliminacionCompleta(operacionesCompletadas, hayError, mensajeError, listener);
                });
    }

    private synchronized void verificarEliminacionCompleta(int[] completadas, boolean[] hayError,
                                                           StringBuilder mensajeError, OnEncuestaEliminadaListener listener) {
        completadas[0]++;
        if (completadas[0] == 2) {
            if (hayError[0]) {
                listener.onError(mensajeError.toString());
            } else {
                listener.onExito();
            }
        }
    }
    public ArrayList<Encuesta> extraerEncuestasDeUsuario(ArrayList<String> ids) {
        ArrayList<Encuesta> encuestas = new ArrayList<>();

        if (ids == null || ids.isEmpty()) {
            return encuestas;
        }

        for (String id : ids) {
            if (id != null && !id.isEmpty()) {
                cargarEncuestaPorId(id, encuestas);
            }
        }
        return encuestas;
    }

    private void cargarEncuestaPorId(String id, ArrayList<Encuesta> encuestas) {
        firestore.collection("encuestas")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            Encuesta encuesta = parseEncuestaFromDocument(documentSnapshot);
                            encuestas.add(encuesta);
                            Log.d(TAG, "Encuesta cargada: " + encuesta.getNombre());
                        } catch (Exception e) {
                            Log.e(TAG, "Error al parsear encuesta: " + id, e);
                            limpiarEncuestaInvalidaDelUsuario(id);
                        }
                    } else {
                        Log.w(TAG, "Encuesta no existe: " + id + ", eliminándola del usuario");
                        limpiarEncuestaInvalidaDelUsuario(id);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar encuesta: " + id, e);
                    if (e.getMessage() != null && e.getMessage().contains("PERMISSION_DENIED")) {
                        limpiarEncuestaInvalidaDelUsuario(id);
                    }
                });
    }

    private void limpiarEncuestaInvalidaDelUsuario(String idEncuestaInvalida) {
        String uid = mAuth.getUid();
        if (uid != null) {
            firestore.collection("usuarios")
                    .document(uid)
                    .update("encuestas", FieldValue.arrayRemove(idEncuestaInvalida))
                    .addOnSuccessListener(aVoid ->
                            Log.d(TAG, "Encuesta inválida eliminada: " + idEncuestaInvalida))
                    .addOnFailureListener(e ->
                            Log.e(TAG, "Error al limpiar encuesta: " + idEncuestaInvalida, e));
        }
    }


    public interface OnEncuestaDesactivada {
        void onSuccess();
        void onError(String error);
    }



    public void desactivarEncuesta(String idEncuesta, OnEncuestaDesactivada callback) {
        // Validaciones
        if (idEncuesta == null || idEncuesta.isEmpty()) {
            callback.onError("ID de encuesta no válido");
            return;
        }

        String uid = mAuth.getUid();
        if (uid == null) {
            callback.onError("Usuario no autenticado");
            return;
        }

        // Llamar solo a Firebase Function (que desactiva + envía emails)
        FirebaseFunctions functions = FirebaseFunctions.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("encuestaId", idEncuesta);

        functions
                .getHttpsCallable("desactivarEncuesta")
                .call(data)
                .addOnSuccessListener(result -> {
                    Log.d(TAG, "Encuesta desactivada correctamente: " + idEncuesta);
                    callback.onSuccess();
                })
                .addOnFailureListener(exception -> {
                    String error = exception.getMessage();
                    Log.e(TAG, "Error al desactivar encuesta: " + idEncuesta, exception);
                    callback.onError(error != null ? error : "Error al desactivar la encuesta");
                });
    }
    private Encuesta parseEncuestaFromDocument(DocumentSnapshot document) {
        Map<String, Object> data = document.getData();
        if (data == null) throw new IllegalStateException("Documento de encuesta vacío");

        String idEncuesta = document.getId();
        String nombre = getStringFromData(data, "nombre", "");
        String descripcion = getStringFromData(data, "descripcion", "");
        boolean tieneEmail = getBooleanFromData(data, "tieneEmail", false);
        boolean estaActiva = getBooleanFromData(data, "activa", false);
        TipoDieta tipoDieta = getTipoDietaFromData(data, "tipoDieta", TipoDieta.OMNIVORA);
        boolean tieneAlimentosPredeterminados = getBooleanFromData(data, "tienealimentosPredeterminados", false);
        ArrayList<String> emails = getArrayListFromData(data, "emails", new ArrayList<>());

        // Extraer votos
        List<Map<String, Object>> rawVotosList = new ArrayList<>();
        if (data.containsKey("votos") && data.get("votos") instanceof List) {
            rawVotosList = (List<Map<String, Object>>) data.get("votos");
        }
        HashMap<String, Integer> votos = extraerVotos(rawVotosList);

        // Extraer fechas y calcular tiempo de vida
        int tiempoVida = calcularTiempoVida(data);

        return new Encuesta(idEncuesta, nombre, descripcion, tieneEmail, tiempoVida,
                estaActiva, tipoDieta, tieneAlimentosPredeterminados, null, emails, votos);
    }

    private String getStringFromData(Map<String, Object> data, String key, String defaultValue) {
        return data.containsKey(key) && data.get(key) != null ? data.get(key).toString() : defaultValue;
    }

    private boolean getBooleanFromData(Map<String, Object> data, String key, boolean defaultValue) {
        return data.containsKey(key) && data.get(key) instanceof Boolean ? (Boolean) data.get(key) : defaultValue;
    }

    private TipoDieta getTipoDietaFromData(Map<String, Object> data, String key, TipoDieta defaultValue) {
        if (data.containsKey(key) && data.get(key) != null) {
            return extraerDieta(data.get(key).toString());
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<String> getArrayListFromData(Map<String, Object> data, String key, ArrayList<String> defaultValue) {
        return data.containsKey(key) && data.get(key) instanceof List ?
                (ArrayList<String>) data.get(key) : defaultValue;
    }

    private int calcularTiempoVida(Map<String, Object> data) {
        Date fechaCreacion = null;
        Date fechaVencimiento = null;

        if (data.containsKey("fechaCreacion") && data.get("fechaCreacion") instanceof Timestamp) {
            fechaCreacion = ((Timestamp) data.get("fechaCreacion")).toDate();
        }
        if (data.containsKey("fechaVencimiento") && data.get("fechaVencimiento") instanceof Timestamp) {
            fechaVencimiento = ((Timestamp) data.get("fechaVencimiento")).toDate();
        }

        if (fechaCreacion != null && fechaVencimiento != null) {
            long diffMillis = fechaVencimiento.getTime() - fechaCreacion.getTime();
            return (int) (diffMillis / (60 * 1000)); // Convertir a minutos
        }
        return 0;
    }

    private TipoDieta extraerDieta(String dieta) {
        if (dieta == null || dieta.trim().isEmpty()) {
            return TipoDieta.OMNIVORA;
        }

        try {
            return TipoDieta.valueOf(dieta.toUpperCase(Locale.ROOT).trim());
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Tipo de dieta no válido: " + dieta + ", usando OMNIVORA por defecto");
            return TipoDieta.OMNIVORA;
        }
    }

    private CategoriaPlato extraerCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return CategoriaPlato.TODOS;
        }

        try {
            return CategoriaPlato.valueOf(categoria.toUpperCase(Locale.ROOT).trim());
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Categoría no válida: " + categoria + ", usando TODOS por defecto");
            return CategoriaPlato.TODOS;
        }
    }

    private HashMap<String, Integer> extraerVotos(List<Map<String, Object>> votosList) {
        HashMap<String, Integer> resultados = new HashMap<>();

        if (votosList == null || votosList.isEmpty()) {
            return resultados;
        }

        for (Map<String, Object> votoItem : votosList) {
            if (votoItem == null) continue;

            String nombreAlimento = "";
            Integer conteo = 0;

            // CAMBIO: Buscar "nombreAlimento" en vez de "nombre"
            if (votoItem.containsKey("nombreAlimento") && votoItem.get("nombreAlimento") instanceof String) {
                nombreAlimento = (String) votoItem.get("nombreAlimento");
            }

            if (votoItem.containsKey("conteo") && votoItem.get("conteo") instanceof Number) {
                conteo = ((Number) votoItem.get("conteo")).intValue();
            }

            if (nombreAlimento != null && !nombreAlimento.trim().isEmpty()) {
                resultados.put(nombreAlimento.trim(), Math.max(0, conteo));
                Log.d(TAG, "Voto agregado: " + nombreAlimento + " = " + conteo);
            } else {
                Log.w(TAG, "Elemento de voto sin nombreAlimento válido: " + votoItem);
            }
        }

        Log.d(TAG, "Total votos extraídos: " + resultados.size());
        return resultados;
    }
}