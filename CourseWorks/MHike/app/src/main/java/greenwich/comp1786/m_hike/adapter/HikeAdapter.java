package greenwich.comp1786.m_hike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.model.Hike;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.VH> {

    public interface HikeListener {
        void onEdit(Hike h);
        void onDelete(Hike h);
        void onAddObservation(Hike h);
    }

    private List<Hike> data;
    private HikeListener listener;

    public HikeAdapter(List<Hike> data, HikeListener listener) {
        this.data = data;
        this.listener = listener;
    }
    public static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSub;
        Button btnEdit, btnObservation;
        ImageButton btnDelete;

        public VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSub = v.findViewById(R.id.tvSub);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnObservation = v.findViewById(R.id.btnObservation);
        }


    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hike, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Hike item = data.get(pos);
        h.tvTitle.setText(item.getName() + " • " + item.getDifficulty());
        h.tvSub.setText(item.getLocation() + " • " + item.getDate());
        h.btnObservation.setOnClickListener(v -> listener.onAddObservation(item));


        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
        h.btnObservation.setOnClickListener(v -> {
            if (listener != null) listener.onAddObservation(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // === ViewHolder ===

}
