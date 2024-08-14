package it.bitprepared.prbm.mobile.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import it.bitprepared.prbm.mobile.R

/**
 * Activity responsible for showing an initial splash screen.
 */
class AppIntroActivity : AppIntro2() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    addSlide(AppIntroFragment.newInstance(
      "Benvenuto in PRBMM!",
      "Il Percorso Rettificato Belga Multimediale & Mobile",
      imageDrawable = R.drawable.ic_launcher_playstore,
    ))

    addSlide(AppIntroFragment.newInstance(
      "Menu principale",
      "Nel menu principale puoi scegliere se creare un nuovo percorso, o se visualizzare i percorsi salvati",
      imageDrawable = R.drawable.ic_launcher_playstore,
    ))

    addSlide(AppIntroFragment.newInstance(
      "Menu principale",
      "Nel menu principale puoi scegliere se creare un nuovo percorso, o se visualizzare i percorsi salvati",
      imageDrawable = R.drawable.ic_launcher_playstore,
    ))

//    addSlide(AppIntroFragment.newInstance(SliderPage(
//      "Gradients!",
//      "This text is written on a gradient background",
//      imageDrawable = R.drawable.ic_slide2,
//      backgroundDrawable = R.drawable.back_slide2,
//    )))
//
//    addSlide(AppIntroFragment.newInstance(
//      "Simple, yet Customizable",
//      "The library offers a lot of customization, while keeping it simple for those that like simple.",
//      imageDrawable = R.drawable.ic_slide3,
//      backgroundDrawable = R.drawable.back_slide3,
//      titleTypefaceFontRes = R.font.opensans_regular,
//      descriptionTypefaceFontRes = R.font.opensans_regular
//    ))
//
//    addSlide(AppIntroFragment.newInstance(
//      "Explore",
//      "Feel free to explore the rest of the library demo!",
//      imageDrawable = R.drawable.ic_slide4,
//      backgroundDrawable = R.drawable.back_slide4
//    ))
//
//    addSlide(AppIntroFragment.newInstance(
//      ":)",
//      "...gradients are awesome!",
//      imageDrawable = R.mipmap.ic_launcher,
//      backgroundDrawable = R.drawable.back_slide5
//    ))
    setTransformer(AppIntroPageTransformerType.Parallax())
  }

  public override fun onSkipPressed(currentFragment: Fragment?) {
    super.onSkipPressed(currentFragment)
    finish()
  }

  public override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    finish()
  }
}

