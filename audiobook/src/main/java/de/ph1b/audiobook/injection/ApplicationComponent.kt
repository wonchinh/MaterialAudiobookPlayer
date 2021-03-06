package de.ph1b.audiobook.injection

import android.content.Context
import dagger.Component
import de.ph1b.audiobook.features.BaseActivity
import de.ph1b.audiobook.features.MainActivity
import de.ph1b.audiobook.features.bookOverview.*
import de.ph1b.audiobook.features.bookPlaying.BookPlayController
import de.ph1b.audiobook.features.bookPlaying.JumpToPositionDialogFragment
import de.ph1b.audiobook.features.bookPlaying.SeekDialogFragment
import de.ph1b.audiobook.features.bookPlaying.SleepTimerDialogFragment
import de.ph1b.audiobook.features.bookmarks.BookmarkDialogFragment
import de.ph1b.audiobook.features.folderChooser.FolderChooserActivity
import de.ph1b.audiobook.features.folderChooser.FolderChooserPresenter
import de.ph1b.audiobook.features.folderOverview.FolderOverviewPresenter
import de.ph1b.audiobook.features.imagepicker.ImagePickerController
import de.ph1b.audiobook.features.settings.SettingsController
import de.ph1b.audiobook.features.settings.dialogs.AutoRewindDialogFragment
import de.ph1b.audiobook.features.settings.dialogs.PlaybackSpeedDialogFragment
import de.ph1b.audiobook.features.settings.dialogs.ThemePickerDialogFragment
import de.ph1b.audiobook.features.widget.WidgetUpdateService
import de.ph1b.audiobook.persistence.PrefsManager
import de.ph1b.audiobook.playback.PlaybackService
import de.ph1b.audiobook.playback.utils.ChangeNotifier
import de.ph1b.audiobook.uitools.CoverReplacement
import javax.inject.Singleton

/**
 * Base component that is the entry point for injection.
 *
 * @author Paul Woitaschek
 */
@Singleton
@Component(modules = arrayOf(BaseModule::class, AndroidModule::class, PrefsModule::class))
interface ApplicationComponent {

  val bookShelfPresenter: BookShelfPresenter
  val context: Context
  val prefsManager: PrefsManager

  fun inject(target: App)
  fun inject(target: AutoRewindDialogFragment)
  fun inject(target: BaseActivity)
  fun inject(target: PlaybackService)
  fun inject(target: MainActivity)
  fun inject(target: BookShelfAdapter)
  fun inject(target: BookmarkDialogFragment)
  fun inject(target: BookPlayController)
  fun inject(target: BookShelfController)
  fun inject(target: ChangeNotifier)
  fun inject(target: CoverReplacement)
  fun inject(target: SettingsController)
  fun inject(target: EditBookTitleDialogFragment)
  fun inject(target: EditBookBottomSheet)
  fun inject(target: EditCoverDialogFragment)
  fun inject(target: FolderChooserActivity)
  fun inject(target: FolderChooserPresenter)
  fun inject(target: FolderOverviewPresenter)
  fun inject(target: ImagePickerController)
  fun inject(target: JumpToPositionDialogFragment)
  fun inject(target: PlaybackSpeedDialogFragment)
  fun inject(target: SeekDialogFragment)
  fun inject(target: SleepTimerDialogFragment)
  fun inject(target: ThemePickerDialogFragment)
  fun inject(target: WidgetUpdateService)
}