package laurenyew.imagebrowser.base.featureManagers

import laurenyew.imagebrowser.base.BuildConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Verify the logic of the FeatureModuleMangerList
 *
 * Should be able to be searched for a saved feature module manager based on the
 * interface that it implements
 * Priority is FIFO
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class FeatureModuleManagerListTest {
    private val testModule1 = MyFeatureModule1()
    private val testModule2 = MyFeatureModule2()
    private val testModule3 = MyFeatureModule3()

    @Before
    fun setup() {
        FeatureModuleManagerList.clear()
    }

    @Test
    fun `given empty feature module manager list, getting any module should return null`() {
        /** Exercise **/
        val module = FeatureModuleManagerList.getFeatureModuleManager<TestInterface1>(TestInterface1::class.java)

        /** Verify **/
        val managers = FeatureModuleManagerList.managers
        assertNotNull(managers)
        assertEquals(0, managers.size)
        assertNull("Should not be able to find a module that doesn't exist", module)
    }

    @Test
    fun `given search for interface in list, should get the class from list`() {
        /** Arrange **/
        FeatureModuleManagerList.addFeatureModuleManager(testModule2)
        FeatureModuleManagerList.addFeatureModuleManager(testModule1)

        /** Exercise **/
        val module = FeatureModuleManagerList.getFeatureModuleManager<TestInterface1>(TestInterface1::class.java)

        /** Verify **/
        val managers = FeatureModuleManagerList.managers
        assertNotNull(managers)
        assertEquals(2, managers.size)
        assertNotNull("Should not be able to find a module that doesn't exist", module)
        assertEquals(testModule1, module)
    }

    @Test
    fun `given search for class in list, should get the class from list`() {
        /** Arrange **/
        FeatureModuleManagerList.addFeatureModuleManager(testModule2)
        FeatureModuleManagerList.addFeatureModuleManager(testModule1)

        /** Exercise **/
        val module = FeatureModuleManagerList.getFeatureModuleManager<MyFeatureModule1>(MyFeatureModule1::class.java)

        /** Verify **/
        val managers = FeatureModuleManagerList.managers
        assertNotNull(managers)
        assertEquals(2, managers.size)
        assertNotNull("Should not be able to find a module that doesn't exist", module)
        assertEquals(testModule1, module)
    }

    @Test
    fun `given search for interface not in list, should get null from list`() {
        /** Arrange **/
        FeatureModuleManagerList.addFeatureModuleManager(testModule1)

        /** Exercise **/
        val module = FeatureModuleManagerList.getFeatureModuleManager<TestInterface2>(TestInterface2::class.java)

        /** Verify **/
        val managers = FeatureModuleManagerList.managers
        assertNotNull(managers)
        assertEquals(1, managers.size)
        assertNull("Should not be able to find a module that doesn't exist", module)
    }


    @Test
    fun `given search for interface, priority is FIFO`() {
        /** Arrange **/
        FeatureModuleManagerList.addFeatureModuleManager(testModule1)
        FeatureModuleManagerList.addFeatureModuleManager(testModule2)
        FeatureModuleManagerList.addFeatureModuleManager(testModule3)

        /** Exercise **/
        val module = FeatureModuleManagerList.getFeatureModuleManager<TestInterface1>(TestInterface1::class.java)

        /** Verify **/
        val managers = FeatureModuleManagerList.managers
        assertNotNull(managers)
        assertEquals(3, managers.size)
        assertNotNull("Should not be able to find a module that doesn't exist", module)
        assertEquals(testModule1, module)
    }

    //region Helper Classes
    class MyFeatureModule1 : FeatureModuleManager(), TestInterface1

    class MyFeatureModule2 : FeatureModuleManager(), TestInterface2
    class MyFeatureModule3 : FeatureModuleManager(), TestInterface1

    interface TestInterface1
    interface TestInterface2
    //endregion
}