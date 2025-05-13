package com.fernando.tastypoll.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fernando.tastypoll.R;
import com.fernando.tastypoll.clases.Singleton;
import com.fernando.tastypoll.clases.Usuario;
import com.fernando.tastypoll.fragments.AboutUs;
import com.fernando.tastypoll.fragments.Ajustes;
import com.fernando.tastypoll.fragments.CrearEncuesta;
import com.fernando.tastypoll.fragments.Encuestas;
import com.fernando.tastypoll.fragments.HomePageFragment;
import com.fernando.tastypoll.interfaces.IUsuarioLlamada;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;

import Enums.TipoDieta;

public class App extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private Usuario usuario;
    private Toolbar toolbar;
    private Singleton singleton;

    private Fragment actualFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        recuperarIdioma();
        iniciarlizarElementos();


        NavigationView navigationView = findViewById(R.id.nav_view);






        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomePageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new HomePageFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomePageFragment());
            } else if (itemId == R.id.encuestas) {
                replaceFragment(new Encuestas());
            }


            return true;
        });

        //Boton crear encuesta
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new CrearEncuesta());
            }
        });

    }
    private void inicializarNavigationView(){

    }
    private void iniciarlizarElementos(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);

        singleton = Singleton.getInstancia();
        singleton.actualizarUsuario();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private void recuperarIdioma(){
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String idioma = prefs.getString("idioma", "en"); // Valor por defecto "en" (inglés)

        // Aplicar el Locale
        Locale locale = idioma.equals("es") ? new Locale("es") : new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        actualFragment = fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_home){
            replaceFragment(new HomePageFragment());
        } else if(id == R.id.nav_settings){
            replaceFragment(Ajustes.newInstance());
        } else if(id == R.id.nav_about){
            replaceFragment(new AboutUs());

        } else if(id == R.id.nav_help){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://elchapuzasinformatico.com")));

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("App", "onResume");
        Singleton.getInstancia().getFireBaseManager().cargarUsuario(new IUsuarioLlamada() {
            @Override
            public void onUsuarioCargado(Usuario user) {
                usuario = user;
                if(actualFragment instanceof Ajustes){
                    ((Ajustes) actualFragment).actualizarUsuario(usuario);
                }
            }

            @Override
            public void onUsuarioNoExiste() {
                Log.e("App_Usuario_error","Usuario no cargadado");
            }

            @Override
            public void onError(String mensaje) {
                Log.e("App_Usuario_error","Usuario no cargadado "+mensaje);
            }
        });
    }*/




}


