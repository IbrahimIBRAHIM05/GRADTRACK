package com.example.gradtraker.ui.student

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StudentProfile(
    val fullName: String = "",
    val universityId: String = "",
    val email: String = "",
    val department: String = "",
    val academicYear: String = ""
)

data class StudentProfileUiState(
    val profile: StudentProfile = StudentProfile(),
    val draft: StudentProfile = StudentProfile(),
    val isEditing: Boolean = false,
    val message: String? = null
)

class StudentProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StudentProfileUiState())
    val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    fun beginEditing() {
        _uiState.value = _uiState.value.copy(
            draft = _uiState.value.profile,
            isEditing = true,
            message = null
        )
    }

    fun updateDraft(update: (StudentProfile) -> StudentProfile) {
        val current = _uiState.value
        if (current.isEditing) _uiState.value = current.copy(draft = update(current.draft))
    }

    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(draft = _uiState.value.profile, isEditing = false, message = null)
    }

    fun saveForSession() {
        val current = _uiState.value
        if (!current.isEditing) return
        _uiState.value = current.copy(
            profile = current.draft,
            isEditing = false,
            message = "Profile changes are held in memory for this session only."
        )
    }
}
