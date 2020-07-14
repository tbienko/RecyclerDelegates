package it.bienkowski.recyclerdelegates

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class DelegatingDiffUtilCallbackTest {

    private val oldList = listOf(1, 2)
    private val newList = listOf(3, 1)
    private val managerMock: RecyclerDelegateManager<Int> = mockk(relaxed = true)
    private val callback = DelegatingDiffUtilCallback(managerMock, oldList, newList)

    @Test
    fun getOldListSize() {
        assertEquals(2, callback.oldListSize)
    }

    @Test
    fun getNewListSize() {
        assertEquals(2, callback.newListSize)
    }

    @Test
    fun areItemsTheSame() {
        callback.areItemsTheSame(0, 1)
        verify { managerMock.areItemsTheSame(1, 1) }
    }

    @Test
    fun areContentsTheSame() {
        callback.areItemsTheSame(1, 0)
        verify { managerMock.areItemsTheSame(2, 3) }
    }

    @Test
    fun getChangePayload() {
        callback.getChangePayload(1, 0)
        verify { managerMock.getChangePayload(2, 3) }
    }
}