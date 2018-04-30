package laurenyew.imagebrowser.browser.helpers

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.picasso.Picasso
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManager
import laurenyew.imagebrowser.base.featureManagers.FeatureModuleManagerList
import laurenyew.imagebrowser.browser.adapters.ImageBrowserRecyclerViewAdapter
import laurenyew.imagebrowser.browser.contracts.ImageBrowserContract
import laurenyew.imagebrowser.browser.contracts.ImageBrowserFeatureModuleContract
import laurenyew.imagebrowser.browser.fragments.ImageBrowserFragment
import org.junit.After
import org.junit.Before
import org.robolectric.RuntimeEnvironment


/**
 *  Base Test class with reusable methods / variables
 */
open class ImageBrowserTestBase {
    val context: Context = RuntimeEnvironment.application.applicationContext
    val mockFeatureModule: MockImageBrowserFeatureModuleManager = mock()
    val mockPresenter: ImageBrowserContract.Presenter = mock()
    val mockAdapter = spy(ImageBrowserRecyclerViewAdapter(mockPresenter))

    @Before
    open fun setup() {
        whenever(mockFeatureModule.getImageBrowserPresenter()).thenReturn(mockPresenter)
        whenever(mockFeatureModule.getImageBrowserAdapter(any())).thenReturn(mockAdapter)
        FeatureModuleManagerList.addFeatureModuleManager(mockFeatureModule)
        //Setup up Picasso
        try {
            Picasso.get()
        } catch (e: IllegalStateException) {
            Picasso.setSingletonInstance(Picasso.Builder(context).build())
        }
    }

    @After
    fun teardown() {
        FeatureModuleManagerList.clear()
    }

    abstract class MockImageBrowserFeatureModuleManager : FeatureModuleManager(), ImageBrowserFeatureModuleContract.Views, ImageBrowserFeatureModuleContract.Activities, ImageBrowserFeatureModuleContract.Adapters, ImageBrowserFeatureModuleContract.Presenters
}