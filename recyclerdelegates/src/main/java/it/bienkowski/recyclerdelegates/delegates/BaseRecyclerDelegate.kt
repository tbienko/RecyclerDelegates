package it.bienkowski.recyclerdelegates.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import it.bienkowski.recyclerdelegates.RecyclerDelegate

/**
 * Base class for delegates. It saves some boilerplate
 * @param itemClass class which is used to match delegate to item
 * @param layoutResId layout to inflate
 */
abstract class BaseRecyclerDelegate<in I, VH : RecyclerView.ViewHolder>(
    private val itemClass: Class<I>,
    @LayoutRes private val layoutResId: Int
) : RecyclerDelegate<I, VH> {

    abstract fun createViewHolder(view: View): VH

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return createViewHolder(view)
    }

    override fun isForItem(item: Any) = itemClass.isAssignableFrom(item.javaClass)
}