package it.bitprepared.prbm.mobile.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.activity.ListPrbmAdapter.OnPrbmClickListener
import it.bitprepared.prbm.mobile.databinding.ActivityListPrbmBinding
import it.bitprepared.prbm.mobile.model.Prbm
import kotlinx.coroutines.launch

/**
 * Activity responsible of PRBM list visualization
 */
class ListPrbmActivity : AppCompatActivity(), OnPrbmClickListener {

    private lateinit var binding: ActivityListPrbmBinding
    private lateinit var adtPrbm: ListPrbmAdapter

    private val viewModel: ListPrbmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListPrbmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        adtPrbm = ListPrbmAdapter(emptyList(), LayoutInflater.from(this), this)
        binding.listPrbm.setLayoutManager(LinearLayoutManager(this))
        val divider = MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.listPrbm.addItemDecoration(divider)
        binding.listPrbm.setAdapter(adtPrbm)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modelState.collect { state ->
                    if (state.prbmList.isNotEmpty()) {
                        showPrbmList(state.prbmList)
                    } else {
                        showEmptyState()
                    }
                    if (state.prbmToEdit != null) {
                        startActivity(Intent(this@ListPrbmActivity, PrbmDetailActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun showPrbmList(prbmList: List<Prbm>) {
        binding.textNoPrbm.visibility = View.INVISIBLE
        adtPrbm.setNewData(prbmList)
    }

    private fun showEmptyState() {
        binding.textNoPrbm.visibility = View.VISIBLE
    }

    override fun onEditPrbm(prbm: Prbm) {
        viewModel.editPrbm(prbm)
    }

    override fun onDeletePrbm(prbm: Prbm) {
        AlertDialog.Builder(this).setTitle(getString(R.string.are_you_sure_to_delete))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deletePrbm(
                    this@ListPrbmActivity, prbm
                )
            }.setNegativeButton(R.string.abort) { _, _ -> }.create().show()
    }
}