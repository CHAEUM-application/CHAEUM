import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.life.R
import com.example.life.YearActivity
import java.time.Year


class YearAdapter(
    private val birthYear: Int,
    private val years: IntArray = IntArray(90) { birthYear + it },
    private val context: Context
) : RecyclerView.Adapter<YearViewHolder>() {

    /*
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val currentYear = Year.now().value
        holder.yearView.setBackgroundColor(
            when {
                years[position] < currentYear -> Color.rgb(69, 219, 147)
                years[position] == currentYear -> {
                    holder.yearView.setOnClickListener {
                        val intent = Intent(context, YearActivity::class.java)
                        context.startActivity(intent)
                    }
                    Color.YELLOW
                }
                else -> Color.WHITE
            }
        )
    } */

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val currentYear = Year.now().value
        holder.yearView.setBackgroundColor(
            when {
                years[position] < currentYear -> {
                    holder.yearView.setOnClickListener {
                        val intent = Intent(context, YearActivity::class.java)
                        intent.putExtra("selectedYear", years[position]) // passing selected year to the YearActivity
                        context.startActivity(intent)
                    }
                    Color.rgb(69, 219, 147)
                }
                years[position] == currentYear -> {
                    holder.yearView.setOnClickListener {
                        val intent = Intent(context, YearActivity::class.java)
                        intent.putExtra("selectedYear", years[position]) // passing selected year to the YearActivity
                        context.startActivity(intent)
                    }
                    Color.YELLOW
                }
                else -> Color.WHITE
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_year, parent, false)
        return YearViewHolder(view)
    }

    override fun getItemCount(): Int = years.size
}

