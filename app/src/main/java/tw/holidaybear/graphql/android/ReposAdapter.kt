package tw.holidaybear.graphql.android

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tw.holidaybear.graphql.android.databinding.ItemRepoBinding
import java.util.*

class ReposAdapter(private val items: MutableList<Repo>) : RecyclerView.Adapter<RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(layoutInflater, parent,false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    fun clearItems() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun swapItems(newItems: List<Repo>?) {
        if (newItems == null) {
            val oldSize = items.size
            items.clear()
            notifyItemRangeRemoved(0, oldSize)
        } else {
            val result = DiffUtil.calculateDiff(ReposDiffCallback(items, newItems))
            items.clear()
            items.addAll(newItems)
            result.dispatchUpdatesTo(this)
        }
    }
}

class ReposDiffCallback(private val oldList: List<Repo>, private val newList: List<Repo>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldId = oldList[oldItemPosition].id
        val newId = newList[newItemPosition].id
        return Objects.equals(oldId, newId)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRepo = oldList[oldItemPosition]
        val newRepo = newList[newItemPosition]
        return Objects.equals(oldRepo, newRepo)
    }
}
