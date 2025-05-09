package com.fernando.tastypoll.interfaces;

import com.fernando.tastypoll.clases.Usuario;

public interface IUsuarioLlamada{
    void onUsuarioCargado(Usuario usuario);
    void onUsuarioNoExiste();
    void onError(String mensajeError);

}
