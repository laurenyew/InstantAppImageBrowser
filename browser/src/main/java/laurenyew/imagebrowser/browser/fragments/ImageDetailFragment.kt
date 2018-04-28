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
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract.View.Companion.ARG_ITEM_ID
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract.View.Companion.ARG_ITEM_IMAGE_TITLE
import laurenyew.imagebrowser.browser.contracts.ImageDetailContract.View.Companion.ARG_ITEM_IMAGE_URL

/**
 * Image Detail Screen
 * Contained in a [ImageBrowserActivity] in two-pane mode (on tablets)
 * or a [ImageDetailActivity] on handsets.
 *
 * Leaving without MVP b/c we already have all the data we need for this simple view
 */
open class ImageDetailFragment : Fragment(), ImageDetailContract.View {
    companion object {
        fun newInstance(itemId: String, imageUrl: String, itemTitle: String? = null): ImageDetailFragment {
            val fragment = ImageDetailFragment()
            val args = Bundle()
            args.putString(ARG_ITEM_ID, itemId)
            args.putString(ARG_ITEM_IMAGE_URL, imageUrl)
            if (itemTitle != null) {
                args.putString(ARG_ITEM_IMAGE_TITLE, itemTitle)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var itemId: String? = null
    private var itemImageUrl: String? = null
    private var itemTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemId = arguments?.getString(ARG_ITEM_ID)
        itemImageUrl = arguments?.getString(ARG_ITEM_IMAGE_URL)
        itemTitle = arguments?.getString(ARG_ITEM_IMAGE_TITLE)
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

        activity?.toolbar?.title = itemTitle
        detailImageTitleTextView.text = itemTitle
    }
}
