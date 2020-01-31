package it.bienkowski.recyclerdelegates.demo

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import it.bienkowski.recyclerdelegates.DelegatingRecyclerAdapter

import it.bienkowski.recyclerdelegates.delegates.BaseRecyclerDelegate
import it.bienkowski.recyclerdelegates.delegates.StaticLayoutDelegate
import it.bienkowski.recyclerdelegates.managers.SimpleDelegateManager
import java.util.*

// We are creating simple "to do" app. Lets define data items
//
// NOTE: There's no need to have base item type (in this case TodoListItem) but using it
// (especially with sealed classes) enables better IDE support and code validation

sealed class TodoListItem

// This class represents typical list item
data class TodoItem(
    val uuid: UUID,
    val name: String,
    val completed: Boolean
) : TodoListItem()

// We would show empty view when user not added any TodoItems
// Typical application needs more items like this, for example you may add LoadingItem and ErrorItem
// but at the moment: keep it simple
class EmptyItem : TodoListItem()

// Delegate that we use to handle TodoItems
class TodoItemDelegate(private val listeners: Listeners) :

// We're using BaseRecyclerDelegate to avoid boilerplate code
    BaseRecyclerDelegate<TodoItem, TodoItemDelegate.Holder>(
        TodoItem::class.java,
        R.layout.item_todo
    ) {

    // Here we bind data & listeners
    override fun onBindViewHolder(holder: Holder, item: TodoItem, payloads: List<Any>) {
        holder.name.text = item.name
        holder.checkbox.setImageResource(getCheckBoxResource(item.completed))

        holder.name.setOnClickListener { listeners.onTodoItemTextClick(item) }
        holder.checkbox.setOnClickListener { listeners.onTodoItemCheckboxClick(item) }
    }

    private fun getCheckBoxResource(completed: Boolean) = when (completed) {
        true -> R.drawable.ic_check_box_black_24dp
        false -> R.drawable.ic_check_box_outline_blank_black_24dp
    }

    // Create holder using inflated layout
    override fun createViewHolder(view: View) = Holder(view)

    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    // We are using ImageButton instead CheckBox to achieve unidirectional data flow easier
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text)
        val checkbox: ImageButton = itemView.findViewById(R.id.checkbox)
    }

    // Listeners to pass user interactions
    interface Listeners {
        fun onTodoItemCheckboxClick(item: TodoItem)
        fun onTodoItemTextClick(item: TodoItem)
    }
}

class ToDoSampleActivity : AppCompatActivity(), TodoItemDelegate.Listeners {

    // View binding, nothing interesting actually
    private val recycler by lazy { findViewById<RecyclerView>(R.id.recycler) }
    private val fab by lazy { findViewById<FloatingActionButton>(R.id.fab) }

    // Manager is middle layer in our stack: it receives adapter calls and passes it to delegates.
    // We're using one of general purpose delegates that are provided by library
    private val manager = SimpleDelegateManager.withDelegates<TodoListItem>(
        TodoItemDelegate(this),
        StaticLayoutDelegate(EmptyItem::class.java, R.layout.item_empty)
    )

    // Manager needs only adapter, typically there is no need to write your own adapters
    private val adapter = DelegatingRecyclerAdapter(manager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        title = "ToDo Sample"

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener { addTodoItem() }

        // On start we show EmptyItem, modifyItems calls are passed through DiffUtil
        adapter.submitItems(EmptyItem())
    }

    // Menu to add "delete completed menu"
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.todo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_completed -> {
                deleteCompletedTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Helper function to get user input
    private fun showEditTextDialog(initialValue: String, callback: (String) -> Unit) {
        val editText = EditText(this).apply {
            setText(initialValue)
        }

        AlertDialog.Builder(this)
            .setView(editText)
            .setPositiveButton("SAVE", { _, _ ->
                callback(editText.text.toString())
            })
            .show()
    }

    // Next helper function to replace TodoItems on list.
    // We're using immutable data model and do not use positions so it's bit harder to do it
    private fun replaceItem(uuid: UUID, newItem: TodoItem) {
        adapter.submitList(
            adapter.items.toMutableList().apply {
                val index = indexOfFirst {
                    when (it) {
                        is TodoItem -> it.uuid == uuid
                        else -> false
                    }
                }
                set(index, newItem)
            }
        )
    }

    // There's how we add new items, thanks to helper functions it's quite easy
    private fun addTodoItem() {
        showEditTextDialog("") { name ->
            adapter.submitList(
                adapter.items.toMutableList().apply {
                    removeAll { it is EmptyItem }
                    add(TodoItem(UUID.randomUUID(), name, false))
                }
            )
        }
    }

    // Toggle completed state on click
    override fun onTodoItemCheckboxClick(item: TodoItem) {
        replaceItem(item.uuid, item.copy(completed = !item.completed))
    }

    // User can modify text label
    override fun onTodoItemTextClick(item: TodoItem) {
        showEditTextDialog(item.name) { newName ->
            replaceItem(item.uuid, item.copy(name = newName))
        }
    }

    // User can delete completed tasks
    private fun deleteCompletedTasks() {
        adapter.submitList(
            adapter.items.toMutableList().apply {
                val iterator = listIterator()
                for (item in iterator) {
                    if (item is TodoItem && item.completed) {
                        iterator.remove()
                    }
                }
                if (isEmpty()) {
                    add(EmptyItem())
                }
            }
        )
    }
}
