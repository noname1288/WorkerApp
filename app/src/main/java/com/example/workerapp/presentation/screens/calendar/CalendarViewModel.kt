package com.example.workerapp.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.example.workerapp.data.source.model.base.JobModel1
import com.example.workerapp.data.source.remote.JobRemoteImpl
import com.example.workerapp.utils.TimeUtils
import com.example.workerapp.utils.cached.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val jobRemoteImpl: JobRemoteImpl
) : ViewModel() {

    private val _morningJobs = MutableStateFlow(emptyList<JobModel1>())
    val morningJobs: StateFlow<List<JobModel1>> = _morningJobs

    private val _afternoonJobs = MutableStateFlow(emptyList<JobModel1>())
    val afternoonJobs: StateFlow<List<JobModel1>> = _afternoonJobs

    private val _nightJobs = MutableStateFlow(emptyList<JobModel1>())
    val nightJobs: StateFlow<List<JobModel1>> = _nightJobs

    private val _uiState = MutableStateFlow<CalendarUiState>(CalendarUiState.Idle)
    val uiState: StateFlow<CalendarUiState> = _uiState

    fun fetchData(date: String) {
        viewModelScope.launch {
            _uiState.value = CalendarUiState.Loading
            try {
                val workerId: String? = UserSession.uid

                if (workerId == null) {
                    _uiState.value = CalendarUiState.Error("User not logged in")
                    return@launch
                }

                val result = jobRemoteImpl.getSchedules(
                    workerId = workerId,
                    date = date
                )

                when (result) {
                    is NetworkResult.Success -> {
                        val jobs = sortAndGroupJob(result.data)
                        _uiState.value = CalendarUiState.Success(jobs)
                    }

                    is NetworkResult.Error -> {
                        _uiState.value = CalendarUiState.Error(result.message)
                    }

                }
            } catch (e: Exception) {
                _uiState.value = CalendarUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun sortAndGroupJob(jobs: List<JobModel1>): Map<String, List<JobModel1>> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val jobsWithTime = jobs.map { job ->
            job to LocalTime.parse(job.startTime, formatter)
        }

        val sortedJobs = jobsWithTime.sortedBy { it.second }

        return sortedJobs.groupBy(
            keySelector = { TimeUtils.getShiftLabel(it.second) },
            valueTransform = { it.first }
        )
    }
}

sealed class CalendarUiState {
    object Idle : CalendarUiState()
    object Loading : CalendarUiState()
    data class Success(val jobs: Map<String, List<JobModel1>>) : CalendarUiState()
    data class Error(val message: String) : CalendarUiState()
}