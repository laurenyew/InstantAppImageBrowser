package laurenyew.imagebrowser.browser.contracts

import android.content.Context

interface ImageDetailContract {
    interface View {
        fun onDetailLoaded(title: String)
    }

    interface Presenter {
        fun onBind(view: View, itemId: String?, context: Context?)

        fun unBind()
    }
}