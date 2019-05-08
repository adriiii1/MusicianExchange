package com.adri.musicianexchange;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class FragmentImagen extends DialogFragment {

    static FragmentImagen newInstance(String url) {
        FragmentImagen frag = new FragmentImagen();
        Bundle bun=new Bundle();
        bun.putString("url",url);
        frag.setArguments(bun);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.example_fragment, container, false);
        ImageView imageView =view.findViewById(R.id.imagenGrande);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String url = bundle.getString("url","");
            Glide.with(this).load(url).into(imageView);
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
