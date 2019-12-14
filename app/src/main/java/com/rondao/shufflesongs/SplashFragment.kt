package com.rondao.shufflesongs

import android.app.Application
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {

    val SPLASH_SCREEN_TIMEOUT = 2000L
    val handler = Handler()

    lateinit var startMainActivity: Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layoutRoot = inflater.inflate(R.layout.fragment_splash, container, false)

        startMainActivity = Runnable {
            this.findNavController().navigate(R.id.action_splashFragment_to_songsListFragment)
            (activity as AppCompatActivity).supportActionBar?.show()
        }

        return layoutRoot
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(startMainActivity, SPLASH_SCREEN_TIMEOUT)
    }

    override fun onStop() {
        handler.removeCallbacks(startMainActivity)
        super.onStop()
    }
}
