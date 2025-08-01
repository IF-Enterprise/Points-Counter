package app.point_counter.view

import SettingsDialog
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.point_counter.R
import android.widget.*
import android.content.Intent
import app.point_counter.viewmodel.ScoreViewModel


class WinDialog: DialogFragment() {
    companion object {
        fun newInstance(winner: String): WinDialog {
            val dialog = WinDialog()
            val args = Bundle()
            args.putString("winner", winner)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_win, null) // ⚠️ usa un layout tipo diálogo

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val winner = arguments?.getString("winner")
        //val scoreManager = arguments?.getSerializable("scoreManager") as ScoreViewModel

        val tvWinner = view.findViewById<TextView>(R.id.tvTituloAjustes)
        tvWinner.text = "VICTORY PLAYER $winner"

        val btnAgain = view.findViewById<Button>(R.id.btnAgain)
        btnAgain.setOnClickListener {
            dismiss() // Cierra el WinDialog
            //scoreManager.resetScore()
        }

        val btnMenu = view.findViewById<Button>(R.id.btonMenu)
        btnMenu.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            dismiss()
        }

        return dialog
    }
}
