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

    fun setList (avatar: List<Avatar>){
        listAvatar.clear()
        listAvatar.addAll(avatar.take(maxItems))
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            ShopRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listAvatar[position].let { viewHolder.bind(it) }

//        viewHolder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listStory[viewHolder.adapterPosition])
//        }
    }

    override fun getItemCount(): Int {
        return listAvatar.size
    }
    class ViewHolder( private var binding: ShopRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listAvatar: Avatar) {
            binding.tvPrice.text = listAvatar.priceAvatar.toString()
            binding.imageAvatar.setImageUrl(listAvatar.imageAvatar)


        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Avatar)
    }
}


//package com.dicoding.testcodequest.ui.Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.dicoding.testcodequest.data.response.Avatar
//import com.dicoding.testcodequest.databinding.ShopRowBinding
//import com.dicoding.testcodequest.utils.setImageUrl
//
//class AvatarAdapter : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {
//
//    private val differCallback = object : DiffUtil.ItemCallback<Avatar>(){
//        override fun areItemsTheSame(oldItem: Avatar, newItem: Avatar): Boolean =
//            oldItem == newItem
//
//        override fun areContentsTheSame(oldItem: Avatar, newItem: Avatar): Boolean =
//            oldItem == newItem
//    }
//
//    private val listDiffer = AsyncListDiffer(this, differCallback)
//    var setData: List<Avatar>
//        get() = listDiffer.currentList
//        set(value) = listDiffer.submitList(value)
//
//    private var listener: ((Avatar) -> Unit)? = null
//    fun setOnItemClick(listener: ((Avatar) -> Unit)?){
//        this.listener = listener
//    }
//
//    private var listenerDetail: ((Avatar) -> Unit)? = null
//    fun setOnDetailItemClick(listener: ((Avatar) -> Unit)?){
//        this.listenerDetail = listener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ShopRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(setData[position], listener, listenerDetail)
//    }
//
//    override fun getItemCount(): Int = setData.size
//
//    inner class ViewHolder(private val binding: ShopRowBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(data: Avatar, listener: ((Avatar) -> Unit)?, listenerDetail: ((Avatar) -> Unit)?){
//            with(binding){
//                tvPrice.text = data.priceAvatar.toString()
//                imageAvatar.setImageUrl(data.imageAvatar.toString())
//
//
//
//            }
//        }
//    }
//
//}