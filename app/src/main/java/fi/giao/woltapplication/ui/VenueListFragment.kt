package fi.giao.woltapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.giao.woltapplication.R
import fi.giao.woltapplication.databinding.FragmentVenueListBinding

class VenueListFragment : Fragment() {
    private lateinit var binding:FragmentVenueListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentVenueListBinding.inflate(inflater,container,false)
        return binding.root
    }

}