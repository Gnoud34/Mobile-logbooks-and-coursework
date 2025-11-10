package greenwich.comp1786.datastore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    Context context;
    ArrayList<Contact> data;

    public ContactAdapter(Context ctx, ArrayList<Contact> list){
        context = ctx;
        data = list;
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView name, dob, email;

        public Holder(View v){
            super(v);
            avatar = v.findViewById(R.id.imgAvatar);
            name = v.findViewById(R.id.txtName);
            dob = v.findViewById(R.id.txtDob);
            email = v.findViewById(R.id.txtEmail);

            View.OnClickListener detailEvent = view -> {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("id", data.get(getAdapterPosition()).getId());
                context.startActivity(i);
            };

            v.setOnClickListener(detailEvent);
            avatar.setOnClickListener(detailEvent);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup p, int v){
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, p,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder h,int pos){
        Contact c = data.get(pos);
        h.avatar.setImageResource(c.getAvatar());
        h.name.setText(c.getName());
        h.dob.setText(c.getDob());
        h.email.setText(c.getEmail());
    }

    @Override
    public int getItemCount(){ return data.size(); }
}
