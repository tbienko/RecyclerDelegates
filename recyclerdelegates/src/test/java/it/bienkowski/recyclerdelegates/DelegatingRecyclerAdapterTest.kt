package it.bienkowski.recyclerdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DelegatingRecyclerAdapterTest {

    private val managerMock: RecyclerDelegateManager<String> = mockk(relaxed = true)

    private val holderMock: RecyclerView.ViewHolder = mockk(relaxed = true)

    @Before
    fun before() {
        mockkStatic(DiffUtil::class)
        every { DiffUtil.calculateDiff(any()) } returns mockk(relaxed = true)
    }

    @Test
    fun getItemCount() {
        val adapter = createAdapterWithItems(emptyList())
        assertEquals(0, adapter.itemCount)

        adapter.submitList(listOf("test"))
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun onBindViewHolder() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onBindViewHolder(holderMock, 0)

        verify { managerMock.onBindViewHolder(holderMock, "test", emptyList()) }
    }

    @Test
    fun onBindViewHolderWithPayload() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onBindViewHolder(holderMock, 0, listOf(1))

        verify { managerMock.onBindViewHolder(holderMock, "test", listOf(1)) }
    }

    @Test
    fun onCreateViewHolder() {
        val adapter = createAdapterWithItems(listOf("test"))

        val parentMock = mockk<ViewGroup>()
        adapter.onCreateViewHolder(parentMock, 1)

        verify { managerMock.onCreateViewHolder(parentMock, 1) }
    }

    @Test
    fun testGetItemViewType() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.getItemViewType(0)

        verify { managerMock.getItemViewType("test") }
    }

    @Test
    fun onViewAttachedToWindow() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onViewAttachedToWindow(holderMock)

        verify { managerMock.onViewAttachedToWindow(holderMock) }
    }

    @Test
    fun onViewDetachedFromWindow() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onViewDetachedFromWindow(holderMock)

        verify { managerMock.onViewDetachedFromWindow(holderMock) }
    }

    @Test
    fun onViewRecycled() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onViewRecycled(holderMock)

        verify { managerMock.onViewRecycled(holderMock) }
    }

    @Test
    fun onFailedToRecycleView() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.onFailedToRecycleView(holderMock)

        verify { managerMock.onFailedToRecycleView(holderMock) }
    }

    @Test
    fun getItemId() {
        val adapter = createAdapterWithItems(listOf("test"))

        adapter.getItemId(0)

        verify { managerMock.getItemId("test") }
    }

    private fun createAdapterWithItems(items: List<String>) =
        DelegatingRecyclerAdapter(managerMock).apply {
            submitList(items)
        }
}