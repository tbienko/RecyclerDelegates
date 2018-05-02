package it.bienkowski.recyclerdelegates.delegates

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.TextView

/**
 * Simple delegate which may be used to display static text layouts.
 *
 * Note that this will be slower than typical approach with fully functional holder.
 * It's good choice for prototyping and lists with limited item count.
 *
 * @param itemClass class which is used to match delegate to item
 * @param layoutResId layout to inflate
 * @param extractor function which extract data from item and returns map which holds view id-s as keys and texts as values
 */
class TextBindingDelegate<in I>(
    private val itemClass: Class<I>,
    @LayoutRes layoutResId: Int,
    private val extractor: (I) -> Map<Int, String>
) : BaseRecyclerDelegate<I, MinimalHolder>(itemClass, layoutResId) {

    override fun onBindViewHolder(holder: MinimalHolder, item: I, payloads: List<Any>) {
        for ((id, text) in extractor(item)) {
            //idea: cache findViewById results?
            holder.itemView.findViewById<TextView>(id).text = text
        }
    }
    override fun createViewHolder(view: View) = MinimalHolder(view)
}