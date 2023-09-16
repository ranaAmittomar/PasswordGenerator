package com.example.passwordgenerator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordsAdapter extends RecyclerView.Adapter<PasswordsAdapter.ViewHolder> {
    private List<String> passwords;

    public PasswordsAdapter(List<String> passwords) {
        this.passwords = passwords;
    }

    @NonNull
    @Override
    public PasswordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordsAdapter.ViewHolder holder, int position) {

        String password = passwords.get(position);
        holder.passwordTextView.setText(password);

    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public void clearData() {
        passwords.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView passwordTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
        }
    }
}
