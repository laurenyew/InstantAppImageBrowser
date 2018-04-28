package laurenyew.imagebrowser.browser.adapters

import android.support.v7.util.DiffUtil
import android.view.View
import com.nhaarman.mockito_kotlin.*
import laurenyew.imagebrowser.base.BuildConfig
import laurenyew.imagebrowser.browser.adapters.data.ImagePreviewDataWrapper
import laurenyew.imagebrowser.browser.adapters.viewHolder.ImagePreviewViewHolder
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageBrowserRecyclerViewAdapterTest {
    private val mockPresenter: ImageBrowserContract.Presenter = mock()
    private lateinit var adapter: ImageBrowserRecyclerViewAdapter
    private val testList = ArrayList<ImagePreviewDataWrapper>()
    private val testList1 = ArrayList<ImagePreviewDataWrapper>()
    private val testList2 = ArrayList<ImagePreviewDataWrapper>()

    @Before
    fun setup() {
        adapter = spy(ImageBrowserRecyclerViewAdapter(mockPresenter))
    }

    //region Test Update Data
    @Test
    fun `given adapter updates the data, the data diff callback will be used`() {
        /** Exercise **/
        adapter.updateData(testList)

        /** Verify **/
        verify(adapter).createDataDiffCallback(argThat { size == 0 }, argThat { equals(testList) })
    }

    @Test
    fun `given adapter updates the data, update data internal will be called`() {
        /** Exercise **/
        adapter.updateData(testList)

        /** Verify **/
        verify(adapter).updateDataInternal(testList)
    }

    @Test
    fun `given data is applied to adapter, diff result will dispatch updates to the adapter`() {
        /** Arrange **/
        val mockDiffResult: DiffUtil.DiffResult = mock()

        /** Exercise **/
        adapter.applyDataDiffResult(testList, mockDiffResult)

        /** Verify **/
        verify(mockDiffResult).dispatchUpdatesTo(adapter)
    }

    @Test
    fun `given multiple updates before an update finishes, the adapter will only run the first update, and queue the rest`() {
        /** Exercise **/
        adapter.updateData(testList)
        adapter.updateData(testList1)
        adapter.updateData(testList2)

        /** Verify **/
        //Adapter should only be called once. The other parts should be queued
        verify(adapter).updateDataInternal(any())
        assertEquals(3, adapter.pendingDataUpdates.size)
        assertEquals(testList, adapter.pendingDataUpdates.first)
        assertEquals(testList2, adapter.pendingDataUpdates.last)
    }

    @Test
    fun `given pending updates, on applying result, adapter will only take latest update and clear the rest`() {
        /** Arrange **/
        adapter.updateData(testList)
        adapter.updateData(testList1)
        adapter.updateData(testList2)

        /** Exercise **/
        adapter.applyDataDiffResult(testList, mock())

        /** Verify **/
        verify(adapter, times(2)).updateDataInternal(any())
        assertEquals(0, adapter.pendingDataUpdates.size)
    }
    //endregion

    //region Test Adapter Views
    fun `given onBindView, adapter should create a click listener on the view`() {
        /** Arrange **/
        val mockView: View = mock()
        val mockViewHolder = ImagePreviewViewHolder(mockView)

        /** Exercise **/
        adapter.onBindViewHolder(mockViewHolder, 0)

        /** Verify **/
        verify(mockView).setOnClickListener(any())
    }
    //endregion
}