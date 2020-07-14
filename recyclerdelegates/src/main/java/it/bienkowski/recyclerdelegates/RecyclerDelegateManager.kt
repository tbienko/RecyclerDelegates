package it.bienkowski.recyclerdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Bridge between [RecyclerView.Adapter] and [RecyclerDelegate], in general it contains
 * methods from [RecyclerView.Adapter] transformed to not have references to position in data set.
 * Additionally it has methods used to compare items using [DiffUtil.Callback]
 */
interface RecyclerDelegateManager<in I> {
    /**
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * Should be called from both versions of original onBindViewHolder
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: I, payloads: List<Any>)

    /**
     * @see RecyclerView.Adapter.getItemViewType
     */
    fun getItemViewType(item: I): Int

    /**
     * @see RecyclerView.Adapter.getItemId
     */
    fun getItemId(item: I): Long

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder)

    /**
     * @see RecyclerView.Adapter.onDetachedFromRecyclerView
     */
    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder)

    /**
     * @see RecyclerView.Adapter.onViewRecycled
     */
    fun onViewRecycled(holder: RecyclerView.ViewHolder)

    /**
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean

    /**
     * @see DiffUtil.Callback.areItemsTheSame
     */
    fun areItemsTheSame(oldItem: I, newItem: I): Boolean

    /**
     * @see DiffUtil.Callback.areContentsTheSame
     */
    fun areContentsTheSame(oldItem: I, newItem: I): Boolean

    /**
     * @see DiffUtil.Callback.getChangePayload
     */
    fun getChangePayload(oldItem: I, newItem: I): Any?
}