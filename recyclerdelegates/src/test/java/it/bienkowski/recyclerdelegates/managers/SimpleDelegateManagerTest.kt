package it.bienkowski.recyclerdelegates.managers

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import it.bienkowski.recyclerdelegates.RecyclerDelegate
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class SimpleDelegateManagerTest {

    private val delegate1Item: BigDecimal = BigDecimal.ZERO

    private val delegate2Item: String = "test"

    private val delegate1Mock: RecyclerDelegate<BigDecimal, RecyclerView.ViewHolder> =
        mockk(relaxed = true)

    private val delegate2Mock: RecyclerDelegate<String, RecyclerView.ViewHolder> =
        mockk(relaxed = true)

    private val parentMock: ViewGroup = mockk()

    private val holderMock: RecyclerView.ViewHolder = mockk(relaxed = true)

    @Suppress("UNCHECKED_CAST")
    private val manager: SimpleDelegateManager<Any> =
        SimpleDelegateManager(listOf(delegate1Mock, delegate2Mock)) as SimpleDelegateManager<Any>

    @Before
    fun before() {
        every { delegate1Mock.isForItem(delegate1Item) } returns true
        every { delegate1Mock.isForItem(delegate2Item) } returns false

        every { delegate2Mock.isForItem(delegate1Item) } returns false
        every { delegate2Mock.isForItem(delegate2Item) } returns true
    }

    @Test
    fun onCreateViewHolder() {
        manager.onCreateViewHolder(parentMock, viewTypeOf(delegate1Item))
        verify { delegate1Mock.onCreateViewHolder(parentMock) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun onCreateViewHolder_invalidViewType() {
        manager.onCreateViewHolder(parentMock, -1)
    }

    @Test
    fun onBindViewHolder() {
        val payloads = listOf(1)
        manager.onBindViewHolder(holderMock, delegate2Item, payloads)
        verify { delegate2Mock.onBindViewHolder(holderMock, delegate2Item, payloads) }
    }

    @Test
    fun getItemViewType() {
        assertEquals(0, viewTypeOf(delegate1Item))
        assertEquals(1, viewTypeOf(delegate2Item))
    }

    @Test(expected = IllegalArgumentException::class)
    fun getItemViewType_unknownItem() {
        assertEquals(1, viewTypeOf(false))
    }

    @Test
    fun getItemId() {
        manager.getItemId(delegate1Item)
        verify { delegate1Mock.getItemId(delegate1Item) }
    }

    @Test
    fun onViewAttachedToWindow() {
        manager.onViewAttachedToWindow(holderMock)
        verify { delegate1Mock.onViewAttachedToWindow(holderMock) }
    }

    @Test
    fun onViewDetachedFromWindow() {
        manager.onViewDetachedFromWindow(holderMock)
        verify { delegate1Mock.onViewDetachedFromWindow(holderMock) }
    }

    @Test
    fun onViewRecycled() {
        manager.onViewRecycled(holderMock)
        verify { delegate1Mock.onViewRecycled(holderMock) }
    }

    @Test
    fun onFailedToRecycleView() {
        manager.onFailedToRecycleView(holderMock)
        verify { delegate1Mock.onFailedToRecycleView(holderMock) }
    }

    @Test
    fun areItemsTheSame() {
        manager.areItemsTheSame(delegate1Item, delegate1Item)
        verify { delegate1Mock.areItemsTheSame(delegate1Item, delegate1Item) }
    }

    @Test
    fun areContentsTheSame() {
        manager.areContentsTheSame(delegate1Item, delegate1Item)
        verify { delegate1Mock.areContentsTheSame(delegate1Item, delegate1Item) }
    }

    @Test
    fun getChangePayload() {
        manager.getChangePayload(delegate1Item, delegate1Item)
        verify { delegate1Mock.getChangePayload(delegate1Item, delegate1Item) }
    }

    private fun viewTypeOf(item: Any) = manager.getItemViewType(item)
}