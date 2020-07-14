package it.bienkowski.recyclerdelegates.managers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.bienkowski.recyclerdelegates.RecyclerDelegate
import it.bienkowski.recyclerdelegates.RecyclerDelegateManager

/**
 * Simple [RecyclerDelegateManager] implementation
 */
class SimpleDelegateManager<in I : Any>(private val delegates: List<RecyclerDelegate<I, in RecyclerView.ViewHolder>>) :
    RecyclerDelegateManager<I> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return findDelegateByViewType(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: I, payloads: List<Any>) {
        findDelegateForItem(item).onBindViewHolder(holder, item, payloads)
    }

    override fun getItemViewType(item: I): Int {
        return delegates.indexOf(findDelegateForItem(item))
    }

    override fun getItemId(item: I): Long {
        return findDelegateForItem(item).getItemId(item)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        findDelegateByViewType(holder.itemViewType).onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        findDelegateByViewType(holder.itemViewType).onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        findDelegateByViewType(holder.itemViewType).onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return findDelegateByViewType(holder.itemViewType).onFailedToRecycleView(holder)
    }

    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean {
        return try {
            findDelegateForItem(oldItem).areItemsTheSame(oldItem, newItem)
        } catch (e: ClassCastException) { //class cast exception will occur when items have different types
            false
        }
    }

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean {
        return findDelegateForItem(oldItem).areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: I, newItem: I): Any? {
        return findDelegateForItem(oldItem).getChangePayload(oldItem, newItem)
    }

    private fun findDelegateForItem(item: I): RecyclerDelegate<I, RecyclerView.ViewHolder> {
        val delegate = delegates.firstOrNull { it.isForItem(item) }
        return delegate ?: throw IllegalArgumentException("No delegate for item: $item")
    }

    private fun findDelegateByViewType(viewType: Int): RecyclerDelegate<I, RecyclerView.ViewHolder> {
        try {
            return delegates[viewType]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw IllegalArgumentException("No delegate for viewType: $viewType")
        }
    }

    companion object {
        fun <I : Any> withDelegates(vararg delegates: RecyclerDelegate<*, *>): SimpleDelegateManager<I> {
            @Suppress("UNCHECKED_CAST")
            return SimpleDelegateManager(delegates.toList() as List<RecyclerDelegate<I, RecyclerView.ViewHolder>>)
        }
    }
}