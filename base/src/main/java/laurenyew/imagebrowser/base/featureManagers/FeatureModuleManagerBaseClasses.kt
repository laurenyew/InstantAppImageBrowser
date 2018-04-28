package laurenyew.imagebrowser.base.featureManagers

/**
 * Feature Module Manager List
 * Created to allow for easy extension of Views, Presenters, etc. for each feature
 * To switch them out, find the main activity for the feature, and just extend it and add your own
 * feature module manager rather than the default
 */
object FeatureModuleManagerList {
    private val managers: ArrayList<FeatureModuleManager> = ArrayList()

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

    fun removeFeatureModuleManager(manager: FeatureModuleManager) {
        managers.remove(manager)
    }
}

/**
 * Base FeatureModuleManager class that should be extended by
 * andy FeatureModuleManager
 */
open class FeatureModuleManager