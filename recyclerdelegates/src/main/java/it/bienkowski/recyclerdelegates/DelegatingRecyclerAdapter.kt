package it.bienkowski.recyclerdelegates

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * [RecyclerView.Adapter] which delegate operations to [RecyclerDelegateManager]
 */
class DelegatingRecyclerAdapter<I : Any>(
    private val manager: RecyclerDelegateManager<I>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items: MutableList<I> = mutableListOf()

    fun submitList(newItems: List<I>) {
        DiffUtil.calculateDiff(DelegatingDiffUtilCallback(manager, items, newItems)).dispatchUpdatesTo(this)
        items.clear()
        items.addAll(newItems)
    }

    @Deprecated("use submitList instead")
    fun submitItems(vararg items: I) = submitList(items.toList())

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        manager.onBindViewHolder(holder, items[position], emptyList())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        manager.onBindViewHolder(holder, items[position], payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return manager.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return manager.getItemViewType(items[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        manager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        manager.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        manager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return manager.onFailedToRecycleView(holder)
    }

    override fun getItemId(position: Int): Long {
        return manager.getItemId(items[position])
    }
}