package fi.giao.woltapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import fi.giao.woltapplication.adapter.VenueAdapter
import fi.giao.woltapplication.database.Favorite
import fi.giao.woltapplication.database.VenueAndFavorite
import fi.giao.woltapplication.databinding.FragmentVenueListBinding
import fi.giao.woltapplication.util.VenueFunctions
import fi.giao.woltapplication.viewmodel.VenueViewModel
import fi.giao.woltapplication.viewmodel.VenueViewModelFactory
import java.util.concurrent.TimeUnit

class VenueListFragment : Fragment() {
    private lateinit var binding: FragmentVenueListBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var cancellationTokenSource = CancellationTokenSource()
    private var isFavorite = false
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority  = PRIORITY_HIGH_ACCURACY
        }
        getLocation()
        val venueAdapter = VenueAdapter(context = requireContext(),::heartClickListener)
        binding.venueListRecyclerView.apply {
            adapter = venueAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.venueAndFavoriteList.observe(viewLifecycleOwner) {
            venueAdapter.submitList(it)

        }
    }

    private fun heartClickListener(venueAndFavorite: VenueAndFavorite):Boolean {
        viewModel.venueInFavorite.observe(viewLifecycleOwner) {
            isFavorite = VenueFunctions.isFavorite(venue_id = venueAndFavorite.venue.id, list = it)
        }
        if (!isFavorite) {
            viewModel.addFavorite(Favorite(id = 0, venue_id = venueAndFavorite.venue.id))

            Toast.makeText(
                requireContext(),
                "Added ${venueAndFavorite.venue.name} to favorite",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.removeFavorite(venueId = venueAndFavorite.venue.id)
            Toast.makeText(
                requireContext(),
                "Remove ${venueAndFavorite.venue.name} from favorite",
                Toast.LENGTH_SHORT
            ).show()
        }
        return !isFavorite
    }

    private fun getLocation(){
        if(checkPermissions()) {
            val currentLocationTask: Task<Location> = fusedLocationProviderClient.getCurrentLocation(
                PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            currentLocationTask.addOnCompleteListener(requireActivity()) {
                val location:Location?=it.result
                if (location == null) {
                    Toast.makeText(requireActivity(),"Null Received",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(),"Get Coordinates", Toast.LENGTH_SHORT).show()
                    viewModel.getVenueData(listOf(location.latitude.toString(),location.longitude.toString()))
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun checkPermissions() : Boolean{
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("gps", "${it.key} = ${it.value}")
                getLocation()
            }
        }
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
}

