package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.go_doggies.com.go_doggies.model.Client;

/**
 * Created by anto004 on 1/9/18.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {
    private Context mContext;
    private List<Client> clients;

    ClientAdapter(Context context, List<Client> clients){
        this.mContext = context;
        this.clients = clients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.client_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Client client = clients.get(position);
        Drawable d = mContext.getDrawable(R.drawable.my_client);
        holder.clientImage.setImageDrawable(d);
        holder.clientName.setText(client.name);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView clientImage;
        public TextView clientName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.clientImage = itemView.findViewById(R.id.client_image);
            this.clientName = itemView.findViewById(R.id.client_name);
        }
    }
}