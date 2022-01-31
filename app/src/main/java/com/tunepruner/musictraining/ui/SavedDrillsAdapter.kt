import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musictraining.R
import com.tunepruner.musictraining.model.music.drill.ChordDrill
import com.tunepruner.musictraining.repositories.LOG_TAG

class SavedDrillsAdapter(private val drillList: Array<ChordDrill>) :
    RecyclerView.Adapter<SavedDrillsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ssid: TextView = view.findViewById(R.id.ssid) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        Log.i(LOG_TAG, "onCreateViewHolder: ")
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(LOG_TAG, "onBindViewHolder: ")
        //set values
        holder.ssid.text = drillList[position].id
        Log.i(LOG_TAG, "${drillList[position].id}")
    }

    override fun getItemCount(): Int {
        Log.i(LOG_TAG, "getItemCount: ")
        return drillList.size
    }

    // update your data
    fun updateData(result: ArrayList<ChordDrill>) {
        Log.i(LOG_TAG, "updateData: ")
//        drillList.clear()
//        drillList.addAll(result)
        notifyDataSetChanged()
    }
}

