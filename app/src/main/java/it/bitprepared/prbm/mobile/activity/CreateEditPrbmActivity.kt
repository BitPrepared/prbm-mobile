package it.bitprepared.prbm.mobile.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import it.bitprepared.prbm.mobile.R
import it.bitprepared.prbm.mobile.databinding.ActivityCreateEditPrbmBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Activity responsible for new PRBM Creation and edit
 */
class CreateEditPrbmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEditPrbmBinding
    private val viewModel: CreateEditPrbmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateEditPrbmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.modelState.collect { state ->
                    if (state.isEditing) {
                        binding.topAppBar.title = getString(R.string.edit_prbm)
                    } else {
                        binding.topAppBar.title = getString(R.string.new_prbm)
                    }
                    binding.editTitle.setTextIfDifferent(state.title)
                    binding.editAuthors.setTextIfDifferent(state.authors)
                    binding.editPlace.setTextIfDifferent(state.place)
                    binding.editNotes.setTextIfDifferent(state.notes)
                    binding.editDate.text = state.date
                    binding.editTime.text = state.time
                    if (state.errorMessageResId != null) {
                        Snackbar.make(binding.root, state.errorMessageResId, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    if (state.saveReady) {
                        if (!state.isEditing) {
                            startActivity(Intent(this@CreateEditPrbmActivity, PrbmDetailActivity::class.java))
                        }
                        finish()
                    }
                }
            }
        }
        binding.editTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.updateTitle(s.toString())
            }
        })
        binding.editAuthors.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.updateAuthors(s.toString())
            }
        })
        binding.editPlace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.updatePlace(s.toString())
            }
        })
        binding.editNotes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                viewModel.updateNotes(s.toString())
            }
        })
        binding.btnCreatePrbm.setOnClickListener {
            viewModel.savePrbm(this@CreateEditPrbmActivity)
        }
        binding.editDate.setOnClickListener { _ ->
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.pick_prbm_date)
                .setSelection(fromDateToTimestamp(binding.editDate.getText().toString()))
                .build()
            datePicker.addOnPositiveButtonClickListener { _ ->
                viewModel.updateDate(datePicker.selection)
            }
            datePicker.show(supportFragmentManager, "datePicker")
        }

        binding.editTime.setOnClickListener { _ ->
            val time = LocalTime.parse(binding.editTime.getText().toString())
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(time.hour)
                .setMinute(time.minute)
                .setTitleText(R.string.pick_prbm_time).build()
            timePicker.addOnPositiveButtonClickListener { _ ->
                viewModel.updateTime(timePicker.hour, timePicker.minute)
            }
            timePicker.show(supportFragmentManager, "timePicker")
        }
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun fromDateToTimestamp(date: String): Long {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(date, dateFormatter)
        return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
    }
}

private fun AppCompatEditText.setTextIfDifferent(input: String) =
    if (text.toString() != input) setText(input) else Unit
