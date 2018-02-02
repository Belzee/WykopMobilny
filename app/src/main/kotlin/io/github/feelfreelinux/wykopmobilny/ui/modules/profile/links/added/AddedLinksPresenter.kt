package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class AddedLinksPresenter(schedulers: Schedulers, profileApi: ProfileApi) : BasePresenter<AddedLinksView>()