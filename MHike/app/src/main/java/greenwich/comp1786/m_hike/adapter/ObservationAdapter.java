package greenwich.comp1786.m_hike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jspecify.annotations.NonNull;

import java.util.List;

import greenwich.comp1786.m_hike.R;
import greenwich.comp1786.m_hike.model.Observation;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    // Interface lắng nghe sự kiện xóa
    public interface ObservationListener {
        void onDelete(Observation obs);
    }

    private List<Observation> observationList;
    private ObservationListener listener;

    // Constructor
    public ObservationAdapter(List<Observation> observationList, ObservationListener listener) {
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_observation.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_observation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Observation obs = observationList.get(position);
        holder.tvObservation.setText(obs.getObservation());
        holder.tvTime.setText(obs.getTime());
        holder.tvComment.setText(obs.getComment() != null ? obs.getComment() : "");

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(obs);
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvObservation, tvTime, tvComment;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvObservation = itemView.findViewById(R.id.tvObs);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvComment = itemView.findViewById(R.id.tvComment);
            btnDelete = itemView.findViewById(R.id.btnDeleteObs);
        }
    }
}
