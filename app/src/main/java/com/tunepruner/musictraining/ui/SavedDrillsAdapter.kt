import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musictraining.R
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.model.music.drill.Drill

class SavedDrillsAdapter(
    private val drillList: ArrayList<Drill>,
    private val clickListener: OnClickListener
) :
    RecyclerView.Adapter<SavedDrillsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ssid: TextView = view.findViewById(R.id.ssid) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        holder.ssid.text = drillList[position].id
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, drillList[position])
        }
    }

    override fun getItemCount(): Int {
        return drillList.size
    }

    // update your data
    fun updateData(result: ArrayList<Drill>) {
        drillList.clear()
        drillList.addAll(result)
        notifyDataSetChanged()
    }
}

interface OnClickListener{
    fun onClick(view: View, drill: Drill)
}