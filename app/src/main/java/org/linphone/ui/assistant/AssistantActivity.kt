/*
 * Copyright (c) 2010-2023 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.ui.assistant

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlin.math.max
import org.linphone.R
import org.linphone.compatibility.Compatibility
import org.linphone.core.tools.Log
import org.linphone.databinding.AssistantActivityBinding
import org.linphone.ui.GenericActivity
import org.linphone.ui.assistant.fragment.PermissionsFragmentDirections

@UiThread
class AssistantActivity : GenericActivity() {
    companion object {
        private const val TAG = "[Assistant Activity]"

        const val SKIP_LANDING_EXTRA = "SkipLandingIfAtLeastAnAccount"
    }

    private lateinit var binding: AssistantActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.assistant_activity)
        binding.lifecycleOwner = this
        setUpToastsArea(binding.toastsArea)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            v.updatePadding(
                insets.left,
                insets.top,
                insets.right,
                max(insets.bottom, keyboard.bottom)
            )
            WindowInsetsCompat.CONSUMED
        }

        // Desactivar completamente el botón atrás y gestos para evitar que el usuario
        // navegue hacia pantallas anteriores en el flujo de asistente
        onBackPressedDispatcher.addCallback {
            // Si el usuario está en la pantalla de configuración SIP, terminar la actividad
            val navController = binding.assistantNavContainer.findNavController()
            if (navController.currentDestination?.id == R.id.thirdPartySipAccountLoginFragment) {
                finish()
            }
            // De lo contrario, simplemente no hacer nada (deshabilitar el botón atrás)
        }

        (binding.root as? ViewGroup)?.doOnPreDraw {
            if (!areAllPermissionsGranted()) {
                Log.w("$TAG Not all required permissions are granted, showing Permissions fragment")
                val action = PermissionsFragmentDirections.actionGlobalPermissionsFragment()
                binding.assistantNavContainer.findNavController().navigate(action)
            } else {
                // Ir directamente a la pantalla de configuración SIP sin pasar por pantallas intermedias
                Log.i("$TAG Navegando directamente a la pantalla de configuración SIP")
                val navController = binding.assistantNavContainer.findNavController()
                
                // Si el destino actual es el landing o la pantalla de inicio, ir directamente a la configuración SIP
                if (navController.currentDestination?.id == R.id.landingFragment) {
                    val action = org.linphone.ui.assistant.fragment.LandingFragmentDirections
                        .actionLandingFragmentToThirdPartySipAccountLoginFragment()
                    navController.navigate(action)
                }
            }
        }
    }

    private fun areAllPermissionsGranted(): Boolean {
        for (permission in Compatibility.getAllRequiredPermissionsArray()) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.w("$TAG Permission [$permission] hasn't been granted yet!")
                return false
            }
        }

        val granted = Compatibility.hasFullScreenIntentPermission(this)
        if (granted) {
            Log.i("$TAG All permissions have been granted!")
        }
        return granted
    }
}
