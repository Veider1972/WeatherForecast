package ru.veider.weatherforecast.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.HistoryFragmentBinding
import ru.veider.weatherforecast.repository.history.HistoryLoadingState
import ru.veider.weatherforecast.ui.utils.showSnack
import ru.veider.weatherforecast.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
                             ): View {
        _binding = HistoryFragmentBinding.inflate(inflater,
                                                  container,
                                                  false)
        return binding.root
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?,
                              ) {
        super.onViewCreated(view,
                            savedInstanceState)

        binding.historyRecyclerView.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner,
                                          Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(historyLoadingState: HistoryLoadingState) {
        when (historyLoadingState) {
            is HistoryLoadingState.Success -> {
                with(binding) {
                    historyRecyclerView.visibility = View.VISIBLE
                    loading.loadingLayout.visibility = View.GONE
                }
                adapter.setData(historyLoadingState.historyData)
            }

            is HistoryLoadingState.Loading -> {
                with(binding) {
                    historyRecyclerView.visibility = View.GONE
                    loading.loadingLayout.visibility = View.VISIBLE
                }
            }
            is HistoryLoadingState.Error -> {
                with(binding) {
                    historyRecyclerView.visibility = View.VISIBLE
                    loading.loadingLayout.visibility = View.GONE

                    historyRecyclerView.showSnack(getString(R.string.error),
                                                  getString(R.string.reload),
                                                  {
                                                      viewModel.getAllHistory()
                                                  })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}