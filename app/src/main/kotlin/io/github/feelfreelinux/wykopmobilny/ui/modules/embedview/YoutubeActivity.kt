package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import io.github.feelfreelinux.wykopmobilny.GOOGLE_KEY
import io.github.feelfreelinux.wykopmobilny.R
import kotlinx.android.synthetic.main.activity_youtubeplayer.*
import java.util.regex.Pattern

class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    val extraURL by lazy { intent.getStringExtra(EXTRA_URL) }

    companion object {
        val REQUEST_CODE_INITIALIZATION_ERROR = 172
        val EXTRA_URL = "URLEXTRA"
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, YoutubeActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Slidr.attach(this, SlidrConfig.Builder().edge(true).build())
        setContentView(R.layout.activity_youtubeplayer)
        youtubePlayer.initialize(GOOGLE_KEY, this)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {
        if (result?.isUserRecoverableError == true) { // ¯\_(ツ)_/¯
            // Show error dialog to user, will try to initialize again after showing this
            result.getErrorDialog(this, REQUEST_CODE_INITIALIZATION_ERROR).show()
            return
        }

        Toast.makeText(this, result.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, restored: Boolean) {
        if (!restored) {
            player?.prepare()
            player?.loadVideo(extractVideoIdFromUrl(extraURL.replace("m.", "")))
        }
    }

    fun YouTubePlayer.prepare() {
        fullscreenControlFlags = YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
        setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
    }

    val youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
    val videoIdRegex = arrayOf("\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9_\\-]*)")

    fun extractVideoIdFromUrl(url: String): String? {
        val youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url)

        for (regex in videoIdRegex) {
            val compiledPattern = Pattern.compile(regex)
            val matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain)

            if (matcher.find()) {
                return matcher.group(1)
            }
        }

        return null
    }

    private fun youTubeLinkWithoutProtocolAndDomain(fullurl: String): String {
        val url = fullurl.replace("m.", "")
        val compiledPattern = Pattern.compile(youTubeUrlRegEx)
        val matcher = compiledPattern.matcher(url)

        return if (matcher.find()) {
            url.replace(matcher.group(), "")
        } else url
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_INITIALIZATION_ERROR) {
            youtubePlayer.initialize(GOOGLE_KEY, this)
        }
    }
}