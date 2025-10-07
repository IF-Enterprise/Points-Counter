import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
                arguments = Bundle().apply { putString("sportType", sportType) }
            }
        }
    }

    private var _rootView: View? = null
    private val rootView get() = _rootView!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: return super.onCreateDialog(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        _rootView = inflater.inflate(R.layout.activity_settings, null)

        val builder = AlertDialog.Builder(context)
            .setView(rootView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setupUI()

        return dialog
    }

    private fun setupUI() {
        // --- UI references ---
        val tvSets = rootView.findViewById<TextView>(R.id.tvSets)
        val seekSets = rootView.findViewById<SeekBar>(R.id.seekBarSets)
        val tvGames = rootView.findViewById<TextView>(R.id.tvGames)
        val seekGames = rootView.findViewById<SeekBar>(R.id.seekBarGames)
        val tvDuracion = rootView.findViewById<TextView>(R.id.tvDuracion)
        val seekDuracion = rootView.findViewById<SeekBar>(R.id.seekBarDuracion)
        val tvPuntos = rootView.findViewById<TextView>(R.id.tvPuntos)
        val seekPuntos = rootView.findViewById<SeekBar>(R.id.seekBarPuntos)
        val btnConfirmar = rootView.findViewById<Button>(R.id.btnConfirmar)

        // --- Tipo de deporte y reglas ---
        val sportType = arguments?.getString("sportType") ?: "pingpong"
        val rules = getRulesForSport(sportType)

        // --- Inicialización segura ---
        seekPuntos.progress = rules.pointsPerSet.coerceIn(1, seekPuntos.max)
        tvPuntos.text = "Puntos por set: ${seekPuntos.progress}"

        if (sportType in listOf("tennis", "padel", "pingpong", "badminton", "voley")) {
            tvSets.visibility = View.VISIBLE
            seekSets.visibility = View.VISIBLE
            seekSets.progress = rules.setsToWin.coerceIn(1, seekSets.max)
            tvSets.text = "Número de sets: ${seekSets.progress}"
        } else {
            tvSets.visibility = View.GONE
            seekSets.visibility = View.GONE
        }

        if (sportType in listOf("tennis", "padel")) {
            tvGames.visibility = View.VISIBLE
            seekGames.visibility = View.VISIBLE
            seekGames.progress = rules.gamesPerSet.coerceIn(1, seekGames.max)
            tvGames.text = "Número de games: ${seekGames.progress}"
        } else {
            tvGames.visibility = View.GONE
            seekGames.visibility = View.GONE
        }

        if (sportType in listOf("football", "basketball")) {
            tvDuracion.visibility = View.VISIBLE
            seekDuracion.visibility = View.VISIBLE
            seekDuracion.progress = rules.duration.coerceIn(1, seekDuracion.max)
            tvDuracion.text = "Duración del partido (min): ${seekDuracion.progress}"
        } else {
            tvDuracion.visibility = View.GONE
            seekDuracion.visibility = View.GONE
        }

        // --- Listeners seguros ---
        seekSets.setOnSeekBarChangeListener(createSafeListener { progress ->
            if (isAdded) tvSets.text = "Número de sets: $progress"
        })

        seekGames.setOnSeekBarChangeListener(createSafeListener { progress ->
            if (isAdded) tvGames.text = "Número de games: $progress"
        })

        seekDuracion.setOnSeekBarChangeListener(createSafeListener { progress ->
            if (isAdded) tvDuracion.text = "Duración del partido (min): $progress"
        })

        seekPuntos.setOnSeekBarChangeListener(createSafeListener { progress ->
            if (isAdded) tvPuntos.text = "Puntos por set: $progress"
        })

        // --- Confirmar ---
        btnConfirmar.setOnClickListener {
            if (!isAdded) return@setOnClickListener
            btnConfirmar.isEnabled = false // Evitar doble click
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
            dismissAllowingStateLoss()
        }
    }

    private fun createSafeListener(onChange: (Int) -> Unit) = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) = onChange(progress)
        override fun onStartTrackingTouch(sb: SeekBar?) {}
        override fun onStopTrackingTouch(sb: SeekBar?) {}
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _rootView = null // Evitar leaks y callbacks a views destruidas
    }
}
