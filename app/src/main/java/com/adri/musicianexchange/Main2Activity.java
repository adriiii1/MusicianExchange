package com.adri.musicianexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
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

        TextView txtUsuario= findViewById(R.id.txtUsuario);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            auth.signOut();
            startActivity(new Intent(Main2Activity.this, loginActivity.class));
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
            Intent intent = new Intent(this, listasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ventas) {
            //Anuncios de venta
            Intent intent = new Intent(this, ventasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_grupos) {
            //Anuncios de grupos
            Intent intent = new Intent(this, gruposActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_conciertos) {
            //Conciertos
            Intent intent = new Intent(this, conciertosActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_grupo) {
            //+ anuncio de grupo
            Intent intent = new Intent(this, nuevoAnuncioGrupo.class);
            startActivity(intent);
        } else if (id == R.id.nav_venta) {
            //+ anuncio de venta
            Intent intent = new Intent(this, nuevoAnuncioVenta.class);
            startActivity(intent);
        }else if (id == R.id.nav_concierto) {
            //+ concierto
            Intent intent = new Intent(this, nuevoAnuncioVenta.class);
            startActivity(intent);
        }else if (id == R.id.nav_playlist) {
            //+ playlist
            Intent intent = new Intent(this, nuevoAnuncioVenta.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
