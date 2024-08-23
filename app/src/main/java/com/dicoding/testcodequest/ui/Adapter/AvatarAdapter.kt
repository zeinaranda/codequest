import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.databinding.ShopRowBinding
import com.dicoding.testcodequest.utils.setImageUrl

class AvatarAdapter(private val listAvatar: ArrayList<Avatar>) : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var binding: ShopRowBinding
    private val maxItems = 21

    // Mengatur daftar avatar dan membatasi jumlah item yang ditampilkan
    fun setList (avatar: List<Avatar>){
        listAvatar.clear()    // Menghapus semua item dari daftar
        listAvatar.addAll(avatar.take(maxItems))         // Menambahkan item yang diambil dari daftar avatar dengan batas maksimum
        notifyDataSetChanged()         // Memberitahu RecyclerView bahwa data telah diubah
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            ShopRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view, onItemClickCallback)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listAvatar[position].let { viewHolder.bind(it) }

        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listAvatar[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listAvatar.size
    }

    class ViewHolder(private var binding: ShopRowBinding, private val onItemClickCallback: OnItemClickCallback) :
        RecyclerView.ViewHolder(binding.root) {
        // menampilkan data avatar ke tampilan item
        fun bind(avatar: Avatar) {
            binding.tvPrice.text = avatar.priceAvatar.toString()
            avatar.imageAvatar?.let { binding.imageAvatar.setImageUrl(it) }
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(avatar)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Avatar)
    }
}


