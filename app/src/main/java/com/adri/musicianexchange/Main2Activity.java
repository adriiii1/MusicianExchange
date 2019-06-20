package com.adri.musicianexchange;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.*;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private ArrayList<Conversacion> listConversaciones = new ArrayList<Conversacion>();
    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter miAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        if(user.getPhotoUrl()!= null){
            Glide.with(this).load(imageUrl).into(imgUsuario);
        }
        txtUsuario.setText(String.valueOf(user.getEmail()));
        
        cargarConversaciones(user);
    }

    private void cargarConversaciones(final FirebaseUser user) {
        dbRef = FirebaseDatabase.getInstance().getReference("Conversaciones").child("conversaciones");

        layoutManager = new LinearLayoutManager(this);
        miAdapter = new MiAdapterConversaciones(listConversaciones);

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listConversaciones.clear();
                Gson gson = new Gson();
                for (DataSnapshot obj: dataSnapshot.getChildren()) {
                    String registro = String.valueOf(obj.getKey());
                    try{
                        if(registro.contains(user.getUid())){
                            String otherUser = registro.replace(user.getUid(),"");
                            Conversacion conver = new Conversacion(otherUser,registro);
                            listConversaciones.add(conver);
                        }

                    }catch (com.google.gson.JsonSyntaxException e){}
                }
                recyclerView = findViewById(R.id.recyclerConvers);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(miAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRef.addValueEventListener(listener);
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

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
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
            Intent intent = new Intent(this, NuevoAnuncioPlaylists.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
