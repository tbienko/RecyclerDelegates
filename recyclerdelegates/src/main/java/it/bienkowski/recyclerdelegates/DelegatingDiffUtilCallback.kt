package it.bienkowski.recyclerdelegates

import androidx.recyclerview.widget.DiffUtil


/**
 * [DiffUtil.Callback] implementation comparing items using
 */
class DelegatingDiffUtilCallback<I>(
    private val manager: RecyclerDelegateManager<I>,
    private val oldList: List<I>,
    private val newList: List<I>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return manager.areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return manager.areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return manager.getChangePayload(oldItem, newItem)
    }
}