import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.testcodequest.data.response.Avatar
import com.dicoding.testcodequest.databinding.ShopRowBinding
import com.dicoding.testcodequest.utils.setImageUrl
import jp.wasabeef.glide.transformations.GrayscaleTransformation

class AvatarAdapter(private val listAvatar: ArrayList<Avatar>, private val ownedAvatars: Set<Int>) : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val maxItems = 21

    // Mengatur daftar avatar dan membatasi jumlah item yang ditampilkan
    fun setList(avatar: List<Avatar>) {
        listAvatar.clear()
        listAvatar.addAll(avatar.take(maxItems))
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = ShopRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view, onItemClickCallback, ownedAvatars)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listAvatar[position].let { viewHolder.bind(it) }
    }

    override fun getItemCount(): Int {
        return listAvatar.size
    }

    class ViewHolder(
        private var binding: ShopRowBinding,
        private val onItemClickCallback: OnItemClickCallback,
        private val ownedAvatars: Set<Int>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatar: Avatar) {
            binding.tvPrice.text = avatar.priceAvatar.toString()

            // Menggunakan Glide untuk memuat gambar dengan transformasi grayscale jika avatar dimiliki
            val requestOptions = RequestOptions().apply {
                if (ownedAvatars.contains(avatar.avatarId)) {
                    transform(GrayscaleTransformation())
                }
            }

            Glide.with(binding.imageAvatar.context)
                .load(avatar.imageAvatar) // Pastikan imageAvatar adalah URL atau sumber daya gambar yang valid
                .apply(requestOptions)
                .into(binding.imageAvatar)

            // Check if avatar is owned
            if (ownedAvatars.contains(avatar.avatarId)) {
                Log.d("AvatarAdapter", "Avatar ID: ${avatar.avatarId} is owned and displayed in grayscale")
                itemView.isClickable = false
                itemView.isEnabled = false
            } else {
                Log.d("AvatarAdapter", "Avatar ID: ${avatar.avatarId} is not owned and displayed normally")
                itemView.isClickable = true
            }

            itemView.setOnClickListener {
                if (itemView.isClickable) {
                    onItemClickCallback.onItemClicked(avatar)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Avatar)
    }
}