package engassignment.figure1.com.feedof500px

import engassignment.figure1.com.feedof500px.models.Model
import org.junit.Test

import org.junit.Assert.*

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ModelUnitTest {

    private var photo: Model.Photo? = null
    private val id: Long = 10
    private val name: String = "Assignment"
    private val description: String = "The purpose of this test is to evaluate your technical skills when faced with an open-ended\n" +
            "engineering assignment"
    private val location: String = "Toronto"
    private val takenAt: String = "2018-01-16"
    private val images: MutableList<Model.Images> = ArrayList()
    private val user: Model.User = Model.User(21, "Eng")

    @Test
    fun testPhotoModel() {
        images.add(Model.Images("https://imageurl.com"))
        photo = Model.Photo(id, name, description, location, takenAt, images, user)
        assertNotNull(user)
        assertNotNull(images)
        assertNotNull(photo)
        assertEquals(user.id, 21)
        assertEquals(user.firstname, "Eng")
        assertEquals(images[0].https_url, "https://imageurl.com")
        assertEquals(photo?.id, 10.toLong())
        assertEquals(photo?.name, "Assignment")
        assertEquals(photo?.description, "The purpose of this test is to evaluate your technical skills when faced with an open-ended\n" +
                "engineering assignment")
        assertEquals(photo?.location, "Toronto")
        assertEquals(photo?.taken_at, "2018-01-16")
        assertEquals(photo?.images, images)
        assertEquals(photo?.user, user)
    }
}
