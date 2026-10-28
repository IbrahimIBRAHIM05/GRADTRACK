package com.example.gradtraker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.gradtraker.ui.theme.supervisor.SupervisorPreviewScreen
import com.example.gradtraker.ui.student.StudentWorkspaceScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    GradTrackWorkspaceRoot()
                }
            }
        }
    }
}

@Composable
private fun GradTrackWorkspaceRoot() {
    var isStudentWorkspace by rememberSaveable { mutableStateOf(false) }
    if (isStudentWorkspace) {
        StudentWorkspaceScreen(onSwitchWorkspace = { isStudentWorkspace = false })
    } else {
        SupervisorPreviewScreen(onSwitchWorkspace = { isStudentWorkspace = true })
    }
}
