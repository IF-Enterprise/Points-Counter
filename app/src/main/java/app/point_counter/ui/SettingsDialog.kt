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
import app.point_counter.data.Sport
import app.point_counter.data.sports.Badminton
import app.point_counter.data.sports.Football
import app.point_counter.data.sports.PingPong
import app.point_counter.data.sports.Tennis
import app.point_counter.ui.ScoreboardActivity

class SettingsDialog : DialogFragment() {

    companion object {
        fun newInstance(sportType: String): SettingsDialog {
            return SettingsDialog().apply {
                arguments = Bundle().apply {
                    putString("sportType", sportType)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_settings, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // --- UI references ---
        val tvSets = view.findViewById<TextView>(R.id.tvSets)
        val seekSets = view.findViewById<SeekBar>(R.id.seekBarSets)
        val tvGames = view.findViewById<TextView>(R.id.tvGames)
        val seekGames = view.findViewById<SeekBar>(R.id.seekBarGames)
        val tvDuracion = view.findViewById<TextView>(R.id.tvDuracion)
        val seekDuracion = view.findViewById<SeekBar>(R.id.seekBarDuracion)
        val tvPuntos = view.findViewById<TextView>(R.id.tvPuntos)
        val seekPuntos = view.findViewById<SeekBar>(R.id.seekBarPuntos)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)

        // --- Tipo de deporte y reglas ---
        val sportType = arguments?.getString("sportType") ?: "pingpong"
        val rules = getRulesForSport(sportType)

        // --- Inicialización segura ---
        // Puntos por set
        seekPuntos.progress = rules.pointsPerSet.coerceIn(1, seekPuntos.max)
        tvPuntos.text = "Puntos por set: ${seekPuntos.progress}"

        // Sets
        if (sportType in listOf("tennis", "padel", "pingpong", "badminton", "voley")) {
            tvSets.visibility = View.VISIBLE
            seekSets.visibility = View.VISIBLE
            seekSets.progress = rules.setsToWin.coerceIn(1, seekSets.max)
            tvSets.text = "Número de sets: ${seekSets.progress}"
        } else {
            tvSets.visibility = View.GONE
            seekSets.visibility = View.GONE
        }

        // Games (solo tenis y padel)
        if (sportType in listOf("tennis", "padel")) {
            tvGames.visibility = View.VISIBLE
            seekGames.visibility = View.VISIBLE
            seekGames.progress = rules.gamesPerSet.coerceIn(1, seekGames.max)
            tvGames.text = "Número de games: ${seekGames.progress}"
        } else {
            tvGames.visibility = View.GONE
            seekGames.visibility = View.GONE
        }

        // Timer (solo fútbol y baloncesto)
        if (sportType in listOf("football", "basketball")) {
            tvDuracion.visibility = View.VISIBLE
            seekDuracion.visibility = View.VISIBLE
            seekDuracion.progress = rules.duration.coerceIn(1, seekDuracion.max)
            tvDuracion.text = "Duración del partido (min): ${seekDuracion.progress}"
        } else {
            tvDuracion.visibility = View.GONE
            seekDuracion.visibility = View.GONE
        }

        // --- Listeners ---
        seekSets.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSets.text = "Número de sets: $progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        seekGames.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                tvGames.text = "Número de games: $progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        seekDuracion.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                tvDuracion.text = "Duración del partido (min): $progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        seekPuntos.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                tvPuntos.text = "Puntos por set: $progress"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // --- Confirmar ---
        btnConfirmar.setOnClickListener {
            val intent = Intent(requireContext(), ScoreboardActivity::class.java).apply {
                putExtra("sportType", sportType)
                putExtra("sets", seekSets.progress)
                putExtra("pointsPerSet", seekPuntos.progress)
                putExtra("duration", seekDuracion.progress)
                if (sportType in listOf("tennis", "padel")) {
                    putExtra("games", seekGames.progress)
                }
            }
            startActivity(intent)
            dismiss()
        }

        return dialog
    }

    // --- Reglas por defecto ---
    private fun getRulesForSport(sportType: String): Sport.SportRules {
        return when (sportType.lowercase()) {
            "tennis" -> Tennis().rules
            "pingpong" -> PingPong().rules
            "football" -> Football().rules
            "badminton" -> Badminton().rules
            else -> Sport.SportRules(
                setsToWin = 3,
                pointsPerSet = 11,
                duration = 90,
                gamesPerSet = 6
            )
        }
    }
}
