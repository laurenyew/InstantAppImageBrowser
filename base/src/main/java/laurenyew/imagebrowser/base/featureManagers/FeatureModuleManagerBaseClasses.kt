package laurenyew.imagebrowser.base.featureManagers

import android.support.annotation.VisibleForTesting

/**
 * Feature Module Manager List
 * Created to allow for easy extension of Views, Presenters, etc. for each feature
 * To switch them out, find the main activity for the feature, and just extend it and add your own
 * feature module manager rather than the default
 */
object FeatureModuleManagerController {
    private val managers: LinkedHashSet<FeatureModuleManager> = LinkedHashSet()

    @Suppress("UNCHECKED_CAST")
    fun <A> getFeatureModuleManager(clazz: Class<*>): A? {
        val manager = managers.firstOrNull {
            clazz.isAssignableFrom(it.javaClass)
        }
        return manager as A?
    }

    fun addFeatureModuleManager(manager: FeatureModuleManager) {
        managers.add(manager)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clear() {
        managers.clear()
    }

    @VisibleForTesting
    fun getManagersForTesting(): List<FeatureModuleManager> = managers.toList()
}

/**
 * Base FeatureModuleManager class that should be extended by
 * every FeatureModuleManager (used by [FeatureModuleManagerController])
 */
open class FeatureModuleManager