package app.point_counter.view

import SettingsDialog
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.point_counter.R
import android.widget.*
import android.content.Intent
import androidx.core.os.bundleOf
import app.point_counter.view.MainActivity

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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Notifica a la Activity que el WinDialog se cerró
        parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_win, null) // ⚠️ usa un layout tipo diálogo

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val winner = arguments?.getString("winner")

        val tvWinner = view.findViewById<TextView>(R.id.tvTituloAjustes)
        tvWinner.text = "VICTORY PLAYER $winner"

        val btnAgain = view.findViewById<Button>(R.id.btnAgain)
        btnAgain.setOnClickListener {
            parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
            dismiss() // Cierra el WinDialog
        }

        val btnMenu = view.findViewById<Button>(R.id.btonMenu)
        btnMenu.setOnClickListener {
            parentFragmentManager.setFragmentResult("winDialogClosed", bundleOf())
            startActivity(Intent(requireContext(), MainActivity::class.java))
            dismiss()
        }

        return dialog
    }
}
