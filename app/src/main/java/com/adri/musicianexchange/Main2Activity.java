package com.adri.musicianexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();
        String imageUrl = null;
        if (user.getPhotoUrl() != null) {
            imageUrl = user.getPhotoUrl().toString();
        }else{
            Log.d("UserProfilePic","No hay foto");
        }
        ImageView imgUsuario = findViewById(R.id.imgUsuario);
        TextView txtUsuario= findViewById(R.id.txtUsuario);
        TextView txtUsr= findViewById(R.id.txtUsr);
        if(user.getPhotoUrl()!= null){
            Glide.with(this).load(imageUrl).into(imgUsuario);
        }
        if(user.getDisplayName()!=null){
            txtUsr.setText((user.getDisplayName()));
        }
        txtUsuario.setText(String.valueOf(user.getEmail()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            auth.signOut();
            startActivity(new Intent(Main2Activity.this, loginActivity.class));
            finish();
        }
        if (id == R.id.perfilMod) {
            startActivity(new Intent(Main2Activity.this, PerfilActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_playlists) {
            //Listas de reproducción
            Intent intent = new Intent(this, ListasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ventas) {
            //Anuncios de venta
            Intent intent = new Intent(this, VentasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_grupos) {
            //Anuncios de grupos
            Intent intent = new Intent(this, GruposActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_conciertos) {
            //Conciertos
            Intent intent = new Intent(this, ConciertosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_grupo) {
            //+ anuncio de grupo
            Intent intent = new Intent(this, NuevoAnuncioGrupo.class);
            startActivity(intent);
        } else if (id == R.id.nav_venta) {
            //+ anuncio de venta
            Intent intent = new Intent(this, NuevoAnuncioVenta.class);
            startActivity(intent);
        }else if (id == R.id.nav_concierto) {
            //+ concierto
            Intent intent = new Intent(this, NuevoAnuncioConcierto.class);
            startActivity(intent);
        }else if (id == R.id.nav_playlist) {
            //+ playlist
            Intent intent = new Intent(this, NuevoAnuncioVenta.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
