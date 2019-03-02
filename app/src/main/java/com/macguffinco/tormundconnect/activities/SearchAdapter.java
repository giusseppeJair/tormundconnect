package com.macguffinco.tormundconnect.activities;

import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macguffinco.tormundconnect.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderDatos>{
   ArrayList<String> listDatos;

    public SearchAdapter(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      //   View view= Lay.from(parent.getContext()).inflate(R.layout.item_list_buscar,null,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            dato= (TextView)itemView.findViewById(R.id.horary);
        }
    }
}
