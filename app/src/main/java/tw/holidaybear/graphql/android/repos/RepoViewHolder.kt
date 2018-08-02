package tw.holidaybear.graphql.android.repos

import android.support.v7.widget.RecyclerView
import tw.holidaybear.graphql.android.data.Repo
import tw.holidaybear.graphql.android.databinding.ItemRepoBinding

class RepoViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo) = with(itemView) {
        binding.repo = item
        binding.executePendingBindings()
    }
}