package de.ph1b.audiobook.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.bluelinelabs.conductor.*
import de.ph1b.audiobook.R
import de.ph1b.audiobook.features.bookOverview.BookShelfController
import de.ph1b.audiobook.features.bookOverview.NoFolderWarningDialogFragment
import de.ph1b.audiobook.features.bookPlaying.BookPlayController
import de.ph1b.audiobook.features.folderOverview.FolderOverviewController
import de.ph1b.audiobook.features.tracking.Tracker
import de.ph1b.audiobook.injection.App
import de.ph1b.audiobook.misc.PermissionHelper
import de.ph1b.audiobook.misc.RouterProvider
import de.ph1b.audiobook.misc.onControllerChanged
import de.ph1b.audiobook.misc.value
import de.ph1b.audiobook.persistence.PrefsManager
import java.io.File
import javax.inject.Inject


/**
 * Activity that coordinates the book shelf and play screens.
 *
 * @author Paul Woitaschek
 */
class MainActivity : BaseActivity(), NoFolderWarningDialogFragment.Callback, RouterProvider {

  @Inject lateinit var prefs: PrefsManager
  @Inject lateinit var permissionHelper: PermissionHelper
  @Inject lateinit var tracker: Tracker

  private lateinit var router: Router

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_book)
    App.component.inject(this)

    val root = findViewById(R.id.root) as ViewGroup
    router = Conductor.attachRouter(this, root, savedInstanceState)
    if (!router.hasRootController()) {
      val rootTransaction = RouterTransaction.with(BookShelfController())
      router.setRoot(rootTransaction)
    }
    router.addChangeListener(object : ControllerChangeHandler.ControllerChangeListener {
      override fun onChangeStarted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
        from?.setOptionsMenuHidden(true)
      }

      override fun onChangeCompleted(to: Controller?, from: Controller?, isPush: Boolean, container: ViewGroup, handler: ControllerChangeHandler) {
        from?.setOptionsMenuHidden(false)
      }
    })
    // track upon changes
    router.onControllerChanged { tracker.track(it) }

    if (savedInstanceState == null) {
      if (intent.hasExtra(NI_MALFORMED_FILE)) {
        val malformedFile = intent.getSerializableExtra(NI_MALFORMED_FILE) as File
        MaterialDialog.Builder(this).title(R.string.mal_file_title)
          .content(getString(R.string.mal_file_message) + "\n\n" + malformedFile)
          .show()
      }
      if (intent.hasExtra(NI_GO_TO_BOOK)) {
        val bookId = intent.getLongExtra(NI_GO_TO_BOOK, -1)
        router.pushController(RouterTransaction.with(BookPlayController.newInstance(bookId)))
      }
    }
  }

  override fun provideRouter() = router

  override fun onStart() {
    super.onStart()

    val anyFolderSet = prefs.collectionFolders.value().size + prefs.singleBookFolders.value().size > 0
    if (anyFolderSet) {
      permissionHelper.storagePermission(this)
    }
  }

  override fun onBackPressed() {
    if (!router.handleBack()) super.onBackPressed()
  }

  companion object {
    private val NI_MALFORMED_FILE = "malformedFile"
    private val NI_GO_TO_BOOK = "niGotoBook"

    /** Returns an intent to start the activity with to inform the user that a certain file may be defect **/
    fun malformedFileIntent(c: Context, malformedFile: File) = Intent(c, MainActivity::class.java).apply {
      putExtra(NI_MALFORMED_FILE, malformedFile)
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }

    /** Returns an intent that lets you go directly to the playback screen for a certain book **/
    fun goToBookIntent(c: Context, bookId: Long) = Intent(c, MainActivity::class.java).apply {
      putExtra(NI_GO_TO_BOOK, bookId)
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
  }

  override fun onNoFolderWarningConfirmed() {
    router.pushController(RouterTransaction.with(FolderOverviewController()))
  }
}
