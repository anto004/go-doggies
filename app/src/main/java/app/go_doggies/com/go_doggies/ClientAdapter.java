package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.go_doggies.com.go_doggies.model.ClientDetails;

/**
 * Created by anto004 on 1/9/18.
 */

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {
    private Context mContext;
    private List<ClientDetails> clients;

    ClientAdapter(Context context, List<ClientDetails> clients){
        this.mContext = context;
        this.clients = clients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.client_item, parent, false);
//        view.setOnClickListener(new MyOnClickListener());
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.itemView.setOnClickListener(myViewHolder);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClientDetails client = this.clients.get(position);
        Drawable d = mContext.getDrawable(R.drawable.my_client);
        holder.clientImage.setImageDrawable(d);
        holder.clientName.setText(client.getClientName());

        holder.itemView.setTag(client);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView clientImage;
        public TextView clientName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.clientImage = itemView.findViewById(R.id.client_image);
            this.clientName = itemView.findViewById(R.id.client_name);
        }

        @Override
        public void onClick(View view) {
            ClientDetails client = (ClientDetails) view.getTag();
            if(client != null) {
                Toast.makeText(mContext, client.getClientName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
