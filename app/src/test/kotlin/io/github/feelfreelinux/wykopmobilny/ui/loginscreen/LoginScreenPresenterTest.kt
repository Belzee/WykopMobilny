package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import org.junit.Before
import org.junit.Test

class LoginScreenPresenterTest {
    lateinit var systemUnderTest: LoginScreenPresenter
    val mockOfView = mock<LoginScreenContract.View>()
    val mockOfWykopApi = mock<WykopApi>()
    val mockOfApiPreferences = mock<IApiPreferences>()

    @Before
    fun setup() {
        systemUnderTest = LoginScreenPresenter(mockOfApiPreferences, mockOfWykopApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldSaveToken() {
        val expectedToken = "example_token"

        val url = "https://a.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/$expectedToken/"
        systemUnderTest.handleUrl(url)

        verify(mockOfApiPreferences).userKey = expectedToken
    }

    @Test
    fun shouldSaveLogin() {
        val expectedLogin = "example_login"
        val url = "https://a.wykop.pl/user/ConnectSuccess/appkey/example_key/login/$expectedLogin/token/example_token/"
        systemUnderTest.handleUrl(url)

        verify(mockOfApiPreferences).login = expectedLogin
    }

    @Test
    fun shouldShowErrorOnEmptyUrl() {
        systemUnderTest.handleUrl("")
        verify(mockOfView, times(1)).showErrorDialog(any())
        verifyNoMoreInteractions(mockOfApiPreferences)
    }

    @Test
    fun shouldExitActivityOnHandle() {
        val url = "https://a.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/example_token/"
        systemUnderTest.handleUrl(url)
        verify(mockOfView, times(1)).goBackToSplashScreen()
    }


}