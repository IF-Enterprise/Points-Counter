package app.point_counter.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import app.point_counter.R

class WinDialog : DialogFragment() {

    companion object {
        fun newInstance(winner: String): WinDialog {
            return WinDialog().apply {
                arguments = Bundle().apply {
                    putString("winner", winner)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Notifica a la Activity que el WinDialog se cerró
        parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_win, null)

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // --- Recuperar argumentos ---
        val winner = arguments?.getString("winner")

        // --- Referencias UI ---
        val tvWinner = view.findViewById<TextView>(R.id.tvTituloAjustes)
        val btnAgain = view.findViewById<Button>(R.id.btnAgain)
        val btnMenu = view.findViewById<Button>(R.id.btonMenu)

        // --- Configuración UI ---
        tvWinner.text = "VICTORY PLAYER $winner"

        // --- Acciones ---
        btnAgain.setOnClickListener {
            parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
            dismiss()
            startActivity(Intent(requireContext(), ScoreboardActivity::class.java))
        }

        btnMenu.setOnClickListener {
            parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
            dismiss()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        return dialog
    }
}
