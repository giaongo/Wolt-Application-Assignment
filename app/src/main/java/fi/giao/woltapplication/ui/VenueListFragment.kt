package fi.giao.woltapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import fi.giao.woltapplication.adapter.VenueAdapter
import fi.giao.woltapplication.database.Venue
import fi.giao.woltapplication.databinding.FragmentVenueListBinding
import fi.giao.woltapplication.viewmodel.VenueViewModel
import fi.giao.woltapplication.viewmodel.VenueViewModelFactory
import org.json.JSONObject

class VenueListFragment : Fragment() {
    private lateinit var binding: FragmentVenueListBinding
    private val viewModel: VenueViewModel by viewModels {
        VenueViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVenueListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val venueAdapter = VenueAdapter(requireContext())
        binding.venueListRecyclerView.apply {
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.result.observe(viewLifecycleOwner) {
            venueAdapter.submitList(it)
        }

    }


}

