package com.example.kickcash.ui.proposalsingle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kickcash.R

class ProposalSingleFragment : Fragment() {

    private lateinit var proposalsingleViewModel: ProposalSingleViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        proposalsingleViewModel =
                ViewModelProviders.of(this).get(ProposalSingleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_proposalsingle, container, false)
        val textView: TextView = root.findViewById(R.id.text_proposalsingle)
        proposalsingleViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}