package laurenyew.imagebrowser.browser.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_detail_activity.*
import kotlinx.android.synthetic.main.image_detail_fragment.*
import laurenyew.imagebrowser.browser.R
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract
import laurenyew.imagebrowser.browser.presenters.ImageDetailPresenter

/**
 * Image Detail Screen
 * Contained in a [ImageBrowserActivity] in two-pane mode (on tablets)
 * or a [ImageDetailActivity] on handsets.
 */
open class ImageDetailFragment : Fragment(), ImageDetailContract.View {
    companion object {
        const val ARG_ITEM_ID = "item_id"
        const val ARG_ITEM_IMAGE_URL = "item_image_url"

        fun newInstance(itemId: String, imageUrl: String? = null): ImageDetailFragment {
            val fragment = ImageDetailFragment()
            val args = Bundle()
            args.putString(ARG_ITEM_ID, itemId)
            if (imageUrl != null) {
                args.putString(ARG_ITEM_IMAGE_URL, imageUrl)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var itemId: String? = null
    private var itemImageUrl: String? = null

    private var presenter: ImageDetailContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemId = arguments?.getString(ARG_ITEM_ID)
        itemImageUrl = arguments?.getString(ARG_ITEM_IMAGE_URL)

        presenter = ImageDetailPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.image_detail_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (itemImageUrl != null) {
            Picasso.get()
                    .load(itemImageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(detailImageView)
        }
        presenter?.onBind(this, itemId, context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.unBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }

    //region MVP
    /**
     * Load the image and the title
     */
    override fun onDetailLoaded(title: String) {
        activity?.toolbar?.title = title
        detailImageTitleTextView.text = title
    }
    //endregion
}
