package laurenyew.imagebrowser.browser.adapters.data

import laurenyew.imagebrowser.base.BuildConfig
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ImageDiffCallbackTest {
    private lateinit var diffCallback: ImageDiffCallback
    private lateinit var oldList: ArrayList<ImagePreviewDataWrapper>
    private lateinit var newList: ArrayList<ImagePreviewDataWrapper>
    private val itemA = ImagePreviewDataWrapper("A", "urlA", "titleA")
    private val itemAv2 = ImagePreviewDataWrapper("A", "url2A", "title2A")
    private val itemCwithAContent = ImagePreviewDataWrapper("C", "urlA", "titleA")
    private val itemB = ImagePreviewDataWrapper("B", "urlB", "titleB")

    @Before
    fun setup() {
        oldList = ArrayList()
        newList = ArrayList()
    }

    @Test
    fun `given two items are the same, diffCallback are items same should return true`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemA)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areItemsTheSame(0, 0)

        /** Verify **/
        assertTrue(result)
    }

    @Test
    fun `given two items are the same, diffCallback are contents same should return true`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemA)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areContentsTheSame(0, 0)

        /** Verify **/
        assertTrue(result)
    }

    @Test
    fun `given two items are different, diffCallback are items same should return false`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemB)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areItemsTheSame(0, 0)

        /** Verify **/
        assertFalse(result)
    }

    @Test
    fun `given two items are different, diffCallback are contents same should return false`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemB)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areContentsTheSame(0, 0)

        /** Verify **/
        assertFalse(result)
    }

    @Test
    fun `given two items share the same id with different contents, diffCallback are items same should return true`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemAv2)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areItemsTheSame(0, 0)

        /** Verify **/
        assertTrue(result)
    }

    @Test
    fun `given two items share the same id with different contents, diffCallback are contents same should return false`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemAv2)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areContentsTheSame(0, 0)

        /** Verify **/
        assertFalse(result)
    }

    @Test
    fun `given two items share the same content with different ids, diffCallback are items same should return false`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemCwithAContent)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areItemsTheSame(0, 0)

        /** Verify **/
        assertFalse(result)
    }

    @Test
    fun `given two items share the same content with different ids, diffCallback are contents same should return true`() {
        /** Arrange **/
        oldList.add(itemA)
        newList.add(itemCwithAContent)
        diffCallback = ImageDiffCallback(oldList, newList)

        /** Exercise **/
        val result = diffCallback.areContentsTheSame(0, 0)

        /** Verify **/
        assertTrue(result)
    }
}