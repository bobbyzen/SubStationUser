package com.bzen.substationuser.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bzen.substationuser.Model.Pengaduan;
import com.bzen.substationuser.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PengaduanAdapter extends RecyclerView.Adapter<PengaduanAdapter.PengaduanViewHolder> {

    ArrayList<Pengaduan> listPengaduan = new ArrayList<>();
    String user = "";

    public PengaduanAdapter(ArrayList<Pengaduan> listPengaduan, String user) {
        this.listPengaduan.addAll(listPengaduan);
        this.user = user;
    }

    @NonNull
    @Override
    public PengaduanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaduan, parent, false);
        return new PengaduanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PengaduanAdapter.PengaduanViewHolder holder, int position) {
        final Pengaduan pengaduan = listPengaduan.get(position);
        if(pengaduan.getStatus_baru().equals("0")){
            holder.ivGardu.setImageResource(R.drawable.gardu_aman);
        }
        else if(pengaduan.getStatus_baru().equals("1")){
            holder.ivGardu.setImageResource(R.drawable.gardu_maintenance);
        }
        else if(pengaduan.getStatus_baru().equals("2")){
            holder.ivGardu.setImageResource(R.drawable.gardu_mati);
        }

        holder.tvGardu.setText(pengaduan.getGardu_id());
        holder.tvMaintenance.setText(pengaduan.getTanggal_baru());

        final String [] maintenance = holder.itemView.getContext().getResources().getStringArray(R.array.jenis_maintenance);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("User").child(pengaduan.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("email").getValue().toString();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog alertDialog = new AlertDialog.Builder(holder.itemView.getContext()).create();
                        alertDialog.setTitle("Deskripsi Gardu");
                        LayoutInflater inflater = (LayoutInflater) holder.itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.aler_dialog_deskirpsi_pengaduan_gardu, null);
                        alertDialog.setView(dialogView);
                        alertDialog.setIcon(R.drawable.gardu_fix);
                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);

                        final TextView tvGardu = dialogView.findViewById(R.id.tvGardu);
                        final TextView tvTanggal = dialogView.findViewById(R.id.tvTanggal);
                        final TextView tvMaintenance = dialogView.findViewById(R.id.tvMaintenance);
                        final TextView tvDeskripsi = dialogView.findViewById(R.id.tvDeskripsi);
                        final TextView tvDihandleOleh = dialogView.findViewById(R.id.tvDihandle);

                        tvGardu.setText(pengaduan.getGardu_id());
                        tvTanggal.setText(pengaduan.getTanggal_lama() + " / " + pengaduan.getTanggal_baru());
                        tvMaintenance.setText(maintenance[Integer.valueOf(pengaduan.getStatus_lama())] + " / " + maintenance[Integer.valueOf(pengaduan.getStatus_baru())]);
                        tvDeskripsi.setText(pengaduan.getLaporan());
                        tvDihandleOleh.setText(user);

                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listPengaduan.size();
    }

    public class PengaduanViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGardu;
        TextView tvGardu, tvMaintenance;

        public PengaduanViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGardu = itemView.findViewById(R.id.ivGardu);
            tvGardu = itemView.findViewById(R.id.tvGardu);
            tvMaintenance = itemView.findViewById(R.id.tvTanggalMaintenance);
        }
    }
}
