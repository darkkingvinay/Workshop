package my.vinay.workshop

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import my.vinay.workshop.databinding.ItemExerciseStatusBinding

class Exercisestatusadapter(val items: ArrayList<ExerciseModel>) :
    RecyclerView.Adapter<Exercisestatusadapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]
        holder.tvitem.text = model.getId()
        when{
            model.getIsSelected() ->{
                holder.tvitem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circulartheme)
                holder.tvitem.setTextColor(Color.parseColor("#212121"))

            }
            model.getIsCompleted() ->{
                holder.tvitem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.accentbackground)
                holder.tvitem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                holder.tvitem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circulargray)
                holder.tvitem.setTextColor(Color.parseColor("#212121"))
            }
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding:ItemExerciseStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvitem = binding.tvitem
    }
}