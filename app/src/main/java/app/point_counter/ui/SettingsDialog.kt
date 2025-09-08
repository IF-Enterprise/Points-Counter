import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import app.point_counter.R
import app.point_counter.ui.ScoreboardActivity

class SettingsDialog : DialogFragment() {

    companion object {
        fun newInstance(sportType: String): SettingsDialog {
            val dialog = SettingsDialog()
            val args = Bundle()
            args.putString("sportType", sportType)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_settings, null)

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Referencias UI
        val tvSets = view.findViewById<TextView>(R.id.tvSets)
        val seekSets = view.findViewById<SeekBar>(R.id.seekBarSets)
        val tvJugadores = view.findViewById<TextView>(R.id.tvJugadores)
        val seekJugadores = view.findViewById<SeekBar>(R.id.seekBarJugadores)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)

        // NUEVO → bloque Games
        val tvGames = view.findViewById<TextView>(R.id.tvGames)
        val seekGames = view.findViewById<SeekBar>(R.id.seekBarGames)

        // Recuperar el sportType
        val sportType = arguments?.getString("sportType") ?: "pingpong"

        // --- Configuración según deporte ---
        if (sportType.equals("tennis", ignoreCase = true)) {
            // Mostrar Games solo en tenis
            tvGames.visibility = View.VISIBLE
            seekGames.visibility = View.VISIBLE

            seekGames.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    tvGames.text = "Número de games: $progress"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        } else {
            // Ocultar en otros deportes
            tvGames.visibility = View.GONE
            seekGames.visibility = View.GONE
        }

        // Listeners de SeekBars normales
        seekSets.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSets.text = "Número de sets: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekJugadores.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvJugadores.text = "Número de jugadores: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Acción del botón
        btnConfirmar.setOnClickListener {
            startActivity(Intent(requireContext(), ScoreboardActivity::class.java).apply {
                putExtra("sportType", sportType)
                putExtra("sets", seekSets.progress)
                putExtra("jugadores", seekJugadores.progress)

                // ✅ Solo añadir games si es tenis
                if (sportType.equals("tennis", ignoreCase = true)) {
                    putExtra("games", seekGames.progress)
                }
            })
            dismiss()
        }

        return dialog
    }
}
