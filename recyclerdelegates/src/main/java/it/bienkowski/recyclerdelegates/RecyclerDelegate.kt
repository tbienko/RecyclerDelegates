package it.bienkowski.recyclerdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Delegated methods from [RecyclerView.Adapter], called from [RecyclerDelegateManager] implementation
 */
interface RecyclerDelegate<in I, VH : RecyclerView.ViewHolder> {

    /**
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    fun onCreateViewHolder(parent: ViewGroup): VH

    /**
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    fun onBindViewHolder(holder: VH, item: I, payloads: List<Any>)

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    fun onViewAttachedToWindow(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    fun onViewDetachedFromWindow(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onViewRecycled
     */
    fun onViewRecycled(holder: VH) {}

    /**
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    fun onFailedToRecycleView(holder: VH): Boolean {
        return false
    }

    /**
     * @see RecyclerView.Adapter.getItemId
     */
    fun getItemId(item: I): Long {
        return RecyclerView.NO_ID
    }

    /**
     * Return true if delegate is capable of item binding
     */
    fun isForItem(item: Any): Boolean

    /**
     * @see DiffUtil.Callback.areItemsTheSame
     */
    fun areItemsTheSame(oldItem: I, newItem: I): Boolean = oldItem == newItem

    /**
     * @see DiffUtil.Callback.areContentsTheSame
     */
    fun areContentsTheSame(oldItem: I, newItem: I): Boolean = oldItem == newItem

    /**
     * @see DiffUtil.Callback.getChangePayload
     */
    fun getChangePayload(oldItem: I, newItem: I): Any? = null
}