package it.bienkowski.recyclerdelegates.delegates

import android.view.View
import androidx.annotation.LayoutRes

/**
 * Simple delegate which may be used to display static layouts
 *
 * @param itemClass class which is used to match delegate to item
 * @param layoutResId layout to inflate
 */
class StaticLayoutDelegate<in I : Any>(
    private val itemClass: Class<I>,
    @LayoutRes layoutResId: Int
) : BaseRecyclerDelegate<I, MinimalHolder>(itemClass, layoutResId) {
    override fun createViewHolder(view: View) = MinimalHolder(view)
    override fun onBindViewHolder(holder: MinimalHolder, item: I, payloads: List<Any>) = Unit
    override fun isForItem(item: Any) = itemClass.isAssignableFrom(item.javaClass)
}