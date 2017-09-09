package io.github.feelfreelinux.wykopmobilny.ui.loginscreen

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface LoginScreenContract {
    interface View : BaseView {
        fun goBackToSplashScreen()
    }

    interface Presenter : BasePresenter<View> {
        fun handleUrl(url : String)
    }
}