package it.bienkowski.recyclerdelegates


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.Adapter] which delegate operations to [RecyclerDelegateManager]
 */
class DelegatingRecyclerAdapter<I : Any>(
    private val manager: RecyclerDelegateManager<I>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var internalItems: List<I> = listOf()

    val items: List<I>
        get() = internalItems

    fun submitList(newItems: List<I>) {
        val diff = DiffUtil.calculateDiff(DelegatingDiffUtilCallback(manager, internalItems, newItems))
        internalItems = newItems
        diff.dispatchUpdatesTo(this)
    }

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