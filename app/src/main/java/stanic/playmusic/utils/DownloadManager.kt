package stanic.playmusic.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_youtube_viewer.view.*
import java.io.File

class DownloadManager(
    val view: View,
    private val activity: Activity
) {

    fun downloadMusic(path: String, link: String) {
        val directory = File(path, "/PlayMusic")
        val request = YoutubeDLRequest(link)
        request.addOption("-o", directory.absolutePath + "/%(title)s.mp3")

        Observable.fromCallable {
            YoutubeDL.getInstance().execute(request) { progress, _ ->
                activity.runOnUiThread {
                    view.download_percent.text = "${progress}%"
                    view.progressBar_download.progress = progress.toInt()
                    view.progressBar_download_seek.progress = progress.toInt()
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view.download_percent.text = "0.0%"
                    view.progressBar_download.progress = 0
                    view.progressBar_download_seek.progress = 0

                    view.download_percent.visibility = View.GONE
                    view.progressBar_download_seek.visibility = View.GONE
                    view.progressBar_download.visibility = View.GONE

                    view.download_button.visibility = View.VISIBLE

                    Toast.makeText(view.context, "Download completo", Toast.LENGTH_SHORT).show()
                }
            ) {
                view.download_percent.text = "0.0%"
                view.progressBar_download.progress = 0
                view.progressBar_download_seek.progress = 0

                view.download_percent.visibility = View.GONE
                view.progressBar_download_seek.visibility = View.GONE
                view.progressBar_download.visibility = View.GONE

                view.download_button.visibility = View.VISIBLE

                Toast.makeText(view.context, "Ocorreu um erro ao fazer o download", Toast.LENGTH_SHORT)
                    .show()
                println(it.message)
                it.printStackTrace()
            }
    }

}